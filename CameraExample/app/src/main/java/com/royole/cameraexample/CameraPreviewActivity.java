package com.royole.cameraexample;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

public class CameraPreviewActivity extends AppCompatActivity {
    private Camera mCamera;
    private Preview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
    }


    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    class Preview extends ViewGroup implements SurfaceHolder.Callback {

        SurfaceView mSurfaceView;
        SurfaceHolder mHolder;
        List<Camera.Size> mSupportedPreviewSizes;
        private boolean cameraInitialized;
        Camera.Size mPreviewSize;

        Preview(Context context) {
            super(context);

            mSurfaceView = new SurfaceView(context);
            addView(mSurfaceView);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            requestLayout();
            mCamera.setParameters(parameters);

            // Important: Call startPreview() to start updating the preview surface.
            // Preview must be started before you can take a picture.
            mCamera.startPreview();
        }

        public void setCamera(Camera camera) {
            if (mCamera == camera) { return; }

            stopPreviewAndFreeCamera();

            mCamera = camera;

            if (mCamera != null) {
                List<Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
                mSupportedPreviewSizes = localSizes;
                requestLayout();

                try {
                    mCamera.setPreviewDisplay(mHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Important: Call startPreview() to start updating the preview
                // surface. Preview must be started before you can take a picture.
                mCamera.startPreview();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Surface will be destroyed when we return, so stop the preview.
            if (mCamera != null) {
                // Call stopPreview() to stop updating the preview surface.
                mCamera.stopPreview();
            }
        }

        /**
         * When this function returns, mCamera will be null.
         */
        private void stopPreviewAndFreeCamera() {

            if (mCamera != null) {
                // Call stopPreview() to stop updating the preview surface.
                mCamera.stopPreview();

                // Important: Call release() to release the camera for use by other
                // applications. Applications should release the camera immediately
                // during onPause() and re-open() it during onResume()).
                mCamera.release();

                mCamera = null;
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }
    }


}
