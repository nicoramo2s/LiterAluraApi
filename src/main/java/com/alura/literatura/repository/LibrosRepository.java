package com.alura.literatura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alura.literatura.model.Libros;

@Repository
public interface LibrosRepository extends JpaRepository<Libros,Long> {

    Libros findByTitulo(String titulo);

    @Query("SELECT l FROM Libros l WHERE l.idioma = :idioma")
    List<Libros> buscarLibrosPorIdioma(String idioma);
}
