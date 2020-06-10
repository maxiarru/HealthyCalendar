package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.misturnos.client.api.ApiService;
import com.example.misturnos.client.api.RetrofitClientInstance;
import com.example.misturnos.models.Credenciales;
import com.example.misturnos.models.Usuario;
import com.example.misturnos.models.Rol;

public class MainActivity extends AppCompatActivity {
    /* definir variables */
    EditText user, pass;
    Button botonLogin;
    private static HttpURLConnection httpConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (EditText) findViewById(R.id.txtUser);
        pass = (EditText) findViewById(R.id.txtPass);
        //password.setTransformationMethod(new PasswordTransformationMethod());
        botonLogin = (Button) findViewById(R.id.btnLogin);
     //   URL url = new URL("")

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String elUser = user.getText().toString();
                    String elPass = pass.getText().toString();
                    ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                    System.out.printf("login user %s\n", elUser);
                    Call<Usuario> call = service.loginUser(new Credenciales(elUser, elPass));
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.code() == 200) {
                                Usuario user = response.body();
                                if (user.isMedico()) {
                                    Intent medicoAct = new Intent(MainActivity.this, CalendarioMedicoActivity.class);
                                    medicoAct.putExtra("USER_ID", user.getId());
                                    startActivity(medicoAct);
                                }
                                else {
                                    Intent pacienteAct = new Intent(MainActivity.this, CalendarioActivity.class);
                                    pacienteAct.putExtra("USER_ID", user.getId());
                                    startActivity(pacienteAct);
                                }
                            } else if (response.code() == 500) {
                                System.out.println("ERROR: authentication failed");
                                Toast.makeText(MainActivity.this, "User or password invalid", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            System.out.printf("ERROR: %s", t.getMessage());
                            Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }
}