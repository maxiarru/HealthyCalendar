package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.CheckBox;

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
    CheckBox recuerdame;
    String elPass, elUsuario , recuerdameAux;
    private static HttpURLConnection httpConnection;
    private String elUser, elPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (EditText) findViewById(R.id.txtUser);
        pass = (EditText) findViewById(R.id.txtPass);
        recuerdame = (CheckBox) findViewById(R.id.cbRememberMe);
        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
            recuerdameAux = bundle.getString("RECUERDAME");
            elPass = bundle.getString("ELPASS");
            elUsuario = bundle.getString("ELUSUARIO");
            if (recuerdameAux == null) {
                recuerdame.setChecked(false);
            } else {
                if (recuerdameAux.equalsIgnoreCase("recordar") ) {
                    recuerdame.setChecked(true);
                    user.setText(elUsuario);
                    pass.setText(elPass);
                    elUser = elUsuario;
                    elPassword = elPass;
                } else {
                    recuerdame.setChecked(false);

                }
            }
        }

        // para los casos que nunca se hace click en el checkbox
        if (!recuerdame.isChecked()){
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
                                    medicoAct.putExtra("tipo_USUARIO", "medico");
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
        else{
            botonLogin = (Button) findViewById(R.id.btnLogin);
            //   URL url = new URL("")
            recuerdameAux = "recordar";
            botonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (elUsuario == null){
                        elUsuario = user.getText().toString();
                        elPass = pass.getText().toString();
                    }

                    ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                    System.out.printf("login user %s\n", elUser);
                    Call<Usuario> call = service.loginUser(new Credenciales(elUser, elPassword));
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.code() == 200) {
                                Usuario user = response.body();
                                if (user.isMedico()) {
                                    Intent medicoAct = new Intent(MainActivity.this, CalendarioMedicoActivity.class);
                                    medicoAct.putExtra("USER_ID", user.getId());
                                    medicoAct.putExtra("tipo_USUARIO", "medico");
                                    medicoAct.putExtra("ELUSUARIO", elUser);
                                    medicoAct.putExtra("ELPASS",elPassword);
                                    medicoAct.putExtra("RECUERDAME", recuerdameAux);
                                    startActivity(medicoAct);
                                }
                                else {
                                    Intent pacienteAct = new Intent(MainActivity.this, CalendarioActivity.class);
                                    pacienteAct.putExtra("USER_ID", user.getId());
                                    pacienteAct.putExtra("ELUSUARIO", elUser);
                                    pacienteAct.putExtra("ELPASS",elPassword);
                                    pacienteAct.putExtra("RECUERDAME", recuerdameAux);
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

// para cuando se tilda o destilda el checkbox

        recuerdame.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(recuerdame.isChecked()){
                botonLogin = (Button) findViewById(R.id.btnLogin);
                //   URL url = new URL("")
                recuerdameAux = "recordar";
                botonLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (elUsuario == null){
                            elUsuario = user.getText().toString();
                            elPass = pass.getText().toString();
                        }
                        String elUser = elUsuario;
                        String elPassword = elPass;
                        ApiService service = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
                        System.out.printf("login user %s\n", elUser);
                        Call<Usuario> call = service.loginUser(new Credenciales(elUser, elPassword));
                        call.enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                if (response.code() == 200) {
                                    Usuario user = response.body();
                                    if (user.isMedico()) {
                                        Intent medicoAct = new Intent(MainActivity.this, CalendarioMedicoActivity.class);
                                        medicoAct.putExtra("USER_ID", user.getId());
                                        medicoAct.putExtra("tipo_USUARIO", "medico");
                                        medicoAct.putExtra("ELUSUARIO", elUser);
                                        medicoAct.putExtra("ELPASS",elPassword);
                                        medicoAct.putExtra("RECUERDAME", recuerdameAux);
                                        startActivity(medicoAct);
                                    }
                                    else {
                                        Intent pacienteAct = new Intent(MainActivity.this, CalendarioActivity.class);
                                        pacienteAct.putExtra("USER_ID", user.getId());
                                        pacienteAct.putExtra("ELUSUARIO", elUser);
                                        pacienteAct.putExtra("ELPASS",elPassword);
                                        pacienteAct.putExtra("RECUERDAME", recuerdameAux);
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
            // no recuerda contraseña
            else {
                recuerdameAux= "olvidar";
                user.setText("");
                pass.setText("");
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
                                        medicoAct.putExtra("tipo_USUARIO", "medico");
                                        medicoAct.putExtra("RECUERDAME", recuerdame.isChecked());
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
        });

    }
}