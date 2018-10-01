package com.omelchenkoaleks.cameraandroid;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// класс, который создаст фотографию
public class PhotoHandler implements Camera.PictureCallback {
    private final Context context;

    public PhotoHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFileDir = getDir();

        // если папки не существует и она не создалась, то выводим сообщение об этом
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Log.d(StartCameraActivity.DEBUG_TAG, "Can not create folder for saving images.");
            Toast.makeText(context, "Can not create folder for saving images.", Toast.LENGTH_LONG).show();
            return;
        }

        // генерируем имя для фото
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String namePhoto = "Photo_" + date;
        String photoFile = namePhoto + ".jpg";

        String photoFileName = pictureFileDir.getPath() + File.separator + photoFile;
        File pictureFile = new File(photoFileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pictureFile);
            // записываем фото, которое получили в байтах
            fileOutputStream.write(data);
            fileOutputStream.close();
            Toast.makeText(context, "Photo saved." + pictureFile.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();

            StartCameraActivity.photo = pictureFile;
        } catch (Exception exception) {
            Log.d(StartCameraActivity.DEBUG_TAG, "File" + photoFileName + "not saved: " + exception.getMessage());
            Toast.makeText(context, "Photo not saved",Toast.LENGTH_LONG).show();
        }

    }

    // метод, который получает путь к папке, где нужно сохранить фото
    public File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");
    }
}
