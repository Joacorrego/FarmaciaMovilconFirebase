package com.example.farmaciamovil;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmaciamovil.Producto;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ActividadInicio extends AppCompatActivity {

    private EditText editTextBuscar;
    private RecyclerView recyclerViewResultados;
    private ProductoAdapter productoAdapter;
    private List<Producto> resultadosBusqueda;
    private FirebaseStorage storage;

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

        storage = FirebaseStorage.getInstance();

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
    }

    private void buscarResultados(String textoBusqueda) {
        resultadosBusqueda.clear();

        if (textoBusqueda.isEmpty()) {
            productoAdapter.notifyDataSetChanged();
            return;
        }

        StorageReference storageRef = storage.getReference().child("productos");


        StorageReference archivoJSON = storageRef.child(textoBusqueda + ".json");

        archivoJSON.getBytes(1024 * 1024)
                .addOnSuccessListener(bytes -> {
                    try {

                        JSONObject jsonObject = new JSONObject(new String(bytes));
                        String nombre = jsonObject.getString("nombre");
                        String imagenUrl = jsonObject.getString("imagenUrl");
                        double valor = jsonObject.getDouble("valor");
                        int cantidadUnidades = jsonObject.getInt("cantidadUnidades");
                        String descripcion = jsonObject.getString("descripcion"); // Agregar la descripción


                        Producto producto = new Producto(nombre, imagenUrl, valor, cantidadUnidades, descripcion);
                        resultadosBusqueda.add(producto);
                        productoAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                })
                .addOnFailureListener(e -> {

                });
    }
}
