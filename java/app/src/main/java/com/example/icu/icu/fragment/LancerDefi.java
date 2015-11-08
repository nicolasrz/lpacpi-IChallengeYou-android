package com.example.icu.icu.fragment;

/**
 * Created by Nicolas on 01/04/2015.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AdapterLancerDefiAmis;
import com.example.icu.icu.fonction.AdapterListeAmiPerso;
import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class LancerDefi extends Fragment  {
    private View rootView;
    private ListView listeAmi;
    private ProgressDialog pDialog;
    private static final String TAG = LancerDefi.class.getSimpleName();
    private HashMap<String,String> map;
    private ArrayList<HashMap<String,String>> arla;
    private AdapterLancerDefiAmis adapter;
    private String pseudoConnecte=null;
    private boolean connecte;
    private CheckBox checkBoxLancerDefi;
    private Button buttonEnvoyerDefi;
    private EditText editTextLancerDefi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lancer_defi, container, false);
        buttonEnvoyerDefi = (Button) rootView.findViewById(R.id.buttonEnvoyerDefi);
        editTextLancerDefi =(EditText) rootView.findViewById(R.id.editTextLancerDefi);
        pseudoConnecte = AppConfig.pseudo;
        listeAmi = (ListView)rootView.findViewById(R.id.listAmiLancerDefi);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        recupererAmiFromBdd();

        buttonEnvoyerDefi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("LancerDefi", "button pres");
                lancerDefi();
            }
        });



        if(pseudoConnecte!=null){
            connecte=true;
        }



        return rootView;
    }


    private void recupererAmiFromBdd(){

        String tag_string_req = "req_liste_amis";

        pDialog.setMessage("Affichage de la liste d'ami");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DEFI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "LancerDefi-ListeAmi: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // success=1 ==> there is an array of data = valeurs
                        JSONArray dataValues = jObj.getJSONArray("listeAmi");
                        // loop each row in the array
                        arla = new ArrayList<HashMap<String, String>>();

                        for(int j=0;j<dataValues.length();j++)
                        {
                            JSONObject values = dataValues.getJSONObject(j);
                            String amiLien = values.getString("amiLien");
                            String amiFrom= values.getString("amiFrom");
                            String amiTo= values.getString("amiTo");
                            String amiDate = values.getString("amiDate");

                            //Création d'une HashMap pour insérer les informations du premier item de notre listView

                            map = new HashMap<String, String>();
                            map.put("amiFrom", amiFrom);
                            map.put("amiTo", amiTo);
                            map.put("amiDate", amiDate);
                            map.put("amiLien", amiLien);
                            Log.v("LancerDefi-listeami",amiFrom +"_"+amiTo+"_"+amiDate+"_"+amiLien);
                            arla.add(map);

                        }
                        adapter=new AdapterLancerDefiAmis(getActivity(),arla);
                        listeAmi.setAdapter(adapter);


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
                Log.e(TAG, "Affichier liste ami erreur: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "listeAmi");
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

    private void lancerDefi(){

        Iterator it = AdapterLancerDefiAmis.hmAmi.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Log.v("LancerDefi",pair.getKey() + " = " + pair.getValue());
            final String realisateurDefi =pair.getValue().toString();
           final String defi =  editTextLancerDefi.getText().toString();

            String tag_string_req = "envoyerDefi";

            pDialog.setMessage("Envoie du defi ...");
            showDialog();


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_REGISTER, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());

                    hideDialog();
    /*
                    nothing to do
    */
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Envoie Defi erreur: " + error.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();

                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("tag", "envoyerDefi");
                    params.put("defi", defi);
                    params.put("lanceurDefi", pseudoConnecte);
                    params.put("realisateurDefi", realisateurDefi);



                    return params;
                }

            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            Log.v("LancerDefi", "Envoie requete pour lancer defi");
        }

        Toast.makeText(getActivity(),
                "Défi envoyé !", Toast.LENGTH_LONG).show();

    }




}
