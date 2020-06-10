package com.example.misturnos.client.api;
import com.example.misturnos.models.Credenciales;
import com.example.misturnos.models.Especialidad;
import com.example.misturnos.models.Paciente;
import com.example.misturnos.models.Turno;
import com.example.misturnos.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("/v1/login")
    Call<Usuario> loginUser(@Body Credenciales cred);

    @GET("/v1/specialties")
    Call<List<Especialidad>> getEspecialidades();

    @GET("/v1/professionals/{id}/specialties")
    Call<List<Especialidad>> getEspecialidadesByProfesional(@Path("id") Integer idProfesional);

    @GET("/v1/patients/{id}")
    Call<Paciente> getPacienteById(@Path("id") Integer idPaciente);

    @GET("/v1/professionals/{id}")
    Call<Paciente> getProfesionalById(@Path("id") Integer idProfesional);

    @GET("/v1/patients/{id}/appointments")
    Call<List<Turno>> getTurnos(@Path("id") Integer idPaciente);
}