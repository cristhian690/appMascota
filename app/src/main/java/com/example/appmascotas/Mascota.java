package com.example.appmascotas;

public class Mascota {
    private int id;
    private String tipo;
    private String nombre;
    private String color;
    private double pesokg;

    // Constructor completo para recibir los datos del servidor
    // Cambia "String pesokg" por "double pesokg" en esta línea
    public Mascota(int id, String tipo, String nombre, String color, double pesokg) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.color = color;
        this.pesokg = pesokg; // Aquí ya no dará error
    }

    // Solo métodos GET
    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public double getPesokg() {
        return pesokg;
    }

}