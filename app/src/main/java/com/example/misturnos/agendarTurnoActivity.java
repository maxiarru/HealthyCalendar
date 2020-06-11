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

import com.example.misturnos.models.Turno;

import java.io.Serializable;
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

        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");
        List<Turno> turnos = (List<Turno>) bundle.getSerializable("TURNOS_DISPONIBLES");

        botonAtras = (ImageButton) findViewById(R.id.btnAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atras = new Intent(agendarTurnoActivity.this, CalendarioActivity.class);
                atras.putExtra("USER_ID", userId);
                startActivity(atras);
            }
        });
        botonOk = (ImageButton) findViewById(R.id.btnOk);
        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ok = new Intent(agendarTurnoActivity.this, CalendarioActivity.class);
                ok.putExtra("USER_ID", userId);
                startActivity(ok);
            }
        });

        listView = (ListView) findViewById(R.id.listaturnosagendar) ;

        MyAdapteragendar myAdapter = new MyAdapteragendar(this, R.layout.cuadro_turno_paciente, turnos);
        listView.setAdapter(myAdapter);

    }
}
