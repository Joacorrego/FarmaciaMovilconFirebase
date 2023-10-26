package com.example.farmaciamovil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ActividadAnadirProducto extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private Button subirFotoButton;
    private Button anadirProductoButton;
    private EditText editTextNombreProducto;
    private EditText editTextDescripcionProducto;
    private EditText editTextValorProducto;
    private EditText editTextCantidadUnidades;

    private Uri imagenUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadanadirproducto);

        subirFotoButton = findViewById(R.id.subir);
        anadirProductoButton = findViewById(R.id.anadir);
        editTextNombreProducto = findViewById(R.id.editTextNombreProducto);
        editTextDescripcionProducto = findViewById(R.id.editTextDescripcionProducto);
        editTextValorProducto = findViewById(R.id.editTextValorProducto);
        editTextCantidadUnidades = findViewById(R.id.editTextCantidadUnidades);

        storageReference = FirebaseStorage.getInstance().getReference();

        subirFotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

        anadirProductoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anadirProducto();
            }
        });


        TextView volverTextView = findViewById(R.id.textView27);
        volverTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadAnadirProducto.this, ActividadInicioAdministrador.class);
                startActivity(intent);
            }
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData();

        }
    }

    private void anadirProducto() {
        if (imagenUri != null) {

            String nombreProducto = editTextNombreProducto.getText().toString();

            if (nombreProducto.isEmpty()) {
                Toast.makeText(this, "Ingresa un nombre para el producto", Toast.LENGTH_SHORT).show();
            } else {

                String nombreArchivo = nombreProducto;

                StorageReference productoRef = storageReference.child("productos/" + nombreArchivo);

                productoRef.putFile(imagenUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            productoRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        try {

                                            JSONObject productoJson = new JSONObject();
                                            productoJson.put("nombre", nombreProducto);
                                            productoJson.put("descripcion", editTextDescripcionProducto.getText().toString());
                                            productoJson.put("valor", Double.parseDouble(editTextValorProducto.getText().toString()));
                                            productoJson.put("cantidadUnidades", Integer.parseInt(editTextCantidadUnidades.getText().toString()));
                                            productoJson.put("imagenUrl", uri.toString()); // Agregar el URL de la imagen


                                            String jsonStr = productoJson.toString();


                                            InputStream inputStream = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));


                                            StorageReference productoJsonRef = storageReference.child("productos/" + nombreArchivo + ".json");
                                            UploadTask uploadTask = productoJsonRef.putStream(inputStream);

                                            uploadTask.addOnSuccessListener(task -> {
                                                Toast.makeText(this, "Producto añadido con éxito", Toast.LENGTH_SHORT).show();
                                            }).addOnFailureListener(e -> {
                                                Toast.makeText(this, "Error al subir el producto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error al obtener el URL de la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        } else {
            Toast.makeText(this, "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
        }
    }
}
