package com.example.appmascotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//RecyclerViewAdapter exige 3 metodos
//onCreateviewHolder    :crear una fila/item
//onBindiewHolder       : llenar los datos de la fila/item
//getItemCount          :  cantidad de elementos
public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    //Atributos
    private  final ArrayList<Mascota>lista;
    private final Context context;
    private final OnAccionListener listener;


    //Interface
    public interface OnAccionListener{
        void onEditar(int position, Mascota mascota);
        void onEliminar (int position, Mascota mascota);
    }

    //Constructor
    //Contexto (activity) ,Lista(obtenemos por WS GET), listener (eventos de los botones)
    public MascotaAdapter (Context context, ArrayList<Mascota> lista, OnAccionListener listener){
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    //Representara cada fila
    static  class  ViewHolder extends  RecyclerView.ViewHolder{
        TextView txtNombre, txtTipo, txtPeso;

        Button btnEditar, btnEliminar;

        //vinculacion (acceso a cada elemento dentro de la item_mascotas.xml)
        ViewHolder (View itemview){
            super(itemview);
            txtNombre = itemview.findViewById(R.id.txtNombre);
            txtTipo = itemview.findViewById(R.id.txtTipo);
            txtPeso = itemview.findViewById(R.id.txtPeso);
            btnEditar = itemview.findViewById(R.id.btnEditar);
            btnEliminar = itemview.findViewById(R.id.btnEliminar);
        }
    }

    //INFLAR = crea layout para cada fila

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mascota, parent, false);
        return  new ViewHolder(view);
    }


    //2. lLenar los datos
    @Override
    public void onBindViewHolder(@NonNull MascotaAdapter.ViewHolder holder, int position) {
        Mascota mascota = lista.get(position);

        holder.txtNombre.setText(mascota.getNombre());
        holder.txtTipo.setText(mascota.getTipo());
        holder.txtPeso.setText("Peso " + String.valueOf(mascota.getPesokg())+ "kg." );

        //btones
        holder.btnEditar.setOnClickListener(v -> {
            listener.onEditar(holder.getAdapterPosition(), mascota);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            listener.onEliminar(holder.getAdapterPosition(), mascota);
        });

    }

    //3. calcular la cantidad de elementos de la lista
    @Override
    public int getItemCount() {
        return lista.size();
    }

    //4. Eliminar un elemento de la lista<Mascota>
    public  void  eliminarItem(int position){
        lista.remove(position);
        notifyItemRemoved(position);
    }
}
