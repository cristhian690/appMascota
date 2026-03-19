package com.example.appmascotas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarCustom extends AppCompatActivity implements MascotaAdapter.OnAccionListener {


    RecyclerView recyclerMascota;
    MascotaAdapter adapter;
    ArrayList<Mascota> listaMascotas;
    RequestQueue requestQueue;

    private final String URL = "http://192.168.101.59:3000/mascotas/";

    private  void loadUI(){
        recyclerMascota = findViewById(R.id.recyclerMascotas);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_custom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadUI();

        //Prepara lista
        listaMascotas = new ArrayList<>();
        adapter = new MascotaAdapter(this, listaMascotas, this); //Implementar dificiocn de clase...
        recyclerMascota.setLayoutManager(new LinearLayoutManager(this));
        recyclerMascota.setAdapter(adapter);

        //WS
        obtenerDatos();
    }

    private void obtenerDatos(){
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                jsonArray -> renderizarLista(jsonArray),
                error -> {
                    Log.e("ErrorWS", error.toString());
                    Toast.makeText(this, "No se obtuvieron los datos", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonArrayRequest);
    }


    private void renderizarLista(JSONArray jsonMascotas){


        try {
            listaMascotas.clear();

            for(int i = 0; i < jsonMascotas.length(); i++){
                JSONObject json =  jsonMascotas.getJSONObject(i);
                listaMascotas.add(new Mascota(
                        json.getInt("id"),
                        json.getString("tipo"),
                        json.getString("nombre"),
                        json.getString("color"),
                        json.getDouble("pesokg")
                ));
            } // fin for
            adapter.notifyDataSetChanged();

        }catch (Exception e){
            Log.e("ErrorJSON", e.toString());
        }
    }

    @Override
    public void onEditar(int position, Mascota mascota) {
        Intent intent = new Intent(ListarCustom.this, Actualizar.class);
             intent.putExtra("id",mascota.getId());
             intent.putExtra("tipo",mascota.getTipo());
             intent.putExtra("nombre",mascota.getNombre());
             intent.putExtra("color",mascota.getColor());
             intent.putExtra("pesokg",mascota.getPesokg());
           startActivity(intent);
    }

    @Override
    public void onEliminar(int position, Mascota mascota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mascotas");
        builder.setMessage("¿Confirma que desea eliminar a " + mascota.getNombre() + "?");

        builder.setPositiveButton("Sí", (dialogInterface, i) -> {
            // Aquí llamamos al método privado que usa el ID y la POSICIÓN
            eliminarMascota(mascota.getId(), position);
        });

        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private void eliminarMascota(int id, int position) {
        requestQueue = Volley.newRequestQueue(this);
        String urlEliminar = this.URL + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                urlEliminar,
                null,
                jsonObject -> {
                    try {
                        // Basado en tu captura: leemos "success" y "message" del JSON de Node.js
                        boolean eliminado = jsonObject.getBoolean("success");
                        String mensaje = jsonObject.getString("message");

                        if (eliminado) {
                            listaMascotas.remove(position); // IMPORTANTE: borrar del ArrayList
                            adapter.notifyItemRemoved(position); // Borrar del RecyclerView
                            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("ErrorJSON", e.toString());
                    }
                },
                error -> {
                    Log.e("ErrorWS", error.toString());
                    Toast.makeText(this, "No se pudo eliminar el registro", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        obtenerDatos();
    }
}