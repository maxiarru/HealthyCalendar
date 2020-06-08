package com.example.misturnos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapteragendar extends BaseAdapter {

    private Context context;
    private int layout;
    // private ArrayList<cuadroDatos> turno;
    private ArrayList<String> turno;
    private  ArrayList<String> medico;
    private CheckBox agendar;

    public MyAdapteragendar(Context context, int layout, ArrayList<String> turno, ArrayList<String> medico){
        this.context = context;
        this.layout = layout;
        this.turno = turno;
        this.medico = medico;
    }

    @Override
    public int getCount() {
        return this.turno.size();
    }

    @Override
    public Object getItem(int position) {
        return this.turno.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //copiamos la vista
        View v = convertView;
        //inflamos la vista que nos llega con nuestro layout personalizado
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.cuadro_turno_paciente, null);
        // nos traemos el vlor actual dependiendo de la pos.i
            String turnoActual = turno.get(position);
            String medicoActual = medico.get(position);
            // referenciamos el elemento a modificar y lo rellenamos
            TextView textView = (TextView) v.findViewById(R.id.txtnroTurnoagendar);
            textView.setText(turnoActual);
            final TextView textProfesional = (TextView) v.findViewById(R.id.txtmedicoagendar);
            textProfesional.setText(medicoActual);
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
