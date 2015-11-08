package com.example.icu.icu.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AdapterMonReseau;
import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.AppController;
import com.example.icu.icu.fonction.LazyAdapter;
import com.example.icu.icu.fonction.SQLiteHandler;
import com.example.icu.icu.fonction.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicolas on 22/03/2015.
 */
public class MonReseau extends Fragment{

    private ProgressDialog pDialog;
    private static final String TAG = MonReseau.class.getSimpleName();
    private SessionManager session;

    private SQLiteHandler db;
    private String pseudo;
    private ListView listViewMonReseau;
    private View rootView;
    private String lanceurDefiChoisi=null;
    private String realisateurDefiChoisi=null;
    private String descriptionDefiChoisi=null;
    private String idDefi=null;
    private TextView txtViewPseudo;
    private String cheminPreuve;
    private AdapterMonReseau adapter;

    private ArrayList<String> list;
    private HashMap<String,String> map;
    private ArrayList<HashMap<String,String>> arh;


    public MonReseau(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_mon_reseau,container,false);
        listViewMonReseau = (ListView)rootView.findViewById(R.id.listViewMonReseau);

        //Création de la ArrayList qui nous permettra de remplir la listView

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        session = new SessionManager(getActivity().getApplicationContext());
        db = new SQLiteHandler(getActivity().getApplicationContext());
        session = new SessionManager(getActivity().getApplicationContext());
        String pseudoConnecte = AppConfig.pseudo;
        recupererDefiRecu();
        return rootView;
    }




    private void recupererDefiRecu(){
        String tag_string_req = "req_reseau_defi_recu";

        pDialog.setMessage("reseau défi ....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DEFI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Mon reseau: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            // success=1 ==> there is an array of data = valeurs
                            JSONArray dataValues = jObj.getJSONArray("cerclePrive");
                            // loop each row in the array
                            arh = new ArrayList<HashMap<String, String>>();

                            for (int j = 0; j < dataValues.length(); j++) {
                                JSONObject values = dataValues.getJSONObject(j);
                                String lanceurDefi = values.getString("lanceurDefi");
                                String defi = values.getString("defi");
                                String idDefi = values.getString("idDefi");
                                String cheminPreuve = values.getString("cheminPreuve");
                                String realisateurDefi = values.getString("realisateurDefi");
                                String typePreuve = values.getString("typePreuve");
                                String commentaireRealisateurDefi = values.getString("commentaireRealisateurDefi");
                                //Création d'une HashMap pour insérer les informations du premier item de notre listView
                                map = new HashMap<String, String>();
                                map.put("lanceurDefi", lanceurDefi);
                                map.put("realisateurDefi", realisateurDefi);
                                map.put("descriptionDefi", defi);
                                map.put("idDefi", idDefi);
                                String preuveDefi = AppConfig.DIR_UPLOAD_PREUVE + "/" + cheminPreuve;
                                map.put("preuveDefi", AppConfig.DIR_UPLOAD_PREUVE + "/" + cheminPreuve);
                                map.put("typePreuve", typePreuve);
                                map.put("commentaireRealisateurDefi", commentaireRealisateurDefi);
                                arh.add(map);
                                Log.i("Row " + (j + 1), "LanceurDefi=" + lanceurDefi + " - " + "defi=" + defi + " - " + "iddefi=" + idDefi + " - " + "imagePreuve=" + " - " + "realisateurDefi=" + realisateurDefi);
                            }


                            adapter = new AdapterMonReseau(getActivity(), arh);
                            listViewMonReseau.setAdapter(adapter);


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
                Log.e(TAG, "Affichage reseau defi erreur: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "cerclePrive");
                params.put("pseudo", AppConfig.pseudo);

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



}
