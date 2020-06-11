package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;

import com.example.misturnos.models.Especialidad;
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

    ImageButton botonAtras;

    private CheckBox cancelar, confirmar;
    ListView listView;
    private List<Turno> misturnos;
    Context ctx;

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
        /*botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int miturno = misturnos.get(0).getId();
                Date mifecha = misturnos.get(0).getDate();
                String miestado = misturnos.get(0).getStatus();
                Paciente mipaciente = misturnos.get(0).getPaciente();
                Profesional miprof = misturnos.get(0).getProfesional();

                System.out.println(miestado + userId + " / " + miturno);
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
        });*/

        // datos a mostrar
        ctx  = this;
        listView = (ListView) findViewById(R.id.listaturnos) ;
        llenarTurnos(userId);
        System.out.println("HASDHASJKDASHDJAS");
        System.out.println(misturnos);
    }

    private void llenarTurnos(Integer userId){
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<List<Turno>> call = service.getTurnos(userId);
        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.code() == 200) {
                    System.out.println("getted turnos ok");
                    List<Turno> turnos = response.body();
                    AdapterMisTurnosPaciente adapterMisTurnosPaciente = new AdapterMisTurnosPaciente(ctx, R.layout.cuadroinfo,  (ArrayList<Turno>) turnos);
                    listView.setAdapter(adapterMisTurnosPaciente);
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
    }
}