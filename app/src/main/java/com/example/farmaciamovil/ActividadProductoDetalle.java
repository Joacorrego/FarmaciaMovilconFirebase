package com.example.farmaciamovil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ActividadProductoDetalle extends AppCompatActivity {
    private TextView nombreProducto;
    private TextView descripcionProducto;
    private TextView valorProducto;
    private TextView cantidadUnidades;
    private ImageView imagenProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);

        nombreProducto = findViewById(R.id.nombreProductoDetalle);
        descripcionProducto = findViewById(R.id.descripcionProductoDetalle);
        valorProducto = findViewById(R.id.valorProductoDetalle);
        cantidadUnidades = findViewById(R.id.cantidadUnidadesDetalle);
        imagenProducto = findViewById(R.id.imagenProductoDetalle);

        Button botonVolver = findViewById(R.id.volver);

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadProductoDetalle.this, ActividadInicio.class);
                startActivity(intent);
            }
        });

        Button botonComprar = findViewById(R.id.comprar);
        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadProductoDetalle.this, ActividadCarritoCompra.class);


                String nombre = nombreProducto.getText().toString();
                String cantidad = cantidadUnidades.getText().toString();
                String precio = valorProducto.getText().toString();
                String imagenUrl = getIntent().getStringExtra("imagenUrl");


                intent.putExtra("nombre_producto", nombre);
                intent.putExtra("cantidad", cantidad);
                intent.putExtra("precio", precio);
                intent.putExtra("imagenUrl", imagenUrl);


                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String nombre = intent.getStringExtra("nombre");
            String descripcion = intent.getStringExtra("descripcion");
            double valor = intent.getDoubleExtra("valor", 0.0);
            int cantidad = intent.getIntExtra("cantidadUnidades", 0);
            String imagenUrl = intent.getStringExtra("imagenUrl");

            nombreProducto.setText(nombre);
            descripcionProducto.setText(descripcion);
            valorProducto.setText("Valor: $" + valor);
            cantidadUnidades.setText("Cantidad: " + cantidad);


            Glide.with(this)
                    .load(imagenUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(imagenProducto);
        }
    }
}
