package co.com.crediya.autenticacion.api;

import co.com.crediya.autenticacion.api.security.JwtUtil;
import co.com.crediya.autenticacion.model.exceptions.BusinessException;
import co.com.crediya.autenticacion.model.user.User;
import co.com.crediya.autenticacion.usecase.user.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/usuarios")
    public ResponseEntity<UserResponseDTO> registrarUsuario(@Valid @RequestBody UserRequestDTO request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User userDomain = User.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .fechaNacimiento(request.getFechaNacimiento())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .correoElectronico(request.getCorreoElectronico())
                .salarioBase(request.getSalarioBase())
                .rol(request.getRol() != null ? request.getRol() : "CLIENTE")
                .password(encodedPassword)
                .build();

        User userSaved = userUseCase.registrarUsuario(userDomain);

        UserResponseDTO response = UserResponseDTO.builder()
                .id(userSaved.getId())
                .nombres(userSaved.getNombres())
                .apellidos(userSaved.getApellidos())
                .correoElectronico(userSaved.getCorreoElectronico())
                .salarioBase(userSaved.getSalarioBase())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // 1. Obtener el usuario (sin validar contraseña en el dominio)
        User user = userUseCase.login(request.getCorreoElectronico(), request.getPassword());

        // 2. Validar hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Credenciales incorrectas");
        }

        // 3. Generar Token
        String token = jwtUtil.generateToken(user.getCorreoElectronico(), user.getRol());

        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(token)
                .rol(user.getRol())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}