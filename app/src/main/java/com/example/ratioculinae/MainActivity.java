package com.example.ratioculinae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.screens.CadastroActivity;
import com.example.ratioculinae.screens.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        iniciarTelaPrincipal();
    }

    private void iniciarTelaPrincipal() {

        Button cadastrarButton = findViewById(R.id.btnCadastro);
        Button loginButton = findViewById(R.id.btnLogin);

        cadastrarButton.setOnClickListener(v -> {
            Intent goToRegisterPage = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(goToRegisterPage);
        });

        loginButton.setOnClickListener(v -> {
            Intent goToLoginPage = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLoginPage);
        });

    }
}