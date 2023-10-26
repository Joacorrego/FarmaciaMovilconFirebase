package com.example.farmaciamovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ActividadPrincipal extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button botonIniciarSesion;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadprincipal);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        botonIniciarSesion = findViewById(R.id.iniciarsesion);

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(ActividadPrincipal.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    iniciarSesion(email, password);
                }
            }
        });

        TextView textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        TextView textViewRecuperarContrasena = findViewById(R.id.textViewRecuperarContrasena);
        textViewRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPasswordReset();
            }
        });

        TextView textViewAdmin = findViewById(R.id.textViewAdmin);
        textViewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAdmin();
            }
        });
    }

    private void iniciarSesion(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ActividadPrincipal.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            goToInicio();
                        } else {

                            Toast.makeText(ActividadPrincipal.this, "Error en el inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToRegister() {
        Intent intent = new Intent(this, ActividadRegistro.class);
        startActivity(intent);
    }

    public void goToPasswordReset() {
        Intent intent = new Intent(this, ActividadRestablecerContrasena.class);
        startActivity(intent);
    }

    public void goToAdmin() {
        Intent intent = new Intent(this, ActividadAdministrador.class);
        startActivity(intent);
    }

    public void goToInicio() {
        Intent intent = new Intent(this, ActividadInicio.class);
        startActivity(intent);
    }
}
