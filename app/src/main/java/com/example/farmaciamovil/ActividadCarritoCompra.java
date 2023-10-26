package com.example.farmaciamovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ActividadCarritoCompra extends AppCompatActivity {

    private TextView textViewNombreProducto;
    private TextView textViewCantidad;
    private TextView textViewPrecio;
    private ImageView imageViewProducto;
    private Spinner spinnerOpciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadcarritocompra);

        textViewNombreProducto = findViewById(R.id.nombreProductoDetalle);
        textViewCantidad = findViewById(R.id.cantidadUnidadesDetalle);
        textViewPrecio = findViewById(R.id.valorProductoDetalle);
        imageViewProducto = findViewById(R.id.imagenProductoDetalle);
        spinnerOpciones = findViewById(R.id.spinnerOpciones);

        Intent intent = getIntent();
        if (intent != null) {
            String nombreProducto = intent.getStringExtra("nombre_producto");
            String cantidad = intent.getStringExtra("cantidad");
            String precio = intent.getStringExtra("precio");
            String imagenUrl = intent.getStringExtra("imagenUrl"); // Obtener la URL de la imagen

            textViewNombreProducto.setText(nombreProducto);
            textViewCantidad.setText(cantidad);
            textViewPrecio.setText(precio);


            Glide.with(this)
                    .load(imagenUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)) // Para evitar guardar en caché
                    .into(imageViewProducto);
        }


        cargarDatosParaSpinner();

        Button buttonVolver = findViewById(R.id.button4);
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadCarritoCompra.this, ActividadInicio.class);
                startActivity(intent);
            }
        });

        Button buttonFinalizarCompra = findViewById(R.id.button3);
        buttonFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadCarritoCompra.this, ActividadEnviodeProducto.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String nombreProducto = data.getStringExtra("nombre_producto");
            String cantidad = data.getStringExtra("cantidad");
            String precio = data.getStringExtra("precio");

            textViewNombreProducto.setText(nombreProducto);
            textViewCantidad.setText(cantidad);
            textViewPrecio.setText(precio);
        }
    }


    private void cargarDatosParaSpinner() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        StorageReference direccionesRef = storageRef.child("direcciones");


        final List<String> nombresCalles = new ArrayList<>();


        direccionesRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        item.getBytes(10240)
                                .addOnSuccessListener(bytes -> {
                                    try {

                                        String jsonStr = new String(bytes, "UTF-8");


                                        JSONObject jsonObject = new JSONObject(jsonStr);


                                        String nombreCalle = jsonObject.getString("nombreCalle");
                                        nombresCalles.add(nombreCalle);


                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                                this, android.R.layout.simple_spinner_item, nombresCalles);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                                        spinnerOpciones.setAdapter(adapter);

                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {

                    e.printStackTrace();
                });
    }
}
