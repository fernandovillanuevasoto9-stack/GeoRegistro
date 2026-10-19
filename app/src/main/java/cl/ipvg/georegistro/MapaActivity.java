package cl.ipvg.georegistro;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        db = FirebaseFirestore.getInstance();

        // Iniciar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        cargarMarcadores();
    }

    private void cargarMarcadores() {
        db.collection("incidencias")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean hayMarcadores = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Double lat = document.getDouble("latitud");
                            Double lon = document.getDouble("longitud");
                            String titulo = document.getString("titulo");

                            if (lat != null && lon != null) {
                                LatLng ubicacion = new LatLng(lat, lon);
                                mMap.addMarker(new MarkerOptions().position(ubicacion).title(titulo));

                                // Mover la cámara a la primera incidencia que encuentre
                                if (!hayMarcadores) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 12f));
                                    hayMarcadores = true;
                                }
                            }
                        }
                        if (!hayMarcadores) {
                            Toast.makeText(this, "No hay incidencias con ubicación para mostrar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar ubicaciones desde Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}