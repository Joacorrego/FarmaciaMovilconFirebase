package com.example.farmaciamovil;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.Button;


public class ActividadSeleccionarProducto extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductoAdaptador2 productoAdaptador;
    private List<DocumentSnapshot> listaProductos;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadseleccionarproducto);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        listaProductos = new ArrayList<>();
        productoAdaptador = new ProductoAdaptador2(listaProductos, this);

        productRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        productRecyclerView.setAdapter(productoAdaptador);

        int espaciadoVerticalEnPixeles = getResources().getDimensionPixelSize(R.dimen.espaciado_vertical);
        productRecyclerView.addItemDecoration(new EspaciadoArticulo(espaciadoVerticalEnPixeles));

        firestore = FirebaseFirestore.getInstance();
        cargarProductosDesdeFirestore();

        Button volverButton = findViewById(R.id.volver);
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadSeleccionarProducto.this, ActividadInicioAdministrador.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void cargarProductosDesdeFirestore() {
        firestore.collection("productos")
                .orderBy("nombre")
                .limit(50)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        listaProductos.addAll(documents);
                        productoAdaptador.notifyDataSetChanged();
                    } else {

                    }
                });
    }
}
