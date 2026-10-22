package co.com.crediya.autenticacion.usecase.user;

import co.com.crediya.autenticacion.model.exceptions.BusinessException;
import co.com.crediya.autenticacion.model.exceptions.EmailAlreadyExistsException;
import co.com.crediya.autenticacion.model.user.User;
import co.com.crediya.autenticacion.model.user.gateways.UserRepository;

import java.math.BigDecimal;

public class UserUseCaseImpl implements UserUseCase {

    private final UserRepository userRepository;

    public UserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registrarUsuario(User user) {
        if (user.getNombres() == null || user.getNombres().trim().isEmpty()) throw new BusinessException("El nombre es obligatorio");
        if (user.getApellidos() == null || user.getApellidos().trim().isEmpty()) throw new BusinessException("El apellido es obligatorio");
        if (user.getCorreoElectronico() == null || user.getCorreoElectronico().trim().isEmpty()) throw new BusinessException("El correo electrónico es obligatorio");
        if (user.getSalarioBase() == null) throw new BusinessException("El salario base es obligatorio");

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!user.getCorreoElectronico().matches(emailRegex)) throw new BusinessException("El formato del correo electrónico es inválido");
        if (user.getSalarioBase().compareTo(BigDecimal.ZERO) <= 0 || user.getSalarioBase().compareTo(new BigDecimal("15000000")) > 0) {
            throw new BusinessException("El salario base debe estar entre 0 y 15,000,000");
        }

        if (userRepository.existsByEmail(user.getCorreoElectronico())) {
            throw new EmailAlreadyExistsException("El correo ya está registrado por otro usuario");
        }

        return userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Credenciales incorrectas"));
    }
}