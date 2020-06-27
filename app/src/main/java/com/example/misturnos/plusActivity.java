package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;
import com.example.misturnos.models.Especialidad;
import com.example.misturnos.models.Turno;
import com.example.misturnos.utils.ComboList;

import java.util.ArrayList;
import java.util.List;


import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class plusActivity extends AppCompatActivity {

    ImageButton botonAtras, botonOk;
    Context contexto;
    int añoD, mesD, diaD, horaD, minD;
    int añoH, mesH, diaH, horaH, minH;
    EditText campoFechaDesde , campoFechaHasta , campoHoraDesde, campoHoraHasta;
    static final int tipoDialogoD = 0;
    static final int tipoDialogoH = 1;
    static final int tipodialogohorD = 2;
    static final int tipoDialogohorH = 3;

    static DatePickerDialog.OnDateSetListener selectorFechaDesde, selectorFechaHasta;
    static TimePickerDialog.OnTimeSetListener selectorHoraDesde, selectorHoraHasta;
    private Spinner spinner;
    private Integer idEspecialidad;
    private List<Turno> turnos;

    //    Dialog customDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");
        String tipoUsuario = bundle.getString("tipo_USUARIO");


        contexto = this;

        spinner = findViewById(R.id.spinnerProfesion2);
        List<ComboList> profesiones = llenarEspecialidadesMedico();
        String placeHolder = "Elegir Especialidad:";
        profesiones.add(0,new ComboList(placeHolder, -1));
        ArrayAdapter<ComboList> dataAdapter = new ArrayAdapter<ComboList>(this, android.R.layout.simple_spinner_item, profesiones);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        Button aceptar = (Button) findViewById(R.id.btnOkfiltro);
        Button cancelar = (Button) findViewById(R.id.btnXfiltro);
        Button lunes = (Button) findViewById(R.id.btnLunes);
        Button martes = (Button) findViewById(R.id.btnMartes);
        Button miercoles = (Button) findViewById(R.id.btnMiercoles);
        Button jueves = (Button) findViewById(R.id.btnJueves);
        Button viernes = (Button) findViewById(R.id.btnViernes);
        Button sabado = (Button) findViewById(R.id.btnSabado);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ok = new Intent(plusActivity.this, CalendarioMedicoActivity.class);
                ok.putExtra("USER_ID", userId);
                ok.putExtra("tipo_USUARIO", tipoUsuario);
                startActivity(ok);
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent atras = new Intent(plusActivity.this, CalendarioMedicoActivity.class);
                atras.putExtra("USER_ID", userId);
                atras.putExtra("tipo_USUARIO", tipoUsuario);

                startActivity(atras);                   }
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

        campoHoraDesde = (EditText)findViewById(R.id.texhoraDesde);
        Calendar calendarH = Calendar.getInstance();
        horaD = calendarH.get(Calendar.HOUR_OF_DAY);
        minD = calendarH.get(Calendar.MINUTE);
        mostrarHoraDesde();
        campoHoraHasta = (EditText)findViewById(R.id.texhoraHasta);
        Calendar calendarH2 = Calendar.getInstance();
        horaH = calendarH2.get(Calendar.HOUR_OF_DAY);
        minH = calendarH2.get(Calendar.MINUTE);
        mostrarHoraHasta();

        campoFechaDesde = (EditText)findViewById(R.id.texDesde);
        Calendar calendario = Calendar.getInstance();
        añoD = calendario.get(Calendar.YEAR);
        mesD = calendario.get(Calendar.MONTH)+1;
        diaD = calendario.get(Calendar.DAY_OF_MONTH);
        mostrarFechaDesde();


        campoFechaHasta = (EditText)findViewById(R.id.texHasta);
        Calendar calendario2 = Calendar.getInstance();
        añoH = calendario2.get(Calendar.YEAR);
        mesH = calendario2.get(Calendar.MONTH)+1;
        diaH = calendario2.get(Calendar.DAY_OF_MONTH);
        mostrarFechaHasta();

        selectorHoraDesde = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                horaD = hourOfDay;
                minD  = minute;
                mostrarHoraDesde();
            }
        };
        selectorHoraHasta = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                horaH = hourOfDay;
                minH  = minute;
                mostrarHoraHasta();
            }
        };
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

    }


    private List<ComboList> llenarEspecialidadesMedico(){
        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");

        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        System.out.println("getting specialties");
        Call<List<Especialidad>> call = service.getEspecialidadesByProfesional(userId);
        final List<ComboList> profesiones = new ArrayList<>();
        call.enqueue(new Callback<List<Especialidad>>() {
            @Override
            public void onResponse(Call<List<Especialidad>> call, Response<List<Especialidad>> response) {
                if (response.code() == 200) {
                    System.out.println("getted specialties ok");
                    List<Especialidad> especialidades = response.body();
                    Integer i = 0;
                    for (Especialidad e : especialidades){
                        String profesionEspecialidad = e.getCategory() + " - " + e.getSubCategory();
                        Integer id = e.getIdSubcategory();
                        profesiones.add(new ComboList(profesionEspecialidad, id));
                    }
                } else if (response.code() == 500) {
                    System.out.println("ERROR: code 500 - get specialties failed");
                    Toast.makeText(plusActivity.this, "get specialties failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Especialidad>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(plusActivity.this, "get specialties failed", Toast.LENGTH_SHORT).show();
            }
        });
        return profesiones;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new DatePickerDialog(this, selectorFechaDesde, añoD, mesD, diaD);
        } else if (id == 1) {
            return new DatePickerDialog(this, selectorFechaHasta, añoH, mesH, diaH);
        } else if (id == 2) {
            return new TimePickerDialog(this, selectorHoraDesde, horaD, minD, true);
        }else if (id == 3){
            return new TimePickerDialog(this, selectorHoraHasta, horaH, minH,true);
        }else{
            return null;
        }
    }
    public void verCalendarioDesde(View control){
        showDialog(tipoDialogoD);
    }

    public void verCalendarioHasta(View control){
        showDialog(tipoDialogoH);
    }
    public void verHoraDesde(View control){
        showDialog(tipodialogohorD);
    }
    public void verHoraHasta(View control){
        showDialog(tipoDialogohorH);
    }


    public void mostrarFechaDesde(){
        campoFechaDesde.setText(diaD + "/" + mesD + "/" + añoD);
    }
    public void mostrarFechaHasta(){
        campoFechaHasta.setText(diaH + "/" + mesH + "/" + añoH);
    }
    public void mostrarHoraDesde(){
        campoHoraDesde.setText(horaD + ":" + minD );
    }
    public void mostrarHoraHasta(){
        campoHoraHasta.setText(horaH + ":" + minH );
    }
}


