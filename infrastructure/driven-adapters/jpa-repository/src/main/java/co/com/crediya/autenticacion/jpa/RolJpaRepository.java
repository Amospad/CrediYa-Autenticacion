package co.com.crediya.autenticacion.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolJpaRepository extends JpaRepository<RolEntity, Long> {
    Optional<RolEntity> findByNombre(String nombre);
}