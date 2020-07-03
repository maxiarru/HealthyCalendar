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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.misturnos.CalendarioMedicoActivity;
import com.example.misturnos.R;
import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;
import com.example.misturnos.models.Especialidad;
import com.example.misturnos.models.Turno;
import com.example.misturnos.plusActivity;
import com.example.misturnos.utils.ComboList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class misTurnosMedico extends AppCompatActivity {

    Button botonFiltro;
    ImageButton botonAtras;
    Context contexto;
    int añoD, mesD, diaD, horaD, minD;
    int añoH, mesH, diaH, horaH, minH;
    EditText campoFechaDesde , campoFechaHasta;
    static final int tipoDialogoD = 0;
    static final int tipoDialogoH = 1;
    static final int tipodialogohorD = 2;
    static final int tipoDialogohorH = 3;
    static DatePickerDialog.OnDateSetListener selectorFechaDesde, selectorFechaHasta;
    private Spinner spinner;
    private Spinner spinnerEstado;
    ListView listView;
    private Calendar calendario2, calendario;
    private ComboList especialidad;
    private ComboList estado;


    //    Dialog customDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_turnos_medico);
        Bundle bundle = this.getIntent().getExtras();
        Integer userId = bundle.getInt("USER_ID");
        String tipoUsuario = bundle.getString("tipo_USUARIO");

        botonAtras = (ImageButton) findViewById(R.id.btnAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atras = new Intent(misTurnosMedico.this, CalendarioMedicoActivity.class);
                atras.putExtra("USER_ID", userId);
                atras.putExtra("tipo_USUARIO", tipoUsuario);
                startActivity(atras);
            }
        });

        contexto = this;

        listView = (ListView) findViewById(R.id.listaturnos) ;
        llenarTurnosMedico(userId);

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
                customDialog.setContentView(R.layout.filtro_medico);

                spinner = (Spinner) customDialog.findViewById(R.id.spinnerProfesion3);
                List<ComboList> profesiones = llenarEspecialidadesMedico();
                String placeHolder = "Elegir Especialidad:";
                profesiones.add(0,new ComboList(placeHolder, -1));
                ArrayAdapter<ComboList> dataAdapter = new ArrayAdapter<ComboList>(customDialog.getContext(), android.R.layout.simple_spinner_item, profesiones);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ComboList item = (ComboList) parent.getItemAtPosition(position);
                        especialidad = item;
                        System.out.println("q tengo ?  " + item.string + item.toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spinner = (Spinner) customDialog.findViewById(R.id.spinnerStatus);
                List<ComboList> estados = new ArrayList<>();
                estados.add(0,new ComboList("Elegir Estado:", -1));
                estados.add(1, new ComboList("confirmed", 1));
                estados.add(2, new ComboList("pending", 2));
                estados.add(3, new ComboList("avaiable", 3));
                estados.add(4, new ComboList("cancelled", 4));

                ArrayAdapter<ComboList> EstadodataAdapter = new ArrayAdapter<ComboList>(customDialog.getContext(), android.R.layout.simple_spinner_item, estados);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(EstadodataAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ComboList item = (ComboList) parent.getItemAtPosition(position);
                        estado = item;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Button aceptar = (Button) customDialog.findViewById(R.id.btnOkfiltro);
                Button cancelar = (Button) customDialog.findViewById(R.id.btnXfiltro);

                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filtrarTurnosMedico(userId , calendario.getTime(), calendario2.getTime(), especialidad, estado);
                        customDialog.dismiss();
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customDialog.dismiss();
                    }
                });

                campoFechaDesde = (EditText)customDialog.findViewById(R.id.texDesde);
                calendario = Calendar.getInstance();
                añoD = calendario.get(Calendar.YEAR);
                mesD = calendario.get(Calendar.MONTH)+1;
                diaD = calendario.get(Calendar.DAY_OF_MONTH);
                calendario.set(Calendar.HOUR_OF_DAY, 8);
                calendario.set(Calendar.MINUTE, 0);
                mostrarFechaDesde();

                campoFechaHasta = (EditText)customDialog.findViewById(R.id.texHasta);
                calendario2 = Calendar.getInstance();
                añoH = calendario2.get(Calendar.YEAR);
                mesH = calendario2.get(Calendar.MONTH)+1;
                diaH = calendario2.get(Calendar.DAY_OF_MONTH);
                calendario2.set(Calendar.HOUR_OF_DAY, 18);
                calendario2.set(Calendar.MINUTE, 0);
                mostrarFechaHasta();



                selectorFechaDesde = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        añoD = year;
                        mesD = month;
                        diaD = dayOfMonth;
                        mostrarFechaDesde();
                        calendario.set(añoD, mesD, diaD);
                        System.out.println("fecha desde    /  " + calendario.getTime());
                    }
                };
                selectorFechaHasta = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        añoH = year;
                        mesH = month;
                        diaH = dayOfMonth;
                        mostrarFechaHasta();
                        //calendarHasta
                        calendario2.set(añoH , mesH, diaH);
                        System.out.println("fecha hasta    /  " + calendario2.getTime());
                    }
                };

                customDialog.show();
            }

        });
    }
    private void llenarTurnosMedico(Integer userId){
        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<List<Turno>> call = service.getTurnosMedico(userId, null, null, null, null);
        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.code() == 200) {
                    System.out.println("getted turnos ok");
                    List<Turno> turnos = response.body();
                    AdapterMisTurnosMedico adapterMisTurnosMedico = new AdapterMisTurnosMedico(contexto, R.layout.cuadro_misturnos_medico,  (ArrayList<Turno>) turnos);
                    listView.setAdapter(adapterMisTurnosMedico);
                } else if (response.code() == 500) {
                    System.out.println("ERROR: code 500 - get specialties failed");
                    Toast.makeText(misTurnosMedico.this, "get turnos failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Turno>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(misTurnosMedico.this, "get turnos failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void filtrarTurnosMedico(Integer userId, Date fechadesde, Date fechahasta, ComboList especialidad, ComboList estado){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String strFechaDesde = formatter.format(fechadesde);
        String strFechaHasta = formatter.format(fechahasta);
        Integer filterEspecialidad;
        String filterEstado;
        if ((Integer) especialidad.tag > 0) {
            filterEspecialidad = (Integer) especialidad.tag;
        }else{
            filterEspecialidad = null;
        }

        if ((Integer) estado.tag > 0) {
            filterEstado = estado.string;
        }else{
            filterEstado = null;
        }

        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<List<Turno>> call = service.getTurnosMedico(userId, strFechaDesde, strFechaHasta, filterEspecialidad, filterEstado);
        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.code() == 200) {
                    System.out.println("getted turnos ok");
                    List<Turno> turnos = response.body();
                    AdapterFiltroMedico adapterfiltroMedico = new AdapterFiltroMedico(contexto, R.layout.cuadro_misturnos_medico,  (ArrayList<Turno>) turnos, fechadesde, fechahasta, especialidad.string);
                    listView.setAdapter(adapterfiltroMedico);
                } else if (response.code() == 500) {
                    System.out.println("ERROR: code 500 - get specialties failed");
                    Toast.makeText(misTurnosMedico.this, "get filter failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Turno>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(misTurnosMedico.this, "get filter failed", Toast.LENGTH_SHORT).show();
            }
        });
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
                    Toast.makeText(misTurnosMedico.this, "get specialties failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Especialidad>> call, Throwable t) {
                System.out.printf("ERROR: %s", t.getMessage());
                Toast.makeText(misTurnosMedico.this, "get specialties failed", Toast.LENGTH_SHORT).show();
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

