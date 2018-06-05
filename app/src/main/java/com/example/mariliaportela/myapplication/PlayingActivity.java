package com.example.mariliaportela.myapplication;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mariliaportela.myapplication.commons.CommonInfo;
import com.example.mariliaportela.myapplication.model.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PlayingActivity extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000;// = 1s
    final static long TIMEOUT = 7000;// = 1s

    int progressValue = 0;

    CountDownTimer mCountDown;
    int index=0;
    int score=0;
    int thisQuestion=0;
    int totalQuestion=0;
    int correctAnswer=0;

    ProgressBar progressBar;
    CardView question_textCardView;
    CardView question_imageCardView;
    ImageView question_image;
    TextView question_text;

    Button btnAnswer1;
    Button btnAnswer2;
    Button btnAnswer3;
    Button btnAnswer4;
    TextView txtScore;
    TextView txtQuestionNum;

    private ImageView imageBackgroundView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        imageBackgroundView = (ImageView) findViewById(R.id.img_background_categoria);
        final String caminhoImagem = getIntent().getStringExtra("IMAGEM");
        Picasso.with(PlayingActivity.this).load(caminhoImagem).into(imageBackgroundView);

        txtScore = (TextView)findViewById(R.id.txtScore);
        txtQuestionNum = (TextView)findViewById(R.id.txtTotalQuestion);
        question_text = (TextView)findViewById(R.id.question_text);
        question_image = (ImageView) findViewById(R.id.question_image);

        question_textCardView = (CardView) findViewById(R.id.question_textCardView);
        question_imageCardView = (CardView) findViewById(R.id.question_imageCardView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnAnswer1 = (Button) findViewById(R.id.btnAnswer1);
        btnAnswer2 = (Button) findViewById(R.id.btnAnswer2);
        btnAnswer3 = (Button) findViewById(R.id.btnAnswer3);
        btnAnswer4 = (Button) findViewById(R.id.btnAnswer4);

        btnAnswer1.setOnClickListener(this);
        btnAnswer2.setOnClickListener(this);
        btnAnswer3.setOnClickListener(this);
        btnAnswer4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mCountDown.cancel();

        System.out.println("index:" + index + " totalQuestion:" +totalQuestion);

        if(index < totalQuestion){ //ainda há perguntas na lista


            Button botaoClicado = (Button) view;


            if(botaoClicado.getText().toString().trim().equals(CommonInfo.questionList.get(index).getCorrectAnswer())){
                //usuário escolheu resposta certa
                score++;
                correctAnswer++;
                index++;
                showQuestion(index);

            } else {
                Intent intent = new Intent(this, DoneActivity.class);
                Bundle dataToDonePage = new Bundle();
                dataToDonePage.putInt("PONTUACAO",score);
                dataToDonePage.putInt("TOTAL",totalQuestion);
                dataToDonePage.putInt("CORRETO",correctAnswer);
                intent.putExtras(dataToDonePage);
                startActivity(intent);
                finish();
            }
            txtScore.setText(String.format("%d",score));
        }
    }

    private void showQuestion(int index) {
        if(index < totalQuestion){
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d /%d", thisQuestion,totalQuestion));
            progressBar.setProgress(0);
            progressValue=0;

            Question question = CommonInfo.questionList.get(index);

            if(question.getIsImageQuestion().equals("true")){
                Picasso.with(getBaseContext()).load(question.getQuestion()).into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_imageCardView.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
                question_textCardView.setVisibility(View.INVISIBLE);


            } else {
                question_text.setText(question.getQuestion());
                question_image.setVisibility(View.INVISIBLE);
                question_imageCardView.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
                question_textCardView.setVisibility(View.VISIBLE);
            }

            btnAnswer1.setText(question.getAnswer1());
            btnAnswer2.setText(question.getAnswer2());
            btnAnswer3.setText(question.getAnswer3());
            btnAnswer4.setText(question.getAnswer4());

            mCountDown.start(); //Inicia o temporizador

        } else { // Se não há mais perguntas, vá para a DoneActivity

            Intent intent = new Intent(this, DoneActivity.class);
            Bundle dataToDonePage = new Bundle();
            dataToDonePage.putInt("PONTUACAO",score);
            dataToDonePage.putInt("TOTAL",totalQuestion);
            dataToDonePage.putInt("CORRETO",correctAnswer);
            intent.putExtras(dataToDonePage);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = CommonInfo.questionList.size();
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long miniSec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                 mCountDown.cancel();
                 index++;
                 showQuestion(index);
            }
        };

        showQuestion(index);
    }
}
