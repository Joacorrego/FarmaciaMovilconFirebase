package com.example.farmaciamovil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.graphics.BitmapFactory;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ActividadModificarProducto extends AppCompatActivity {
    private ImageView imagenProductoDetalle;
    private EditText nombreProductoDetalle;
    private EditText cantidadUnidadesDetalle;
    private EditText valorProductoDetalle;
    private EditText descripcionProductoDetalle;
    private Button botonGuardarCambios;
    private Button botonEliminarProducto;
    private static final int SELECCIONAR_IMAGEN = 1;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Uri imageUri;
    private DocumentReference productReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_modificarproducto);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            String productoId = intent.getStringExtra("productoId");
            productReference = db.collection("productos").document(productoId);
        }

        imagenProductoDetalle = findViewById(R.id.imagenProductoDetalle);
        nombreProductoDetalle = findViewById(R.id.nombreProductoDetalle);
        cantidadUnidadesDetalle = findViewById(R.id.cantidadUnidadesDetalle);
        valorProductoDetalle = findViewById(R.id.valorProductoDetalle);
        descripcionProductoDetalle = findViewById(R.id.descripcionProductoDetalle);
        botonGuardarCambios = findViewById(R.id.botonGuardarCambios);
        botonEliminarProducto = findViewById(R.id.botonEliminarProducto);

        productReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Producto producto = documentSnapshot.toObject(Producto.class);
                if (producto != null) {
                    nombreProductoDetalle.setText(producto.getNombre());
                    cantidadUnidadesDetalle.setText(String.valueOf(producto.getCantidadUnidades()));
                    valorProductoDetalle.setText(String.valueOf(producto.getValor()));
                    descripcionProductoDetalle.setText(producto.getDescripcion());
                }
            }
        });

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
                    long cantidadUnidades = Long.parseLong(cantidadUnidadesDetalle.getText().toString());
                    double valor = Double.parseDouble(valorProductoDetalle.getText().toString());
                    String descripcion = descripcionProductoDetalle.getText().toString();


                    productReference.update(
                            "nombre", nuevoNombre,
                            "cantidadUnidades", cantidadUnidades,
                            "valor", valor,
                            "descripcion", descripcion
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (imageUri != null) {

                                StorageReference storageRef = storage.getReference();
                                StorageReference imageRef = storageRef.child("fotosproducto/" + nuevoNombre);


                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                try {
                                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] imageData = baos.toByteArray();
                                    UploadTask uploadTask = imageRef.putBytes(imageData);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            e.printStackTrace();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String nuevaImagenUrl = uri.toString();


                                                    productReference.update("imagenUrl", nuevaImagenUrl);
                                                    volverAActividadSeleccionarProducto();
                                                }
                                            });
                                        }
                                    });
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                volverAActividadSeleccionarProducto();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        botonEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreProducto = nombreProductoDetalle.getText().toString();
                String imagenPath = "fotosproducto/" + nombreProducto;

                StorageReference storageRef = storage.getReference().child(imagenPath);
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        productReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                volverAActividadSeleccionarProducto();
                            }
                        });
                    }
                });
            }
        });
    }

    private void volverAActividadSeleccionarProducto() {
        Intent intent = new Intent(ActividadModificarProducto.this, ActividadSeleccionarProducto.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECCIONAR_IMAGEN && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                imagenProductoDetalle.setImageURI(imageUri);
            }
        }
    }
}
