package trap7.menglin.filterme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;
    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    public static final int TYPE_GLASSES = 0;
    public static final int TYPE_FACEMASK = 1;
    public static final int TYPE_HAT = 2;
    public static final int TYPE_NOSE = 3;
    public static final int TYPE_BLUSH = 4;
    private int mType;

    private Bitmap mBitmap;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;
    private  boolean demo;
    FaceGraphic(GraphicOverlay overlay, Bitmap bitmap, int type, boolean demo) {
        super(overlay);
        this.mBitmap = bitmap;
        this.mType = type;
        this.demo = demo;
        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];
        mBitmap =   Bitmap.createScaledBitmap(mBitmap, (int)(mBitmap.getWidth()*.6), (int)(mBitmap.getHeight()*.6), false);

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void setId(int id) {
        mFaceId = id;
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }


        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
        if(demo) {
            canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);

            for (Landmark lm : face.getLandmarks()) {
                  canvas.drawCircle(translateX(lm.getPosition().x), translateY(lm.getPosition().y), FACE_POSITION_RADIUS, mFacePositionPaint);
            }
            canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
            canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), x - ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint);
            canvas.drawText("right eye: " + String.format("%.2f", face.getIsRightEyeOpenProbability()), x + ID_X_OFFSET * 2, y + ID_Y_OFFSET * 2, mIdPaint);
            canvas.drawText("left eye: " + String.format("%.2f", face.getIsLeftEyeOpenProbability()), x - ID_X_OFFSET * 2, y - ID_Y_OFFSET * 2, mIdPaint);

            // Draws a bounding box around the face.
            float xOffset = scaleX(face.getWidth() / 2.0f);
            float yOffset = scaleY(face.getHeight() / 2.0f);
            float left = x - xOffset;
            float top = y - yOffset;
            float right = x + xOffset;
            float bottom = y + yOffset;
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
        else
            drawFilter(canvas);

    }

    public void drawFilter(Canvas canvas) {
        switch (mType) {
            case Filter.TYPE_GLASSES:
                drawGlasses(canvas);
                break;
            case Filter.TYPE_FACEMASK:
                break;
            case Filter.TYPE_HAT:
                break;
            case Filter.TYPE_NOSE:
                break;
            case Filter.TYPE_BLUSH:
                break;
            default:
                Log.e("FILTER", "bruh if this message shows then there is seriously sometime wrong.");
                break;
        }
    }

    private Landmark getLandmark(int type) {
        for (Landmark landmark : mFace.getLandmarks())
            if (landmark.getType() == type)
                return landmark;
        return null;
    }

    public void drawGlasses(Canvas canvas) {
        Landmark leftEye = getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
        Landmark rightEye = getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
        if (leftEye != null && rightEye != null) {
            float eyeDist = Math.abs(leftEye.getPosition().x - rightEye.getPosition().x);
            int delta = (int) scaleX(eyeDist) / 2;
            int     left = (int) translateX(leftEye.getPosition().x),
                    right = (int) translateX(rightEye.getPosition().x),
                    top = (int) translateY(leftEye.getPosition().y),
                    bottom = (int) translateY(rightEye.getPosition().y);
            if (left > right) {
                int temp = left;
                left = right;
                right = temp;
            }
            if (top > bottom) {
                int temp = top;
                top = bottom;
                bottom = temp;
            }
//            float angle = (float) Math.atan2((right - left) , (bottom - top));
//            Matrix matrix = new Matrix();
//            matrix.postRotate(angle);
            Rect glassesRect = new Rect(left - delta, top - delta, right + delta, bottom + delta);
            canvas.drawCircle((int) translateX(leftEye.getPosition().x),
                    (int) translateY(leftEye.getPosition().y), 20, new Paint(Color.GREEN));
            canvas.drawCircle((int) translateX(rightEye.getPosition().x),
                    (int) translateY(rightEye.getPosition().y), 20, new Paint(Color.RED));
            canvas.drawBitmap(mBitmap, null, glassesRect, null);
        }

    }
}