package com.example.ratioculinae.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tab_users")
public class Usuario {

    @NonNull
    @PrimaryKey
    public String uuid;
    public String nome;
    public String email;

    private String preferenciasAlimentares;
    private Boolean usarPreferencias = false;

    public Usuario(@NonNull String uuid, String nome, String email) {
        this.uuid = uuid;
        this.nome = nome;
        this.email = email;
        this.usarPreferencias = false;
    }

    public String getPreferenciasAlimentares() {
        return preferenciasAlimentares;
    }

    public void setPreferenciasAlimentares(String preferenciasAlimentares) {
        this.preferenciasAlimentares = preferenciasAlimentares;
    }

    public Boolean getUsarPreferencias() {
        return usarPreferencias != null ? usarPreferencias : false;
    }

    public void setUsarPreferencias(Boolean usarPreferencias) {
        this.usarPreferencias = usarPreferencias;
    }
}
