package com.example.misturnos;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;
import com.example.misturnos.models.Turno;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMisTurnosPaciente extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CheckBox> checkAceptar;
    private ArrayList<CheckBox> checkCancelar;
    private  List<Turno> turnos;
    private Integer idPatient;

    public AdapterMisTurnosPaciente(Context context, int layout, ArrayList<Turno> turnos){
        this.context = context;
        this.layout = layout;
        this.turnos = turnos;
    }

    @Override
    public int getCount() {
        return this.turnos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.turnos.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //copiamos la vista
        View v = convertView;
        //inflamos la vista que nos llega con nuestro layout personalizado
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.cuadroinfo, null);
        // nos traemos el vlor actual dependiendo de la pos.i

        String estado = turnos.get(position).getStatus();
        if (estado.equals("pending")   ||  estado.equals("confirmed") ||  estado.equals("cancelled"))
        {
            Turno turnoActual = turnos.get(position);

            // referenciamos el elemento a modificar y lo rellenamos
            TextView textView = (TextView) v.findViewById(R.id.txtnroTurno);
            textView.setText(String.valueOf(turnoActual.getId()));

            TextView textViewEstado = (TextView) v.findViewById(R.id.txtestado);
            textViewEstado.setText(estado);

            TextView textViewProfesional = (TextView) v.findViewById(R.id.txtprofesional);
            textViewProfesional.setText(turnoActual.getProfesional().getName());

            ZonedDateTime zt = turnoActual.getDate().toInstant().atZone(ZoneId.systemDefault());
            LocalDate localDate = zt.toLocalDate();
            String turnoMes = String.valueOf(localDate.getMonthValue());
            String turnoAño = String.valueOf(localDate.getYear());
            String turnoDia = String.valueOf(localDate.getDayOfMonth());
            String turnoHora = String.valueOf(zt.getHour());
            if (turnoHora.length() == 1) {
                turnoHora = "0" + turnoHora;
            }
            String turnoMinutos = String.valueOf(zt.getMinute());
            if (turnoMinutos.length() == 1) {
                turnoMinutos = "0" + turnoMinutos;
            }

            TextView textFecha = (TextView) v.findViewById(R.id.tctFecha);
            String fecha = String.format("%s/%s/%s", turnoDia, turnoMes, turnoAño);
            textFecha.setText(fecha);

            TextView textHora = (TextView) v.findViewById(R.id.txtHorario);
            String hora = String.format("%s:%s", turnoHora, turnoMinutos);
            textHora.setText(hora);

            Button confirmar = (Button) v.findViewById(R.id.cbConfirmar);
            confirmar.setTag(position);

            Button cancelar = (Button) v.findViewById(R.id.cbCancelar);
            cancelar.setTag(position);

            if (estado.equals("confirmed") || estado.equals("cancelled")){
                cancelar.setEnabled(false);
                confirmar.setEnabled(false);
            }

            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                    System.out.println("request appointment");
                    idPatient = ((Activity) context).getIntent().getExtras().getInt("USER_ID");
                    Call<Void> call = service.putConfirmarTurnos(idPatient, turnoActual.getId());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                System.out.println("request appointment ok");
                                Intent volver = new Intent(context, CalendarioActivity.class);
                                volver.putExtra("USER_ID", idPatient);
                                context.startActivity(volver);
                            }
                            else if (response.code() == 500) {
                                System.out.println("ERROR: code 500 - request appointment failed");
                                Toast.makeText(v.getContext(), "solo pueda confirmar un turno dentro de las previas 12hs", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.printf("ERROR: %s", t.getMessage());
                            Toast.makeText(v.getContext(), "confirmar turno no disponible", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                    System.out.println("request appointment");
                    Integer idPatient = ((Activity) context).getIntent().getExtras().getInt("USER_ID");
                    Call<Void> call = service.putCancelarTurnos(idPatient, turnoActual.getId());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                System.out.println("cancel appointment ok");
                                Intent volver = new Intent(context, CalendarioActivity.class);
                                volver.putExtra("USER_ID", idPatient);
                                context.startActivity(volver);
                            }
                            else if (response.code() == 500) {
                                System.out.println("ERROR: code 500 - request appointment failed");
                                Toast.makeText(v.getContext(), "fallo cancelar turno", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.printf("ERROR: %s", t.getMessage());
                            Toast.makeText(v.getContext(), "cancelar turno no disponible", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            return v;
        }
        v.setVisibility(View.INVISIBLE);
        return v;
    }
}