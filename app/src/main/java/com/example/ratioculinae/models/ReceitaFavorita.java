package com.example.ratioculinae.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "receitas_favoritas")
public class ReceitaFavorita {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String jsonReceita;
    public ReceitaFavorita(String jsonReceita) {
        this.jsonReceita = jsonReceita;
    }
}
