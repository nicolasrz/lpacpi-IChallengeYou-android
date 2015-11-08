package com.example.icu.icu.fragment;

import android.app.Fragment;
import android.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fonction.SQLiteHandler;
import com.example.icu.icu.fonction.SessionManager;

import java.util.HashMap;

/**
 * Created by Nicolas on 31/03/2015.
 */
public class Deconnexion extends Fragment {


    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;

    public Deconnexion() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deconnexion, container, false);

        btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });



        //SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());

        if (!session.isLoggedIn()) {
            AppConfig.pseudo=null;
            logoutUser();

        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");





        return rootView;
    }



    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        //  Intent intent = new Intent(MainActivity.this, Connexion.class);
        //startActivity(intent);
        //finish();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new Connexion()).commit();
    }
}
