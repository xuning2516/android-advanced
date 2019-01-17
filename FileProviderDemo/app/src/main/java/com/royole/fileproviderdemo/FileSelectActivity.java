package com.royole.fileproviderdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.royole.androidcommon.utils.PermissionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileSelectActivity extends AppCompatActivity {
    private static final String TAG = "zhanghao";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // The path to the root of this app's internal storage
    private File mPrivateRootDir;
    // The path to the "images" subdirectory
    private File mImagesDir;
    // Array of files in the images subdirectory
    File[] mImageFiles;
    // Array of filenames corresponding to mImageFiles
    String[] mImageFilenames;


    Intent mResultIntent;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        mContext = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        initRequestPermission();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        mDataset = new String[50];
//        for(int i = 0;i<mDataset.length;++i){
//            mDataset[i] = "item"+i;
//        }

        initDatabase();



        // Set up an Intent to send back to apps that request a file
        mResultIntent =
                new Intent("com.example.myapp.ACTION_RETURN_FILE");
        // Get the files/ subdirectory of internal storage
        mPrivateRootDir = getFilesDir();
        // Get the files/images subdirectory;
        mImagesDir = new File(mPrivateRootDir, "images");
        // Get the files in the images subdirectory
        mImageFiles = mImagesDir.listFiles();
        // Set the Activity's result to null to begin with
        setResult(Activity.RESULT_CANCELED, null);
        /*
         * Display the file names in the ListView mFileListView.
         * Back the ListView with the array mImageFilenames, which
         * you can create by iterating through mImageFiles and
         * calling File.getAbsolutePath() for each File
         */
    }

    private  void initRequestPermission(){
        Log.d(TAG, "initRequestPermission: ");
        PermissionUtils.checkPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                new PermissionUtils.PermissionCheckCallBack(){
                    @Override
                    public void onGranted() {
                        Log.d(TAG, "onGranted: ");
                        initDatabase();
                    }

                    @Override
                    public void onDenied(String... permission) {
                        Log.d(TAG, "onDenied: ");
                        PermissionUtils.requestPermissions(mContext,permission,11);
                    }

                    @Override
                    public void onDeniedDontAsk(String... permission) {
                        Log.d(TAG, "onDeniedDontAsk: ");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(mContext, permissions, grantResults, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onGranted() {
                Log.d(TAG, "onGranted: ");
                initDatabase();
            }

            @Override
            public void onDenied(String... permission) {

            }

            @Override
            public void onDeniedDontAsk(String... permission) {

            }
        });
    }

    private String rootFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Camera";

    private void initDatabase(){
        refreshListItems(rootFile);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
    }
    List<String> mDataset = new ArrayList<String>();
    private void refreshListItems(String path) {
        setTitle(path);
        mDataset.clear();
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {

                if (file.isDirectory()) {

                } else {
                    mDataset.add(file.getAbsolutePath());
                }

            }
        }

    }

    /**
     * Created by nixu on 2018/3/5.
     */

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            //...
            ViewHolder vh = new ViewHolder(v);
            File myFile = new File(parent.getContext().getFilesDir(),"images");
            Log.d(TAG, "onCreateViewHolder: myFile:"+myFile.getAbsolutePath());
            try {
                // Resolve to canonical path to keep path checking fast
                Log.d(TAG, "onCreateViewHolder: myFile getCanonicalPath:"+myFile.getCanonicalPath());
            } catch (IOException e) {
                throw new IllegalArgumentException(
                        "Failed to resolve canonical path for " + myFile, e);
            }


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 /*
                 * Get a File for the selected file name.
                 * Assume that the file names are in the
                 * mImageFilename array.
                 */
//                    File requestFile = new File(v.getContext().getFilesDir(),"images/a.jpg");
//                    File requestFileDir = new File(v.getContext().getFilesDir(),"images");
//                    requestFileDir.mkdirs();
//                    try {
//                        requestFile.createNewFile();
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
                    TextView textView = (TextView)v;
                    File requestFile = new File(textView.getText().toString());
                /*
                 * Most file-related method calls need to be in
                 * try-catch blocks.
                 */
                    // Use the FileProvider to get a content URI
                    Uri fileUri = null;
                    try {
                        fileUri = FileProvider.getUriForFile(
                                FileSelectActivity.this,
                                "com.royole.fileproviderdemo.fileprovider",
                                requestFile);
                        Log.d(TAG, "onClick: "+fileUri);
                        if (fileUri != null) {
                            // Grant temporary read permission to the content URI
                            mResultIntent.addFlags(
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            // Put the Uri and MIME type in the result Intent
                            mResultIntent.setDataAndType(
                                    fileUri,
                                    getContentResolver().getType(fileUri));
                            // Set the result
                            FileSelectActivity.this.setResult(Activity.RESULT_OK,
                                    mResultIntent);
                        } else {
                            mResultIntent.setDataAndType(null, "");
                            FileSelectActivity.this.setResult(RESULT_CANCELED,
                                    mResultIntent);
                        }

                    } catch (IllegalArgumentException e) {
                        Log.e("zhanghao",
                                "The selected file can't be shared: " + fileUri,e);
                    }
                    finish();
                }
            });
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset.get(position));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
