package cl.ipvg.georegistro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity implements SensorEventListener {

    private EditText etTitulo, etDescripcion, etCategoria, etLatitud, etLongitud;
    private Button btnGuardar;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private SensorManager sensorManager;
    private Sensor acelerometro;
    private long ultimoTiempoAct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = FirebaseFirestore.getInstance();

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCategoria = findViewById(R.id.etCategoria);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        btnGuardar = findViewById(R.id.btnGuardar);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        obtenerUbicacion();

        btnGuardar.setOnClickListener(v -> validarYGuardar());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        if (acelerometro == null) {
            Toast.makeText(this, "Aviso: Este dispositivo no cuenta con acelerómetro", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (acelerometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (acelerometro != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double aceleracion = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

            long tiempoActual = System.currentTimeMillis();
            if (aceleracion > 12 && (tiempoActual - ultimoTiempoAct > 2000)) {
                ultimoTiempoAct = tiempoActual;
                Toast.makeText(this, "¡Movimiento detectado! Puedes guardar la incidencia.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                etLatitud.setText(String.valueOf(location.getLatitude()));
                etLongitud.setText(String.valueOf(location.getLongitude()));
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. Se requiere para registrar incidencias.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void validarYGuardar() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();
        String latStr = etLatitud.getText().toString().trim();
        String lonStr = etLongitud.getText().toString().trim();

        if (titulo.isEmpty() || descripcion.isEmpty() || categoria.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
            Toast.makeText(this, "Completa los campos y espera la ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitud = Double.parseDouble(latStr);
        double longitud = Double.parseDouble(lonStr);
        long timestamp = System.currentTimeMillis();

        Incidencia nuevaIncidencia = new Incidencia(titulo, descripcion, categoria, latitud, longitud, timestamp);

        // Guardar en Firestore
        db.collection("incidencias")
            .add(nuevaIncidencia)
            .addOnSuccessListener(documentReference -> {
                Toast.makeText(this, "Incidencia guardada con éxito", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la pantalla y vuelve al menú principal
            })
            .addOnFailureListener(e -> {
                // Manejo de errores sin cerrar la aplicación (RF03)
                Toast.makeText(this, "Error al guardar. Revisa tu conexión a red.", Toast.LENGTH_LONG).show();
            });
    }
}
