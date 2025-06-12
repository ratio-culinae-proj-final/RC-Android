package com.example.ratioculinae.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "tab_users")
public class Usuario {

    @NonNull
    @PrimaryKey
    public String uuid;
    public String nome;
    public String email;
    public String senha;

    public Usuario(@NonNull String uuid, String nome, String email, String senha) {
        this.uuid = uuid;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
