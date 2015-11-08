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
import android.widget.ImageView;
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
import java.util.Map;

/**
 * Created by nruiz on 02/04/2015.
 */
public class AdapterListeAmiPerso extends BaseAdapter {

    private Activity activity;
    private String[] data;
    private ArrayList<String> list;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    private ArrayList<HashMap<String,String>> arh;
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();
    private static final String TAG = Connexion.class.getSimpleName();
    private String amiLien =null;
    private String ami=null;
    private String menu;
    private String personneASupprimer;
    private String lienASupprimer;

    public AdapterListeAmiPerso(Activity a,ArrayList<HashMap<String,String>> ar,String typeMenu) {
        arh=ar;
        menu=typeMenu;
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
            vi = inflater.inflate(R.layout.list_view_ami, null);

        // Déclaration des composant du layout
        final TextView txtViewPseudoAmi = (TextView) vi.findViewById(R.id.pseudoAmi);
        ImageView imgViewSupprimerAmi = (ImageView)vi.findViewById(R.id.supprimerAmi);
        final TextView txtViewAmiLien = (TextView) vi.findViewById(R.id.amiLien);
        // Récupération données du arrayhasmap
        amiLien = arh.get(position).get("amiLien");

        String amiTo = arh.get(position).get("amiTo");
        String amiFrom = arh.get(position).get("amiFrom");

        if(amiTo.equals(AppConfig.pseudo)){
            ami=amiFrom;
        }else{
            ami=amiTo;
        }

        if(menu!=null){
            imgViewSupprimerAmi.setVisibility(View.GONE);
        }

        txtViewPseudoAmi.setText(ami);
        txtViewAmiLien.setText(amiLien);


        // Listener sur icone de suppression ami
        imgViewSupprimerAmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setTitle("Suppression Ami");
                 personneASupprimer = txtViewPseudoAmi.getText().toString();
                 lienASupprimer = txtViewAmiLien.getText().toString();
                adb.setMessage("Etes-vous sur de vouloir supprimer " + personneASupprimer + " de vos amis ?");
                adb.setNegativeButton("Oui !", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.v("AlertDialog","Ok pour suppresion "+personneASupprimer+" qui a le lien" + lienASupprimer);
                        supprimerAmi(lienASupprimer);

                    }

                });
                adb.setPositiveButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.v("AlertDialog","Annuler...");

                    }
                });

                //on affiche la boite de dialogue
                adb.show();
            }
        });



        return vi;
    }


    private void supprimerAmi(final String lienASupprimer){

            String tag_string_req = "req_supprimer_ami";


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_GET_DEFI, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                   // Log.d(TAG, "Supprimer Ami: " + response.toString());
                    // Ne rien faire l'ami est supprimé
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Affichage suppression ami erreur: " + error.getMessage());
                    Toast.makeText(activity,
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("tag", "supprimerAmi");
                    params.put("amiLien", lienASupprimer);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



        // Une fois que l'ami est supprimé on rafraichi le fragment liste ami
        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment Ami = new Ami();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,Ami);
        fragmentTransaction.commit();


    }
}