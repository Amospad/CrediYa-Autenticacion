package co.com.crediya.autenticacion.api;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private BigDecimal salarioBase;
}