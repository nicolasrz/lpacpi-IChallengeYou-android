package com.example.icu.icu.fragment;

/**
 * Created by Nicolas on 29/03/2015.
 */
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.icu.icu.R;
import com.example.icu.icu.fonction.AppConfig;

public class Categories extends Fragment {
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_categorie, container, false);

        return rootView;
    }
}
