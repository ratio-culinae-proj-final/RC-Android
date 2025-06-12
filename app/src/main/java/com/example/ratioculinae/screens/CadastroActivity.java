package com.example.ratioculinae.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText nomeField;
    private EditText emailField;
    private EditText senhaField;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        inicializarTelaCadastro();
    }

    private void inicializarTelaCadastro() {
        nomeField = findViewById(R.id.etNome);
        emailField = findViewById(R.id.etEmail);
        senhaField = findViewById(R.id.etSenha);
        registerButton = findViewById(R.id.btnRegistrar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Continuar
            }
        });
    }
}