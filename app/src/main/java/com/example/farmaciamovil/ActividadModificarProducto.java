package com.example.farmaciamovil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.json.JSONException;
import org.json.JSONObject;

public class ActividadModificarProducto extends AppCompatActivity {
    private JSONObject producto;
    private ImageView imagenProductoDetalle;
    private EditText nombreProductoDetalle;
    private EditText cantidadUnidadesDetalle;
    private EditText valorProductoDetalle;
    private EditText descripcionProductoDetalle;
    private Button botonGuardarCambios;
    private Button botonEliminarProducto; // El botón de eliminar
    private static final int SELECCIONAR_IMAGEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_modificarproducto);

        Intent intent = getIntent();
        if (intent != null) {
            String datosJSON = intent.getStringExtra("datosJSON");
            try {
                producto = new JSONObject(datosJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        imagenProductoDetalle = findViewById(R.id.imagenProductoDetalle);
        nombreProductoDetalle = findViewById(R.id.nombreProductoDetalle);
        cantidadUnidadesDetalle = findViewById(R.id.cantidadUnidadesDetalle);
        valorProductoDetalle = findViewById(R.id.valorProductoDetalle);
        descripcionProductoDetalle = findViewById(R.id.descripcionProductoDetalle);
        botonGuardarCambios = findViewById(R.id.botonGuardarCambios);
        botonEliminarProducto = findViewById(R.id.botonEliminarProducto); // El botón de eliminar

        try {
            nombreProductoDetalle.setText(producto.getString("nombre"));
            cantidadUnidadesDetalle.setText(String.valueOf(producto.getInt("cantidadUnidades")));
            valorProductoDetalle.setText(String.valueOf(producto.getInt("valor")));
            descripcionProductoDetalle.setText(producto.getString("descripcion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imagenProductoDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECCIONAR_IMAGEN);
            }
        });

        botonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String nuevoNombre = nombreProductoDetalle.getText().toString();
                    int cantidadUnidades = Integer.parseInt(cantidadUnidadesDetalle.getText().toString());
                    int valor = Integer.parseInt(valorProductoDetalle.getText().toString());
                    String descripcion = descripcionProductoDetalle.getText().toString();

                    String nombreArchivoActual = producto.optString("nombre") + ".json";
                    eliminarJSONDeFirebase(nombreArchivoActual);

                    producto.put("nombre", nuevoNombre);
                    producto.put("cantidadUnidades", cantidadUnidades);
                    producto.put("valor", valor);
                    producto.put("descripcion", descripcion);

                    String nombreArchivoNuevo = nuevoNombre + ".json";
                    guardarCambiosEnFirebase(nombreArchivoNuevo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        botonEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreArchivoActual = producto.optString("nombre") + ".json";
                eliminarJSONDeFirebase(nombreArchivoActual);
            }
        });
    }


    private void eliminarJSONDeFirebase(String nombreArchivo) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference referenciaAlmacenamiento = storage.getReference().child("productos");

        referenciaAlmacenamiento.child(nombreArchivo).delete()
                .addOnSuccessListener(aVoid -> {
                    volverAActividadSeleccionarProducto();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }


    private void volverAActividadSeleccionarProducto() {
        Intent intent = new Intent(ActividadModificarProducto.this, ActividadSeleccionarProducto.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }


    private void guardarCambiosEnFirebase(String nombreArchivo) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference referenciaAlmacenamiento = storage.getReference().child("productos/" + nombreArchivo);

        String nuevoJSON = producto.toString();
        referenciaAlmacenamiento.putBytes(nuevoJSON.getBytes())
                .addOnSuccessListener(taskSnapshot -> {
                    volverAActividadSeleccionarProducto();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECCIONAR_IMAGEN && resultCode == RESULT_OK && data != null) {
            Uri imagenUri = data.getData();
            producto.remove("imagenUrl");
            try {
                producto.put("imagenUrl", imagenUri.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            imagenProductoDetalle.setImageURI(imagenUri);
        }
    }
}
