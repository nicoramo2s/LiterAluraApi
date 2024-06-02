package com.alura.literatura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alura.literatura.model.Autores;

public interface AutoresRepository extends JpaRepository<Autores, Long> {
    Autores findByNombreContainsIgnoreCase(String nombre);

    Optional<Autores> findByNombreAndFechaNacimientoAndFechaFallecimiento(String nombre, Integer fechaNacimiento, Integer fechaFallecimiento);

    @Query("SELECT a FROM Autores a WHERE a.fechaNacimiento <= :fecha AND a.fechaFallecimiento >= :fecha")
    List<Autores> buscarAutoresPorDeterminadoAño(int fecha);
}
