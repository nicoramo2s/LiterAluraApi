package com.alura.literatura.principal;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.alura.literatura.model.ApiResponse;
import com.alura.literatura.model.Autores;
import com.alura.literatura.model.DatosAutores;
import com.alura.literatura.model.DatosLibros;
import com.alura.literatura.model.Libros;
import com.alura.literatura.repository.AutoresRepository;
import com.alura.literatura.repository.LibrosRepository;
import com.alura.literatura.service.ConsumoApi;
import com.alura.literatura.service.ConvierteDatos;

public class Principal {
    private Scanner input = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibrosRepository librosRepository;
    private AutoresRepository autoresRepository;

    public Principal(LibrosRepository librosRepository, AutoresRepository autoresRepository) {
        this.librosRepository = librosRepository;
        this.autoresRepository = autoresRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    --------------------------------
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma

                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = input.nextInt();
            input.nextLine();
            switch (opcion) {
                case 1:
                    buscarYGuardarLibros();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorFecha();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
    }

    private Optional<DatosLibros> consultarDatosLibro() {
        System.out.println("\nEscribe el titulo del libro que deseas buscar");
        var tituloLibro = input.nextLine();
        var json = consumoApi.obtenerDatosDeApi(URL_BASE + "?search=" + tituloLibro.toLowerCase().replace(" ", "+"));
        ApiResponse datos = convierteDatos.obtenerDatos(json, ApiResponse.class);
        return datos.libros().stream()
                .map(l -> new DatosLibros(l.titulo(),
                        l.autores().stream()
                                .map(a -> new DatosAutores(a.nombre(), a.fechaNacimiento(), a.fechaFallecimiento()))
                                .collect(Collectors.toList()),
                        l.idiomas(), l.numeroDeDescargas()))
                .findFirst();
    }

    private void buscarYGuardarLibros() {
        Optional<DatosLibros> datos = consultarDatosLibro();
        if (datos.isPresent()) {
            Libros libro = new Libros(datos);
            Libros tituloLibro = librosRepository.findByTitulo(libro.getTitulo());
            if (tituloLibro != null && tituloLibro.getTitulo().equals(libro.getTitulo())) {
                System.out.println("-------------------\nEl Libro ya esta registrado y no puedes volverlo a registrar");
            } else {
                List<Autores> autoresList = datos.get().autores().stream()
                        .map(a -> {
                            // Buscar autor existente o crear uno nuevo
                            Optional<Autores> autorExistente = autoresRepository
                                    .findByNombreAndFechaNacimientoAndFechaFallecimiento(
                                            a.nombre(), a.fechaNacimiento(), a.fechaFallecimiento());
                            if (autorExistente.isPresent()) {
                                return autorExistente.get();
                            } else {
                                Autores nuevoAutor = new Autores(a.nombre(), a.fechaNacimiento(),
                                        a.fechaFallecimiento());
                                return autoresRepository.save(nuevoAutor);
                            }
                        })
                        .collect(Collectors.toList());

                // Asociar los autores al libro
                libro.setAutores(autoresList);
                System.out.println(libro.toString());
                // Guardar el libro
                librosRepository.save(libro);

            }
        } else {
            System.out.println("----------------\nNo se encontraron datos para el libro especificado.");
        }
    }

    public void listarLibrosRegistrados() {
        List<Libros> libros = librosRepository.findAll();
        if (!libros.isEmpty()) {
            System.out.println(libros);
        } else {
            System.out.println("-------------\nNo hay Libros registrado");
        }
    }

    public void listarAutoresRegistrados() {
        List<Autores> autores = autoresRepository.findAll();
        if (!autores.isEmpty()) {
            System.out.println(autores);
        } else {
            System.out.println("-------------\nNo hay autores registrados");
        }
    }

    public void listarAutoresVivosPorFecha() {
        System.out.println("Ingrese el año vivo del autor que desee buscar");
        int fecha = input.nextInt();
        List<Autores> autores = autoresRepository.buscarAutoresPorDeterminadoAño(fecha);
        if (!autores.isEmpty()) {
            System.out.println(autores.toString());
        } else {
            System.out.println("------------------\nNo hay autores registrados en ese año");
        }
    }

    public void listarLibrosPorIdioma() {
        System.out.println("""

                Ingrese el idioma del libro que desea buscar
                es - Español
                en - Ingles
                jp - Japones
                fr - Frances
                pt - Portugues

                """);
        String idioma = input.nextLine();

        List<Libros> libros = librosRepository.buscarLibrosPorIdioma(idioma);

        if (!libros.isEmpty()) {
            System.out.println(libros.toString());
        } else {
            System.out.println("---------------------------------\nNo tenemos libros registrados en ese idioma");
        }

    }
}
