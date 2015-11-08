package com.example.icu.icu.fonction;
/**
 * Created by Nicolas on 27/03/2015.
 */

import java.io.File;
import java.io.IOException;
import android.app.Fragment;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.icu.icu.MainActivity;
import com.example.icu.icu.R;
import com.example.icu.icu.fragment.Ami;
import com.example.icu.icu.fragment.DefiRecu;

public class UploadActivity extends Fragment {
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private String filePath = null;
    private String defi =null;
    private String idDefi = null;
    private String lanceurDefi=null;
    private String commentaireRealisateurDefi=null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private Button btnUpload;
    private String typePreuve=null;
    private EditText editTxtCommentaireRealisateur;
    private CheckBox checkBoxPrive;
    private CheckBox checkBoxPublic;
    private static String typeDefi;
    long totalSize = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_upload,container,false);

        txtPercentage = (TextView)rootView.findViewById(R.id.txtPercentage);
        btnUpload = (Button) rootView.findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);
        vidPreview = (VideoView) rootView.findViewById(R.id.videoPreview);
        editTxtCommentaireRealisateur = (EditText) rootView.findViewById(R.id.editTextcommentaireRealisateur);
        checkBoxPrive = (CheckBox) rootView.findViewById(R.id.checkBoxPrive);
        checkBoxPublic = (CheckBox) rootView.findViewById(R.id.checkBoxPublic);



        Log.v("Upload","Fragment Upload");

        filePath = getArguments().getString("filePath");
        idDefi = getArguments().getString("idDefi");
        defi=getArguments().getString("defi");
        filePath = getArguments().getString("filePath");
        lanceurDefi = getArguments().getString("lanceurDefi");
        typePreuve = getArguments().getString("typePreuve");



        boolean isImage = getArguments().getBoolean("ismage",true);
        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }





        checkBoxPrive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




                if (isChecked) {
                    typeDefi="prive";
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Le defi serait vu que par votre cercle d'amis", Toast.LENGTH_LONG).show();
                }
            }
        });
        checkBoxPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if (isChecked) {
                    typeDefi="public";
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Le défi sera vu par tout le monde", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // uploading the file to server
                if(checkBoxPrive.isChecked()&&checkBoxPublic.isChecked()){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Veuillez choisir qu'un seul type de défi ", Toast.LENGTH_LONG).show();
                }else if(!checkBoxPrive.isChecked()&&!checkBoxPublic.isChecked()){
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Veuillez choisir un type", Toast.LENGTH_LONG).show();
                }else{
                    commentaireRealisateurDefi=editTxtCommentaireRealisateur.getText().toString();
                    new UploadFileToServer().execute();
                }


            }
        });

        return rootView;
    }

    /**
     * Displaying captured image/video on the screen
     * */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConfig.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("defi",new StringBody(defi));
                entity.addPart("idDefi", new StringBody(idDefi));
                entity.addPart("lanceurDefi", new StringBody(lanceurDefi));
                entity.addPart("typePreuve",new StringBody(typePreuve));
                entity.addPart("commentaireRealisateurDefi",new StringBody(commentaireRealisateurDefi));
                entity.addPart("typeDefi",new StringBody(typeDefi));
                Log.v("UploadActivity",typeDefi);

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            //showAlert(result);
            showAlert("Bien envoyé !");

            super.onPostExecute(result);

        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        Fragment PagePersoDefiRecu = new com.example.icu.icu.fragment.PagePersoDefiRecu();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame,PagePersoDefiRecu);
                        fragmentTransaction.commit();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }






}