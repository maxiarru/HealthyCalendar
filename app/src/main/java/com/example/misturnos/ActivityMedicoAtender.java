package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.misturnos.models.Turno;

import java.util.List;

public class ActivityMedicoAtender extends AppCompatActivity {


    Context contexto;
    ImageButton botonAtras;
    ListView listView;
    private List<String> misturnos, medicos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_atender);

        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");
        List<Turno> turnos = (List<Turno>) bundle.getSerializable("TURNOS_DISPONIBLES");

        botonAtras = (ImageButton) findViewById(R.id.btnAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atras = new Intent(ActivityMedicoAtender.this, CalendarioMedicoActivity.class);
                atras.putExtra("USER_ID", userId);
                startActivity(atras);
            }
        });

        listView = (ListView) findViewById(R.id.listaturnosagendar) ;

        MyAdapterAgendarTurnoPaciente myAdapter = new MyAdapterAgendarTurnoPaciente(this, R.layout.cuadro_turno_paciente, turnos);
        listView.setAdapter(myAdapter);

    }
}
