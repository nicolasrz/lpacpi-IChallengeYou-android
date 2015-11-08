package com.example.icu.icu.fragment;

/**
 * Created by Nicolas on 01/04/2015.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AdapterListeAmiPerso;
import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ami extends Fragment{
    private AdapterListeAmiPerso adapter;
    private ListView listeAmi;
    boolean connecte=false;
    private ProgressDialog pDialog;
    private static final String TAG = Connexion.class.getSimpleName();
    private HashMap<String,String> map;
    private ArrayList<HashMap<String,String>> arla;
    private String pseudoConnecte = null;
    private FragmentManager fragmentManager;
    View rootView;
    private String vueActuelle;
    private String typeMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pseudoConnecte = AppConfig.pseudo;
        if(pseudoConnecte!=null){
            connecte=true;
        }
        fragmentManager = getFragmentManager();
        rootView = inflater.inflate(R.layout.fragment_ami, container, false);
        vueActuelle="Ami";
        Bundle args = getArguments();

        // Je suis dans Page Perso et que je clique sur ami je change le menu
        if(args!=null){
            typeMenu = args.getString("menu");
            if(typeMenu!=null){
                rootView.findViewById(R.id.menu_ami).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.menu_ami).setVisibility(View.GONE);
                AppConfig.miseEnPlaceMenuPagePerso(rootView,fragmentManager,vueActuelle,getActivity());
            }
         //sinon je laisse le menu de base de la page Ami
        }else{
            rootView.findViewById(R.id.menu_ami).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.menu_page_perso).setVisibility(View.GONE);
            AppConfig.miseEnPlaceMenuAmi(rootView,fragmentManager,vueActuelle);
            typeMenu=null;


        }


        listeAmi = (ListView)rootView.findViewById(R.id.listAmi);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);



        recupererAmiFromBdd();



        return rootView;
    }




    private void recupererAmiFromBdd(){

            String tag_string_req = "req_defi_recu";

            pDialog.setMessage("Affichage de la liste d'ami");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_GET_DEFI, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Ami-ListeAmi: " + response.toString());
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
                                Log.v("Ami-listeami",amiFrom +"_"+amiTo+"_"+amiDate+"_"+amiLien);
                                arla.add(map);

                            }
                            adapter=new AdapterListeAmiPerso(getActivity(),arla,typeMenu);
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





}

