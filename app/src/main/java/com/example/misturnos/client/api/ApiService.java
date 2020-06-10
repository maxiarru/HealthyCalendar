package com.example.misturnos.client.api;
import com.example.misturnos.models.Credenciales;
import com.example.misturnos.models.Usuario;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("/v1/login")
    Call<Usuario> loginUser(@Body Credenciales cred);
}