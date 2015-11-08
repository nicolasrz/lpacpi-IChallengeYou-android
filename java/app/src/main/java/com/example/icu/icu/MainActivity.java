package com.example.icu.icu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;

import com.example.icu.icu.fonction.AppConfig;
import com.example.icu.icu.fragment.Ami;
import com.example.icu.icu.fragment.Autre;
import com.example.icu.icu.fragment.Deconnexion;

import com.example.icu.icu.fragment.DefiRecu;
import com.example.icu.icu.fragment.LancerDefi;
import com.example.icu.icu.fragment.Moi;
import com.example.icu.icu.fragment.MonReseau;
import com.example.icu.icu.fragment.PagePersoDefiRealise;
import com.example.icu.icu.fragment.Profil;
import com.example.icu.icu.fragment.Aide;
import com.example.icu.icu.fragment.AjoutAmi;
import com.example.icu.icu.fragment.Apropos;
import com.example.icu.icu.fragment.Best;
import com.example.icu.icu.fragment.Categories;
import com.example.icu.icu.fragment.Connexion;
import com.example.icu.icu.fragment.Contact;

import com.example.icu.icu.fragment.DerniersDefis;
import com.example.icu.icu.fragment.Favoris;
import com.example.icu.icu.fragment.Inscription;
import com.example.icu.icu.fragment.Invitations;
import com.example.icu.icu.fragment.ListeAmi;
import com.example.icu.icu.fragment.SuppressionAmi;
import com.example.icu.icu.fonction.DrawerItemCustomAdapter;
import com.example.icu.icu.fonction.ObjectDrawerItem;
import com.example.icu.icu.fonction.SQLiteHandler;
import com.example.icu.icu.fragment.TestFragmentr;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {


    private ListView listView;
    // declare properties
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SQLiteHandler db;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    private String pseudoMenu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView1);
        // for proper titles
        mTitle = mDrawerTitle = getTitle();
        // initialize properties
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.Menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // list the drawer items

        ObjectDrawerItem[]  drawI = new ObjectDrawerItem[14];
        if(AppConfig.pseudo!=null){
            pseudoMenu=AppConfig.pseudo;
        }else{
            pseudoMenu="Moi";
        }
        drawI[0] = new ObjectDrawerItem(R.mipmap.dernierdefi1, "Derniers Défis");
        drawI[1] = new ObjectDrawerItem(R.mipmap.reseau2, "Mon reseau");
        drawI[2] = new ObjectDrawerItem(R.mipmap.lancedef3, "Lancer défi");
        drawI[3] = new ObjectDrawerItem(R.mipmap.categorie4, "Catégorie");
        drawI[4] = new ObjectDrawerItem(R.mipmap.best5, "Best");
        drawI[5] = new ObjectDrawerItem(R.mipmap.favori6, "Favoris");
        drawI[6] = new ObjectDrawerItem(R.mipmap.ligne1, "");
        drawI[7] = new ObjectDrawerItem(R.mipmap.inscription7, "Inscription");
        drawI[8] = new ObjectDrawerItem(R.mipmap.connexion8, "Connexion");
        drawI[9] = new ObjectDrawerItem(R.mipmap.deco9, "Deconnexion");
        drawI[10] = new ObjectDrawerItem(R.mipmap.ligne1, "");
        drawI[11] = new ObjectDrawerItem(R.mipmap.autre12, pseudoMenu);
        drawI[12] = new ObjectDrawerItem(R.mipmap.ami11, "Ami");
        drawI[13] = new ObjectDrawerItem(R.mipmap.autre12, "Autre");
    /*    drawI[14] = new ObjectDrawerItem(R.mipmap.rez4, "");
        drawI[15] = new ObjectDrawerItem(R.mipmap.autre121, "defirecu");
        drawI[16] = new ObjectDrawerItem(R.mipmap.rez6, "test");
*/


        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawI);

        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);

        // set the item click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());





        // for app icon control for nav drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.mipmap.menu,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        if (savedInstanceState == null) {
            // on first time display view for first nav item
            selectItem(0);
        }





        db = new SQLiteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        String test = user.get("name");
        Log.v("MainActivity:","test="+ test);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // to change up caret
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    // navigation drawer click listener
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        // update the main content by replacing fragments

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new DerniersDefis();
                break;
            case 1:
                fragment = new MonReseau();
                break;
            case 2:
                fragment = new LancerDefi();
                break;
            case 3:
                fragment = new Categories();
                break;
            case 4:
                fragment = new Best();
                break;
            case 5:
                fragment = new Favoris();
                break;
            case 6:
                //fragment = new Inscription();
                break;
            case 7:
                fragment = new Inscription();
                break;
            case 8:
                fragment = new Connexion();
                break;
            case 9:
                fragment = new Deconnexion();
                break;
            case 10:
                //fragment = new MonReseau();
                break;
            case 11:
                fragment = new PagePersoDefiRealise();
                break;
            case 12:
                fragment = new Ami();
                break;
            case 13:
                fragment = new Autre();
                break;
/*
            case 14:
               // fragment = new Aide();
                break;
            case 15:
                fragment = new DefiRecu();
                break;
            case 16:
                fragment = new TestFragmentr();
                break;
*/
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);

            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }



}