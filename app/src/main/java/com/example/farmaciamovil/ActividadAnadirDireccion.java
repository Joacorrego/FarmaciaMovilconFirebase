package com.example.farmaciamovil;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ActividadAnadirDireccion extends AppCompatActivity {

    private EditText nombreCalleEditText;
    private EditText numeroCalleEditText;
    private EditText regionEditText;
    private EditText ciudadEditText;
    private Button buttonAddAddress;
    private TextView mensajeTextView;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadanadirdirrecion);

        storageReference = FirebaseStorage.getInstance().getReference();

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


        JSONObject direccionJson = new JSONObject();
        try {
            direccionJson.put("nombreCalle", nombreCalle);
            direccionJson.put("numeroCalle", numeroCalle);
            direccionJson.put("region", region);
            direccionJson.put("ciudad", ciudad);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String jsonStr = direccionJson.toString();


        InputStream inputStream = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));


        String nombreArchivo = nombreCalle + ".json";


        StorageReference direccionRef = storageReference.child("direcciones/" + nombreArchivo);
        direccionRef.putStream(inputStream)
                .addOnSuccessListener(taskSnapshot -> {

                    mensajeTextView.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {

                });
    }


    public void volverAActividadMisDirecciones(View view) {
        Intent intent = new Intent(this, ActividadMisDirecciones.class);
        startActivity(intent);
    }
}
