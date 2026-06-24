package com.ecomarket.logistica.util;

import com.ecomarket.logistica.model.Proveedor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculador de fecha estimada de entrega (ETA) para envios.
 * Considera el proveedor y la distancia estimada entre origen y destino.
 * Si no hay proveedor, usa 3 dias habiles por defecto.
 */
public final class EtaCalculator {

    private static final double VELOCIDAD_PROMEDIO_KMH = 60.0;
    private static final double KILOMETROS_POR_HORA_HABIL = 8.0;
    private static final double HORAS_POR_KM = 1.0 / VELOCIDAD_PROMEDIO_KMH;
    private static final int DIAS_HABILES_POR_DEFECTO = 3;

    private EtaCalculator() {}

    /**
     * Calcula la fecha estimada de entrega sumando horas de traslado y horas adicionales
     * Del proveedor (si tiene plazo de despacho).
     *
     * @param origen ciudad/region de origen
     * @param destino ciudad/region de destino
     * @param proveedor proveedor asignado (puede ser null)
     * @param ahora momento de despacho
     * @return fecha estimada de entrega
     */
    public static LocalDateTime calcular(String origen, String destino, Proveedor proveedor, LocalDateTime ahora) {
        if (origen == null || destino == null) {
            return ahora.plusDays(DIAS_HABILES_POR_DEFECTO);
        }
        double distancia = estimarDistancia(origen, destino);
        double horasTraslado = distancia * HORAS_POR_KM;
        double horasProveedor = proveedor != null && proveedor.getPlazoDespachoHoras() != null
                ? proveedor.getPlazoDespachoHoras()
                : KILOMETROS_POR_HORA_HABIL;
        double totalHoras = horasTraslado + horasProveedor;
        // IE 2.2.1: La ETA no puede ser anterior al momento actual.
        // Minimo 1 hora operativa para preparar despacho.
        long horas = Math.max(1L, (long) Math.ceil(totalHoras));
        LocalDateTime eta = ahora.plusHours(horas);
        if (eta.isBefore(ahora)) {
            eta = ahora.plusHours(1);
        }
        return eta;
    }

    /**
     * Estima la distancia entre origen y destino.
     * Heuristica simple: si son iguales -> 0 km, si comparten los primeros 3 caracteres
     * -> 50 km (misma region aprox), en otro caso -> 300 km (interregional).
     * En un sistema real esto consultaria una API de geocoding o una tabla de ciudades.
     */
    static double estimarDistancia(String origen, String destino) {
        String o = origen.toLowerCase().trim();
        String d = destino.toLowerCase().trim();
        if (o.equals(d)) return 0.0;
        int len = Math.min(3, Math.min(o.length(), d.length()));
        if (len > 0 && o.substring(0, len).equals(d.substring(0, len))) return 50.0;
        return 300.0;
    }

    public static class RutaResult {
        public double distanciaTotalKm;
        public double tiempoTotalHoras;
        public List<String> ordenParadas;

        public RutaResult(double distanciaTotalKm, double tiempoTotalHoras, List<String> ordenParadas) {
            this.distanciaTotalKm = distanciaTotalKm;
            this.tiempoTotalHoras = tiempoTotalHoras;
            this.ordenParadas = ordenParadas;
        }
    }

    /**
     * Calcula la ruta óptima minimizando la distancia (algoritmo del vecino más cercano).
     *
     * @param paradas lista de paradas a visitar
     * @return resultado con la distancia total, tiempo y orden
     */
    public static RutaResult calcularRutaOptima(List<String> paradas) {
        if (paradas == null || paradas.isEmpty()) {
            throw new IllegalArgumentException("La lista de paradas no puede estar vacia");
        }
        if (paradas.size() == 1) {
            return new RutaResult(0.0, 0.0, new ArrayList<>(paradas));
        }

        List<String> pendientes = new ArrayList<>(paradas);
        List<String> orden = new ArrayList<>();
        double distanciaTotal = 0.0;

        String actual = pendientes.remove(0);
        orden.add(actual);

        while (!pendientes.isEmpty()) {
            String masCercano = null;
            double menorDistancia = Double.MAX_VALUE;

            for (String candidato : pendientes) {
                double dist = estimarDistancia(actual, candidato);
                if (dist < menorDistancia) {
                    menorDistancia = dist;
                    masCercano = candidato;
                }
            }

            distanciaTotal += menorDistancia;
            orden.add(masCercano);
            pendientes.remove(masCercano);
            actual = masCercano;
        }

        double tiempoTotal = distanciaTotal * HORAS_POR_KM;
        return new RutaResult(distanciaTotal, tiempoTotal, orden);
    }
}
