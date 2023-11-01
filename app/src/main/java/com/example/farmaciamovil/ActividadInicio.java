package com.example.farmaciamovil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmaciamovil.Producto;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ActividadInicio extends AppCompatActivity {

    private EditText editTextBuscar;
    private RecyclerView recyclerViewResultados;
    private ProductoAdapter productoAdapter;
    private List<Producto> resultadosBusqueda;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadinicio);

        resultadosBusqueda = new ArrayList<>();

        editTextBuscar = findViewById(R.id.editTextBuscar);
        recyclerViewResultados = findViewById(R.id.recyclerViewResultados);

        recyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));
        productoAdapter = new ProductoAdapter(resultadosBusqueda, this);
        recyclerViewResultados.setAdapter(productoAdapter);

        db = FirebaseFirestore.getInstance();

        editTextBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textoBusqueda = charSequence.toString();
                buscarResultados(textoBusqueda);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ImageView informacionImageView = findViewById(R.id.informacion);
        informacionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActividadInicio.this, ActividadInformacionPersonal.class);
                startActivity(intent);
            }
        });

        ImageView ubicacionImageView = findViewById(R.id.ubicacion);
        ubicacionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActividadInicio.this, ActividadMisDirecciones.class);
                startActivity(intent);
            }
        });

        TextView textViewVolver = findViewById(R.id.textViewVolver);
        textViewVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActividadInicio.this, ActividadPrincipal.class);


                startActivity(intent);
            }
        });
    }

    private void buscarResultados(String textoBusqueda) {
        resultadosBusqueda.clear();

        if (textoBusqueda.isEmpty()) {
            productoAdapter.notifyDataSetChanged();
            return;
        }

        db.collection("productos")
                .whereEqualTo("nombre", textoBusqueda)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (com.google.firebase.firestore.DocumentSnapshot document : task.getResult().getDocuments()) {
                            Producto producto = document.toObject(Producto.class);
                            resultadosBusqueda.add(producto);
                        }
                        productoAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {

                });
    }
}
