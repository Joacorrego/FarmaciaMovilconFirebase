package com.example.farmaciamovil;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class EspaciadoArticulo extends RecyclerView.ItemDecoration {
    private int espaciadoVertical;

    public EspaciadoArticulo(int espaciadoVertical) {
        this.espaciadoVertical = espaciadoVertical;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = espaciadoVertical;
    }
}
