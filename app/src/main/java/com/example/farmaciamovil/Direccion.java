package com.example.farmaciamovil;

public class Direccion {
    private String nombreCalle;
    private String numeroCalle;
    private String region;
    private String ciudad;

    public Direccion() {

    }

    public Direccion(String nombreCalle, String numeroCalle, String region, String ciudad) {
        this.nombreCalle = nombreCalle;
        this.numeroCalle = numeroCalle;
        this.region = region;
        this.ciudad = ciudad;
    }

    public String getNombreCalle() {
        return nombreCalle;
    }

    public String getNumeroCalle() {
        return numeroCalle;
    }

    public String getRegion() {
        return region;
    }

    public String getCiudad() {
        return ciudad;
    }
}
