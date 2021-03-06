package com.example.misturnos.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Turno implements Serializable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("date")
    private Date date;

    @SerializedName("status")
    private String status;

    @SerializedName("patient")
    private Paciente paciente;

    @SerializedName("professional")
    private Profesional profesional;

    @SerializedName("specialty")
    private Especialidad especialidad;

    public Turno(Integer id, Date date, String status, Paciente paciente, Profesional profesional, Especialidad especialidad){
        this.id = id;
        this.date = date;
        this.status = status;
        this.paciente = paciente;
        this.profesional = profesional;
        this.especialidad = especialidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

}
