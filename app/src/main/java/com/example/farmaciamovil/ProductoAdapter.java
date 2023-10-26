package com.example.farmaciamovil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private Context context;

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultado, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        
        Glide.with(context)
                .load(producto.getImagenUrl())
                .into(holder.imagenImageView);

        holder.nombreTextView.setText(producto.getNombre());
        holder.valorTextView.setText("Valor: $" + producto.getValor());
        holder.cantidadTextView.setText("Cantidad: " + producto.getCantidadUnidades());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActividadProductoDetalle.class);
                intent.putExtra("nombre", producto.getNombre());
                intent.putExtra("valor", producto.getValor());
                intent.putExtra("cantidadUnidades", producto.getCantidadUnidades());
                intent.putExtra("descripcion", producto.getDescripcion());
                intent.putExtra("imagenUrl", producto.getImagenUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagenImageView;
        public TextView nombreTextView;
        public TextView valorTextView;
        public TextView cantidadTextView;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenImageView = itemView.findViewById(R.id.imagenResultado);
            nombreTextView = itemView.findViewById(R.id.nombreResultado);
            valorTextView = itemView.findViewById(R.id.valorResultado);
            cantidadTextView = itemView.findViewById(R.id.cantidadResultado);
        }
    }
}
