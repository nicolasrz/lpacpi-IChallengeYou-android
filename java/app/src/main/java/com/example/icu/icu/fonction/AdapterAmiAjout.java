package com.example.icu.icu.fonction;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fragment.Connexion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nruiz on 15/04/2015.
 */
public class AdapterAmiAjout extends BaseAdapter {

    private Activity activity;
    private String[] data;
    private ArrayList<String> list;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    private ArrayList<HashMap<String,String>> arh;
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();
    private static final String TAG = Connexion.class.getSimpleName();


    public AdapterAmiAjout(Activity a,ArrayList<HashMap<String,String>> ar) {
        arh=ar;
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
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
            vi = inflater.inflate(R.layout.list_view_ami_ajouter, null);

        // Déclaration des composant du layout
        TextView  txtViewAjouterAmiPseudo =(TextView) vi.findViewById(R.id.txtViewAjouterAmiPseudo);
        TextView  txtViewAjouterAmiEmail =(TextView) vi.findViewById(R.id.txtViewAjouterAmiEmail);
        TextView  txtViewAjouterAmiAge =(TextView) vi.findViewById(R.id.txtViewAjouterAmiAge);
        ImageView imageViewAjouterAmiAvatar = (ImageView) vi.findViewById(R.id.imageViewAjouterAmiAvatar);


        // Récupération données du arrayhasmap
        String pseudo = arh.get(position).get("pseudo");
        String email = arh.get(position).get("email");
        String age = arh.get(position).get("age");
        String avatar = arh.get(position).get("avatar");



        // Affectation valeur des composant.

            txtViewAjouterAmiPseudo.setText(pseudo);
            txtViewAjouterAmiEmail.setText(email);
            txtViewAjouterAmiAge.setText(age);
            imageLoader.DisplayImage(avatar,imageViewAjouterAmiAvatar);


        return vi;
    }






}