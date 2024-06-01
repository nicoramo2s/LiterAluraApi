package com.alura.literatura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alura.literatura.model.Autores;

public interface AutoresRepository extends JpaRepository<Autores, Long> {
    Autores findByNombreContainsIgnoreCase(String nombre);
    Optional<Autores> findByNombreAndFechaNacimientoAndFechaFallecimiento(String nombre, Integer fechaNacimiento, Integer fechaFallecimiento);
}
