package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel Kingsley on 09-11-2017.
 */

public class BCustomListAdapter2 extends ArrayAdapter<String> {
    Context context;
    int resource;
    String bookName;
    List<Integer> chapters = new ArrayList<Integer>();
    List<Integer> percents = new ArrayList<Integer>();
    List<Integer> totalVerses = new ArrayList<Integer>();
    List<Integer> memorizedVerses = new ArrayList<Integer>();

    public BCustomListAdapter2(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

        bookName = objects.get(0);
        int n = objects.size();

        DBHandler dbHandler = new DBHandler(context);

        for(int i=1;i<=n;i++){
            chapters.add(i);
            totalVerses.add(dbHandler.getNumOfVerse(bookName,i));
            memorizedVerses.add(dbHandler.getMemorizedVerses(bookName,i).size());
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.resource,parent,false);




        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView heading = (TextView) rowView.findViewById(R.id.heading);
        TextView percent = (TextView) rowView.findViewById(R.id.percent);
        TextView number = (TextView) rowView.findViewById(R.id.number);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_menu_bible);
        imageView.setImageBitmap(bitmap);
        heading.setText("Chapter " + chapters.get(position));
        percent.setText(( (memorizedVerses.get(position)*100) / totalVerses.get(position) ) + "%");
        number.setText(memorizedVerses.get(position) + "/" + totalVerses.get(position));

        return rowView;
    }
}
