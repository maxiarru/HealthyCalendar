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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;
import com.example.misturnos.models.Turno;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterFiltroMedico extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Turno> turnos;
    private Integer idMedico;
    private String tipoUsuario;
    private Date fechaDesde, fechaHasta;
    private String especialidad , especialidadCompleta;

    public AdapterFiltroMedico(Context context, int layout, ArrayList<Turno> turnos, Date fechadesde, Date fechahasta, String especialidad) {
        this.context = context;
        this.layout = layout;
        this.turnos = turnos;
        this.fechaDesde = fechadesde;
        this.fechaHasta = fechahasta;
        this.especialidad = especialidad;
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
        Turno turnocomparar = turnos.get(position);
        System.out.println("llega hasta aca  " + turnocomparar.getEspecialidad() + turnocomparar.getId() + turnocomparar.getDate());

        especialidadCompleta = turnocomparar.getEspecialidad().getCategory() + " - " + turnocomparar.getEspecialidad().getSubCategory();
        System.out.println("**** pre if ****** " + especialidadCompleta + " / " + especialidad);

        if (turnocomparar.getDate().before(fechaHasta) && turnocomparar.getDate().after(fechaDesde)
            &&  especialidadCompleta ==  especialidad ) {
            System.out.println("comparando ok la tiene q mostrar " + especialidadCompleta + " / " + especialidad);
            //copiamos la vista
            View v = convertView;
            //inflamos la vista que nos llega con nuestro layout personalizado
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            v = layoutInflater.inflate(R.layout.cuadro_misturnos_medico, null);
            // nos traemos el valor actual dependiendo de la pos.i

            String estado = turnos.get(position).getStatus();
            //      if (estado.equals("pending")   ||  estado.equals("confirmed") ||  estado.equals("cancelled"))
            //      {
            Turno turnoActual = turnos.get(position);

            // referenciamos el elemento a modificar y lo rellenamos
            TextView textView = (TextView) v.findViewById(R.id.txtnroTurno);
            textView.setText(String.valueOf(turnoActual.getId()));

            TextView textViewEstado = (TextView) v.findViewById(R.id.txtestado);
            textViewEstado.setText(estado);

            TextView textViewPaciente = (TextView) v.findViewById(R.id.txtpaciente);
            textViewPaciente.setText(turnoActual.getPaciente().getName());

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

            TextView textFecha = (TextView) v.findViewById(R.id.txtFecha);
            String fecha = String.format("%s/%s/%s", turnoDia, turnoMes, turnoAño);
            textFecha.setText(fecha);

            TextView textHora = (TextView) v.findViewById(R.id.txtHorario);
            String hora = String.format("%s:%s", turnoHora, turnoMinutos);
            textHora.setText(hora);


            Button cancelar = (Button) v.findViewById(R.id.cbCancelar);
            cancelar.setTag(position);

            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                    System.out.println("request appointment");
                    idMedico = ((Activity) context).getIntent().getExtras().getInt("USER_ID");
                    tipoUsuario = ((Activity) context).getIntent().getExtras().getString("tipo_USUARIO");
                    System.out.println("**** tengo el id? ******" + idMedico + " / " + turnoActual.getId());
                    Call<Void> call = service.putCancelarTurnoMedico(idMedico, turnoActual.getId());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                System.out.println("cancel appointment ok");
                                Intent volver = new Intent(context, CalendarioMedicoActivity.class);
                                volver.putExtra("USER_ID", idMedico);
                                volver.putExtra("tipo_USUARIO", tipoUsuario);
                                Toast.makeText(v.getContext(), "turno cancelado!", Toast.LENGTH_SHORT).show();
                                context.startActivity(volver);
                            } else if (response.code() == 500) {
                                System.out.println("ERROR: code 500 - request appointment failed");
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    String errorMessage = jObjError.getString("description");
                                    Toast.makeText(v.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("ni 200 ni 500 " + response.code() + " / " + response.message() + " / " + response.body());
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
        System.out.println("comparando ok No aplica filtro " + especialidadCompleta + " / " + especialidad);
        //copiamos la vista
        View v = convertView;
        //inflamos la vista que nos llega con nuestro layout personalizado
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.cuadro_misturnos_medico, null);
        // nos traemos el valor actual dependiendo de la pos.i

        String estado = turnos.get(position).getStatus();
        //      if (estado.equals("pending")   ||  estado.equals("confirmed") ||  estado.equals("cancelled"))
        //      {
        Turno turnoActual = turnos.get(position);

        // referenciamos el elemento a modificar y lo rellenamos
        TextView textView = (TextView) v.findViewById(R.id.txtnroTurno);
        textView.setText(String.valueOf(turnoActual.getId()));

        TextView textViewEstado = (TextView) v.findViewById(R.id.txtestado);
        textViewEstado.setText(estado);

        TextView textViewPaciente = (TextView) v.findViewById(R.id.txtpaciente);
        textViewPaciente.setText(turnoActual.getPaciente().getName());

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

        TextView textFecha = (TextView) v.findViewById(R.id.txtFecha);
        String fecha = String.format("%s/%s/%s", turnoDia, turnoMes, turnoAño);
        textFecha.setText(fecha);

        TextView textHora = (TextView) v.findViewById(R.id.txtHorario);
        String hora = String.format("%s:%s", turnoHora, turnoMinutos);
        textHora.setText(hora);


        Button cancelar = (Button) v.findViewById(R.id.cbCancelar);
        cancelar.setTag(position);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                System.out.println("request appointment");
                idMedico = ((Activity) context).getIntent().getExtras().getInt("USER_ID");
                tipoUsuario = ((Activity) context).getIntent().getExtras().getString("tipo_USUARIO");
                System.out.println("**** tengo el id? ******" + idMedico + " / " + turnoActual.getId());
                Call<Void> call = service.putCancelarTurnoMedico(idMedico, turnoActual.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            System.out.println("cancel appointment ok");
                            Intent volver = new Intent(context, CalendarioMedicoActivity.class);
                            volver.putExtra("USER_ID", idMedico);
                            volver.putExtra("tipo_USUARIO", tipoUsuario);
                            Toast.makeText(v.getContext(), "turno cancelado!", Toast.LENGTH_SHORT).show();
                            context.startActivity(volver);
                        } else if (response.code() == 500) {
                            System.out.println("ERROR: code 500 - request appointment failed");
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                String errorMessage = jObjError.getString("description");
                                Toast.makeText(v.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("ni 200 ni 500 " + response.code() + " / " + response.message() + " / " + response.body());
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
}
//System.out.println("pasa por aca");
//        v.clearAnimation();
//        v.setVisibility(View.GONE);

    //  return v;
    //}

