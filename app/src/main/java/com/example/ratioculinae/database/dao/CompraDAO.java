package com.example.ratioculinae.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ratioculinae.models.Compra;

import java.util.List;

@Dao
public interface CompraDAO {
    @Insert
    long insert(Compra compra);

    @Query("SELECT * FROM tab_compras")
    List<Compra> listarCompras();

    @Delete
    void delete(Compra compra);
}