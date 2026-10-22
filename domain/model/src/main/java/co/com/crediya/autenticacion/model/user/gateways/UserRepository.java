package co.com.crediya.autenticacion.model.user.gateways;

import co.com.crediya.autenticacion.model.user.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}