package com.example.misturnos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.CalendarView;
import sun.bob.mcalendarview.MCalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDate;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;
import com.example.misturnos.models.Credenciales;
import com.example.misturnos.models.Especialidad;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;

import com.example.misturnos.models.Turno;
import com.example.misturnos.utils.ComboList;

public class CalendarioActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button botonSalir, botonTurnos;
    private Integer idEspecialidad;
    private List<Turno> turnos;
    MCalendarView calendarioPaciente;
    private String elPass, elUsuario , recuerdame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");
        elPass      = bundle.getString("ELPASS");
        elUsuario   = bundle.getString("ELUSUARIO");
        recuerdame  = bundle.getString("RECUERDAME");

        spinner = findViewById(R.id.spinnerProfesion);
        List<ComboList> profesiones = llenarEspecialidades();
        String placeHolder = "Elegir Especialidad:";
        profesiones.add(0,new ComboList(placeHolder, -1));
        ArrayAdapter<ComboList> dataAdapter = new ArrayAdapter<ComboList>(this, android.R.layout.simple_spinner_item, profesiones);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ComboList item = (ComboList) parent.getItemAtPosition(position);
                if (!item.toString().equals(placeHolder))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    String month = String.valueOf(localDate.getMonthValue());
                    if (localDate.getMonthValue() < 10){
                        month = "0" + month;
                    }
                    String startDate = String.format("%s-%s-01T00:00:00Z", String.valueOf(localDate.getYear()), month);
                    idEspecialidad = (Integer) item.tag;
                    buscarTurnos(idEspecialidad, startDate);

                    Integer year = localDate.getYear();
                    Integer monthInt = localDate.getMonthValue();
                    System.out.println("EL MES QUE ESTOY LEYENDO!!!!");
                    System.out.println(monthInt);
                    List<Integer> turnosPorDia = filtrarDiasConTurnoMes(year, monthInt);
                    if (turnosPorDia.isEmpty()) {
                        Toast.makeText(CalendarioActivity.this, "no hay turnos disponibles en este mes", Toast.LENGTH_SHORT).show();
                    }else{
                        for (Integer d : turnosPorDia){
                            calendarioPaciente.markDate(year, monthInt, d);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        botonSalir = (Button) findViewById(R.id.btnSalir);
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent volver = new Intent(CalendarioActivity.this, MainActivity.class);
                    startActivity(volver);
            }
        });
        botonTurnos = (Button) findViewById(R.id.btnMisTurnos);
        botonTurnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent turnos = new Intent(CalendarioActivity.this, misTurnosPaciente.class);
                turnos.putExtra("USER_ID", userId);
                startActivity(turnos);
            }
        });

        calendarioPaciente = (MCalendarView) findViewById(R.id.calendarViewPaciente);

        calendarioPaciente.setOnMonthChangeListener(new OnMonthChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMonthChange(int year, int month) {
                List<Integer> turnosPorDia = filtrarDiasConTurnoMes(year, month);
                if (turnosPorDia.isEmpty()) {
                    Toast.makeText(CalendarioActivity.this, "no hay turnos disponibles en este mes", Toast.LENGTH_SHORT).show();
                }else{
                    for (Integer d : turnosPorDia){
                        calendarioPaciente.markDate(year, month, d);
                    }
                }
            }
        });

        calendarioPaciente.setOnDateClickListener(new OnDateClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateClick(View view, DateData date) {
                Intent agendar = new Intent(CalendarioActivity.this, agendarTurnoActivity.class);
                List<Turno> turnosPorDia = filtrarTurnosPorDia(date.getYear(), date.getMonth(), date.getDay());
                agendar.putExtra("USER_ID", userId);
                agendar.putExtra("TURNOS_DISPONIBLES", (Serializable) turnosPorDia);
                if (turnosPorDia.isEmpty()) {
                    Toast.makeText(CalendarioActivity.this, "no hay turnos disponibles ese dia", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(agendar);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Turno> filtrarTurnosPorDia(Integer year, Integer month, Integer day){
        List<Turno> turnosFiltrados = new ArrayList<>();
        if (turnos == null) {
            return turnosFiltrados;
        }
        for (Turno t : turnos){
            LocalDate localDate = t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Integer turnoMes = localDate.getMonthValue();
            Integer turnoAño = localDate.getYear();
            Integer turnoDia = localDate.getDayOfMonth();

            if ((year.equals(turnoAño)) && (month.equals(turnoMes)) && (day.equals(turnoDia))) {
                turnosFiltrados.add(t);
            }
        }
        return turnosFiltrados;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Integer> filtrarDiasConTurnoMes(Integer year, Integer month){
        List<Integer> diasConTurno = new ArrayList<>();
        if (turnos == null) {
            return diasConTurno;
        }

        for (Turno t : turnos){
            LocalDate localDate = t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Integer turnoMes = localDate.getMonthValue();
            Integer turnoAño = localDate.getYear();
            Integer turnoDia = localDate.getDayOfMonth();

            if ((year.equals(turnoAño)) && (month.equals(turnoMes))){
                if (!diasConTurno.contains(turnoDia)) {
                    diasConTurno.add(turnoDia);
                }
            }
        }
        return diasConTurno;
    }
    private List<ComboList> llenarEspecialidades(){
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        System.out.println("getting specialties");
        Call<List<Especialidad>> call = service.getEspecialidades();
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
                    Toast.makeText(CalendarioActivity.this, "get specialties failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Especialidad>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(CalendarioActivity.this, "get specialties failed", Toast.LENGTH_SHORT).show();
            }
        });
        return profesiones;
    }

    public void buscarTurnos(Integer idEspecialidad, String mes) {
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        //arreglar api para que soporte mas de un filtro
        //Call<List<Turno>> call = service.getTurnosByEspecialidad(idEspecialidad, mes);
        System.out.println("Especialidad");
        System.out.println(idEspecialidad);
        Call<List<Turno>> call = service.getTurnosByEspecialidad(idEspecialidad);
        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.code() == 200) {
                    turnos = response.body();
                    if (turnos.isEmpty()) {
                        Toast.makeText(CalendarioActivity.this, "no hay turnos disponbiles", Toast.LENGTH_SHORT).show();
                    }else {
                        System.out.println("Imprimo Turnos");
                        System.out.println(turnos);
                    }
                } else if (response.code() == 500) {
                    System.out.println("ERROR: search appointments failed");
                    Toast.makeText(CalendarioActivity.this, "search appointments failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Turno>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(CalendarioActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
