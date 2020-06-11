package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;

import com.example.misturnos.models.Paciente;
import com.example.misturnos.models.Profesional;
import com.example.misturnos.models.Turno;
import com.example.misturnos.models.Usuario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    Paciente lpaciente;
    Profesional lprofesional;
    final List<Turno> turnosF = new ArrayList<Turno>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_turnos_paciente);
        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");

        botonAtras = (ImageButton) findViewById(R.id.btnAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atras = new Intent(misTurnosPaciente.this, CalendarioActivity.class);
                atras.putExtra("USER_ID", userId);
                startActivity(atras);
            }
        });
        botonOk = (ImageButton) findViewById(R.id.btnOk);
        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int miturno = turnosF.get(0).getId();
                Date mifecha = turnosF.get(0).getDate();
                String miestado = turnosF.get(0).getStatus();
                Paciente mipaciente = turnosF.get(0).getPaciente();
                Profesional miprof = turnosF.get(0).getProfesional();
                
               System.out.println("el estado violador es un macho opresor " + miestado + userId + " / " + miturno);
               // String miestado2 = misturnos.get(0).getStatus();
               // System.out.println("el violador eres tu" + miestado2);
                if (miestado == "confirmed") {
                    ApiService serviceturno = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                    Call<Turno> call = serviceturno.putConfirmarTurnos(userId, miturno);
                    call.enqueue(new Callback<Turno>() {
                        @Override
                        public void onResponse(Call<Turno> call, Response<Turno> response) {
                            if (response.code() == 200) {
                                Intent ok = new Intent(misTurnosPaciente.this, CalendarioActivity.class);
                                ok.putExtra("USER_ID", userId);
                                startActivity(ok);
                            } else if (response.code() == 500) {
                                System.out.println("ERROR: authentication failed");
                                Toast.makeText(misTurnosPaciente.this, "confirmacion de turno invalida", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Turno> call, Throwable t) {
                            System.out.printf("ERROR: %s", t.getMessage());
                            Toast.makeText(misTurnosPaciente.this, "fallo confirmar turno", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(miestado == "cancelled"){
                    System.out.println("cancelando turno" );
                    ApiService serviceturno = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                    Call<Turno> call = serviceturno.putCancelarTurnos(userId, miturno);
                    call.enqueue(new Callback<Turno>() {
                        @Override
                        public void onResponse(Call<Turno> call, Response<Turno> response) {
                            System.out.println(response.code() + " codigo " + response.isSuccessful());
                            if (response.code() == 200) {
                                Intent ok = new Intent(misTurnosPaciente.this, CalendarioActivity.class);
                                ok.putExtra("USER_ID", userId);
                                startActivity(ok);
                            } else if (response.code() == 500) {
                                System.out.println("ERROR: authentication failed");
                                Toast.makeText(misTurnosPaciente.this, "cancelacion de turno invalida", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Turno> call, Throwable t) {
                            System.out.printf("ERROR: %s", t.getMessage());
                            Toast.makeText(misTurnosPaciente.this, "fallo cancelar turno", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // datos a mostrar


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
              //          CheckBox cbConfirmar    = null;

                        turnosF.add(new Turno(nroturno, date, status,paciente,profesional));
                    }
                    MyAdapter myAdapter = new MyAdapter(ctx, R.layout.cuadroinfo,  (ArrayList<Turno>) turnosF);
                    listView.setAdapter(myAdapter);

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
