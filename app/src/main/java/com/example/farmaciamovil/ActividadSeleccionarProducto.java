package com.example.farmaciamovil;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ActividadSeleccionarProducto extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductoAdaptador2 productoAdaptador;
    private List<JSONObject> listaProductos;

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


        enumerarYDescargarArchivosJSON();
    }

    private void enumerarYDescargarArchivosJSON() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference referenciaAlmacenamiento = storage.getReference().child("productos");

        referenciaAlmacenamiento.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {

                descargarJSONDesdeFirebaseStorage(item);
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }

    private void descargarJSONDesdeFirebaseStorage(StorageReference referenciaAlmacenamiento) {
        referenciaAlmacenamiento.getBytes(1024 * 1024).addOnSuccessListener(bytes -> {

            String jsonString = new String(bytes);


            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                listaProductos.add(jsonObject);
                productoAdaptador.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }
}
