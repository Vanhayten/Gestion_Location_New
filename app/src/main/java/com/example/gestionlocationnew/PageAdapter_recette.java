package com.example.gestionlocationnew;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PageAdapter_recette extends ArrayAdapter<list_recette> {

    private int mColorResourceId;


    private static final String LOG_TAG = PageAdapter_recette.class.getSimpleName();

    public PageAdapter_recette(Activity context, ArrayList<list_recette> resource) {
        super(context, 0, resource);

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.vicucule_rows, parent, false);
        }


        final list_recette currentAndroidFlavor = getItem(position);



        TextView cinTextView = (TextView) listItemView.findViewById(R.id.matrV);

        cinTextView.setText(currentAndroidFlavor.getId());


        TextView nomTextView = (TextView) listItemView.findViewById(R.id.marqueV);

        nomTextView.setText(currentAndroidFlavor.getPrix());

        return listItemView;
    }


}