package com.example.misturnos.models;
import com.google.gson.annotations.SerializedName;
public class Paciente {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("dni")
    private Integer dni;

    @SerializedName("sex")
    private String sex;

    @SerializedName("birthDay")
    private String birthDay;

    public Paciente(Integer id, String name, Integer dni, String sex, String birthDay) {
        this.id = id;
        this.name = name;
        this.dni = dni;
        this.sex = sex;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
}
