package com.example.farmaciamovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ActividadAdministrador extends AppCompatActivity {


    private static final String USUARIO_REQUERIDO = "admin";
    private static final String CONTRASENA_REQUERIDA = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_administrador);

        CardView loginCardViewAdmin = findViewById(R.id.loginCardViewAdmin);
        final EditText editTextEmailAdmin = findViewById(R.id.editTextEmailAdmin);
        final EditText editTextPasswordAdmin = findViewById(R.id.editTextPasswordAdmin);
        Button buttonLoginAdmin = findViewById(R.id.buttonLoginAdmin);


        buttonLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usuario = editTextEmailAdmin.getText().toString().trim();
                String contrasena = editTextPasswordAdmin.getText().toString();


                if (usuario.equals(USUARIO_REQUERIDO) && contrasena.equals(CONTRASENA_REQUERIDA)) {

                    goToInicioAdministrador();
                } else {

                    mostrarErrorDeCredenciales();
                }
            }
        });


        TextView textViewVolver = findViewById(R.id.textViewVolver);
        textViewVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverAActividadPrincipal();
            }
        });
    }

    private void mostrarErrorDeCredenciales() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error de inicio de sesión");
        builder.setMessage("Usuario o contraseña incorrectos. Inténtelo de nuevo.");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToInicioAdministrador() {
        Intent intent = new Intent(this, ActividadInicioAdministrador.class);
        startActivity(intent);
    }

    private void volverAActividadPrincipal() {
        Intent intent = new Intent(this, ActividadPrincipal.class);
        startActivity(intent);
    }
}
