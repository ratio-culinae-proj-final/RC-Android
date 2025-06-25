package com.example.ratioculinae.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ratioculinae.models.Ingrediente;

import java.util.List;

@Dao
public interface IngredienteDAO {

    @Insert
    void insert(Ingrediente ingrediente);

    @Query("SELECT * FROM tab_ingredientes")
    List<Ingrediente> listarIngredientes();
}
