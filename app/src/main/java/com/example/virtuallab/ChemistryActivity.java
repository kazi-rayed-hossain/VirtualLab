package com.example.virtuallab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChemistryActivity extends AppCompatActivity {

    Button periodic_table_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemistry);

        periodic_table_btn = findViewById(R.id.periodic_table_btn);
        periodic_table_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChemistryActivity.this,PeriodicTable.class);
                startActivity(intent);
            }
        });
    }
}
