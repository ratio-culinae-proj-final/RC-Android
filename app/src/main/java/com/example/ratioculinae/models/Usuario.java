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

    public Usuario(@NonNull String uuid, String nome, String email) {
        this.uuid = uuid;
        this.nome = nome;
        this.email = email;
    }
}
