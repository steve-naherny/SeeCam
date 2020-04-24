package com.example.testapp;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class InvisibleVideoRecorder {



    private static final String TAG = "InvisibleVideoRecorder";
    private final CameraCaptureSessionStateCallback cameraCaptureSessionStateCallback = new CameraCaptureSessionStateCallback();
    private final CameraDeviceStateCallback cameraDeviceStateCallback = new CameraDeviceStateCallback();
    private MediaRecorder mediaRecorder;
    private CameraManager cameraManager;
    private Context context;
    public boolean isRunning = false;

    private CameraDevice cameraDevice;

    private Handler handler;

    public InvisibleVideoRecorder(Context context) {
        this.context = context;

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

            final String filename = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "stev.mp4";
            mediaRecorder.setOutputFile(filename);
            Log.d(TAG, "start: haiiii" + filename);

            CamcorderProfile profile = CamcorderProfile.get(CameraMetadata.LENS_FACING_BACK, CamcorderProfile.QUALITY_720P);
            Log.d(TAG, "start: profile " + profile.toString());
            mediaRecorder.setOrientationHint(90);
            mediaRecorder.setProfile(profile);
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.d(TAG, "start: exception" + e.getMessage());
        }

    }

    public void start() {
        Log.d(TAG, "start: ");

        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraManager.openCamera(String.valueOf(CameraMetadata.LENS_FACING_EXTERNAL), cameraDeviceStateCallback, handler);
        } catch (CameraAccessException | SecurityException e) {
            Log.d(TAG, "start: exception " + e.getMessage());
        }
        isRunning = true;
    }

    public void stop() {
        Log.d(TAG, "stop: ");
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        cameraDevice.close();
        isRunning = false;
    }

    private class CameraCaptureSessionStateCallback extends CameraCaptureSession.StateCallback {
        private final static String TAG = "CamCaptSessionStCb";

        @Override
        public void onActive(CameraCaptureSession session) {
            Log.d(TAG, "onActive: ");
            super.onActive(session);
        }

        @Override
        public void onClosed(CameraCaptureSession session) {
            Log.d(TAG, "onClosed: ");
            super.onClosed(session);
        }

        @Override
        public void onConfigured(CameraCaptureSession session) {
            Log.d(TAG, "onConfigured: ");
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            Log.d(TAG, "onConfigureFailed: ");
        }

        @Override
        public void onReady(CameraCaptureSession session) {
            Log.d(TAG, "onReady: ");
            super.onReady(session);
            try {
                CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                builder.addTarget(mediaRecorder.getSurface());
                CaptureRequest request = builder.build();
                session.setRepeatingRequest(request, null, handler);
                mediaRecorder.start();
            } catch (CameraAccessException e) {
                Log.d(TAG, "onConfigured: " + e.getMessage());

            }
        }

        @Override
        public void onSurfacePrepared(CameraCaptureSession session, Surface surface) {
            Log.d(TAG, "onSurfacePrepared: ");
            super.onSurfacePrepared(session, surface);
        }
    }

    private class CameraDeviceStateCallback extends CameraDevice.StateCallback {
        private final static String TAG = "CamDeviceStateCb";

        @Override
        public void onClosed(CameraDevice camera) {
            Log.d(TAG, "onClosed: ");
            super.onClosed(camera);
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            Log.d(TAG, "onDisconnected: ");
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.d(TAG, "onError: ");
        }

        @Override
        public void onOpened(CameraDevice camera) {
            Log.d(TAG, "onOpened: ");
            cameraDevice = camera;
            try {
                camera.createCaptureSession(Arrays.asList(mediaRecorder.getSurface()), cameraCaptureSessionStateCallback, handler);
            } catch (CameraAccessException e) {
                Log.d(TAG, "onOpened: " + e.getMessage());
            }
        }
    }

}
