package com.example.misturnos.models;
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

}
