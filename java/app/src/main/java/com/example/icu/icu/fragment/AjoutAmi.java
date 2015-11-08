package com.example.icu.icu.fragment;

/**
 * Created by Nicolas on 29/03/2015.
 */
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AdapterAmiAjout;

import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AjoutAmi extends Fragment {
    private View rootView;
    private String vueActuelle;
    private EditText editTextRechercherAmi;
    private Button boutonRechercherAmi;
    private ProgressDialog pDialog;
    private HashMap<String,String> map;
    private static final String TAG = Connexion.class.getSimpleName();
    private ArrayList<HashMap<String,String>> aramiAjout;
    private AdapterAmiAjout adapter;
    private ListView listeAjouterAmi;
    private FragmentManager fragmentManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ami_ajouter, container, false);

        vueActuelle="AjoutAmi";
        fragmentManager = getFragmentManager();

        Log.v("VueActuelle:",vueActuelle);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);


        AppConfig.miseEnPlaceMenuAmi(rootView,fragmentManager,vueActuelle);

        editTextRechercherAmi = (EditText)rootView.findViewById(R.id.editTextRechercherAmi);
        boutonRechercherAmi = (Button)rootView.findViewById(R.id.boutonRechercherAmi);
        listeAjouterAmi = (ListView)rootView.findViewById(R.id.listeAjouterAmi);

        listenerBoutonRechercherAmi();
        listenerListeAjouterAmi();
        return rootView;
    }


    private void listenerBoutonRechercherAmi(){
    boutonRechercherAmi.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Log.v("AjoutAmi","Bouton rechercher pressed");
            rechercherAmi(editTextRechercherAmi.getText().toString());
            Toast.makeText(getActivity(),
                    "Recherche de "+ editTextRechercherAmi.getText().toString(), Toast.LENGTH_LONG).show();
        }
    });

}


    private void listenerListeAjouterAmi(){
        listeAjouterAmi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Ajouter un ami");
                adb.setMessage("Laissez lui un petit message ;)");
                final EditText input = new EditText(getActivity());
                adb.setView(input);
                adb.setNegativeButton("Envoyer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String message = input.getEditableText().toString();
                        String pseudo = aramiAjout.get(position).get("pseudo");
                        String pseudoDemandeur = AppConfig.pseudo;


                        Log.v("AlertDialog","Accepter -> " + pseudo+"_"+pseudoDemandeur+"_"+message);
                        ajouterAmi(pseudo, pseudoDemandeur,message);
                        Toast.makeText(getActivity(),
                                "Demande d'ami envoyé", Toast.LENGTH_LONG).show();
                        rafraichirFragmentAjoutAmi();
                    }

                });
                adb.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.v("AlertDialog","annuler");
                    }
                });

                //on affiche la boite de dialogue
                adb.show();
            }
        });
    }

    private void rechercherAmi(final String pseudoOuEmail){

        String tag_string_req = "req_rechercher_ami";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DEFI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Recherche ami: " + response.toString());
                hideDialog();
                try {
                 JSONObject jObj = new JSONObject(response.toString());
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // success=1 ==> there is an array of data = valeurs
                        JSONArray dataValues = jObj.getJSONArray("rechercherAmi");
                        aramiAjout = new ArrayList<HashMap<String, String>>();
                        // loop each row in the array
                        for(int j=0;j<dataValues.length();j++)
                        {
                            JSONObject values = dataValues.getJSONObject(j);
                            String pseudo= values.getString("pseudo");
                            String email= values.getString("email");
                            String age = values.getString("age");
                            String avatar = values.getString("avatar");
                            Log.v("AJOTUAMI",email+"_"+pseudo+"_"+AppConfig.pseudo);
                            //Création d'une HashMap pour insérer les informations du premier item de notre listView


                            if(pseudo!=AppConfig.pseudo){
                            //    Log.v("AJOUTERAMI","Je pase dans le if et appconfig pseudo =" + AppConfig.pseudo+"_");
                                map = new HashMap<String, String>();
                                map.put("pseudo", pseudo);
                                map.put("email", email);
                                map.put("age", age);
                                map.put("avatar", avatar);

                                Log.v("AjouterAmi", pseudo + "_" + age + "_" + email + "_" + avatar);

                                aramiAjout.add(map);

                            }

                        }
                        adapter=new AdapterAmiAjout(getActivity(),aramiAjout);
                        listeAjouterAmi.setAdapter(adapter);

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
                Log.e(TAG, "Recherche ami erreur: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "rechercherAmi");
                params.put("pseudoOuEmail", pseudoOuEmail);
                params.put("pseudoOuEmailPerso", AppConfig.pseudo);
                System.out.println("pseudoOuEmail"  + pseudoOuEmail + "pseudoOuEmailPerso" + AppConfig.pseudo);

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


    private void ajouterAmi(final String pseudo,final String pseudoDemandeur, final String message){

        String tag_string_req = "req_ajouter_ami";


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
                Log.e(TAG, "Affichage ajouter ami erreur: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "ajouterAmi");
                params.put("pseudo", pseudo);
                params.put("pseudoDemandeur", pseudoDemandeur);
                params.put("message", message);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void rafraichirFragmentAjoutAmi(){

        // Une fois que l'ami est accepter ou non, on rafraichi la page
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        Fragment AjoutAmi = new AjoutAmi();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,AjoutAmi);
        fragmentTransaction.commit();
    }
}

