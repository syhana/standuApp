package com.example.standu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


public class Daftar extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    private EditText nama_lengkapDaftar, usernameDaftar, emailDaftar, passwordDaftar;
    private Button button_daftar;
    private TextView redirect_masuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        auth = FirebaseAuth.getInstance();
        nama_lengkapDaftar = findViewById(R.id.nama_lengkap_daftar);
        usernameDaftar = findViewById(R.id.username_daftar);
        emailDaftar = findViewById(R.id.email_daftar);
        passwordDaftar = findViewById(R.id.password_daftar);
        button_daftar = findViewById(R.id.button_daftar);
        redirect_masuk = findViewById(R.id.link_masuk);

        button_daftar.setOnClickListener(view -> {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("users");

            String nama = nama_lengkapDaftar.getText().toString().trim();
            String user = usernameDaftar.getText().toString().trim();
            String mail = emailDaftar.getText().toString().trim();
            String pass = passwordDaftar.getText().toString().trim();

            if(nama.isEmpty()){
                nama_lengkapDaftar.setError("Nama Lengkap tidak boleh kosong");
            }
            if(user.isEmpty()){
                usernameDaftar.setError("Username tidak boleh kosong");
            }
            if(mail.isEmpty()){
                emailDaftar.setError("Email tidak boleh kosong");
            }
            if(pass.isEmpty()){
                passwordDaftar.setError("Password tidak boleh kosong");
            }
            if(pass.length() < 6){
                passwordDaftar.setError("Password harus 6 karakter atau lebih");
            }else {
                auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid = auth.getCurrentUser().getUid();
                            User_data userData = new User_data(nama, user, mail, pass);
                            reference.child(uid).setValue(userData);
                            Toast.makeText(Daftar.this, "Pendaftaran Akun Berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Daftar.this, Masuk.class));
                        }else{
                            Toast.makeText(Daftar.this, "Pendaftaran Akun Gagal\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        redirect_masuk.setOnClickListener(view ->
                startActivity(new Intent(Daftar.this, Masuk.class)));
    }
}