package com.example.misturnos.utils;
import java.util.ArrayList;
public class ComboList {
    public String string;
    public Object tag;

    public ComboList(String stringPart, Object tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString() {
        return string;
    }
}