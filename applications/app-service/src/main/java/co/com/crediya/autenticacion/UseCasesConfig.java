package co.com.crediya.autenticacion;

import co.com.crediya.autenticacion.model.user.gateways.UserRepository;
import co.com.crediya.autenticacion.usecase.user.UserUseCase;
import co.com.crediya.autenticacion.usecase.user.UserUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCasesConfig {

    @Bean
    public UserUseCase userUseCase(UserRepository userRepository) {
        return new UserUseCaseImpl(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}