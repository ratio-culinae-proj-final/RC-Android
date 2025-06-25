package com.example.ratioculinae.database.relations;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(
        tableName = "tab_usuario_ingredientes",
        primaryKeys = {"usuarioId", "ingredienteId"}
)
public class UsuarioIngredienteRef {

    @NonNull
    public String usuarioId;

    public int ingredienteId;
}
