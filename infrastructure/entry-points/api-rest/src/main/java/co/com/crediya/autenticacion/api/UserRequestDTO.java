package co.com.crediya.autenticacion.api;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserRequestDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correoElectronico;

    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "1.0", message = "El salario debe ser mayor a 0")
    @DecimalMax(value = "15000000.0", message = "El salario no puede superar los 15,000,000")
    private BigDecimal salarioBase;

    private String rol;
    private String password;
}