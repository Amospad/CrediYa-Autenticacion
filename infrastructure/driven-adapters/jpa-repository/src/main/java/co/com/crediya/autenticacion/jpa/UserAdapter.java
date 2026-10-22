package co.com.crediya.autenticacion.jpa;

import co.com.crediya.autenticacion.model.user.User;
import co.com.crediya.autenticacion.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final RolJpaRepository rolJpaRepository;

    @Override
    public User save(User user) {
        // Buscar el rol en la base de datos (si no existe, se crea)
        RolEntity rolEntity = rolJpaRepository.findByNombre(user.getRol())
                .orElseGet(() -> rolJpaRepository.save(RolEntity.builder()
                        .nombre(user.getRol())
                        .descripcion("Rol " + user.getRol())
                        .build()));

        UserEntity entity = UserEntity.builder()
                .id(user.getId())
                .nombres(user.getNombres())
                .apellidos(user.getApellidos())
                .fechaNacimiento(user.getFechaNacimiento())
                .direccion(user.getDireccion())
                .telefono(user.getTelefono())
                .correoElectronico(user.getCorreoElectronico())
                .salarioBase(user.getSalarioBase())
                .rol(rolEntity)
                .password(user.getPassword())
                .build();

        UserEntity saved = userJpaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByCorreoElectronico(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByCorreoElectronico(email).map(this::mapToDomain);
    }

    private User mapToDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .fechaNacimiento(entity.getFechaNacimiento())
                .direccion(entity.getDireccion())
                .telefono(entity.getTelefono())
                .correoElectronico(entity.getCorreoElectronico())
                .salarioBase(entity.getSalarioBase())
                .rol(entity.getRol().getNombre())
                .password(entity.getPassword())
                .build();
    }
}