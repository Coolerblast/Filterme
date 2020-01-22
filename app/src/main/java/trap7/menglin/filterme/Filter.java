package trap7.menglin.filterme;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

public class Filter {

    public static final int TYPE_GLASSES = 0;
    public static final int TYPE_FACEMASK = 1;
    public static final int TYPE_HAT = 2;
    public static final int TYPE_NOSE = 3;
    public static final int TYPE_BLUSH = 4;


    Face mFace;
    int mType;
    Bitmap mBitmap;
    GraphicOverlay.Graphic mGraphic;

    public Filter(Face face, int type, Bitmap bitmap, GraphicOverlay.Graphic graphic) {
        mFace = face;
        mType = type;
        mBitmap = bitmap;
        mGraphic = graphic;
    }

    public void draw(Canvas canvas) {
        switch (mType) {
            case Filter.TYPE_GLASSES:
                break;
            case Filter.TYPE_FACEMASK:
                break;
            case Filter.TYPE_HAT:
                break;
            case Filter.TYPE_NOSE:
                break;
            default:
                Log.e("FILTER", "bruh if this message shows then there is seriously sometime wrong.");
                break;
        }
    }

    public void drawGlasses(Canvas canvas) {
        Landmark leftEye = mFace.getLandmarks().get(FirebaseVisionFaceLandmark.LEFT_EYE);
        Landmark rightEye = mFace.getLandmarks().get(FirebaseVisionFaceLandmark.RIGHT_EYE);
        if (leftEye != null && rightEye != null) {
            float eyeDist = leftEye.getPosition().x - rightEye.getPosition().x;
            int delta = (int) eyeDist / 2;
            Rect glassesRect = new Rect((int) mGraphic.translateX(leftEye.getPosition().x) - delta,
                    (int) mGraphic.translateY(leftEye.getPosition().y) - delta,
                    (int) mGraphic.translateX(leftEye.getPosition().x) + delta,
                    (int) mGraphic.translateY(leftEye.getPosition().y) + delta);
            canvas.drawBitmap(mBitmap, null, glassesRect, null);
        }

    }
}
