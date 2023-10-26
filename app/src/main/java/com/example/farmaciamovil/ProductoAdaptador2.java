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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class ProductoAdaptador2 extends RecyclerView.Adapter<ProductoAdaptador2.ViewHolder> {

    private List<JSONObject> productList;
    private Context context;

    public ProductoAdaptador2(List<JSONObject> productList, Context context) {
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
        JSONObject producto = productList.get(position);

        try {
            holder.nombreProducto.setText(producto.getString("nombre"));
            holder.valorProducto.setText("Valor: $" + producto.getInt("valor"));
            holder.cantidadUnidades.setText("Cantidad de Unidades: " + producto.getInt("cantidadUnidades"));

            String imageUrl = producto.getString("imagenUrl");
            Glide.with(context).load(imageUrl).into(holder.imagenProducto);

            holder.imagenProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, ActividadModificarProducto.class);
                    intent.putExtra("datosJSON", producto.toString()); // Pasa los datos del producto
                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
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
