package com.example.misturnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
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
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencias = getSharedPreferences("mispreferencias", Context.MODE_PRIVATE);
        cargaLogin();
        setCredenciales();

        recuerdame.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!recuerdame.isChecked()) {
                preferencias.edit().clear().apply();
                SharedPreferences.Editor editor = preferencias.edit();
            }
        });

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(" cuando hace click --------> " + recuerdame.isChecked()  );
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
                                savePreferencias(elUser,elPass);

                            }
                            else {
                                Intent pacienteAct = new Intent(MainActivity.this, CalendarioActivity.class);
                                pacienteAct.putExtra("USER_ID", user.getId());
                                startActivity(pacienteAct);
                                savePreferencias(elUser,elPass);
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

    private void setCredenciales() {
        String usuario = getUsuarioPreferencias();
        String password = getPassPreferencias();
        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(password)){
            user.setText(usuario);
            pass.setText(password);
            recuerdame.setChecked(true);
        }
    }

    private void savePreferencias(String usuarioP, String passP){
        if (recuerdame.isChecked()){
            System.out.println("------+++++ como estan mis preferencias ? +++++++----" + usuarioP + passP + recuerdame.isChecked());
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("usuario", usuarioP);
            editor.putString("pass", passP);
            editor.putBoolean("recuerdame", recuerdame.isChecked());
            editor.commit();
            editor.apply();
        }else{
            System.out.println("------+++++ borrando mis preferencias +++++++----" + usuarioP + passP + recuerdame.isChecked());
            preferencias.edit().clear().apply();
            SharedPreferences.Editor editor = preferencias.edit();
         //   System.out.println(" asdasdasd" + );
        }
    }

    private String getPassPreferencias(){
        return preferencias.getString("pass","");
    }
    private  String getUsuarioPreferencias(){
        return preferencias.getString("usuario","");
    }
    private boolean getRecordarPreferencias(){
        return preferencias.getBoolean("recuerdame", recuerdame.isChecked());
    }

    private  void cargaLogin(){
        user = (EditText) findViewById(R.id.txtUser);
        pass = (EditText) findViewById(R.id.txtPass);
        recuerdame = (CheckBox) findViewById(R.id.cbRememberMe);
        botonLogin = (Button) findViewById(R.id.btnLogin);
    }
}