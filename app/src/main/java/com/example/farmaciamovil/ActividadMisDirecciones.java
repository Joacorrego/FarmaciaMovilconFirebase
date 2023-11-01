package com.example.farmaciamovil;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.farmaciamovil.ActividadAnadirDireccion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ActividadMisDirecciones extends AppCompatActivity {

    private TextView direccionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadmisdirecciones);

        direccionTextView = findViewById(R.id.direccionTextView);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("direcciones")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                        if (documents.isEmpty()) {
                            direccionTextView.setText("No se encontraron direcciones en Firestore.");
                        } else {
                            mostrarDirecciones(documents);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        direccionTextView.setText("Error al acceder a Firestore.");
                    }
                });

        Button botonAgregarDireccion = findViewById(R.id.agregardireccion);
        Button botonVolver = findViewById(R.id.volver);

        botonAgregarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActividadMisDirecciones.this, ActividadAnadirDireccion.class);
                startActivity(intent);
            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActividadMisDirecciones.this, ActividadInicio.class);
                startActivity(intent);
            }
        });
    }

    private void mostrarDirecciones(List<DocumentSnapshot> documents) {
        for (DocumentSnapshot doc : documents) {
            String nombreCalle = doc.getString("nombreCalle");
            String numeroCalle = doc.getString("numeroCalle");
            String region = doc.getString("region");
            String ciudad = doc.getString("ciudad");

            direccionTextView.append("Nombre de la calle: " + nombreCalle + "\n");
            direccionTextView.append("Número de la calle: " + numeroCalle + "\n");
            direccionTextView.append("Región: " + region + "\n");
            direccionTextView.append("Ciudad: " + ciudad + "\n\n");
        }
    }
}
