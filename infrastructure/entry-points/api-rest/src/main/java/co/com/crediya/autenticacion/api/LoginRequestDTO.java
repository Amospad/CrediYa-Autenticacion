package co.com.crediya.autenticacion.api;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String correoElectronico;
    private String password;
}