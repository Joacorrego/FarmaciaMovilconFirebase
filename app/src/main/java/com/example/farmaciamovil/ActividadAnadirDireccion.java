package com.example.farmaciamovil;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.farmaciamovil.ActividadMisDirecciones;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ActividadAnadirDireccion extends AppCompatActivity {

    private EditText nombreCalleEditText;
    private EditText numeroCalleEditText;
    private EditText regionEditText;
    private EditText ciudadEditText;
    private Button buttonAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadanadirdirrecion);

        nombreCalleEditText = findViewById(R.id.nombreCalleEditText);
        numeroCalleEditText = findViewById(R.id.numeroCalleEditText);
        regionEditText = findViewById(R.id.regionEditText);
        ciudadEditText = findViewById(R.id.ciudadEditText);

        buttonAddAddress = findViewById(R.id.buttonAddAddress);
        buttonAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarDireccion();
            }
        });
    }

    private void agregarDireccion() {
        String nombreCalle = nombreCalleEditText.getText().toString();
        String numeroCalle = numeroCalleEditText.getText().toString();
        String region = regionEditText.getText().toString();
        String ciudad = ciudadEditText.getText().toString();


        Direccion direccion = new Direccion(nombreCalle, numeroCalle, region, ciudad);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("direcciones")
                .add(direccion)
                .addOnSuccessListener(documentReference -> {


                    Intent intent = new Intent(this, ActividadMisDirecciones.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {

                });
    }
}
