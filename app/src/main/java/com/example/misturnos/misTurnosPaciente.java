package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;

import com.example.misturnos.models.Paciente;
import com.example.misturnos.models.Profesional;
import com.example.misturnos.models.Turno;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class misTurnosPaciente extends AppCompatActivity {

    ImageButton botonAtras, botonOk;

    private CheckBox cancelar, confirmar;
    ListView listView;
    private List<Turno> misturnos;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_turnos_paciente);


        botonAtras = (ImageButton) findViewById(R.id.btnAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atras = new Intent(misTurnosPaciente.this, CalendarioActivity.class);
                startActivity(atras);
            }
        });
        botonOk = (ImageButton) findViewById(R.id.btnOk);
        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ok = new Intent(misTurnosPaciente.this, CalendarioActivity.class);
                startActivity(ok);
            }
        });

        // datos a mostrar

        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");
        System.out.println("tengo id? " + userId);
        ctx  = this;
        listView = (ListView) findViewById(R.id.listaturnos) ;
        misturnos = llenarTurnos(userId);
        System.out.println("pre adapter " + userId);
     //   MyAdapter myAdapter = new MyAdapter(ctx, R.layout.cuadroinfo, (ArrayList<Turno>) misturnos);
     //   listView.setAdapter(myAdapter);


    }

    private List<Turno> llenarTurnos(Integer userId){
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<List<Turno>> call = service.getTurnos(userId);
        final List<Turno> turnosF = new ArrayList<Turno>();
        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.code() == 200) {
                    System.out.println("getted turnos ok");
                    List<Turno> turnos = response.body();
                    // profesiones.add(0, "Especialidades:");
                    //turnosF.add(new LosTurnos());
                    for (Turno t : turnos){
                        int nroturno            = t.getId();
                        Date date               = t.getDate();
                        String status           = t.getStatus();
                        Paciente paciente       = t.getPaciente();
                        Profesional profesional = t.getProfesional();
                        System.out.println("datos " + nroturno + " / "+ status);
                        turnosF.add(new Turno(nroturno, date, status,paciente,profesional));

                    }
                    System.out.println(turnosF);
                    MyAdapter myAdapter = new MyAdapter(ctx, R.layout.cuadroinfo, (ArrayList<Turno>) turnosF);
                    listView.setAdapter(myAdapter);
                    System.out.println("terminando list view" );

                } else if (response.code() == 500) {
                    System.out.println("ERROR: code 500 - get specialties failed");
                    Toast.makeText(misTurnosPaciente.this, "get turnos failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Turno>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(misTurnosPaciente.this, "get turnos failed", Toast.LENGTH_SHORT).show();
            }
        });
        return turnosF;
    }
}
