package com.example.ratioculinae.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ratioculinae.models.ReceitaFavorita;

import java.util.List;

@Dao
public interface ReceitaFavoritaDao {

    @Insert
    void inserir(ReceitaFavorita receita);

    @Query("SELECT * FROM receitas_favoritas")
    List<ReceitaFavorita> listar();

    @Delete
    void deletar(ReceitaFavorita receita);
}
