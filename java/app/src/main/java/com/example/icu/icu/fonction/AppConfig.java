package com.example.icu.icu.fonction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.icu.icu.MainActivity;
import com.example.icu.icu.R;
import com.example.icu.icu.fragment.Ami;
import com.example.icu.icu.fragment.AmiInvitation;
import com.example.icu.icu.fragment.PagePersoDefiRecu;

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
public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://37.187.122.209/icu/";

    // Server user register url
    public static String URL_REGISTER = "http://37.187.122.209/icu/index.php";
    public static String URL_GET_DEFI = "http://37.187.122.209/icu/index.php";
    public static  String pseudo=null;
    public static String email=null;
    public static String avatar=null;
    public static ImageView menuPagePersoAvatar;


    public static final String FILE_UPLOAD_URL = "http://37.187.122.209/icu/uploadPhoto.php";

    public static String URL_UPLOAD_AVATAR = "http://37.187.122.209/icu/uploadPhoto.php";
    public static String IMAGE_DIRECTORY_NAME ="photo";
    public static String DIR_UPLOAD_PREUVE="http://37.187.122.209/icu/";



    public static void miseEnPlaceMenuPagePerso(View rootView,
                                                final FragmentManager fragmentManager,
                                                final String vueActuelle, final Activity activity) {


        //Partie haute du menu

        TextView menuPagePersoPseudo;
        ImageView menuPagePersoBoutonAction;

        menuPagePersoAvatar = (ImageView) rootView.findViewById(R.id.menuPagePersoAvatar);
        menuPagePersoPseudo = (TextView) rootView.findViewById(R.id.menuPagePersoPseudo);
        menuPagePersoBoutonAction = (ImageView) rootView.findViewById(R.id.menuPagePersoBoutonAction);
        final CharSequence[] items = {"Demander en ami", "Envoyer defi", "Poke"};
        final boolean[] selected = {true, false, true};
        menuPagePersoBoutonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Action")
                        .setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialogInterface, int item, boolean b) {
                                Log.d("Myactivity", String.format("%s: %s", items[item], b));
                            }
                        });

                builder.create().show();
            }
        });


        // Menu
        ImageView pagePersoAmi;
        ImageView pagePersoDefiRealise;
        ImageView pagePersoDefiLance;
        ImageView pagePersoDefiRecu;
        pagePersoDefiRealise = (ImageView) rootView.findViewById(R.id.menuPagePersoDefiRealise);
        pagePersoDefiRecu = (ImageView) rootView.findViewById(R.id.menuPagePersoDefiRecu);
        //  pagePersoDefiLance = (ImageView) rootView.findViewById(R.id.menuPagePersoDefiLance);
        //  pagePersoAmi = (ImageView) rootView.findViewById(R.id.menuPagePersoAmi);


        pagePersoDefiRealise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("pagePersoDefiRealise", "pagePersoDefiRealise pushed");
                //   FragmentManager fragmentManager = getFragmentManager();
                Fragment PagePersoDefiRealise = new com.example.icu.icu.fragment.PagePersoDefiRealise();

                if (vueActuelle != "pagePersoDefiRealise") {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, PagePersoDefiRealise);
                    fragmentTransaction.commit();
                }


            }
        });

        /*pagePersoAmi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami","pagePersoAmi  pushed");

              //  FragmentManager fragmentManager = getFragmentManager();
                Fragment Ami = new com.example.icu.icu.fragment.Ami();
                if(vueActuelle!="Ami") {
                    Bundle args = new Bundle();
                    args.putString("menu","menu_page_perso");
                    Ami.setArguments(args);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,Ami);
                    fragmentTransaction.commit();
                }


            }
        });
*/
        pagePersoDefiRecu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami", "pagePersoDefiRecu  pushed");
                //FragmentManager fragmentManager = getFragmentManager();
                Fragment DefiRecu = new PagePersoDefiRecu();
                if (vueActuelle != "pagePersoDefiRecu") {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, DefiRecu);
                    fragmentTransaction.commit();
                }
            }
        });

        /*pagePersoDefiLance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami","pagePersoDefiLance  pushed");
               // FragmentManager fragmentManager = getFragmentManager();
                Fragment DefiLance = new com.example.icu.icu.fragment.DefiLance();
                if(vueActuelle!="pagePersoDefiLance") {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, DefiLance);
                    fragmentTransaction.commit();
                }

            }
        });

*/
    }
    public static void miseEnPlaceMenuAmi(View rootView,
                                    final FragmentManager fragmentManager,
                                    final String vueActuelle){
        ImageView menuListeAmis;
        ImageView menuAjouterAmi;
        ImageView menuInvitationRecu;
        ImageView menuContactTel;
        menuListeAmis = (ImageView) rootView.findViewById(R.id.menuListeDesAmis);
        menuAjouterAmi = (ImageView) rootView.findViewById(R.id.menuAjouterAmi);
        menuInvitationRecu = (ImageView)rootView.findViewById(R.id.menuInvitationRecu);
       // menuContactTel = (ImageView)rootView.findViewById(R.id.menuContactTelephone);

        menuListeAmis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami", "menuLsite ami pushed");
                Fragment AmiListe = new Ami();

                if(vueActuelle!="Ami"){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,AmiListe);
                    fragmentTransaction.commit();
                }


            }
        });

        menuAjouterAmi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami","menuAjouterAmi  pushed");
                Fragment AjoutAmi = new com.example.icu.icu.fragment.AjoutAmi();
                if(vueActuelle!="AmiAjout") {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, AjoutAmi);
                    fragmentTransaction.commit();
                }


            }
        });

        menuInvitationRecu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami","menuInvitation ami pushed");
                Fragment AmiInvitation = new com.example.icu.icu.fragment.AmiInvitation();
                if(vueActuelle!="AmiInvitation") {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, AmiInvitation);
                    fragmentTransaction.commit();
                }
            }
        });

        /*menuContactTel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami","menuContact ami pushed");
                Fragment AmiInvitation = new AmiInvitation();
                if(vueActuelle!="AmiContact") {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, AmiInvitation);
                    fragmentTransaction.commit();
                }

            }
        });*/
    }
}