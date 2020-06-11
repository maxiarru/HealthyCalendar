package com.example.misturnos;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.misturnos.models.Turno;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterMisTurnosPaciente extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CheckBox> checkAceptar;
    private ArrayList<CheckBox> checkCancelar;
    private  List<Turno> turnos;

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
                }
            });

            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            return v;
        }
        v.setVisibility(View.INVISIBLE);
        return v;
    }
}