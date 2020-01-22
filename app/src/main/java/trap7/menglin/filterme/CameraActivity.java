package trap7.menglin.filterme;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.os.Environment.getExternalStorageDirectory;
import static java.io.File.separator;

public class CameraActivity extends AppCompatActivity {
    CameraSource mCameraSource;
    FaceDetector mDetector;
    Vibrator vibrator;
    CameraSourcePreview cameraView;
    Matrix matrix = new Matrix();

    GraphicOverlay mGraphicOverlay;
    GraphicFaceTracker mGraphicFaceTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matrix.postRotate(90);
        setContentView(R.layout.activity_camera);

        cameraView = (CameraSourcePreview) findViewById(R.id.cameraView);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);

        createCameraSource();
    }

    public void createCameraSource() {
        mDetector = new FaceDetector.Builder(getApplicationContext()).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).setLandmarkType(FaceDetector.ALL_LANDMARKS).build();

        mDetector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        mCameraSource = new CameraSource.Builder(getApplicationContext(), mDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .build();
    }

    private void startCameraSource() {
        if (mCameraSource != null) {
            try {
                cameraView.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e("CAMERA SOURCE", "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    public void swapCamera(View v) {
        if (mCameraSource != null) {
            mCameraSource.stop();
            mCameraSource = new CameraSource.Builder(getApplicationContext(), mDetector)
                    .setRequestedPreviewSize(640, 480)
                    .setFacing((mCameraSource.getCameraFacing() + 1) % 2)
                    .setRequestedFps(30.0f)
                    .build();            matrix.postRotate(180);
            startCameraSource();
        }
    }

    public void takePicture(View view) {
        mCameraSource.takePicture(null, new
                CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes) {
                        // Generate the Face Bitmap
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap face = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        // Generate the Eyes Overlay Bitmap
                        cameraView.setDrawingCacheEnabled(true);
                        Bitmap overlay = cameraView.getDrawingCache();
                        // Generate the final merged image
                        Bitmap result = Bitmap.createBitmap(face, 0, 0, face.getWidth(), face.getHeight(), matrix, true);

                        result = mergeBitmaps(result, overlay);


                        cameraView.destroyDrawingCache();
                        // Save result image to file
                        try {
                            String mainpath = getExternalStorageDirectory() + separator + "Pictures" + separator + "Filterme" + separator;
                            File basePath = new File(mainpath);
                            if (!basePath.exists())
                                Log.d("CAPTURE_BASE_PATH", basePath.mkdirs() ? "Success" : "Failed");
                            String path = mainpath + "photo_" + getPhotoTime() + ".jpg";
                            File captureFile = new File(path);
                            captureFile.createNewFile();
                            if (!captureFile.exists())
                                Log.d("CAPTURE_FILE_PATH", captureFile.createNewFile() ? "Success" : "Failed");
                            FileOutputStream stream = new FileOutputStream(captureFile);
                            result.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                            stream.flush();
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public String getPhotoTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Calendar c = Calendar.getInstance();
        return df.format(c.getTime());

    }

    public Bitmap mergeBitmaps(Bitmap face, Bitmap overlay) {
        // Create a new image with target size
        int width = face.getWidth();
        int height = face.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Rect faceRect = new Rect(0, 0, width, height);
        Rect overlayRect = new Rect(0, 0, overlay.getWidth(), overlay.getHeight());

        // Draw face and then overlay (Make sure rects are as needed)
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(face, faceRect, faceRect, null);
        canvas.drawBitmap(overlay, overlayRect, faceRect, null);
        return newBitmap;
    }


    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }


    private class GraphicFaceTrackerFactory
            implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);
        }

        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }

        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }
}