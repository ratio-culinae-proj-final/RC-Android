package com.example.ratioculinae.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ratioculinae.R;
import com.example.ratioculinae.utils.TemporizadorManager;

public class TemporizadorActivity extends AppCompatActivity {

    private TextView tvTempo;
    private SeekBar seekBarTempo;
    private ImageButton btnExcluir, btnPlayPause, btnReiniciar, btnVoltar;

    private TemporizadorManager temporizadorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporizador);

        inicializarTela();
    }

    private void inicializarTela() {
        // Inicializa a UI
        tvTempo = findViewById(R.id.tvTempo);
        seekBarTempo = findViewById(R.id.seekBarTempo);
        btnExcluir = findViewById(R.id.btnExcluir);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Inicializa o TemporizadorManager
        temporizadorManager = new TemporizadorManager(tvTempo, seekBarTempo, btnPlayPause);

        // Configura o SeekBar
        temporizadorManager.configurarSeekBar();

        // Configura os botões
        configurarBotoes();
    }

    private void configurarBotoes() {
        btnPlayPause.setOnClickListener(view -> temporizadorManager.toggleTemporizador());
        btnExcluir.setOnClickListener(view -> temporizadorManager.pararTemporizador());
        btnReiniciar.setOnClickListener(view -> temporizadorManager.reiniciarTemporizador());
        btnVoltar.setOnClickListener(v -> {
            Intent goBack = new Intent(TemporizadorActivity.this, HomePageActivity.class);
            startActivity(goBack);
        });
    }
}
