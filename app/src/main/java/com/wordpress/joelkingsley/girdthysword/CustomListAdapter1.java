package com.wordpress.joelkingsley.girdthysword;

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
 * Created by Joel Kingsley on 05-10-2017.
 */

//Custom List Adapter for Today and Overdue lists in TabHost

public class CustomListAdapter1 extends ArrayAdapter<Chunk>{
    Context context;
    int resource;
    List<Chunk> objects = new ArrayList<Chunk>();
    List<String> heading = new ArrayList<String>();
    List<String> subheading = new ArrayList<String>();

    public CustomListAdapter1(@NonNull Context context, @LayoutRes int resource, @NonNull List<Chunk> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        DBHandler dbHandler = new DBHandler(context);
        for(int i=0;i<objects.size();i++) {
            this.objects.add(objects.get(i));
            this.heading.add(objects.get(i).toString());
            this.subheading.add(dbHandler.retSection(objects.get(i).getSecId()).toString());
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource,parent,false);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView heading = (TextView) rowView.findViewById(R.id.heading);
        TextView subheading = (TextView) rowView.findViewById(R.id.subheading);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_menu_bible);
        imageView.setImageBitmap(bitmap);
        heading.setText(objects.get(position).toString());
        subheading.setText(objects.get(position).getNextDateOfReview());

        return rowView;
    }
}
