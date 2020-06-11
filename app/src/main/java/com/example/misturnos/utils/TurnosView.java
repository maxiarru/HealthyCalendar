package com.example.misturnos.utils;

public class TurnosView {
    public String string;
    public Object tag;

    public TurnosView(String stringPart, Object tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString() {
        return string;
    }
}