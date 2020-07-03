package com.example.misturnos.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Horario implements Serializable {
    @SerializedName("year")
    private Integer year;

    @SerializedName("month")
    private Integer month;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public List<HorarioItem> getItems() {
        return items;
    }

    public void setItems(List<HorarioItem> items) {
        this.items = items;
    }

    @SerializedName("schedule")
    private List<HorarioItem> items;

    public Horario(Integer year, Integer month, List<HorarioItem> items){
        this.year = year;
        this.month = month;
        this.items = items;
    }
}
