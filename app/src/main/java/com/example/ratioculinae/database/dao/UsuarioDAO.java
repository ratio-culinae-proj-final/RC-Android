package com.example.ratioculinae.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.ratioculinae.models.Usuario;

@Dao
public interface UsuarioDAO {

    @Insert
    void criarUsuario(Usuario usuario);
    @Query("SELECT * FROM tab_users WHERE email = :email LIMIT 1")
    Usuario buscarPorEmail(String email);

    @Query("SELECT * FROM tab_users WHERE uuid = :uuid LIMIT 1")
    Usuario getUsuarioByUuid(String uuid);

}
