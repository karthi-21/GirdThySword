package com.code.codemercenaries.girdthysword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Joel Kingsley on 05-12-2017.
 */

public class SCustomListAdapter1 extends ArrayAdapter<String> {

    final String SETTINGS_PREF = "settings_pref";
    int resource;
    Context context;
    SharedPreferences settingsPreferences;
    private int settingsItemsSize = 2;


    public SCustomListAdapter1(@NonNull Context context, int resource) {
        super(context, resource);
        this.resource = resource;
        this.context = context;
        settingsPreferences = context.getSharedPreferences(SETTINGS_PREF, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.resource, parent, false);

        if (position == 0) {
            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView desc = (TextView) rowView.findViewById(R.id.desc);
            TextView chunkSize = (TextView) rowView.findViewById(R.id.value);

            int chunk = settingsPreferences.getInt("chunk_size", 3);
            chunkSize.setText(Integer.toString(chunk));

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChunkSize(v);
                }
            });
        }
        if (position == 1) {
            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView desc = (TextView) rowView.findViewById(R.id.desc);
            TextView themeValue = (TextView) rowView.findViewById(R.id.value);

            title.setText("Theme");
            desc.setText("Color scheme of the app");

            String theme = settingsPreferences.getString("theme", "original");
            themeValue.setText(theme);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTheme(v);
                }
            });
        }
        return rowView;
    }

    @Override
    public int getCount() {
        return settingsItemsSize;
    }

    public void setChunkSize(final View v) {
        CharSequence sizes[] = new CharSequence[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        /*List<Integer> sizes = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));*/

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pick a size");
        builder.setItems(sizes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int size) {

                settingsPreferences.edit().putInt("chunk_size", size + 1).apply();
                Toast.makeText(context, "Chunk Size changed to " + Integer.toString(size + 1),
                        Toast.LENGTH_LONG).show();
                TextView tv = v.findViewById(R.id.value);
                tv.setText(Integer.toString(size + 1));
            }
        });
        builder.show();
    }

    public void setTheme(final View v) {
        final String themes[] = new String[]{"original", "white"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pick a theme");
        builder.setItems(themes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {

                settingsPreferences.edit().putString("theme", themes[pos]).apply();
                Toast.makeText(context, "Theme changed to " + themes[pos],
                        Toast.LENGTH_LONG).show();
                TextView tv = v.findViewById(R.id.value);
                tv.setText(themes[pos]);
            }
        });
        builder.show();
    }
}
