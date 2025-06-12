package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText senhaField;
    private Button loginButton;
    private Button irParaCadastroButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        inicializarTelaLogin();
    }

    private void inicializarTelaLogin() {
        emailField = findViewById(R.id.emailLogin);
        senhaField = findViewById(R.id.senhaLogin);
        loginButton = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> realizarLogin());

    }

    private void realizarLogin() {
        String email = emailField.getText().toString().trim();
        String senha = senhaField.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                        Intent goToHomePage = new Intent(LoginActivity.this, HomePageActivity.class);
                        startActivity(goToHomePage);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Falha no login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
