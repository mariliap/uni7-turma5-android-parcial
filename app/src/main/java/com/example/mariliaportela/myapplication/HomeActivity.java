package com.example.mariliaportela.myapplication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragmentoSelecionado =  null;
                switch (item.getItemId()) {
                    case R.id.action_category:
                        fragmentoSelecionado = CategoryFragment.newInstance();
                        break;
                    case R.id.action_ranking:
                        fragmentoSelecionado = RankingFragment.newInstance();
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragmentoSelecionado);
                transaction.commit();
                return true;

            }
        });

        setDefaultFragmento();
    }

    private void setDefaultFragmento(){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
            transaction.commit();
    }
}
