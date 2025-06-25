package com.example.ratioculinae.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tab_ingredientes")
public class Ingrediente {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "quantidade")
    private int quantidade;

    public Ingrediente() {}
    public Ingrediente(String getNomeTela, int getQuantidadeTela) {
        this.nome = getNomeTela;
        this.quantidade = getQuantidadeTela;
    }

    public int getId() {return id;};
    public void setId(int id) {this.id = id;};

    public int getQuantidade() {return quantidade;};
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;};

    public String getNome() {return nome;};
    public void  setNome(String nome) {this.nome = nome;};

    @Override
    public String toString() {
        return nome;
    }
}
