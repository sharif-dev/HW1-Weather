package com.sharifdev.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class CityAdapter extends BaseAdapter {
    private Context context;
    private List<String> cities;

    public CityAdapter(Context context, List<String> cities) {
        this.context = context;
        this.cities = cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public String getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        }

        String currentItem = getItem(position);

        TextView cityNameTextView = convertView.findViewById(R.id.city_name);
        TextView ProvinceNameTextView = convertView.findViewById(R.id.province_name);
        TextView CountryNameTextView = convertView.findViewById(R.id.country_name);

        cityNameTextView.setText(currentItem);
        ProvinceNameTextView.setText(currentItem + " province");
        CountryNameTextView.setText(currentItem + " country");

        return convertView;
    }
}
