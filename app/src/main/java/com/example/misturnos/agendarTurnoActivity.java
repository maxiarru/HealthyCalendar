package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class agendarTurnoActivity extends AppCompatActivity {

    Context contexto;
    ImageButton botonAtras, botonOk;
    ListView listView;
    private List<String> misturnos, medicos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_turno);
        botonAtras = (ImageButton) findViewById(R.id.btnAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atras = new Intent(agendarTurnoActivity.this, CalendarioActivity.class);
                startActivity(atras);
            }
        });
        botonOk = (ImageButton) findViewById(R.id.btnOk);
        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ok = new Intent(agendarTurnoActivity.this, CalendarioActivity.class);
                startActivity(ok);
            }
        });

        listView = (ListView) findViewById(R.id.listaturnosagendar) ;
        misturnos = new ArrayList<String>();
        misturnos.add("123");
        misturnos.add("3213");
        misturnos.add("4213");
        misturnos.add("33322");
        misturnos.add("122112");
        medicos = new ArrayList<String>();
        medicos.add("Carlos Sal");
        medicos.add("Oscar Cito");
        medicos.add("Carlos Sal");
        medicos.add("Ricardo Loris");
        medicos.add("Oscar Cito");

        MyAdapteragendar myAdapter = new MyAdapteragendar(this, R.layout.cuadro_turno_paciente, (ArrayList<String>) misturnos, (ArrayList<String>) medicos);
        listView.setAdapter(myAdapter);

    }
}
