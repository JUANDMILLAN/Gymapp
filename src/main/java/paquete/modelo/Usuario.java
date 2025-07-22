package paquete.modelo;

public class Usuario {
    private int id;
    private String nombre;
    private String aria;
    private String fechaVencimiento;
    private String alerta;
    private int dias_pagados;// o simplemente ignorar este campo visualmente desde la vista
    /**
     * Copyright (c) 2025 Juan Diego Millán Arango
     *
     * Todos los derechos reservados.
     *
     * Este código fuente es propiedad de Juan Diego Millán Arango.
     * Su uso, copia, distribución o modificación no está permitida sin autorización explícita por escrito.
     */



    public Usuario(int id, String nombre, String aria, String fechaVencimiento, String alerta, int dias_pagados) {
        this.id = id;
        this.nombre = nombre;
        this.aria = aria;
        this.fechaVencimiento = fechaVencimiento;
        this.alerta = alerta;
        this.dias_pagados = dias_pagados; // este campo no se usa en la vista, pero se puede usar para cálculos internos
    }

    public Usuario() {}




    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAria() {
        return aria;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getAlerta() {
        return alerta;
    }

    public int getDias_pagados() {
        return dias_pagados;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAria(String aria) {
        this.aria = aria;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public void setDias_pagados(int dias_pagados) {
        this.dias_pagados = dias_pagados;
    }
}
