package com.code.codemercenaries.girdthysword;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TabHost tabHost;
    ListView today;
    ListView overdue;
    ListView tomorrow;
    ListView all;
    FloatingActionButton fab;
    FloatingActionButton fab_add;
    FloatingActionButton fab_delete;
    TextView tv_add;
    TextView tv_delete;

    boolean fab_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.nav_title_about);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.whiteText), 0, s.length(), 0);
        tools.setTitle(s);

        navigationView.setNavigationItemSelectedListener(this);

        tabHost = (TabHost) findViewById(R.id.tabhost); // initiate TabHost
        today = (ListView) findViewById(R.id.today_list);
        overdue = (ListView) findViewById(R.id.overdue_list);
        tomorrow = (ListView) findViewById(R.id.tomorrow_list);
        all = (ListView) findViewById(R.id.all_list);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_delete = (TextView) findViewById(R.id.tv_delete);

        setupTabHost();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            setupLists();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        setupFabs();
    }

    private void setupLists() throws ParseException {

        final List<Chunk> todayChunks;
        final List<Chunk> overdueChunks;
        final List<Chunk> tomorrowChunks;
        List<Chunk> allChunks;

        DBHandler dbHandler = new DBHandler(this);

        //dbHandler.deleteAllChunks();
        //dbHandler.deleteAllSections();

        /*dbHandler.addChunk(new Chunk(1,"John",3,16,18, "17/10/2017", 1,1, false));
        dbHandler.addChunk(new Chunk(1,"Romans",4,5,7, "18/10/2017", 1,2,false));
        dbHandler.addChunk(new Chunk(1, "Romans",1,1,3, "10/10/2017", 1,3,false));
        dbHandler.addSection(new Section("John",3,16,18,1));
        dbHandler.addSection(new Section("Romans",4,5,7,2));
        dbHandler.addSection(new Section("Romans",1,1,3,3));*/


        todayChunks = new ArrayList<Chunk>();
        overdueChunks = new ArrayList<Chunk>();
        tomorrowChunks = new ArrayList<Chunk>();
        allChunks = dbHandler.getAllChunks();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        ca.add(Calendar.DATE,1);
        String tomDate = df.format(ca.getTime());

        for (Chunk c : allChunks) {
            Log.d("Reading:",c.toString());

            if(!c.getNextDateOfReview().equals("NA")) {
                Log.d("CurrDate:",currDate);
                Log.d("DateObj:",c.getNextDateOfReview());
                Date dateObj = df.parse(c.getNextDateOfReview());
                Date currDateObj = df.parse(currDate);
                Date tomDateObj = df.parse(tomDate);
                if(currDateObj.equals(dateObj)){
                    Log.d("Today",c.toString());
                    todayChunks.add(c);
                }
                else if(dateObj.before(currDateObj)){
                    overdueChunks.add(c);
                }
                else if(tomDateObj.equals(dateObj)) {
                    tomorrowChunks.add(c);
                }
            }
        }

        for(Chunk c:todayChunks) {
            Log.d("TodayList:",c.toString());
        }
        CustomListAdapter1 todayAdapter = new CustomListAdapter1(this,R.layout.chunk_custom_list1,todayChunks);
        today.setAdapter(todayAdapter);
        today.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this,ReviewActivity.class);
                intent.putExtra("EXTRA_CHUNK_ID", todayChunks.get(i).get_id());
                startActivity(intent);
            }
        });

        CustomListAdapter1 overdueAdapter = new CustomListAdapter1(this,R.layout.chunk_custom_list1,overdueChunks);
        overdue.setAdapter(overdueAdapter);
        overdue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this,ReviewActivity.class);
                intent.putExtra("EXTRA_CHUNK_ID", overdueChunks.get(i).get_id());
                startActivity(intent);
            }
        });

        CustomListAdapter1 tomorrowAdapter = new CustomListAdapter1(this,R.layout.chunk_custom_list1,tomorrowChunks);
        tomorrow.setAdapter(tomorrowAdapter);

        CustomListAdapter1 allAdapter = new CustomListAdapter1(this,R.layout.chunk_custom_list2,allChunks);
        all.setAdapter(allAdapter);

    }

    private void setupFabs() {
        fab_status = false;
        fab_hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                if(fab_status == false) {
                    fab_show();
                    fab_status = true;
                }
                else {
                    fab_hide();
                    fab_status = false;
                }
            }


        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,NewSectionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fab_show() {
        fab_add.show();
        fab_delete.show();
        tv_add.setVisibility(View.VISIBLE);
        tv_delete.setVisibility(View.VISIBLE);
    }

    private void fab_hide() {
        fab_add.hide();
        fab_delete.hide();
        tv_add.setVisibility(View.INVISIBLE);
        tv_delete.setVisibility(View.INVISIBLE);
    }

    private void setupTabHost() {

        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        tabHost.setup();

        spec = tabHost.newTabSpec("Today");
        spec.setIndicator("TODAY");
        spec.setContent(R.id.tab1);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Overdue");
        spec.setIndicator("OVERDUE");
        spec.setContent(R.id.tab2);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Tomorrow");
        spec.setIndicator("TOMORROW");
        spec.setContent(R.id.tab3);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("All");
        spec.setIndicator("ALL");
        spec.setContent(R.id.tab4);
        tabHost.addTab(spec);

        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch(tabId){
                    case "Today":
                        break;
                    case "Overdue":
                        break;
                    case "Tomorrow":
                        break;
                    case "All":
                        break;
                    default:
                }
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
        getMenuInflater().inflate(R.menu.home, menu);
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
            // Handle the camera action
        } else if (id == R.id.nav_bible) {
            Intent intent = new Intent(HomeActivity.this,BibleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(HomeActivity.this,RewardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(HomeActivity.this,StatsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
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
