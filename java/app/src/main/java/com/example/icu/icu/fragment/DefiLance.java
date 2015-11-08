package com.example.icu.icu.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.icu.icu.R;
import android.app.Fragment;
/**
 * Created by nruiz on 17/04/2015.
 */
public class DefiLance extends Fragment {
    private View rootView;
    private ImageView pagePersoAmi;
    private ImageView pagePersoDefiRealise;
    private ImageView pagePersoDefiLance;
    private ImageView pagePersoDefiRecu;
    private String vueActuelle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.affichage_defi, container, false);
        vueActuelle="pagePersoDefiLance";

        miseEnPlaceMenu();
        return rootView;
    }


    private void miseEnPlaceMenu(){
        pagePersoDefiRealise = (ImageView)rootView.findViewById(R.id.menuPagePersoDefiRealise);
        pagePersoDefiRecu = (ImageView)rootView.findViewById(R.id.menuPagePersoDefiRecu);
       // pagePersoDefiLance = (ImageView) rootView.findViewById(R.id.menuPagePersoDefiLance);
        //pagePersoAmi = (ImageView) rootView.findViewById(R.id.menuPagePersoAmi);



        pagePersoDefiRealise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("pagePersoDefiRealise", "pagePersoDefiRealise pushed");
                FragmentManager fragmentManager = getFragmentManager();
                Fragment PagePersoDefiRealise = new PagePersoDefiRealise();

                if(vueActuelle!="pagePersoDefiRealise"){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,PagePersoDefiRealise);
                    fragmentTransaction.commit();
                }


            }
        });

        pagePersoAmi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami","pagePersoAmi  pushed");

                FragmentManager fragmentManager = getFragmentManager();
                Fragment Ami = new Ami();
                if(vueActuelle!="pagePersoAmi") {
                    Bundle args = new Bundle();
                    args.putString("menu","menu_page_perso");
                    Ami.setArguments(args);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,Ami);
                    fragmentTransaction.commit();
                }


            }
        });

        pagePersoDefiRecu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami","pagePersoDefiRecu  pushed");
                FragmentManager fragmentManager = getFragmentManager();
                Fragment DefiRecu = new PagePersoDefiRecu();
                if(vueActuelle!="pagePersoDefiRecu") {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, DefiRecu);
                    fragmentTransaction.commit();
                }
            }
        });

        pagePersoDefiLance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Ami","pagePersoDefiLance  pushed");
                FragmentManager fragmentManager = getFragmentManager();
                Fragment DefiLance = new DefiLance();
                if(vueActuelle!="pagePersoDefiLance") {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, DefiLance);
                    fragmentTransaction.commit();
                }

            }
        });
    }
}
