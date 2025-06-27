package com.example.ratioculinae.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.database.AppDatabase;
import com.example.ratioculinae.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText senhaField;
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
        Button loginButton = findViewById(R.id.login);
        TextView btnCadastrar = findViewById(R.id.btnCadastrar);


        firebaseAuth = FirebaseAuth.getInstance();

        btnCadastrar.setOnClickListener(v -> {
            Intent goCadastrar = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(goCadastrar);
        });

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

                        if (user != null) {
                            String userEmail = user.getEmail();
                            buscarUsuarioNoRoomESalvarUUID(userEmail);
                        }
                    } else {
                        Toast.makeText(this, "Falha no login: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void buscarUsuarioNoRoomESalvarUUID(String email) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Usuario usuario = db.usuarioDAO().buscarPorEmail(email);

            if (usuario != null) {
                SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                prefs.edit().putString("uuid", usuario.uuid).apply();

                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                    finish();
                });
            } else {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Usuário não encontrado no banco local!", Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
