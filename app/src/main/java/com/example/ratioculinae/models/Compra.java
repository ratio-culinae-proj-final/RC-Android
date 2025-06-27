package com.example.ratioculinae.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tab_compras")
public class Compra {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "produto")
    private String produto;

    @ColumnInfo(name = "quantidade")
    private int quantidade;

    public Compra() {}
    public Compra(String setProduto, int setQuantidade) {
        this.produto = setProduto;
        this.quantidade = setQuantidade;
    }

    public int getId() {return id;};
    public void setId(int id) {this.id = id;};

    public int getQuantidade() {return quantidade;};
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;};

    public String getProduto() {return produto;};
    public void  setProduto(String produto) {this.produto = produto;};

    public String getProdutoQuantidade() {return produto + " - " + quantidade;};
}