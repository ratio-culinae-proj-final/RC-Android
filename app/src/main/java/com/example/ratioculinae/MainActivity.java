package com.example.ratioculinae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ratioculinae.screens.CadastroActivity;
import com.example.ratioculinae.screens.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button cadastrarButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        iniciarTelaPrincipal();
    }

    private void iniciarTelaPrincipal() {

        cadastrarButton = findViewById(R.id.btnCadastro);
        loginButton = findViewById(R.id.btnLogin);

        cadastrarButton.setOnClickListener(v -> {
            Intent goToRegisterPage = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(goToRegisterPage);
            finish();
        });

        loginButton.setOnClickListener(v -> {
            Intent goToLoginPage = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLoginPage);
            finish();
        });

    }
}