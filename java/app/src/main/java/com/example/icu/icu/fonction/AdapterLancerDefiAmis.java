package com.example.icu.icu.fonction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fragment.Ami;
import com.example.icu.icu.fragment.Connexion;
import com.example.icu.icu.fragment.DefiAccepte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nruiz on 02/04/2015.
 */
public class AdapterLancerDefiAmis extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    private ArrayList<HashMap<String,String>> arh;

    private static final String TAG = Connexion.class.getSimpleName();
    private String amiLien =null;
    private String ami=null;
    public static HashMap<Integer,String> hmAmi = new HashMap<Integer,String>();




    public AdapterLancerDefiAmis(Activity a,ArrayList<HashMap<String,String>> ar) {
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



    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_view_lancer_defi_ami, null);




        // Déclaration des composant du layout
        final TextView txtViewLancerDefiAmi = (TextView) vi.findViewById(R.id.txtViewLancerDefiAmi);
        final CheckBox checkBoxLancerDefi = (CheckBox) vi.findViewById(R.id.checkBoxLancerDefi);



        // Récupération données du arrayhasmap
        amiLien = arh.get(position).get("amiLien");
        String amiTo = arh.get(position).get("amiTo");
        final String amiFrom = arh.get(position).get("amiFrom");

        if(amiTo.equals(AppConfig.pseudo)){
            ami=amiFrom;
        }else{
            ami=amiTo;
        }

        // SetTExt
        txtViewLancerDefiAmi.setText(ami);

        checkBoxLancerDefi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String amiChoisi = txtViewLancerDefiAmi.getText().toString();
                    hmAmi.put(position,amiChoisi);
                    Log.v("AdapterLancerDefi","Ajout " + hmAmi.get(position));
                }else{
                    Log.v("AdapterLancerDefi","Remove " + hmAmi.get(position));
                  hmAmi.remove(position);
                }


            }
        });

        return vi;
    }



}