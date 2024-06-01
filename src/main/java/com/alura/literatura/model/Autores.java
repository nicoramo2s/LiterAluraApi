package com.alura.literatura.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "autores", uniqueConstraints = @UniqueConstraint(columnNames = { "nombre", "fecha_nacimiento",
        "fecha_fallecimiento" }))
public class Autores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "fecha_nacimiento")
    private Integer fechaNacimiento;

    @Column(name = "fecha_fallecimiento")
    private Integer fechaFallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Libros> libros = new ArrayList<>();

    public Autores() {
    }

    public Autores(String nombre, Integer fechaNacimiento, Integer fechaFallecimiento) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libros> getLibros() {
        return libros;
    }

    public void setLibros(List<Libros> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        // Uniendo los títulos de los libros en una sola cadena
        String titulosLibros = libros.stream()
                .map(Libros::getTitulo)
                .collect(Collectors.joining(", "));
        return "\nAutores: " + nombre +
                "\nFecha de nacimiento: " + fechaNacimiento +
                "\nFecha de fallecimiento: " + fechaFallecimiento +
                "\nLibros: " + titulosLibros + "\n";
    }

}
