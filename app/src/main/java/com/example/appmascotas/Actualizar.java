package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Actualizar extends AppCompatActivity {

    private int idMascota;
    private EditText edtTipo, edtNombre, edtColor, edtPeso;
    private Button btnActualizar; // Nombre del botón según tu XML
    private RequestQueue requestQueue;
    private final String URL = "http://192.168.101.59:3000/mascotas/";

    private void loadUI() {
        // Vinculamos usando los IDs exactos de tu nuevo XML
        edtTipo = findViewById(R.id.edtTipo);
        edtNombre = findViewById(R.id.edtNombre);
        edtColor = findViewById(R.id.edtColor);
        edtPeso = findViewById(R.id.edtPeso);
        btnActualizar = findViewById(R.id.btnActualizar); // Cambiado a btnActualizar
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar); // Tu nuevo archivo XML

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();

        // Recibimos los datos que vienen desde ListarCustom
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idMascota = extras.getInt("id");
            edtTipo.setText(extras.getString("tipo"));
            edtNombre.setText(extras.getString("nombre"));
            edtColor.setText(extras.getString("color"));
            edtPeso.setText(String.valueOf(extras.getDouble("pesokg")));
        }

        btnActualizar.setOnClickListener(v -> ejecutarActualizacion());
    }

    private void ejecutarActualizacion() {
        requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("tipo", edtTipo.getText().toString().trim());
            jsonObject.put("nombre", edtNombre.getText().toString().trim());
            jsonObject.put("color", edtColor.getText().toString().trim());
            jsonObject.put("pesokg", Double.parseDouble(edtPeso.getText().toString().trim()));
        } catch (JSONException | NumberFormatException e) {
            Log.e("ErrorDatos", e.toString());
        }

        // Petición PUT para actualizar en Node.js
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL + idMascota,
                jsonObject,
                response -> {
                    Toast.makeText(this, "Mascota actualizada", Toast.LENGTH_SHORT).show();
                    finish(); // Regresa a la lista automáticamente
                },
                error -> {
                    Log.e("ErrorWS", error.toString());
                    Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }
}