package com.ecomarket.usuarios.util;

import com.ecomarket.usuarios.exception.CredencialesInvalidasException;

public final class PasswordValidador {

    private PasswordValidador() {}

    public static boolean esValida(String password) {
        if (password == null) return false;
        // Al menos 8 caracteres, 1 mayuscula, 1 minuscula, 1 numero
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    public static void validar(String password) {
        if (!esValida(password)) {
            throw new CredencialesInvalidasException("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número");
        }
    }
}
