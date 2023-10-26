package com.example.farmaciamovil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ActividadEnviodeProducto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadenviodeproducto);


        Button buttonEnviar = findViewById(R.id.button5);


        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActividadEnviodeProducto.this, ActividadInicio.class);
                startActivity(intent);
            }
        });
    }
}
