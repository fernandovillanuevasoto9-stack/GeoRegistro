package cl.ipvg.georegistro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIrRegistro = findViewById(R.id.btnIrRegistro);
        Button btnIrListado = findViewById(R.id.btnIrListado);
        Button btnIrMapa = findViewById(R.id.btnIrMapa);

        btnIrRegistro.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegistroActivity.class)));
        btnIrListado.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ListadoActivity.class)));
        btnIrMapa.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapaActivity.class)));
    }
}