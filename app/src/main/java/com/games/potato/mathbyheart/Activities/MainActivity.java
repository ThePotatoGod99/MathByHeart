package com.games.potato.mathbyheart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.games.potato.mathbyheart.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.games.potato.mathbyheart.math.Math;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);

        if (preferences.getBoolean("first_start", true)) {
            Math.print("FirstRun");
            firstRun();
            preferences.edit().putBoolean("first_start", false).commit();
        }
     //   reset();//TODO: delete

        File file = new File(getFilesDir(), "default_formulas");
        list(file);


        Math.print("\n\nReadingXML\n");
        try {
            BufferedReader bf = new BufferedReader(new FileReader(new File(getFilesDir(), "default_formulas/starredFormulas.xml")));
            String line;
            while ((line = bf.readLine()) != null) {
                Math.print(line);
            }
            Math.print("ADSF");
        } catch (IOException e) {

        }


        ListView listView = (ListView) findViewById(R.id.list_view);

        String items[] = {getString(R.string.integrals),
                getString(R.string.derivatives),
                getString(R.string.multiplications),
                getString(R.string.others),
                "starredFormulas"};
        String formulas[] = {"$$\\int{\\frac{1}{1+x^2}}dx$$",
                "$$\\frac{d}{du}(\\frac{u}{v})$$",
                "$$3 \\times 4$$",
                "$$\\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}$$",
                "xd"};
        CustomList customList = new CustomList(this, items, formulas);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(MainActivity.this, "ADFASDF " + position, Toast.LENGTH_SHORT).show();

                Intent practiceActivityIntent = new Intent(MainActivity.this, PracticeActivity.class);

                practiceActivityIntent.setData(Uri.parse(parent.getItemAtPosition(position).toString() + ".xml"));
                startActivity(practiceActivityIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void list(File file) {
        for (String string : file.list()) {
            Math.print(string);
        }
        Math.print("\n-----------\n");
    }


    public void firstRun() {
        copyFileOrDir(getString(R.string.path_default_formulas));
        Math.print("\nEnd first run\n");
    }


    //TODO: Move to other file

    public void reset() {
        Math.print("FirstRun: \n\n");
        File file = new File(getFilesDir(), "default_formulas");

        Math.print("Deleting these files: ");
        list(file);

        for (File file1 : file.listFiles()) {
            file1.delete();
        }

        list(file);


        firstRun();
        Math.print("\nFiles:");
        list(file);


        Math.print("\n\nApp Starting \n\n\n");


    }

    public void copyFileOrDir(String path) {
        String assets[];
        try {
            assets = this.getAssets().list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                File file = new File(getFilesDir(), path);

                if (!file.exists()) {
                    file.mkdir();
                }
                for (int i = 0; i < assets.length; ++i) {
                    copyFileOrDir(path + "/" + assets[i]);
                }
            }
        } catch (IOException e) {
            Math.print("IOException " + e);
        }
    }

    private void copyFile(String filename) {

        BufferedWriter bufferedWriter;
        BufferedReader bufferedReader;
        File newFileName = new File(getFilesDir(), filename);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
            bufferedWriter = new BufferedWriter(new FileWriter(newFileName));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
            }

            bufferedReader.close();
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (Exception e) {
            Math.print("EXCEPTION " + e);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Menu menu = ((NavigationView) findViewById(R.id.nav_view)).getMenu();


        switch (id) {
            case R.id.action_add_folder:
                break;
            case R.id.starred:

                break;
        }
        if (id == R.id.action_add_folder) {
            Toast.makeText(this, "reset()", Toast.LENGTH_SHORT).show();
            reset();
            //  menu.add(item.getGroupId(), Menu.NONE, , "hola");
        }
        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
