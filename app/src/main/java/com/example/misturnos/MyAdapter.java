package com.example.misturnos;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.misturnos.models.Turno;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CheckBox> checkAceptar;
    private ArrayList<CheckBox> checkCancelar;
    private  ArrayList<Turno> turnos;

    private CheckBox cancelar, confirmar;

    public MyAdapter(Context context, int layout, ArrayList<Turno> turnos){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //copiamos la vista
        View v = convertView;
        //inflamos la vista que nos llega con nuestro layout personalizado
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.cuadroinfo, null);
        // nos traemos el vlor actual dependiendo de la pos.i

        if (turnos.get(position).getStatus().equals("pending")   ||
            turnos.get(position).getStatus().equals("confirmed") ||
            turnos.get(position).getStatus().equals("cancelled")
            ) {
            System.out.println("en el get view del myadapter");
            int    turnoActual = turnos.get(position).getId();
            Date   fecha       = turnos.get(position).getDate();
            String estado      = turnos.get(position).getStatus();
            String profesional = turnos.get(position).getProfesional().getName();
            //confirmar = turnos.get(position).getCbConfirmar();
            // referenciamos el elemento a modificar y lo rellenamos
            TextView textView = (TextView) v.findViewById(R.id.txtnroTurno);
            //textView.setText(turnoActual);
            textView.setText(String.valueOf(turnoActual));

            TextView textViewEstado = (TextView) v.findViewById(R.id.txtestado);
            textViewEstado.setText(estado);

            TextView textViewProfesional = (TextView) v.findViewById(R.id.txtprofesional);
            textViewProfesional.setText(profesional);

            TextView textViewhorario = (TextView) v.findViewById(R.id.txtFecha);
            textViewhorario.setText(String.valueOf(fecha));


            confirmar = (CheckBox) v.findViewById(R.id.cbConfirmar);
            confirmar.setTag(position);
            //confirmar.setTag(cbConfirmar.getTag(position));
            //System.out.println(confirmar.isChecked() + " 111 llega aca?" + confirmar.getTag() );

            cancelar = (CheckBox) v.findViewById(R.id.cbCancelar);
            cancelar.setTag(position);
            System.out.println(cancelar.isChecked() + " llega aca?" + position);

           confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CompoundButton)v).isChecked()){
                     cancelar.setChecked(false);
                        textViewEstado.setText("confirmed");
                        turnos.get(position).setStatus("confirmed");
                    }
                    else{
                        textViewEstado.setText(estado);
                    }
                }
            });

            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CompoundButton)v).isChecked()){
                        confirmar.setChecked(false);
                        textViewEstado.setText("cancelled");
                        turnos.get(position).setStatus("cancelled");
                    }
                    else{
                        textViewEstado.setText(estado);
                    }
                }
            });

        return v;
    }
        v.setVisibility(View.INVISIBLE);
        return v;
    }
}
