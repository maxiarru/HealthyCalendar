package com.example.misturnos.client.api;
import com.example.misturnos.models.Credenciales;
import com.example.misturnos.models.Especialidad;
import com.example.misturnos.models.Horario;
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

    @GET("/v1/professionals/{id}/appointments")
    Call<List<Turno>> getTurnosMedico(@Path("id") Integer idProfesional, @Query("startDate") String startDate, @Query("finishDate") String finishDate, @Query("idspecialty") Integer idSpecialty, @Query("status") String status);

    @PUT("/v1/professionals/{id}/appointments/{idAppointment}/cancel")
    Call<Void> putCancelarTurnoMedico(@Path("id") Integer idMedico, @Path("idAppointment") Integer idTurno);

    @PUT("/v1/professionals/{id}/appointments/{idAppointment}/attend")
    Call<Void> putAtenderTurnoMedico(@Path("id") Integer idMedico, @Path("idAppointment") Integer idTurno);

    @GET("/v1/appointments")
        //Call<List<Turno>> getTurnosByEspecialidad(@Query("idspecialty") Integer idSpecialty, @Query("startDate") String startDate);
    Call<List<Turno>> getTurnosByEspecialidad(@Query("idspecialty") Integer idSpecialty);

    @GET("/v1/patients/{id}/appointments")
    Call<List<Turno>> getTurnos(@Path("id") Integer idPaciente);

    @PUT("/v1/patients/{id}/appointments/{idAppointment}/confirm")
    Call<Void> putConfirmarTurnos(@Path("id") Integer idPaciente, @Path("idAppointment") Integer idTurno);

    @PUT("/v1/patients/{id}/appointments/{idAppointment}/cancel")
    Call<Void> putCancelarTurnos(@Path("id") Integer idPaciente, @Path("idAppointment") Integer idTurno);

    @PUT("/v1/patients/{id}/appointments/{idAppointment}/request")
    Call<Void> putAgendarTurno(@Path("id") Integer idPaciente, @Path("idAppointment") Integer idTurno);

    @POST("/v1/professionals/{id}/specialties/{idSpecialty}/schedule")
    Call<Void> SetHorariosMedico(@Path("id") Integer idMedico, @Path("idSpecialty") Integer idSpecialty, @Body Horario horario);
}