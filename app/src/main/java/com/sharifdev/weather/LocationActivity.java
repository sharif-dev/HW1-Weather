package com.sharifdev.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class LocationActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        String[] programming_languages = {"Pyhton", "C", "C++", "Java", "C#", "JavaScript",
                "XML", "HTML5", "Operating Systems", "Android", "Windows",
                "PHP", "Mongodb", "SQL"};

        ListView listview = findViewById(R.id.listview);
        EditText Search = findViewById(R.id.input);

        adapter = new ArrayAdapter<>(this, R.layout.item, R.id.city_name,
                programming_languages);
        listview.setAdapter(adapter);

        Search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                LocationActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

}
