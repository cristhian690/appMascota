package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    String tipo, nombre, color;
    double peso;

    EditText edtTipo, edtNombre, edtColor, edtPeso;

    Button btnRegistrarMascota;

    //Enviar / recibir los datos hacia el servicio
    RequestQueue requestQueue;

    //URL
    private final String URL = "http://192.168.101.59:3000/mascotas/";

    private void loadUI(){
        edtTipo = findViewById(R.id.edtTipo);
        edtNombre = findViewById(R.id.edtNombre);
        edtColor = findViewById(R.id.edtColor);
        edtPeso = findViewById(R.id.edtPeso);
        btnRegistrarMascota = findViewById(R.id.btnRegistrarMascota);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Metodo con referencias
        loadUI();

        //Eventos
        btnRegistrarMascota.setOnClickListener((v) -> { validarRegistro(); });
    }

    private void resetUI() {
        edtTipo.setText("");
        edtNombre.setText("");
        edtColor.setText("");
        edtPeso.setText("");
        edtTipo.requestFocus();
    }


    private void validarRegistro(){
        if (edtTipo.getText().toString().isEmpty()){
            edtTipo.setError("Complete con Perro, Gato");
            edtTipo.requestFocus();
            return;
        }

        if (edtNombre.getText().toString().isEmpty()){
            edtNombre.setError("Escriba el nombre");
            edtNombre.requestFocus();
            return;
        }

        if (edtColor.getText().toString().isEmpty()){
            edtColor.setError("Este campo es obligatorio");
            edtColor.requestFocus();
            return;
        }

        if (edtPeso.getText().toString().isEmpty()){
            edtPeso.setError("Ingrese un valor");
            edtPeso.requestFocus();
            return;
        }

        tipo = edtTipo.getText().toString().trim();
        nombre = edtNombre.getText().toString().trim();
        color = edtColor.getText().toString().trim();

        // CORRECCIÓN 1: No declarar 'double' otra vez, usar la variable de la clase
        try {
            peso = Double.parseDouble(edtPeso.getText().toString());
        } catch (NumberFormatException e) {
            edtPeso.setError("Número inválido");
            return;
        }

        // CORRECCIÓN 2: Agregar 'return' para detener el proceso si el tipo es incorrecto
        if (!tipo.equalsIgnoreCase("Perro") && !tipo.equalsIgnoreCase("Gato")){
            edtTipo.setError("Solo se permite: Perro, Gato");
            edtTipo.requestFocus();
            return;
        }

        if (peso < 0){
            edtPeso.setError("Solo se permite valores positivos");
            edtPeso.requestFocus();
            return;
        }

        // CORRECCIÓN 3: Usar Registrar.this en lugar de getApplicationContext()
        AlertDialog.Builder builder = new AlertDialog.Builder(Registrar.this);
        builder.setTitle("Mascotas");
        builder.setMessage("¿Seguro de registrar a " + nombre + "?");

        builder.setPositiveButton("Sí", (a,b) -> {
            registrarMascota();
        });
        builder.setNegativeButton("No", null);

        builder.create().show();
    }

    private void registrarMascota(){
        //Comunicacion
        requestQueue = Volley.newRequestQueue(this);

        //POST = requiere JSON(Datos a enviar)
        JSONObject jsonObject = new JSONObject();

        //Asignar los valores de las cajas
        try{
            jsonObject.put("tipo", tipo);
            jsonObject.put("nombre", nombre);
            jsonObject.put("color", color);
            jsonObject.put("pesokg", peso);
        }catch (JSONException e){
            Log.e("Error", e.toString());
        }

        Log.d("ValoresWS", jsonObject.toString());

        //Definir un objeto (respuesta obtener)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //EXITO
                        //Log.d("Resultado", jsonObject.toString());
                        Toast.makeText(getApplicationContext(), "Guardado correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error proceso", Toast.LENGTH_SHORT).show();
                        Log.e("SWError", volleyError.toString());
                    }
                }
        );

        //Ejecutamos el proceso
        requestQueue.add(jsonObjectRequest);
    }
}