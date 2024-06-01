package com.alura.literatura.service.impl;

public interface IConvierteDatos {
    // <T> T obtenerDatosConvertidos(String json, Class<T> clase);
    <T> T obtenerDatos(String json, Class<T> clase);


}
