package com.sharifdev.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharifdev.weather.models.City;

import java.util.List;

public class CityAdapter extends BaseAdapter {
    private Context context;
    private List<City> cities;

    public CityAdapter(Context context, List<City> cities) {
        this.context = context;
        this.cities = cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public City getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.city_item, parent, false);
        }

        City currentItem = getItem(position);

        TextView cityNameTextView = convertView.findViewById(R.id.city_name);
        TextView ProvinceNameTextView = convertView.findViewById(R.id.province_name);
        TextView CountryNameTextView = convertView.findViewById(R.id.country_name);

        String[] data = currentItem.getName().split(",\\s+");

        cityNameTextView.setText(data[0]);
        ProvinceNameTextView.setText(data.length > 1 ? data[1] : "");
        CountryNameTextView.setText(data.length > 2 ? data[2] : "");

        return convertView;
    }
}
