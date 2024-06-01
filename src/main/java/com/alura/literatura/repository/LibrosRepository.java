package com.alura.literatura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alura.literatura.model.Libros;

@Repository
public interface LibrosRepository extends JpaRepository<Libros,Long> {

}
