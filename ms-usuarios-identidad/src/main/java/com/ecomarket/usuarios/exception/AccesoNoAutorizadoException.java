package com.ecomarket.usuarios.exception;

public class AccesoNoAutorizadoException extends RuntimeException {

    public AccesoNoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}