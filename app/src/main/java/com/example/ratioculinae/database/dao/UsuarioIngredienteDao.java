package com.example.ratioculinae.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.ratioculinae.database.relations.UsuarioIngredienteRef;
import com.example.ratioculinae.models.Ingrediente;

import java.util.List;

@Dao
public interface UsuarioIngredienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UsuarioIngredienteRef ref);

    @Query("SELECT i.* FROM tab_ingredientes i " +
            "INNER JOIN tab_usuario_ingredientes ui ON i.id = ui.ingredienteId " +
            "WHERE ui.usuarioId = :usuarioId")
    List<Ingrediente> getIngredientesByUsuario(String usuarioId);
}
