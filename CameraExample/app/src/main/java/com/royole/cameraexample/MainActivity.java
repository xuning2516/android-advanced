package com.royole.cameraexample;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView mAllAppDirs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAllAppDirs = findViewById(R.id.allAppDirs);

    }

    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.takephoto:
                intent.setComponent(new ComponentName("com.royole.cameraexample","com.royole.cameraexample.TakePhotosActivity"));
                startActivity(intent);
                break;
            case R.id.getAppDir:
                getAllAppDir();
                break;
            case R.id.recordvideo:
                intent = new Intent(this,RecordVideoActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    private void getAllAppDir(){
        File externalCacheDir = getExternalCacheDir();
        File[] externalCacheDirs = getExternalCacheDirs();
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] externalFileDirs = getExternalFilesDirs(Environment.DIRECTORY_RINGTONES);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("externalcache: "+externalCacheDir.getAbsolutePath());
        stringBuilder.append("\n externalcachedirs : \n");
        for(File file:externalCacheDirs){
            stringBuilder.append(""+file.getAbsolutePath()+ "\n");
        }
        stringBuilder.append("externalFilesDir: "+externalFilesDir.getAbsolutePath());
        stringBuilder.append("\n externalFileDirs : \n");
        for(File file:externalFileDirs){
            stringBuilder.append(""+file.getAbsolutePath()+ "\n");
        }

        String externalState = Environment.getExternalStorageState(getExternalFilesDir(null));
        stringBuilder.append(" externalState : "+ externalState + "\n");
        boolean isExternalStorageEmulated = Environment.isExternalStorageEmulated();
        stringBuilder.append(" isExternalStorageEmulated : "+ isExternalStorageEmulated + "\n");
        boolean isExternalStorageRemovable = Environment.isExternalStorageRemovable();
        stringBuilder.append(" isExternalStorageRemovable : "+ isExternalStorageRemovable + "\n");

        File cacheDir = getCacheDir();
        File filesDir = getFilesDir();
        stringBuilder.append("cacheDir: "+cacheDir.getAbsolutePath() + "\n");
        stringBuilder.append("filesDir: "+filesDir.getAbsolutePath() + "\n");

        mAllAppDirs.setText(stringBuilder.toString());
    }
}
