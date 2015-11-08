package com.example.icu.icu.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AdapterPagePersoDefiRecu;
import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.DialogInterface;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by Nicolas on 22/03/2015.
 */
public class PagePersoDefiRecu extends Fragment{

    private ProgressDialog pDialog;
    private static final String TAG = Connexion.class.getSimpleName();
    private String pseudo;
    private View rootView;
    private String idDefi=null;
    private HashMap<String,String> map;
    private ArrayList<HashMap<String,String>> arlDefi;
    private String vueActuelle;
    private FragmentManager fragmentManager;
    private AdapterPagePersoDefiRecu adapter;
    private ListView listeDefi;

    public PagePersoDefiRecu(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_page_perso_defi_recu,container,false);
        vueActuelle="pagePersoDefiRecu";
        fragmentManager = getFragmentManager();
        AppConfig.miseEnPlaceMenuPagePerso(rootView,fragmentManager,vueActuelle,getActivity());
        Log.v("PagePersoDefiRecu",vueActuelle);


        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pseudo = AppConfig.pseudo;

        listeDefi = (ListView)rootView.findViewById(R.id.listViewPagePersoDefiRecu);

        recupererDefiRecu();
        listenerListeDefiRecu();
        return rootView;

    }

    private void listenerListeDefiRecu(){

        listeDefi.setOnItemClickListener(new OnItemClickListener() {
        @Override

        public void onItemClick(AdapterView<?> a, View v, int position, final long id) {

            final String defi = arlDefi.get(position).get("defi");
            final String lanceurdefi = arlDefi.get(position).get("lanceurDefi");
            final String idDefi = arlDefi.get(position).get("idDefi");
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setTitle("Faire ce defi ?");
            adb.setMessage(defi);
            adb.setNegativeButton("CAP !", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Log.v("AlertDialog","Cap pressed");
                   Log.v("AlertDialog",idDefi+"_"+defi+"_"+lanceurdefi);

                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment DefiAccepteFragment = new DefiAccepte();
                    Bundle args = new Bundle();
                    args.putString("lanceurDefi",lanceurdefi);
                    args.putString("defi",defi);
                    args.putString("idDefi",idDefi);
                    Log.v("PagePersoDefiRecu", "envoie des parametres "+idDefi+"_"+defi+"_"+lanceurdefi);
                    DefiAccepteFragment.setArguments(args);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,DefiAccepteFragment);
                    fragmentTransaction.commit();


                }

            });
            adb.setPositiveButton("Pas CAP:s !", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Log.v("AlertDialog","Pas cap pressed");

                }
            });
            adb.setNeutralButton("Plus tard !", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Log.v("AlertDialog","Neutre pressed");

                }
            });
            //on affiche la boite de dialogue
            adb.show();


        }



    });



}




    private void recupererDefiRecu(){
        String tag_string_req = "req_defi_recu";

        pDialog.setMessage("affichage défi reçu....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_DEFI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Défi reçu: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.toString());
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // success=1 ==> there is an array of data = valeurs
                        JSONArray dataValues = jObj.getJSONArray("valeurDefi");
                        arlDefi = new ArrayList<HashMap<String, String>>();
                        // loop each row in the array
                        for(int j=0;j<dataValues.length();j++)
                        {
                            JSONObject values = dataValues.getJSONObject(j);
                            String lanceurDefi= values.getString("lanceurDefi");
                            String defi= values.getString("resumeDefi");
                            String idDefi = values.getString("idDefi");


                            map = new HashMap<String, String>();
                            map.put("lanceurDefi",lanceurDefi);
                            map.put("defi",defi);
                            map.put("idDefi",idDefi);

                            arlDefi.add(map);
                            Log.i("Row "+(j+1), lanceurDefi+" - "+defi + " - " + idDefi);
                        }
                        adapter=new AdapterPagePersoDefiRecu(getActivity(),arlDefi);
                        listeDefi.setAdapter(adapter);

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
                Log.e(TAG, "Affichage defi reçu erreur: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "defiRecu");
                params.put("pseudo", pseudo);

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
