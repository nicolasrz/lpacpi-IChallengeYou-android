package com.example.icu.icu.fonction;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.icu.icu.R;
import com.example.icu.icu.fonction.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterMonReseau extends BaseAdapter {

    private Activity activity;
    private String[] data;
    private ArrayList<String> list;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    private ArrayList<HashMap<String,String>> arh;
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();
    public AdapterMonReseau(Activity a,ArrayList<HashMap<String,String>> ar) {
        arh=ar;

        activity = a;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        // return data.length;
        return arh.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_view_mon_reseau, null);

        // Déclaration des composant du layout

        TextView textRealisateurDefi = (TextView)vi.findViewById(R.id.reseauRealisateurDefi);
        ImageView photoProfil = (ImageView)vi.findViewById(R.id.reseauPhotoProfil);

        TextView textDefi=(TextView)vi.findViewById(R.id.reseauTextViewDefi);
        ImageView imageDefi=(ImageView)vi.findViewById(R.id.reseauImageViewPreuveDefi);
        final VideoView videoDefi = (VideoView)vi.findViewById(R.id.reseauVideoViewPreuveDefi);
        TextView textCommentaireRealisateurDefi = (TextView)vi.findViewById(R.id.reseauCommentaireRealisateurDefi);





        String realisateurDefi = arh.get(position).get("realisateurDefi");
        String descriptiondefi = arh.get(position).get("descriptionDefi");
        String preuveDefi = arh.get(position).get("preuveDefi");
        Log.v("AdapterMonReseau",preuveDefi);
        String commentaireRealisateurDefi = arh.get(position).get("commentaireRealisateurDefi");
        String typePreuve = arh.get(position).get("typePreuve");
        // Affectation valeur des composant.

        textRealisateurDefi.setText(realisateurDefi);
        imageLoader.DisplayImage("http://38.media.tumblr.com/avatar_d9a5049aa42a_48.png",photoProfil);


        textDefi.setText(descriptiondefi);
        if (typePreuve.equals("photo")){
            imageLoader.DisplayImage(preuveDefi,imageDefi);
            videoDefi.setVisibility(View.INVISIBLE);

        }else if(typePreuve.equals("video")){

            imageDefi.setVisibility(View.INVISIBLE);
            Uri video = Uri.parse(preuveDefi);
            videoDefi.setVideoURI(video);
            videoDefi.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.v("AdapterMonReseau", "video touché");
                    if (!bVideoIsBeingTouched) {
                        bVideoIsBeingTouched = true;
                        videoDefi.start();
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                bVideoIsBeingTouched = false;
                            }
                        }, 100);
                        videoDefi.start();
                    }


                    return true;

                }
            });



        }


        textCommentaireRealisateurDefi.setText(commentaireRealisateurDefi);


        return vi;
    }
}