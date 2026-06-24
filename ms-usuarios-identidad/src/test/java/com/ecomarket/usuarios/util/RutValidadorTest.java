package com.ecomarket.usuarios.util;

import com.ecomarket.usuarios.exception.CredencialesInvalidasException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RutValidadorTest {

    @Test
    void normalizar_null_retornaNull() {
        assertNull(RutValidador.normalizar(null));
    }

    @Test
    void normalizar_valido_retornaLimpio() {
        assertEquals("12345678-9", RutValidador.normalizar("12.345.678-9"));
        assertEquals("12345678-K", RutValidador.normalizar(" 12.345.678-k "));
    }

    @Test
    void esValido_null_retornaFalse() {
        assertFalse(RutValidador.esValido(null));
    }

    @Test
    void esValido_corto_retornaFalse() {
        assertFalse(RutValidador.esValido("1"));
    }

    @Test
    void esValido_sinGuion_validaIgual() {
        // "123456785" (termina en 5, válido para 12345678)
        assertTrue(RutValidador.esValido("123456785"));
    }

    @Test
    void esValido_conLetras_retornaFalse() {
        assertFalse(RutValidador.esValido("A2345678-5"));
    }

    @Test
    void esValido_dvIncorrecto_retornaFalse() {
        assertFalse(RutValidador.esValido("12345678-6"));
    }

    @Test
    void esValido_dvK_retornaTrue() {
        assertTrue(RutValidador.esValido("6-K")); // 6*2 = 12, 12%11=1, 11-1=10 -> K
    }

    @Test
    void esValido_dvCero_retornaTrue() {
        assertTrue(RutValidador.esValido("31-0")); // 1*2 + 3*3 = 11, 11%11=0, 11-0=11 -> 0
    }

    @Test
    void validar_valido_noLanzaExcepcion() {
        assertDoesNotThrow(() -> RutValidador.validar("12.345.678-5"));
    }

    @Test
    void validar_invalido_lanzaExcepcion() {
        assertThrows(CredencialesInvalidasException.class, () -> RutValidador.validar("12.345.678-6"));
    }
}
