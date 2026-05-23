package com.ecomarket.reportes.dto;

import com.ecomarket.reportes.entity.FormatoExportacion;
import jakarta.validation.constraints.*;

public class ExportacionRequestDTO {

    @NotNull(message = "El formato de exportación es obligatorio")
    private FormatoExportacion formato;

    public FormatoExportacion getFormato() { return formato; }
    public void setFormato(FormatoExportacion formato) { this.formato = formato; }
}