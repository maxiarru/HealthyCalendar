package com.example.misturnos;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private int layout;
   // private ArrayList<cuadroDatos> turno;
    private  ArrayList<String> turno;
    private  ArrayList<String> estado;
    private CheckBox cancelar, confirmar;

    public MyAdapter(Context context, int layout, ArrayList<String> turno, ArrayList<String> estado){
        this.context = context;
        this.layout = layout;
        this.turno = turno;
        this.estado = estado;
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
        v = layoutInflater.inflate(R.layout.cuadroinfo, null);
        // nos traemos el vlor actual dependiendo de la pos.i
        if (estado.get(position).equals("disponible")) {
            String turnoActual = turno.get(position);
            String estadoActual = estado.get(position);
            // referenciamos el elemento a modificar y lo rellenamos
            TextView textView = (TextView) v.findViewById(R.id.txtnroTurno);
            textView.setText(turnoActual);
            TextView textViewEstado = (TextView) v.findViewById(R.id.txtestado);
            textViewEstado.setText(estadoActual);
            confirmar = (CheckBox) v.findViewById(R.id.cbConfirmar);
            cancelar = (CheckBox) v.findViewById(R.id.cbCancelar);
            System.out.println(confirmar.isChecked());
            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CompoundButton)v).isChecked()){
                     cancelar.setChecked(false);
                    }
                }
            });
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CompoundButton)v).isChecked()){
                        confirmar.setChecked(false);
                    }
                }
            });

            if (confirmar.isChecked() == true){
                cancelar.setChecked(false);
            }
            if (cancelar.isChecked()==true){
                confirmar.setChecked(false);
            }
        return v;
    }
        v.setVisibility(View.INVISIBLE);
        return v;
    }
}
