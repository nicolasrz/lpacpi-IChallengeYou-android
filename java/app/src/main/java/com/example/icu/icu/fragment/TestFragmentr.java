package com.example.icu.icu.fragment;

/**
 * Created by Nicolas on 29/03/2015.
 */
import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;
import android.widget.MediaController;
import com.example.icu.icu.R;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
public class TestFragmentr extends Fragment {

private ProgressDialog pDialog;
private VideoView videoview;
private String VideoURL;
private  MediaController mediacontroller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fargment_test, container, false);
        Button boutonVideo = (Button) rootView.findViewById(R.id.buttonVideo);
        VideoURL = "http://37.187.122.209/icu/uploads/VID_18_eidole_toto_20150401_131918.mp4";
        videoview = (VideoView) rootView.findViewById(R.id.videoView);
        // Execute StreamVideo AsyncTask

        boutonVideo.setOnClickListener(handler);
        Log.v("TSZT VIDEO","1");

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                videoview.start();
            }
        });


        return rootView;
    }



   View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            // Create a progressbar
            pDialog = new ProgressDialog(getActivity());
            // Set progressbar title
            pDialog.setTitle("Android Video Streaming Tutorial");
            // Set progressbar message
            pDialog.setMessage("Buffering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            // Show progressbar
            pDialog.show();
            Log.v("TSZT VIDEO","2");
            try {
                // Start the MediaController
               mediacontroller = new MediaController(
                        getActivity());
                mediacontroller.setAnchorView(videoview);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(VideoURL);
                videoview.setMediaController(mediacontroller);
                videoview.setVideoURI(video);
                Log.v("TSZT VIDEO","3");


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            videoview.requestFocus();
            videoview.setOnPreparedListener(new OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    pDialog.dismiss();
                    videoview.start();
                    Log.v("TSZT VIDEO","4");

                }
            });

        }

    };

}
