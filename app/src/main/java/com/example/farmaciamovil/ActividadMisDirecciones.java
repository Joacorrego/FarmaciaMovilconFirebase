package com.example.farmaciamovil;
import org.json.JSONObject;
import org.json.JSONException;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ActividadMisDirecciones extends AppCompatActivity {

    private TextView direccionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadmisdirecciones);

        direccionTextView = findViewById(R.id.direccionTextView);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("direcciones");

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> files = listResult.getItems();
                        if (files.isEmpty()) {
                            direccionTextView.setText("No se encontraron archivos JSON en la carpeta 'direcciones'.");
                        } else {
                            mostrarContenidoArchivos(files);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        direccionTextView.setText("Error al acceder a Firebase Storage.");
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

    private void mostrarContenidoArchivos(List<StorageReference> files) {
        List<String> contenidoArchivos = new ArrayList<>();
        int numArchivos = files.size();

        for (StorageReference file : files) {
            file.getBytes(1024 * 1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            String jsonString = new String(bytes);


                            contenidoArchivos.add(jsonString);


                            if (contenidoArchivos.size() == numArchivos) {
                                mostrarContenido(contenidoArchivos);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
        }
    }

    private void mostrarContenido(List<String> contenidoArchivos) {
        for (String jsonString : contenidoArchivos) {
            try {

                JSONObject jsonObject = new JSONObject(jsonString);


                direccionTextView.append("Nombre de la calle: " + jsonObject.getString("nombreCalle") + "\n");
                direccionTextView.append("Número de la calle: " + jsonObject.getString("numeroCalle") + "\n");
                direccionTextView.append("Región: " + jsonObject.getString("region") + "\n");
                direccionTextView.append("Ciudad: " + jsonObject.getString("ciudad") + "\n\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
