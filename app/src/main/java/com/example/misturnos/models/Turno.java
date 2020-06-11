package com.example.misturnos.models;
import android.widget.CheckBox;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Turno {
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


    private  CheckBox cbConfirmar;

    public Turno(Integer id, Date date, String status, Paciente paciente, Profesional profesional){
        this.id            = id;
        this.date          = date;
        this.status        = status;
        this.paciente      = paciente;
        this.profesional   = profesional;
      //  this.cbConfirmar   = cbConfirmar;
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
   // public CheckBox getCbConfirmar() {
   //     return cbConfirmar;
    //}

    //public void setCbConfirmar(CheckBox cbConfirmar) {
    //    this.cbConfirmar = cbConfirmar;
    //}
}
