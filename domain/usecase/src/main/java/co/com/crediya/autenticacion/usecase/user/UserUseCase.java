package co.com.crediya.autenticacion.usecase.user;

import co.com.crediya.autenticacion.model.user.User;

public interface UserUseCase {
    User registrarUsuario(User user);
    User login(String email, String password);
}