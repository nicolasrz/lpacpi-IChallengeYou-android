package com.example.icu.icu.fragment;

/**
 * Created by Nicolas on 11/04/2015.
 */
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AdapterAmiInvitation;
import com.example.icu.icu.fonction.AdapterListeAmiPerso;
import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AmiInvitation extends Fragment {
    private View rootView;
    private String vueActuelle;
    private static final String TAG = Connexion.class.getSimpleName();
    private AdapterAmiInvitation adapter;
    private ListView listeInvitationAmi;
    private ProgressDialog pDialog;
    private ArrayList<HashMap<String,String>> aramiInvitation;
    private HashMap<String,String> map;
    private String pseudoConnecte = null;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ami_invitation, container, false);
        vueActuelle="AmiInvitation";
        fragmentManager=getFragmentManager();
        listeInvitationAmi = (ListView)rootView.findViewById(R.id.listeInvitationAmi);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pseudoConnecte = AppConfig.pseudo;

        AppConfig.miseEnPlaceMenuAmi(rootView,fragmentManager,vueActuelle);
        recupererAmiInvitation();
        listenerListeAmi();



        return rootView;
    }


    private void listenerListeAmi(){
        listeInvitationAmi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Invitation");
                adb.setMessage("Que voulez-vous faire ?");
                adb.setNegativeButton("Accepter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String amiFrom = aramiInvitation.get(position).get("amiFrom");
                        Log.v("AlertDialog", "Accepter -> " + amiFrom);
                        accepterAmi(amiFrom);

                    }

                });
                adb.setPositiveButton("Refuser", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.v("AlertDialog", "refuser");
                        String amiFrom = aramiInvitation.get(position).get("amiFrom");
                        refuserAmi(amiFrom);

                    }
                });

                //on affiche la boite de dialogue
                adb.show();
            }
        });
    }

    private void recupererAmiInvitation(){

        String tag_string_req = "req_invitation_recu";

        pDialog.setMessage("Affichage des invitations");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DEFI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Ami-invitation reçu: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // success=1 ==> there is an array of data = valeurs
                        JSONArray dataValues = jObj.getJSONArray("invitationRecu");
                        // loop each row in the array
                        aramiInvitation = new ArrayList<HashMap<String, String>>();

                        for(int j=0;j<dataValues.length();j++)
                        {
                            JSONObject values = dataValues.getJSONObject(j);
                            String amiLien = values.getString("amiLien");
                            String amiFrom= values.getString("amiFrom");
                            String amiTo= values.getString("amiTo");
                            String amiDate = values.getString("amiDate");
                            String amiTexteDemande = values.getString("amiTexteDemande");

                            //Création d'une HashMap pour insérer les informations du premier item de notre listView

                            map = new HashMap<String, String>();
                            map.put("amiFrom", amiFrom);
                            map.put("amiTo", amiTo);
                            map.put("amiDate", amiDate);
                            map.put("amiLien", amiLien);
                            map.put("amiTexteDemande",amiTexteDemande);
                            Log.v("Ami-invitation", amiFrom + "_" + amiTo + "_" + amiDate + "_" + amiLien + "_" + amiTexteDemande);
                            aramiInvitation.add(map);

                        }
                        adapter=new AdapterAmiInvitation(getActivity(),aramiInvitation);
                        listeInvitationAmi.setAdapter(adapter);



                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Affichage invitation ami erreur: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "invitationRecu");
                params.put("pseudo", pseudoConnecte);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void accepterAmi(final String amiFrom){

        String tag_string_req = "req_accepter_ami";


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
                Log.e(TAG, "Affichage accepter ami erreur: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "accepterAmi");
                params.put("amiFrom", amiFrom);
                params.put("amiTo", AppConfig.pseudo);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);



        // Une fois que l'ami est accepter ou non, on rafraichi la page
        rafraichirFragmentInvitation();
    }
    private void refuserAmi(final String amiFrom){
        String tag_string_req = "req_refuser_ami";


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
                Log.e(TAG, "Echec refus ami: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "refuserAmi");
                params.put("amiFrom", amiFrom);
                params.put("amiTo", AppConfig.pseudo);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        rafraichirFragmentInvitation();




    }

    private void rafraichirFragmentInvitation(){

        // Une fois que l'ami est accepter ou non, on rafraichi la page
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        Fragment AmiInvitation = new AmiInvitation();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,AmiInvitation);
        fragmentTransaction.commit();
    }

}
