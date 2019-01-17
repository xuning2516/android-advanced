package com.royole.fileproviderdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

public class NinePngActivity extends AppCompatActivity {
    private static final String TAG = "zhanghaon";
    private Button myButton;
    private SeekBar seekBar1;
    private SeekBar seekBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_png);
        myButton = findViewById(R.id.button6);
        seekBar1 = findViewById(R.id.seekBar5);
        seekBar2 = findViewById(R.id.seekBar6);

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.d(TAG, "onProgressChanged: progress " + progress+ " - " + myButton.getWidth());
                myButton.setWidth(progress*2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: 2progress " + progress+ " - " + myButton.getHeight());
                myButton.setHeight(progress*3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
