package com.games.potato.mathbyheart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.games.potato.mathbyheart.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.games.potato.mathbyheart.math.Xd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;


    MainMenuList mainMenuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MobileAds.initialize(this, "ca-app-pub-6790111268050062~2012680831");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                // .addTestDevice("FB2944BD719884A6E6319E924DCEFB28")
                .build();//"FB2944BD719884A6E6319E924DCEFB28"
        mAdView.loadAd(adRequest);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);

        if (preferences.getBoolean("first_start", true)) {
            Xd.print("FirstRun");
            reset();
            preferences.edit().putBoolean("first_start", false).commit();
        }

        reload();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void reload() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        File file = new File(getFilesDir(), "default_formulas");

        ArrayList<String> items = new ArrayList<String>(Arrays.asList(file.list()));
        items.remove("starredList");//TODO: Change
        items.add("Create");
        items.add("⭐");/* Star emoji */


        mainMenuList = new MainMenuList(this,
                items);

        listView.setAdapter(mainMenuList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String string = parent.getItemAtPosition(position).toString();
                if (string.equals("Create")) {
                    Intent createActivityIntent = new Intent(MainActivity.this, CreateActivity.class);
                    startActivity(createActivityIntent);
                } else {
                    if (string.equals("⭐")) {
                        string = "starredList";
                    }
                    Intent practiceActivityIntent = new Intent(MainActivity.this, PracticeActivity.class);
                    practiceActivityIntent.setData(Uri.parse(string));
                    startActivity(practiceActivityIntent);

                }
            }
        });


    }


    public void list(File file) {
        for (String string : file.list()) {
            Xd.print(string);
        }
        Xd.print("\n-----------\n");
    }


    public void firstRun() {
        copyFileOrDir(getString(R.string.path_default_formulas));
        Xd.print("\nEnd first run\n");
    }


    //TODO: Move to other file

    public void reset() {
        Toast.makeText(this, "Reset to default", Toast.LENGTH_SHORT).show();
        Xd.print("FirstRun: \n\n");
        File file = new File(getFilesDir(), "default_formulas");

        if (file.listFiles() != null) {
            Xd.print("Deleting these files: ");

            for (File file1 : file.listFiles()) {
                Xd.print(file1.toString());
                file1.delete();
            }

            list(file);
        }

        firstRun();
        Xd.print("\nFiles:");
        if (file != null) {
            list(file);
        }


        Xd.print("\n\nApp Starting \n\n\n");

        reload();

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
            Xd.print("IOException " + e);
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
            Xd.print("EXCEPTION " + e);
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
        switch (id) {
            case R.id.action_reset:
                reset();
                break;
            case R.id.action_reload:
                reload();
                Toast.makeText(this, "Reload", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_edit:
                mainMenuList.toggleButtonsVisible();
                break;
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
            case R.id.action_reset:
                break;
            case R.id.starred:

                break;
        }
        if (id == R.id.action_reset) {
            reset();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MainMenuList extends ArrayAdapter<String> implements ListAdapter {
        //TODO: Rename
        private final MainActivity context;
        private ArrayList<String> items;
        private String[] formulas;
        private TextView[] textViews;

        private boolean showButtons = false;

        public MainMenuList(MainActivity context, ArrayList<String> items) {
            super(context, R.layout.list_layout, R.id.text_view, items);
            this.items = items;
            this.formulas = formulas;
            this.context = context;
            textViews = new TextView[items.size()];


        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            super.getView(position, view, parent);

            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_layout, null, true);

            textViews[position] = (TextView) rowView.findViewById(R.id.text_view);
            Button button_edit = (Button) rowView.findViewById(R.id.action_edit);//TODO: Change to btn_edit
            button_edit.setVisibility(
                    showButtons ? View.VISIBLE : View.INVISIBLE
            );
            button_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditListActivity.class);
                    intent.setData(Uri.parse(items.get(position)));
                    startActivity(intent);
                }
            });

            Button button_delete = (Button) rowView.findViewById(R.id.action_delete);
            button_delete.setVisibility(
                    showButtons ? View.VISIBLE : View.INVISIBLE
            );
            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = new File(getFilesDir()
                            , getString(R.string.path_default_formulas) + "/" + items.get(position));
                    file.delete();
                    context.reload();
                }
            });

            try {
                textViews[position].setText(items.get(position));
            } catch (ArrayIndexOutOfBoundsException e) {
                //TODO
            }
            return rowView;
        }


        public void toggleButtonsVisible() {
            showButtons = showButtons ? false : true;
            this.notifyDataSetChanged();
        }

    }
}
