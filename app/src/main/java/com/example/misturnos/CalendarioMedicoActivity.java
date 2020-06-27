package com.example.misturnos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;
import com.example.misturnos.models.Credenciales;
import com.example.misturnos.models.Especialidad;
import com.example.misturnos.models.Turno;
import com.example.misturnos.models.Usuario;
import com.example.misturnos.utils.ComboList;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarioMedicoActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button botonSalir, botonTurnos ;
    ImageButton botonPlus;
    private Switch modoPaciente;
    private Integer idEspecialidad;
    private List<Turno> turnos;
    private String tipoUsuario = "medico";
    CalendarView calendarioMedico;
    Date milocaldate;
    private Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_medico);
        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");
        tipoUsuario = bundle.getString("tipo_USUARIO");
        ctx = this;
        modoPaciente = (Switch) findViewById(R.id.swmodoPaciente);
        if (tipoUsuario.toString().equals("paciente")){
            modoPaciente.setChecked(true);

        }else {
            modoPaciente.setChecked(false);
        }
        cargarPantalla();
        modoPaciente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                tipoUsuario = "paciente";
                cargarPantalla();
            } else {
                tipoUsuario = "medico";
                cargarPantalla();
            }
        });

        botonSalir = (Button) findViewById(R.id.btnSalir);
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volver = new Intent(CalendarioMedicoActivity.this, MainActivity.class);
                startActivity(volver);
            }
        });

    }

    private void  cargarPantalla(){

        if (tipoUsuario.toString().equals("medico")){
            Bundle bundle = this.getIntent().getExtras();
            Integer userId = bundle.getInt("USER_ID");
            spinner = findViewById(R.id.spinnerProfesion);
            List<ComboList> profesiones = llenarEspecialidadesMedico();
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
                        milocaldate = date;
                        String month = String.valueOf(localDate.getMonthValue());
                        if (localDate.getMonthValue() < 10){
                            month = "0" + month;
                        }
                        String startDate = String.format("%s-%s-01T00:00:00Z", String.valueOf(localDate.getYear()), month);
                        //localDate.plusMonths(1);
                        //String finishDate = "%s-%s-01T00:00:00Z".format(String.valueOf(localDate.getMonthValue()), localDate.getYear());
                        idEspecialidad = (Integer) item.tag;
                        System.out.println(startDate);

                        //crearTurnosMedico(idEspecialidad, startDate);

                        buscarTurnosMedico(idEspecialidad, startDate);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            botonTurnos = (Button) findViewById(R.id.btnMisTurnos);
            botonTurnos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Misturnos = new Intent(CalendarioMedicoActivity.this, misTurnosMedico.class);

                    Misturnos.putExtra("tipo_USUARIO", tipoUsuario);
                    Misturnos.putExtra("USER_ID", userId);

                    startActivity(Misturnos);
                }
            });

            botonPlus = (ImageButton) findViewById(R.id.btnPlus);
            botonPlus.setVisibility(View.VISIBLE);
            botonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent turnosPlus = new Intent(CalendarioMedicoActivity.this, plusActivity.class);
                    turnosPlus.putExtra("USER_ID", userId);
                    turnosPlus.putExtra("tipo_USUARIO", tipoUsuario);

                    startActivity(turnosPlus);
                }
            });
            calendarioMedico = (CalendarView) findViewById(R.id.calendarViewMedico);

            calendarioMedico.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    Intent agendar = new Intent(CalendarioMedicoActivity.this, agendarTurnoActivity.class);

                    List<Turno> turnosPorDia = filtrarTurnosPorDia(year, month, dayOfMonth);



                    agendar.putExtra("USER_ID", userId);
                    agendar.putExtra("TURNOS_DISPONIBLES", (Serializable) turnosPorDia);
                    if (turnosPorDia.isEmpty()) {
                        Toast.makeText(CalendarioMedicoActivity.this, "no hay turnos disponibles ese dia", Toast.LENGTH_SHORT).show();
                    }else{
                        startActivity(agendar);
                    }
                }
            });

            // ********* si es paciente *************************************************************************************

        } else if (tipoUsuario.toString().equals("paciente")){

            Bundle bundle = this.getIntent().getExtras();
            Integer userId = bundle.getInt("USER_ID");
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
                        //localDate.plusMonths(1);
                        //String finishDate = "%s-%s-01T00:00:00Z".format(String.valueOf(localDate.getMonthValue()), localDate.getYear());
                        idEspecialidad = (Integer) item.tag;
                        buscarTurnos(idEspecialidad, startDate);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            botonTurnos = (Button) findViewById(R.id.btnMisTurnos);
            botonTurnos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Misturnos = new Intent(CalendarioMedicoActivity.this, misTurnosPaciente.class);
                    Misturnos.putExtra("tipo_USUARIO", tipoUsuario);
                    Misturnos.putExtra("USER_ID", userId);

                    startActivity(Misturnos);
                }
            });
            botonPlus = (ImageButton) findViewById(R.id.btnPlus);
            botonPlus.setClickable(false);
            botonPlus.setVisibility(View.INVISIBLE);
        }

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
                    Toast.makeText(CalendarioMedicoActivity.this, "get specialties failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Especialidad>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(CalendarioMedicoActivity.this, "get specialties failed", Toast.LENGTH_SHORT).show();
            }
        });
        return profesiones;
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
                    Toast.makeText(CalendarioMedicoActivity.this, "get specialties failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Especialidad>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(CalendarioMedicoActivity.this, "get specialties failed", Toast.LENGTH_SHORT).show();
            }
        });
        return profesiones;
    }

    public void buscarTurnos(Integer idEspecialidad, String mes) {
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        //arreglar api para que soporte mas de un filtro
        //Call<List<Turno>> call = service.getTurnosByEspecialidad(idEspecialidad, mes);
        Call<List<Turno>> call = service.getTurnosByEspecialidad(idEspecialidad);
        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.code() == 200) {
                    turnos = response.body();
                    if (turnos.isEmpty()) {
                        Toast.makeText(CalendarioMedicoActivity.this, "no hay turnos disponbiles", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(turnos);
                    }
                } else if (response.code() == 500) {
                    System.out.println("ERROR: search appointments failed");
                    Toast.makeText(CalendarioMedicoActivity.this, "search appointments failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Turno>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(CalendarioMedicoActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    public void crearTurnosMedico(Integer idEspecialidad, String mes) {
        List<Turno> turnos = new ArrayList<>();
        turnos.add( new Turno(1,milocaldate,"sinturno",null,null));
        turnos.add( new Turno(2,milocaldate,"sinturno",null,null));
        turnos.add( new Turno(3,milocaldate,"sinturno",null,null));
        turnos.add( new Turno(4,milocaldate,"sinturno",null,null));
System.out.println("creando turnos --->" + turnos.size());

    }

     */

    public void buscarTurnosMedico(Integer idEspecialidad, String mes) {
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        //arreglar api para que soporte mas de un filtro
        //Call<List<Turno>> call = service.getTurnosByEspecialidad(idEspecialidad, mes);
        Call<List<Turno>> call = service.getTurnosByEspecialidad(idEspecialidad);

        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.code() == 200) {
                    turnos = response.body();
                    if (turnos.isEmpty()) {
                        Toast.makeText(CalendarioMedicoActivity.this, "no hay turnos disponbiles", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(turnos);
                    }
                } else if (response.code() == 500) {
                    System.out.println("ERROR: search appointments failed");
                    Toast.makeText(CalendarioMedicoActivity.this, "search appointments failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Turno>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(CalendarioMedicoActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
            Integer turnoMes = localDate.getMonthValue() - 1;
            Integer turnoAño = localDate.getYear();
            Integer turnoDia = localDate.getDayOfMonth();

            if ((year.equals(turnoAño)) && (month.equals(turnoMes)) && (day.equals(turnoDia))) {
                turnosFiltrados.add(t);
            }
        }
        return turnosFiltrados;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Turno> filtrarTurnosPorDiaMedico(Integer year, Integer month, Integer day){
        List<Turno> turnosFiltrados = new ArrayList<>();

        for (Turno t : turnos){
            LocalDate localDate = t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Integer turnoMes = localDate.getMonthValue() - 1;
            Integer turnoAño = localDate.getYear();
            Integer turnoDia = localDate.getDayOfMonth();

            if ((year.equals(turnoAño)) && (month.equals(turnoMes)) && (day.equals(turnoDia))) {
                turnosFiltrados.add(t);
            }
        }
        return turnosFiltrados;
    }
}