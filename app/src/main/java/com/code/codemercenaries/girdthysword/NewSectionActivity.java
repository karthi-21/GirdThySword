package com.code.codemercenaries.girdthysword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class NewSectionActivity extends AppCompatActivity {

    final String INDEX_PREF = "index_pref";
    final String SYSTEM_PREF = "system_pref";
    SharedPreferences indexPreferences;
    SharedPreferences systemPreferences;
    int chunkSize;
    private Spinner spinner1,spinner2,spinner3,spinner4;
    private NumberPicker numberPicker1,numberPicker2;
    Button submit;
    List<String> bookItems = new ArrayList<>(Arrays.asList("Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua",
            "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles",
            "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes",
            "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea",
            "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai",
            "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans",
            "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians",
            "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon",
            "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"));

    Integer numOfChap[] = {50,40,27,36,34,24,21,4,31,24,22,25,29,36,10,13,10,42,150,31,12,8,66,52,5,48,
            12,14,3,9,1,4,7,3,3,3,2,14,4,28,16,24,21,28,16,16,13,6,6,4,4,5,3,6,4,3,1,13,5,5,3,5,1,1,1,22};

    List<String> availBookNames;
    List<Integer> availChapNums;
    List<Integer> availStartVerses;
    List<Integer> availEndVerses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        systemPreferences = getSharedPreferences(SYSTEM_PREF,0);
        indexPreferences = getSharedPreferences(INDEX_PREF,0);
        submit = (Button) findViewById(R.id.button);
        addItemsOnBookNameSpinner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addItemsOnBookNameSpinner(){
        availBookNames = new ArrayList<String>();
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        DBHandler dbHandler = new DBHandler(this);
        for(String s:bookItems){
            if(indexPreferences.getBoolean(s,false) == false){
                availBookNames.add(s);
            }
        }

        ArrayAdapter<String> StringAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,availBookNames);
        StringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(StringAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addItemsOnChapterNumSpinner(availBookNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addItemsOnChapterNumSpinner(final String bookName){
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        availChapNums = new ArrayList<Integer>();

        DBHandler dbHandler = new DBHandler(this);
        int n = dbHandler.getNumofChap(bookName);

        for(int i=1;i<=n;i++){
            boolean added = indexPreferences.getBoolean(bookName+"_"+i,false);
            Log.d("addItemsOnChapter","Chapter " + i + " " + added);
            if(!added){
                availChapNums.add(i);
            }
        }
        ArrayAdapter<Integer> IntegerAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,availChapNums);
        IntegerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(IntegerAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addItemsOnStartVerseSpinner(bookName,availChapNums.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addItemsOnStartVerseSpinner(final String bookName, final int chapNum){
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        List<Integer> verseList = new ArrayList<>();
        DBHandler dbHandler = new DBHandler(this);
        availStartVerses = dbHandler.getAvailableVersesOfChap(bookName,chapNum);
        ArrayAdapter<Integer> IntegerAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,availStartVerses);
        IntegerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(IntegerAdapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                addItemsOnEndVerseSpinner(bookName,chapNum,i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }
        });
        if(availStartVerses.size() == 0){
            addItemsOnEndVerseSpinner(bookName,chapNum,0);
        }
    }

    public void addItemsOnEndVerseSpinner(String bookName,int chapNum,int startVersePos){
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        List<Integer> verseList = new ArrayList<>();
        DBHandler dbHandler = new DBHandler(this);
        availEndVerses = new ArrayList<Integer>();
        Log.d("addItemsOnEVSpinner","setup");
        for(int i=startVersePos;i<availStartVerses.size();i++){
            if(i==availStartVerses.size()-1){
                availEndVerses.add(availStartVerses.get(i));
            }
            else if(availStartVerses.get(i)+1 == availStartVerses.get(i+1)){
                availEndVerses.add(availStartVerses.get(i));
            }
            else{
                availEndVerses.add(availStartVerses.get(i));
                break;
            }
        }
        ArrayAdapter<Integer> IntegerAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,availEndVerses);
        IntegerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(IntegerAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    public void submit(){
        int secId;
        String bookName = spinner1.getSelectedItem().toString();
        int chapNum = Integer.parseInt(spinner2.getSelectedItem().toString());
        int startVerse = Integer.parseInt(spinner3.getSelectedItem().toString());
        int endVerse = Integer.parseInt(spinner4.getSelectedItem().toString());
        DBHandler dbHandler = new DBHandler(this);

        if(dbHandler.getMaxSecId()==-1){
            secId = 1;
        }
        else{
            secId = dbHandler.getMaxSecId() + 1;
        }

        Section section = new Section(bookName,chapNum,startVerse,endVerse,secId);
        dbHandler.addSection(section);

        SharedPreferences systemPreferences = getSharedPreferences(SYSTEM_PREF,0);
        chunkSize = systemPreferences.getInt("chunk_size",3);

        List<Chunk> chunkList = chunkize(section,chunkSize);
        for(int i=0;i<chunkList.size();i++){
            Log.d("Add Sub Chunk:","SID=" + chunkList.get(i).getSecId() + " " + chunkList.get(i).toString() + " " + chunkList.get(i).getNextDateOfReview());
            dbHandler.addChunk(chunkList.get(i));
        }

        SharedPreferences indexPreferences = getSharedPreferences(INDEX_PREF,0);

        dbHandler.setMemoryToAdded(section);

        if(dbHandler.addedChapter(bookName,chapNum)){
            indexPreferences.edit().putBoolean(bookName+"_"+chapNum,true).commit();
            Log.d("Submit","Chapter added");
            /*for(int i=1;i<=numOfChap[bookItems.indexOf(bookName)];i++){
            }*/
        }


        Toast.makeText(NewSectionActivity.this, "Added " + bookName + " " + chapNum + ":" + startVerse + "-" + endVerse,
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(NewSectionActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public List<Chunk> chunkize(Section section,int chunkSize){
        List<Chunk> chunks = new ArrayList<Chunk>();
        int min = section.get_start_verse_num();
        int max = section.get_end_verse_num();
        int seq = 1;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        while(max-min>=chunkSize) {
            Chunk subChunk = new Chunk();
            subChunk.setBookName(section
                    .get_book_name());
            subChunk.setChapNum(section.get_chap_num());
            subChunk.setSeq(seq);
            subChunk.setSpace(1);
            subChunk.setStartVerseNum(min);
            subChunk.setEndVerseNum(min+chunkSize-1);
            subChunk.setSecId(section.get_sec_id());
            if(seq==1){
                subChunk.setNextDateOfReview(currDate);
            }
            else{
                subChunk.setNextDateOfReview("NA");
            }
            subChunk.setMastered(false);
            chunks.add(subChunk);
            seq++;
            min = min+chunkSize;
        }
        if(max-min>=0){
            Chunk subChunk = new Chunk();
            subChunk.setBookName(section.get_book_name());
            subChunk.setChapNum(section.get_chap_num());
            subChunk.setSeq(seq);
            subChunk.setSpace(1);
            subChunk.setStartVerseNum(min);
            subChunk.setEndVerseNum(max);
            subChunk.setSecId(section.get_sec_id());
            if(seq==1){
                subChunk.setNextDateOfReview(currDate);
            }
            else{
                subChunk.setNextDateOfReview("NA");
            }
            subChunk.setMastered(false);
            chunks.add(subChunk);
            seq++;
            min = min+chunkSize;
        }
        return chunks;
    }
}
