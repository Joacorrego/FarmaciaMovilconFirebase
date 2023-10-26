package com.example.farmaciamovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ActividadInicioAdministrador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_inicioadministrador);


        TextView textViewSalir = findViewById(R.id.textViewSalir);
        textViewSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadInicioAdministrador.this, ActividadAdministrador.class);
                startActivity(intent);
            }
        });


        Button buttonAnadirProducto = findViewById(R.id.buttonAnadirProducto);
        buttonAnadirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadInicioAdministrador.this, ActividadAnadirProducto.class);
                startActivity(intent);
            }
        });


        Button buttonModificarProducto = findViewById(R.id.buttonModificarProducto);
        buttonModificarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadInicioAdministrador.this, ActividadSeleccionarProducto.class);
                startActivity(intent);
            }
        });
    }
}
