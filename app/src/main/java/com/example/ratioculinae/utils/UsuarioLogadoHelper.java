package com.example.ratioculinae.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Usuario;

public class UsuarioLogadoHelper {

    public static Usuario getUsuarioLogado(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String uuid = prefs.getString("uuid", null);

        if (uuid != null) {
            AppDatabase db = AppDatabase.getInstance(context);
            return db.usuarioDAO().getUsuarioByUuid(uuid);
        }

        return null;
    }
}
