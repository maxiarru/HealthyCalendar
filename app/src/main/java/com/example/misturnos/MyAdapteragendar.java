package com.example.misturnos;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.misturnos.models.Turno;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MyAdapteragendar extends BaseAdapter {

    private final List<Turno> turnos;
    private Context context;
    private int layout;
    // private ArrayList<cuadroDatos> turno;
    private CheckBox agendar;

    public MyAdapteragendar(Context context, int layout, List<Turno> turnos){
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
        v = layoutInflater.inflate(R.layout.cuadro_turno_paciente, null);
        // nos traemos el vlor actual dependiendo de la pos.i
            Turno turnoActual = turnos.get(position);
            // referenciamos el elemento a modificar y lo rellenamos
            TextView textView = (TextView) v.findViewById(R.id.txtnroTurnoagendar);
            textView.setText(turnoActual.getId().toString());


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

            final TextView textProfesional = (TextView) v.findViewById(R.id.txtmedicoagendar);
            textProfesional.setText(turnoActual.getProfesional().getName());
            agendar = (CheckBox) v.findViewById(R.id.cbAgendar);
            agendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CompoundButton)v).isChecked()){
                        System.out.println("marco un turno / " + R.id.cbAgendar+ " / " + textProfesional);

                    }
                }
            });

            return v;

    }
}
