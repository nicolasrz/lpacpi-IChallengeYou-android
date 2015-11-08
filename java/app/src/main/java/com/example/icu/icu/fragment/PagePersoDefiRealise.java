package com.example.icu.icu.fragment;

/**
 * Created by Nicolas on 01/04/2015.
 */
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.AppController;
import com.example.icu.icu.fonction.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PagePersoDefiRealise extends Fragment {
    private View rootView;
    private String vueActuelle;
    private FragmentManager fragmentManager;
    public ImageLoader imageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recupererInformationsUtilisateur();
        rootView = inflater.inflate(R.layout.fragment_page_perso_realise, container, false);
        imageLoader=new ImageLoader(getActivity().getApplicationContext());
        vueActuelle="pagePersoDefiRealise";
        fragmentManager = getFragmentManager();
        Log.v("pagePersoDefiRealise",vueActuelle);
        AppConfig.miseEnPlaceMenuPagePerso(rootView,fragmentManager,vueActuelle,getActivity());





        return rootView;
    }

    public void recupererInformationsUtilisateur(){
        String tag_string_req = "req_select_utilisateur";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DEFI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("APPCONFIG",  response.toString());
                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // success=1 ==> there is an array of data = valeurs
                        JSONArray dataValues = jObj.getJSONArray("selectUtilisateur");
                        for(int j=0;j<dataValues.length();j++)
                        {
                            JSONObject values = dataValues.getJSONObject(j);
                            AppConfig.avatar = values.getString("avatar");
                            imageLoader.DisplayImage(AppConfig.avatar,AppConfig.menuPagePersoAvatar);
                            Log.v("APPCONFIG","Lien de lavatar:" + AppConfig.avatar);
                        }
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APPCONFIG", "Affichage invitation ami erreur: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "selectUtilisateur");
                params.put("pseudo", AppConfig.pseudo);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }










}
