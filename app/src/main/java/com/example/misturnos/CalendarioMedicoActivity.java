package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class CalendarioMedicoActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button botonSalir, botonTurnos ;
    ImageButton botonPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_medico);
        spinner = findViewById(R.id.spinnerProfesion);
        List<String> profesiones = new ArrayList<>();
        profesiones.add(0,"Elegir Profesion");
        profesiones.add("Pediatra");
        profesiones.add("Obstetra");
        profesiones.add("Cardiologo");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profesiones);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Elegir Profesion"))
                {
                }
                else
                {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "selected: " + item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        botonSalir = (Button) findViewById(R.id.btnSalir);

        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volver = new Intent(CalendarioMedicoActivity.this, MainActivity.class);
                startActivity(volver);
            }
        });
        botonTurnos = (Button) findViewById(R.id.btnMisTurnos);
        botonTurnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Misturnos = new Intent(CalendarioMedicoActivity.this, plusActivity.class);
                startActivity(Misturnos);
            }
        });

        botonPlus = (ImageButton) findViewById(R.id.btnPlus);
        botonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent turnosPlus = new Intent(CalendarioMedicoActivity.this, plusActivity.class);
                startActivity(turnosPlus);
            }
        });
    }
}