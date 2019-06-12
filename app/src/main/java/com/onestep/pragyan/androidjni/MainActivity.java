package com.onestep.pragyan.androidjni;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.madv.soundtouch.SoundTouch;

import java.io.File;


public class MainActivity extends Activity {

    private ImageView imageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView title = (TextView) findViewById(R.id.text);
        title.setText("Call JNI");

        TextView tv = (TextView) findViewById(R.id.text_view);
        tv.setText("调用JNI");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLibVersion();
                process();
            }
        });
    }

    /// Play audio file
    protected void playWavFile(String fileName) {
        File file2play = new File(fileName);
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file2play), "audio/wav");
        startActivity(i);
    }

    /// Helper class that will execute the SoundTouch processing. As the processing may take
    /// some time, run it in background thread to avoid hanging of the UI.
    protected class ProcessTask extends AsyncTask<ProcessTask.Parameters, Integer, Long> {

        /// Helper class to store the SoundTouch file processing parameters
        public final class Parameters {
            String inFileName;
            String outFileName;
            float tempo;
            float pitch;
        }

        @Override
        protected Long doInBackground(Parameters... params) {
            return doSoundTouchProcessing(params[0]);
        }

        /// Function that does the SoundTouch processing
        public final long doSoundTouchProcessing(Parameters params) {
            SoundTouch st = new SoundTouch();
            st.setTempo(params.tempo);
            st.setPitchSemiTones(params.pitch);
            Log.i("SoundTouch", "process file " + params.inFileName);
            long startTime = System.currentTimeMillis();
            int res = st.processFile(params.inFileName, params.outFileName);
            long endTime = System.currentTimeMillis();
            float duration = (endTime - startTime) * 0.001f;

            Log.i("SoundTouch", "process file done, duration = " + duration);
            if (res != 0) {
                String err = SoundTouch.getErrorString();
                return -1L;
            }

            st.setSampleRate(8000);
            st.setChannels(1);
            st.putSamples(new byte[640], 640);
            st.receiveSamples(new byte[640], 640);
            st.close();

            // Play file if so is desirable
            playWavFile(params.inFileName);

            playWavFile(params.outFileName);
            return 0L;
        }
    }

    protected void checkLibVersion() {
        String ver = SoundTouch.getVersionString();
        Log.d("SoundTouch", "SoundTouch native library version = " + ver);
    }

    protected void process() {
        try {
            ProcessTask task = new ProcessTask();
            ProcessTask.Parameters params = task.new Parameters();
            // parse processing parameters
            params.inFileName = Environment.getExternalStorageDirectory() + "/DCIM/" + "hensen.wav";
            params.outFileName = Environment.getExternalStorageDirectory() + "/DCIM/" + "hensen_o.wav";
            params.tempo = 0.9f;
            params.pitch = -8;

            // start SoundTouch processing in a background thread
            task.execute(params);
        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }
}
