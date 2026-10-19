package cl.ipvg.georegistro;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class ListadoActivity extends AppCompatActivity {

    private ListView lvIncidencias;
    private ArrayList<String> listaIncidencias;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        lvIncidencias = findViewById(R.id.lvIncidencias);
        listaIncidencias = new ArrayList<>();

        // Usamos un diseño de lista simple que ya viene en Android
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaIncidencias);
        lvIncidencias.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        obtenerIncidencias();
    }

    private void obtenerIncidencias() {
        db.collection("incidencias")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaIncidencias.clear();
                        // Recorremos los documentos que llegaron de Firebase
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String titulo = document.getString("titulo");
                            String categoria = document.getString("categoria");

                            // Añadimos cada incidencia al arreglo
                            listaIncidencias.add(titulo + " | Categoría: " + categoria);
                        }
                        // Le avisamos a la vista que los datos se actualizaron
                        adapter.notifyDataSetChanged();

                        if (listaIncidencias.isEmpty()) {
                            Toast.makeText(this, "Aún no hay incidencias registradas.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Manejo de errores de red sin cerrar la app (RF03)
                        Toast.makeText(this, "Error de red al obtener datos de Firestore.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}