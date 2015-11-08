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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.AppController;
import com.example.icu.icu.fonction.SQLiteHandler;
import com.example.icu.icu.fonction.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicolas on 22/03/2015.
 */
public class DefiRecu extends Fragment{

    private ProgressDialog pDialog;
    private static final String TAG = Connexion.class.getSimpleName();
    private SessionManager session;
    private ListView lv;
    private List<String> myListofData ;
    private SQLiteHandler db;
    private String pseudo;
    private ListView maListViewPerso;
    ArrayList<HashMap<String, String>> listItem;
    private View rootView;
    private String lanceurDefiChoisi=null;
    private String descriptionDefiChoisi=null;
    private String idDefi=null;
    private TextView txtViewPseudo;


    public DefiRecu(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_defirecu,container,false);
        //lv=(ListView)rootView.findViewById(R.id.listView1);
        maListViewPerso = (ListView)rootView.findViewById(R.id.listviewperso);
        //myListofData = new ArrayList<String>();
        //Création de la ArrayList qui nous permettra de remplir la listView
        listItem = new ArrayList<HashMap<String, String>>();
        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        session = new SessionManager(getActivity().getApplicationContext());
        db = new SQLiteHandler(getActivity().getApplicationContext());
        session = new SessionManager(getActivity().getApplicationContext());
        // Fetching user details from sqlite
     //   HashMap<String, String> user = db.getUserDetails();
        pseudo = AppConfig.pseudo;

      //  txtViewPseudo = (TextView) rootView.findViewById(R.id.textViewPseudo);

        recupererDefiRecu();




        return rootView;
    }

    private void mettreAdapter(){


    //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
    SimpleAdapter mSchedule = new SimpleAdapter (this.getActivity().getBaseContext(), listItem, android.R.layout.simple_list_item_2,
        //    SimpleAdapter mSchedule = new SimpleAdapter (this.getActivity().getBaseContext(), listItem, R.layout.list_view_dernier_defi,
            new String[] {"lanceurDefi", "descriptionDefi","idDefi"}, new int[] {R.id.lanceurDefi, R.id.textViewDefi});

  //On attribue à notre listView l'adapter que l'on vient de créer
    maListViewPerso.setAdapter(mSchedule);
    maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
        @Override
        @SuppressWarnings("unchecked")
        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            //on récupère la HashMap contenant les infos de notre item (titre, description, img)
            HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
            lanceurDefiChoisi=map.get("lanceurDefi");
            descriptionDefiChoisi=map.get("descriptionDefi");
            idDefi=map.get("idDefi");



            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setTitle("Faire ce defi ?");
            adb.setMessage(descriptionDefiChoisi);
            adb.setNegativeButton("CAP !", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Log.v("AlertDialog","Cap pressed");

                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment DefiAccepteFragment = new DefiAccepte();
                    Bundle args = new Bundle();
                    args.putString("lanceurDefiChoisi",lanceurDefiChoisi);
                    args.putString("descriptionDefiChoisi",descriptionDefiChoisi);
                    args.putString("idDefi",idDefi);
                    Log.v("DefiRecu", "envoie du parametre idDefi" + idDefi);
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



    private void afficherListeDefiRecu(String lanceurDefi,String defi,String idDefi){
        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        //Création d'une HashMap pour insérer les informations du premier item de notre listView
        map = new HashMap<String, String>();
        map.put("lanceurDefi", lanceurDefi);
        map.put("descriptionDefi", defi);
        map.put("idDefi", idDefi);

        listItem.add(map);


        mettreAdapter();

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
                        // loop each row in the array
                        for(int j=0;j<dataValues.length();j++)
                        {
                            JSONObject values = dataValues.getJSONObject(j);
                            String lanceurDefi= values.getString("lanceurDefi");
                            String defi= values.getString("resumeDefi");
                            String idDefi = values.getString("idDefi");

                            afficherListeDefiRecu(lanceurDefi,defi,idDefi);
                            Log.i("Row "+(j+1), lanceurDefi+" - "+defi + " - " + idDefi);
                        }

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
