package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class misTurnosPaciente extends AppCompatActivity {
public TextView  textoEstado, textoEspecialidad, textoHorario, textoTurno, textoFecha;
    ImageButton botonAtras, botonOk;
    private String estado, especialidad, horario, nroTurno , fecha;
    private CheckBox cancelar, confirmar;
    ListView listView;
    private List<String> misturnos, misestados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_turnos_paciente);


        botonAtras = (ImageButton) findViewById(R.id.btnAtras);
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atras = new Intent(misTurnosPaciente.this, CalendarioActivity.class);
                startActivity(atras);
            }
        });
        botonOk = (ImageButton) findViewById(R.id.btnOk);
        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ok = new Intent(misTurnosPaciente.this, CalendarioActivity.class);
                startActivity(ok);
            }
        });
        //cuadroDatos turno1 = new cuadroDatos("1234","disponible","carlos roter","11:00/11:30","17/06/2020");
        //cuadroDatos turno2 = new cuadroDatos("6254","disponible","juan pinelo","14:00/14:30","19/06/2020");

        // datos a mostrar
        listView = (ListView) findViewById(R.id.listaturnos) ;
        misturnos = new ArrayList<String>();
        misturnos.add("123");
        misturnos.add("3213");
        misturnos.add("4213");
        misturnos.add("33322");
        misturnos.add("122112");
        misestados = new ArrayList<String>();
        misestados.add("disponible");
        misestados.add("disponible");
        misestados.add("cancelado");
        misestados.add("disponible");
        misestados.add("cancelado");

        //forma visual en que mostraremos los datos
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, misturnos);
        // enlazamos el adaptador con nuestro list view
        //listView.setAdapter(adapter);
        // System.out.println((ArrayList<String>)misestados );
        //for (int i = 0 ; i <= misestados.size()){
        // if (misestados.toString().equals("disponible")){
        MyAdapter myAdapter = new MyAdapter(this, R.layout.cuadroinfo, (ArrayList<String>) misturnos, (ArrayList<String>) misestados);
        listView.setAdapter(myAdapter);

        //}


     /*   nroTurno = "1234";
        estado = "disponible";
        especialidad = "tocaVulto";
        horario = "8:30 / 9:00 hs";
        this.crearCuadroTurno("1234","disponible","catadordecarne","10:00 a 10:30");
        this.crearCuadroTurno("1235","disponible","catadordeempanada","10:30 a 11:00");
*/

    }
  /*  public void crearCuadroTurno(String turn,String est, String esp, String time){
        nroTurno = turn;
        estado = est;
        especialidad = esp;
        horario = time;
        LinearLayout micuadro = new LinearLayout(this);
        micuadro = (LinearLayout) findViewById(R.id.cuadroTurnoPaciente);
        textoEstado = (TextView)findViewById(R.id.txtestado);
        textoEstado.setText(estado);
        textoEspecialidad = (TextView)findViewById(R.id.txtespecialiad);
        textoEspecialidad.setText(especialidad);
        textoTurno = (TextView)findViewById(R.id.txtnroTurno);
        textoTurno.setText(nroTurno);
        textoHorario = (TextView)findViewById(R.id.txtHorario);
        textoHorario.setText(horario);

        if (estado == "disponible"){
            System.out.println("pasa por aca " + estado + "-/-" + textoEstado.getText().toString() + "/" + nroTurno );
            micuadro.setVisibility(View.VISIBLE);
        }
    }

   */
}
