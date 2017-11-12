package com.code.codemercenaries.girdthysword;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class VerseListActivity extends AppCompatActivity {

    final String SYSTEM_PREF = "system_pref";
    SharedPreferences systemPreferences;

    TextView cd;
    ListView verseList;

    String bookName;
    int chapNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        cd = (TextView) findViewById(R.id.chapterDesc);
        verseList = (ListView) findViewById(R.id.verseList);
        bookName = getIntent().getExtras().getString("EXTRA_BOOK_NAME");
        chapNum = getIntent().getExtras().getInt("EXTRA_CHAP_NUM");

        cd.setText(bookName + " " + chapNum);

        systemPreferences = getSharedPreferences(SYSTEM_PREF,0);
        systemPreferences.edit().putString("curr_book_name", bookName).commit();
        systemPreferences.edit().putInt("curr_chap_num", chapNum).commit();

        setupList();
    }

    private void setupList(){
        DBHandler dbHandler = new DBHandler(this);
        List<ReadableVerse> verses = dbHandler.getChapterWithMemory(bookName,chapNum);
        BCustomListAdapter3 bCustomListAdapter3 = new BCustomListAdapter3(this,R.layout.bible_custom_list3,verses);
        verseList.setAdapter(bCustomListAdapter3);
    }

}
