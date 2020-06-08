package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    /* definir variables */
    EditText usser, pass;
    String usuario;
    Button botonLogin;
    private static HttpURLConnection httpConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usser = (EditText) findViewById(R.id.txtUsser);
        pass = (EditText) findViewById(R.id.txtPass);
        botonLogin = (Button) findViewById(R.id.btnLogin);
     //   URL url = new URL("")

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String elUsser = usser.getText().toString();
                    String elPass = pass.getText().toString();
                    if (elUsser.toString().equals("medico")) {
                        Intent aux1 = new Intent(MainActivity.this, CalendarioMedicoActivity.class);
                        startActivity(aux1);
                    }
                    else{
                    Intent aux = new Intent(MainActivity.this, CalendarioActivity.class);
                    startActivity(aux);
                }
            }
        });
    }
}
