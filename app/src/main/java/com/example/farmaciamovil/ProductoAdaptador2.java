package com.example.farmaciamovil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;

public class ProductoAdaptador2 extends RecyclerView.Adapter<ProductoAdaptador2.ViewHolder> {

    private List<DocumentSnapshot> productList;
    private Context context;

    public ProductoAdaptador2(List<DocumentSnapshot> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seleccionarproducto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DocumentSnapshot document = productList.get(position);
        if (document.exists()) {
            String nombre = document.getString("nombre");
            double valor = document.getDouble("valor");
            long cantidadUnidades = document.getLong("cantidadUnidades");
            String imagenUrl = document.getString("imagenUrl");

            holder.nombreProducto.setText(nombre);
            holder.valorProducto.setText("Valor: $" + valor);
            holder.cantidadUnidades.setText("Cantidad de Unidades: " + cantidadUnidades);

            Glide.with(context).load(imagenUrl).into(holder.imagenProducto);

            holder.imagenProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActividadModificarProducto.class);
                    intent.putExtra("productoId", document.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenProducto;
        TextView nombreProducto;
        TextView valorProducto;
        TextView cantidadUnidades;

        public ViewHolder(View itemView) {
            super(itemView);
            imagenProducto = itemView.findViewById(R.id.imagenProducto);
            nombreProducto = itemView.findViewById(R.id.nombreProducto);
            valorProducto = itemView.findViewById(R.id.valorProducto);
            cantidadUnidades = itemView.findViewById(R.id.cantidadUnidades);
        }
    }
}
