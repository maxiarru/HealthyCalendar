package com.example.misturnos.models;
import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName("id")
    private Integer id;

    @SerializedName("email")
    private String email;

    @SerializedName("status")
    private String status;

    @SerializedName("roles")
    private Rol[] roles;

    public Usuario(Integer id, String email, String status, Rol[] roles){
        this.id = id;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }

    public Rol[] getRoles() {
        return roles;
    }


    public void setRoles(Rol[] roles) {
        this.roles = roles;
    }



    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMedico() {
        for (int i=0; i<roles.length; i++){
            if (roles[i].getName().equals("professional")) {
                return true;
            }
        }
        return false;
    };
}
