package com.ecomarket.logistica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ProveedorDTO {

    @Positive(message = "El ID debe ser positivo")
    private Long id;

    @NotBlank(message = "La razon social es obligatoria")
    @Size(max = 120, message = "La razon social no puede superar los 120 caracteres")
    private String razonSocial;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 15, message = "El RUT no puede superar los 15 caracteres")
    private String rut;

    @Size(max = 100, message = "El contacto no puede superar los 100 caracteres")
    private String contacto;

    @Email(message = "El email debe tener un formato valido")
    private String email;

    @Pattern(regexp = "^[+0-9\\s-]{8,20}$", message = "El telefono debe tener un formato valido")
    private String telefono;

    @NotBlank(message = "El tipo de proveedor es obligatorio")
    private String tipoProveedor;

    @NotBlank(message = "La cobertura es obligatoria")
    private String cobertura;

    private Boolean activo;
    private LocalDateTime fechaRegistro;

    public ProveedorDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoProveedor() { return tipoProveedor; }
    public void setTipoProveedor(String tipoProveedor) { this.tipoProveedor = tipoProveedor; }

    public String getCobertura() { return cobertura; }
    public void setCobertura(String cobertura) { this.cobertura = cobertura; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
