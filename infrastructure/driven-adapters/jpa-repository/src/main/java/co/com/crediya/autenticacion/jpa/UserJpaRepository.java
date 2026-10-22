package co.com.crediya.autenticacion.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByCorreoElectronico(String correoElectronico);
    Optional<UserEntity> findByCorreoElectronico(String correoElectronico);
}