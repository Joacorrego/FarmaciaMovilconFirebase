package com.example.farmaciamovil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ActividadRestablecerContrasena extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadrestablecercontrasena);


        Button buttonEnviarCodigo = findViewById(R.id.buttonEnviarCodigo);
        buttonEnviarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToMainActivity();
            }
        });
    }


    public void goToMainActivity() {

        Intent intent = new Intent(this, ActividadPrincipal.class);
        startActivity(intent);
    }
}
