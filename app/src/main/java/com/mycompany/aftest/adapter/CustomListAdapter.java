package com.mycompany.aftest.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mycompany.aftest.R;
import com.mycompany.aftest.app.AppController;
import com.mycompany.aftest.app.PromotionsActivity;
import com.mycompany.aftest.model.Promotions;

import java.util.List;

/**
 * Created by shefeng on 6/29/2016.
 */
public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Promotions> promotionsItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Promotions> promotionsItems) {
        this.activity = activity;
        this.promotionsItems = promotionsItems;
    }

    @Override
    public int getCount() {
        return promotionsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return promotionsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        // getting billionaires data for the row
        final Promotions m = promotionsItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // name
        name.setText(m.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,PromotionsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",m);
                intent.putExtras(bundle);
                activity.startActivity(intent);

            }
        });

        // Wealth Source
        //source.setText("Wealth Source: " + String.valueOf(m.getSource()));


        // worth.setText(String.valueOf(m.getWorth()));

        // release year
        //year.setText(String.valueOf(m.getYear()));

        return convertView;
    }

}
