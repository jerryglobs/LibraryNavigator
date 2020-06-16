package com.example.librarynavigator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        EditText searchTextView = (EditText)findViewById(R.id.searchTextView);
        String searchText = searchTextView.getText().toString();

        Intent in = new Intent(MainActivity.this, BookListView.class);
        in.putExtra("searchText", searchText);
        startActivity(in);
    }
}
