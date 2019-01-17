package com.royole.fileproviderdemo;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "zhanghao";

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.imageView);
    }

    public void toFileSelect(View view){
        Intent intent = new Intent(MainActivity.this,FileSelectActivity.class);
        startActivity(intent);
    }

    public void toCardView(View view){
        Intent intent = new Intent(MainActivity.this,CardActivity.class);
        startActivity(intent);
    }

    public void toNinePng(View view){
        Intent intent = new Intent(MainActivity.this,NinePngActivity.class);
        startActivity(intent);
    }

    public void requestImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/jpg");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: " + resultCode + " data: " + data);
        switch (requestCode) {
            case 100:
                if (resultCode != RESULT_OK) {
                    Log.d(TAG, "onActivityResult: non ok");

                } else {
                    Uri returnUri = data.getData();
                    AssetFileDescriptor inputFd = null;
                    try {
                        inputFd = getContentResolver().openAssetFileDescriptor(returnUri, "r");
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    FileDescriptor fd = inputFd.getFileDescriptor();
                    String mimeType = getContentResolver().getType(returnUri);

                    Cursor returnCursor =
                            getContentResolver().query(returnUri, null, null, null, null);
                    /*
                     * Get the column indexes of the data in the Cursor,
                     * move to the first row in the Cursor, get the data,
                     * and display it.
                     */
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    TextView nameView = (TextView) findViewById(R.id.filename_text);
                    TextView sizeView = (TextView) findViewById(R.id.filesize_text);
                    nameView.setText(returnCursor.getString(nameIndex));
                    sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));

                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(returnUri));
                        mImageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

}
