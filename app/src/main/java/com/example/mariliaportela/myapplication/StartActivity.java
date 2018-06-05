package com.example.mariliaportela.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mariliaportela.myapplication.commons.CommonInfo;
import com.example.mariliaportela.myapplication.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;

public class StartActivity extends AppCompatActivity {

    private Button btnPlay;
    private ImageView imageView;

    private FirebaseDatabase database;
    private DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        loadQuestion(CommonInfo.categoryId);

        imageView = (ImageView) findViewById(R.id.img_categoria_escolhida);
        final String caminhoImagem = getIntent().getStringExtra("IMAGEM");
        Picasso.with(StartActivity.this).load(caminhoImagem).into(imageView);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, PlayingActivity.class);
                intent.putExtra("IMAGEM", caminhoImagem);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestion(String categoryId) {

        //Se lista de perguntas já está preenchida, limpa para colocar as novas perguntas
        if(CommonInfo.questionList.size() > 0){
            CommonInfo.questionList.clear();
        }

        questions.orderByChild("categoryId").equalTo(categoryId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                Question question = postSnapshot.getValue(Question.class);
                                CommonInfo.questionList.add((question));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        //Randomização da lista
        Collections.shuffle(CommonInfo.questionList);
    }
}
