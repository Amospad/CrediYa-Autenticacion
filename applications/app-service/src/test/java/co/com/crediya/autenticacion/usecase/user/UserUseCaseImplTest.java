package co.com.crediya.autenticacion.usecase.user;

import co.com.crediya.autenticacion.model.exceptions.BusinessException;
import co.com.crediya.autenticacion.model.exceptions.EmailAlreadyExistsException;
import co.com.crediya.autenticacion.model.user.User;
import co.com.crediya.autenticacion.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserUseCaseImpl userUseCase;

    private User validUser;

    @BeforeEach
    void setUp() {
        // Preparamos un usuario de prueba válido antes de cada test
        validUser = User.builder()
                .nombres("Juan")
                .apellidos("Perez")
                .correoElectronico("juan.perez@example.com")
                .salarioBase(new BigDecimal("5000000"))
                .password("password123")
                .rol("CLIENTE")
                .build();
    }

    @Test
    @DisplayName("Debe registrar un usuario exitosamente cuando todos los datos son válidos")
    void registrarUsuario_Exitoso() {
        // Arrange (Preparar)
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        // Act (Actuar)
        User resultado = userUseCase.registrarUsuario(validUser);

        // Assert (Afirmar)
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCorreoElectronico()).isEqualTo("juan.perez@example.com");
        
        // Verificamos que se llamó al encriptador y al repositorio
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el correo ya está registrado")
    void registrarUsuario_EmailDuplicado() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userUseCase.registrarUsuario(validUser))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("El correo ya está registrado por otro usuario");

        // Verificamos que NUNCA se llamó a guardar
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre está vacío")
    void registrarUsuario_NombreVacio() {
        // Arrange
        validUser.setNombres("");

        // Act & Assert
        assertThatThrownBy(() -> userUseCase.registrarUsuario(validUser))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("El nombre es obligatorio");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el salario es negativo")
    void registrarUsuario_SalarioInvalido() {
        // Arrange
        validUser.setSalarioBase(new BigDecimal("-100"));

        // Act & Assert
        assertThatThrownBy(() -> userUseCase.registrarUsuario(validUser))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("El salario base debe estar entre 0 y 15,000,000");
    }

    @Test
    @DisplayName("Debe hacer login exitosamente con credenciales válidas")
    void login_Exitoso() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));

        // Act
        User resultado = userUseCase.login("juan.perez@example.com", "password123");

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCorreoElectronico()).isEqualTo("juan.perez@example.com");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el usuario no existe en el login")
    void login_UsuarioNoExiste() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userUseCase.login("noexiste@example.com", "password123"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Credenciales incorrectas");
    }
}