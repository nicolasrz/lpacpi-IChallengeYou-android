package com.example.icu.icu.fonction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icu.icu.R;
import com.example.icu.icu.fragment.Connexion;
import com.example.icu.icu.fragment.DefiAccepte;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nruiz on 17/04/2015.
 */
public class AdapterPagePersoDefiRecu extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater=null;
    private ArrayList<HashMap<String,String>> arh;
    private static final String TAG = Connexion.class.getSimpleName();


    public AdapterPagePersoDefiRecu(Activity a,ArrayList<HashMap<String,String>> ar) {
        arh=ar;
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            vi = inflater.inflate(R.layout.list_view_defi_recu, null);

        // Déclaration des composant du layout
        TextView txtViewLanceurDefi = (TextView) vi.findViewById(R.id.txtViewLanceurDefi);
        TextView txtViewDefi = (TextView) vi.findViewById(R.id.txtViewDefi);
        TextView txtViewIdDefi = (TextView) vi.findViewById(R.id.txtViewIdDefi);


        // Récupération données du arrayhasmap
        String lanceurDefi = arh.get(position).get("lanceurDefi");
        String defi = arh.get(position).get("defi");
        String idDefi=arh.get(position).get("idDefi");


        // Affectation valeur des composant.
        txtViewDefi.setText(defi);
        txtViewLanceurDefi.setText(lanceurDefi);
        txtViewIdDefi.setText(idDefi);




        return vi;
    }






}