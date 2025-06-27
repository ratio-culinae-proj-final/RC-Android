package com.example.ratioculinae.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ratioculinae.database.dao.CompraDAO;
import com.example.ratioculinae.database.dao.IngredienteDAO;
import com.example.ratioculinae.database.dao.ReceitaFavoritaDao;
import com.example.ratioculinae.database.dao.UsuarioDAO;
import com.example.ratioculinae.database.dao.UsuarioIngredienteDao;
import com.example.ratioculinae.database.relations.UsuarioIngredienteRef;
import com.example.ratioculinae.models.Compra;
import com.example.ratioculinae.models.Ingrediente;
import com.example.ratioculinae.models.ReceitaFavorita;
import com.example.ratioculinae.models.Usuario;

@Database(entities = {Usuario.class, Ingrediente.class, UsuarioIngredienteRef.class, ReceitaFavorita.class, Compra.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract UsuarioDAO usuarioDAO();
    public abstract IngredienteDAO ingredientesDAO();
    public abstract UsuarioIngredienteDao usuarioIngredienteDao();
    public abstract ReceitaFavoritaDao receitaFavoritaDao();
    public abstract CompraDAO compraDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ratio_culinae.db").build();
        }
        return instance;
    }
}
