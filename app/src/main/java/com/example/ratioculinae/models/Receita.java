package com.example.ratioculinae.models;

import org.json.JSONArray;

public class Receita {
    private final String nome;
    private final JSONArray ingredientes;
    private final String modoPreparo;
    private final String dificuldade;
    private final String tempoPreparo;
    private final String imagem;

    public Receita(String nome, JSONArray ingredientes, String modoPreparo, String dificuldade, String tempoPreparo, String imagem) {
        this.nome = nome;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
        this.dificuldade = dificuldade;
        this.tempoPreparo = tempoPreparo;
        this.imagem = imagem;
    }

    public String getNome() { return nome; }
    public JSONArray getIngredientes() { return ingredientes; }
    public String getModoPreparo() { return modoPreparo; }
    public String getDificuldade() { return dificuldade; }
    public String getTempoPreparo() { return tempoPreparo; }
    public String getImagem() { return imagem; }
}
