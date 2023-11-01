package com.example.farmaciamovil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ActividadInformacionPersonal extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private Uri imageUri;

    private EditText editTextNombreCompleto, editTextCorreoElectronico, editTextNumeroTelefono, editTextGenero;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadinformacionpersonal);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        editTextNombreCompleto = findViewById(R.id.editTextNombreCompleto);
        editTextCorreoElectronico = findViewById(R.id.editTextCorreoElectronico);
        editTextNumeroTelefono = findViewById(R.id.editTextNumeroTelefono);
        editTextGenero = findViewById(R.id.editTextGenero);
        imageView = findViewById(R.id.imageView3);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        Button buttonGuardarCambios = findViewById(R.id.buttonGuardarCambios);
        buttonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambiosEnBaseDeDatos();
            }
        });


        cargarDatosUsuarioActual();
    }


    private void guardarCambiosEnBaseDeDatos() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            String nombreCompleto = editTextNombreCompleto.getText().toString();
            String correoElectronico = editTextCorreoElectronico.getText().toString();
            String numeroTelefono = editTextNumeroTelefono.getText().toString();
            String genero = editTextGenero.getText().toString();


            if (imageUri != null) {
                StorageReference imageRef = storageReference.child("profile_images/" + userId);
                imageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {

                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                databaseReference.child(userId).child("profileImage").setValue(imageUrl);


                                guardarDatosUsuario(userId, nombreCompleto, correoElectronico, numeroTelefono, genero);
                            });
                        })
                        .addOnFailureListener(e -> {

                        });
            } else {
                guardarDatosUsuario(userId, nombreCompleto, correoElectronico, numeroTelefono, genero);
            }
        }
    }


    private void cargarDatosUsuarioActual() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            databaseReference.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot != null) {
                            String nombreCompleto = dataSnapshot.child("nombreCompleto").getValue(String.class);
                            String correoElectronico = dataSnapshot.child("correoElectronico").getValue(String.class);
                            String numeroTelefono = dataSnapshot.child("numeroTelefono").getValue(String.class);
                            String genero = dataSnapshot.child("genero").getValue(String.class);
                            String imageUrl = dataSnapshot.child("profileImage").getValue(String.class);

                            editTextNombreCompleto.setText(nombreCompleto);
                            editTextCorreoElectronico.setText(correoElectronico);
                            editTextNumeroTelefono.setText(numeroTelefono);
                            editTextGenero.setText(genero);

                            if (imageUrl != null) {

                                Glide.with(ActividadInformacionPersonal.this)
                                        .load(imageUrl)
                                        .into(imageView);
                            }
                        }
                    }
                }
            });
        }
    }


    private void guardarDatosUsuario(String userId, String nombreCompleto, String correoElectronico, String numeroTelefono, String genero) {
        databaseReference.child(userId).child("nombreCompleto").setValue(nombreCompleto);
        databaseReference.child(userId).child("correoElectronico").setValue(correoElectronico);
        databaseReference.child(userId).child("numeroTelefono").setValue(numeroTelefono);
        databaseReference.child(userId).child("genero").setValue(genero);

        Toast.makeText(ActividadInformacionPersonal.this, "Cambios guardados.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
