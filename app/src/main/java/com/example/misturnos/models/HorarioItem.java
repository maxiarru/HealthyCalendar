package com.example.misturnos.models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HorarioItem implements Serializable {
    @SerializedName("day")
    private Integer day;

    @SerializedName("startTime")
    private String startTime;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    @SerializedName("finishTime")
    private String finishTime;

    public HorarioItem(Integer day, String startTime, String finishTime){
        this.day = day;
        this.startTime  = startTime;
        this.finishTime = finishTime;
    }
}
