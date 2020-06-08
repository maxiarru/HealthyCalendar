package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import java.util.Calendar;

public class plusActivity extends AppCompatActivity {
 Button  botonFiltro;
ImageButton botonAtras, botonOk;
Context contexto;
int añoD, mesD, diaD;
    int añoH, mesH, diaH;
EditText campoFechaDesde , campoFechaHasta;
 static final int tipoDialogoD = 0;
    static final int tipoDialogoH = 1;
 static DatePickerDialog.OnDateSetListener selectorFechaDesde, selectorFechaHasta;
    private Spinner spinner;

//    Dialog customDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnos);



        botonAtras = (ImageButton) findViewById(R.id.btnAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atras = new Intent(plusActivity.this, CalendarioMedicoActivity.class);
                startActivity(atras);
            }
        });
        botonOk = (ImageButton) findViewById(R.id.btnOk);
        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ok = new Intent(plusActivity.this, CalendarioMedicoActivity.class);
                startActivity(ok);
            }
        });
        contexto = this;
     // boton de filtros
       botonFiltro = (Button) findViewById(R.id.btnFiltro);
       botonFiltro.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               //creamos objeto tipo dialogo
               final Dialog customDialog = new Dialog(contexto);
               // no queremos un titulo
               customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
               // impedir que se cancele el dialogo
               customDialog.setCancelable(false);
               // fondo transparente
               customDialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
               //le asignamos el xml q tiene el diseño
               customDialog.setContentView(R.layout.menuplus);

               spinner = (Spinner)customDialog.findViewById(R.id.spinnerProfesion2);
               List<String> profesiones = new ArrayList<>();
               profesiones.add(0,"Elegir Profesion");
               profesiones.add("Pediatra");
               profesiones.add("Obstetra");
               profesiones.add("Cardiologo");
               ArrayAdapter<String> dataAdapter;
               dataAdapter = new ArrayAdapter<>(customDialog.getContext(), android.R.layout.simple_spinner_item, profesiones);
               dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               spinner.setAdapter(dataAdapter);
               spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       if (parent.getItemAtPosition(position).equals("Elegir Especialidad"))
                       {
                       }
                       else
                       {
                           String item = parent.getItemAtPosition(position).toString();
                           Toast.makeText(parent.getContext(), "selected: " + item, Toast.LENGTH_SHORT).show();
                       }
                   }
                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {
                   }
               });

               Button aceptar = (Button) customDialog.findViewById(R.id.btnOkfiltro);
               Button cancelar = (Button) customDialog.findViewById(R.id.btnXfiltro);
               Button lunes = (Button) customDialog.findViewById(R.id.btnLunes);
               Button martes = (Button) customDialog.findViewById(R.id.btnMartes);
               Button miercoles = (Button) customDialog.findViewById(R.id.btnMiercoles);
               Button jueves = (Button) customDialog.findViewById(R.id.btnJueves);
               Button viernes = (Button) customDialog.findViewById(R.id.btnViernes);
               Button sabado = (Button) customDialog.findViewById(R.id.btnSabado);

               aceptar.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       customDialog.dismiss();
                   }
               });
               cancelar.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       customDialog.dismiss();
                   }
               });
               lunes.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View l) {
                       if (l.isActivated()) {
                           l.setActivated(false);
                       }else {
                       l.setActivated(true);
                       }
                       }
               });
               martes.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View ma) {
                       if (ma.isActivated()) {
                           ma.setActivated(false);
                       }else {
                           ma.setActivated(true);
                       }
                   }
               });
               miercoles.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View mi) {
                       if (mi.isActivated()) {
                           mi.setActivated(false);
                       }else {
                           mi.setActivated(true);
                       }
                   }
               });
               jueves.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View j) {
                       if (j.isActivated()) {
                           j.setActivated(false);
                       }else {
                           j.setActivated(true);
                       }
                   }
               });
               viernes.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (v.isActivated()) {
                           v.setActivated(false);
                       }else {
                           v.setActivated(true);
                       }
                   }
               });
               sabado.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View s) {
                       if (s.isActivated()) {
                           s.setActivated(false);
                       }else {
                           s.setActivated(true);
                       }
                   }
               });

               campoFechaDesde = (EditText)customDialog.findViewById(R.id.texDesde);
               Calendar calendario = Calendar.getInstance();
               añoD = calendario.get(Calendar.YEAR);
               mesD = calendario.get(Calendar.MONTH)+1;
               diaD = calendario.get(Calendar.DAY_OF_MONTH);
               mostrarFechaDesde();

               campoFechaHasta = (EditText)customDialog.findViewById(R.id.texHasta);
               Calendar calendario2 = Calendar.getInstance();
               añoH = calendario2.get(Calendar.YEAR);
               mesH = calendario2.get(Calendar.MONTH)+1;
               diaH = calendario2.get(Calendar.DAY_OF_MONTH);
               mostrarFechaHasta();
               selectorFechaDesde = new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       añoD = year;
                       mesD = month;
                       diaD = dayOfMonth;
                       mostrarFechaDesde();
                   }
               };
               selectorFechaHasta = new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       añoH = year;
                       mesH = month;
                       diaH = dayOfMonth;
                       mostrarFechaHasta();
                   }
               };

               customDialog.show();
           }

       });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {

            return new DatePickerDialog(this, selectorFechaDesde, añoD, mesD, diaD);
        } else if (id == 1) {
            return new DatePickerDialog(this, selectorFechaHasta, añoH, mesH, diaH);
        } else {
            return null;
        }
    }
    public void verCalendarioDesde(View control){
        showDialog(tipoDialogoD);
    }

    public void verCalendarioHasta(View control){
        showDialog(tipoDialogoH);
    }


    public void mostrarFechaDesde(){
        campoFechaDesde.setText(diaD + "/" + mesD + "/" + añoD);
    }
    public void mostrarFechaHasta(){
        campoFechaHasta.setText(diaH + "/" + mesH + "/" + añoH);
    }
}

