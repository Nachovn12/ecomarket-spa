package com.ecomarket.usuarios.util;

import com.ecomarket.usuarios.exception.CredencialesInvalidasException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidadorTest {

    @Test
    void esValida_null_retornaFalse() {
        assertFalse(PasswordValidador.esValida(null));
    }

    @Test
    void esValida_valida_retornaTrue() {
        assertTrue(PasswordValidador.esValida("Password123"));
    }

    @Test
    void esValida_invalida_retornaFalse() {
        assertFalse(PasswordValidador.esValida("12345678")); // Sin letras
        assertFalse(PasswordValidador.esValida("password123")); // Sin mayúsculas
        assertFalse(PasswordValidador.esValida("PASSWORD123")); // Sin minúsculas
        assertFalse(PasswordValidador.esValida("Pass12")); // Corta
    }

    @Test
    void validar_valida_noLanzaExcepcion() {
        assertDoesNotThrow(() -> PasswordValidador.validar("Password123"));
    }

    @Test
    void validar_invalida_lanzaExcepcion() {
        assertThrows(CredencialesInvalidasException.class, () -> PasswordValidador.validar("invalida"));
    }
}
