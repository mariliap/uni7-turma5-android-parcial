package com.example.mariliaportela.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mariliaportela.myapplication.commons.CommonInfo;
import com.example.mariliaportela.myapplication.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtNewUserName, edtNewPassword, edtNewEmail;
    MaterialEditText edtUser, edtPassword;

    Button btnSignUp, btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtUser = (MaterialEditText)findViewById(R.id.edtUser);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignIn = (Button)findViewById(R.id.btn_sign_in);
        btnSignUp = (Button)findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtUser.getText().toString(), edtPassword.getText().toString());
            }
        });
    }

    private void signIn(final String user, final String pwd) {

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists()){
                    if(!user.isEmpty() && !pwd.isEmpty()){
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if(login.getPassword().equals(pwd)){
                            Intent homeActivity = new Intent(MainActivity.this, HomeActivity.class);
                            //CommonInfo.user = user;
                            startActivity(homeActivity);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Senha errada.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this,
                                        "Por favor, digite seu usuário e senha.",
                                         Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MainActivity.this, "Usuário não existe.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Por favor, entrar com todas as informações");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View sign_up_layout = layoutInflater.inflate(R.layout.sign_up_layout, null);


        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_person_black_24dp);


        edtNewUserName = (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewPassword = (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewPassword);
        edtNewEmail = (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewUserName);

        alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = new User(
                        edtNewUserName.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUserName()).exists()){
                            Toast.makeText(MainActivity.this, "Usuário já existe.", Toast.LENGTH_SHORT).show();
                        } else {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "Registro de usuário com sucesso!", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
