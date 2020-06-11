package com.example.misturnos.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profesional implements Serializable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("dni")
    private Integer dni;

    @SerializedName("doctorNumber")
    private Integer doctorNumber;

    @SerializedName("birthDay")
    private String birthDay;

    public Profesional(Integer id, String name, Integer dni, Integer doctorNumber, String birthDay) {
        this.id = id;
        this.name = name;
        this.dni = dni;
        this.doctorNumber = doctorNumber;
        this.birthDay = birthDay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public Integer getDoctorNumber() {
        return doctorNumber;
    }

    public void setDoctorNumber(Integer doctorNumber) {
        this.doctorNumber = doctorNumber;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
}
