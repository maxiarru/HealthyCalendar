package com.example.misturnos.models;
import com.google.gson.annotations.SerializedName;

public class Especialidad {
    @SerializedName("id")
    private Integer id;

    @SerializedName("category")
    private String category;

    @SerializedName("idSubcategory")
    private Integer idSubcategory;

    @SerializedName("subCategory")
    private String subCategory;

    public Especialidad(Integer id, String category, Integer idSubcategory, String subCategory){
        this.id = id;
        this.category = category;
        this.idSubcategory = idSubcategory;
        this.subCategory = subCategory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getIdSubcategory() {
        return idSubcategory;
    }

    public void setIdSubcategory(Integer idSubcategory) {
        this.idSubcategory = idSubcategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }


}
