package com.omelchenkoaleks.cameraandroid;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class StartCameraActivity extends AppCompatActivity {
    public final static String DEBUG_TAG = "StartCameraActivity";
    private Camera camera;
    private int cameraId = 0;
    public static File photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_camera);

        // проверяем есть ли камера на устройстве
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "\n" + "This device is not a camera.", Toast.LENGTH_LONG).show();
        } else {
            // получаем ID камеры
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "Front camera not found.", Toast.LENGTH_LONG).show();
            } else {
                // открываем камеру для съемки
                camera = Camera.open(cameraId);
            }
        }
    }

    // метод, который вызывается при клике на кнопку
    public void makePhoto(View view) {
        camera.startPreview();
        // сделать фото
        camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
    }

    // поиск камеры
    private int findFrontFacingCamera() {
        int cameraId = -1;

        // Поиск Фронтальной камеры
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            // тут вы указываете какую камеру использовать CAMERA_FACING_BACK или CAMERA_FACING_FRONT
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d(DEBUG_TAG, "The camera is found.");
                cameraId = i;
                break;
            }
        }

        // возвращаем id найденной камеры
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }

}
