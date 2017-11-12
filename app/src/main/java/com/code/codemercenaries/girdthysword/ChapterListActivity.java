package com.code.codemercenaries.girdthysword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChapterListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String SYSTEM_PREF = "system_pref";
    SharedPreferences systemPreferences;

    ListView chapList;
    TextView bt;

    String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_chapter_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.nav_title_about);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.whiteText), 0, s.length(), 0);
        tools.setTitle(s);

        chapList = (ListView) findViewById(R.id.chapterList);
        bt = (TextView) findViewById(R.id.bookTitle);

        if(getIntent().getExtras() !=null)
            bookName = getIntent().getExtras().getString("EXTRA_BOOK_NAME");
        else{
            systemPreferences = getSharedPreferences(SYSTEM_PREF,0);
            bookName = systemPreferences.getString("curr_book_name","Genesis");
        }

        /*if(customTitleSupported){
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.activity_chapter_list);
            bt.setText(bookName);
        }*/

        bt.setText(bookName);

        if(bookName!=null){
            setupList();
        }

    }

    private void setupList() {

        DBHandler dbHandler = new DBHandler(this);
        List<String> objects = new ArrayList<String>();

        int n = dbHandler.getNumofChap(bookName);

        for(int i=0;i<n;i++){
            objects.add(bookName);
        }

        BCustomListAdapter2 adapter = new BCustomListAdapter2(this,R.layout.bible_custom_list2,objects);
        chapList.setAdapter(adapter);
        chapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChapterListActivity.this,VerseListActivity.class);
                intent.putExtra("EXTRA_BOOK_NAME",bookName);
                intent.putExtra("EXTRA_CHAP_NUM",i+1);
                startActivity(intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.chapter_list, menu);
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

        if (id == R.id.nav_home) {
            Intent intent = new Intent(ChapterListActivity.this,HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bible) {
            Intent intent = new Intent(ChapterListActivity.this,BibleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(ChapterListActivity.this,RewardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(ChapterListActivity.this,StatsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(ChapterListActivity.this,ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
