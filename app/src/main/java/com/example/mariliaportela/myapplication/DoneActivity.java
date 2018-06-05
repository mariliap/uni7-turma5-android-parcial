package com.example.mariliaportela.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DoneActivity extends AppCompatActivity {


    private  TextView resultadoView;
    private  TextView resultadoDetalhesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        Integer score = getIntent().getIntExtra("PONTUACAO",10);
        Integer totalQuestion = getIntent().getIntExtra("TOTAL",10);
        Integer correctAnswer =  getIntent().getIntExtra("CORRETO", 10);

        resultadoView = (TextView) findViewById(R.id.resultado);
        resultadoView.setText("Sua pontuação é " + score);

        resultadoDetalhesView = (TextView) findViewById(R.id.resultado_detalhes);
        resultadoDetalhesView.setText("Você acertou " + correctAnswer +" de " +totalQuestion);
    }
}
