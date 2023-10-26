package com.example.farmaciamovil;

public class Producto {
    private String nombre;
    private String imagenUrl;
    private double valor;
    private int cantidadUnidades;
    private String descripcion;

    public Producto() {

    }

    public Producto(String nombre, String imagenUrl, double valor, int cantidadUnidades, String descripcion) {
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.valor = valor;
        this.cantidadUnidades = cantidadUnidades;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public double getValor() {
        return valor;
    }

    public int getCantidadUnidades() {
        return cantidadUnidades;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
