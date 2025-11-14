package com.yanhua.ms.usuario.service;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanhua.ms.usuario.dto.LoginRequest;
import com.yanhua.ms.usuario.dto.LoginResponse;
import com.yanhua.ms.usuario.exception.UnauthorizedException;
import com.yanhua.ms.usuario.model.UsuarioModel;
import com.yanhua.ms.usuario.repository.IUsuarioRepository;

@Service
public class UsuarioService implements IUsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public LoginResponse login(LoginRequest login) {
        logger.info("[UsuarioService][LOGIN] → Intento de login para nombre: {}", login.getNombre());

        UsuarioModel user = usuarioRepository.findByNombre(login.getNombre())
                .orElseThrow(() -> new UnauthorizedException("Acceso incorrecto"));

        if (!Objects.equals(user.getClave(), login.getClave())) {
            throw new UnauthorizedException("Acceso incorrecto");
        }

        logger.info("[UsuarioService][LOGIN][SUCCESS] → Usuario autenticado: {}", user.getNombre());

        String token = UUID.randomUUID().toString();
        return new LoginResponse("Acceso correcto", token);
    }

    @Override
    public Iterable<UsuarioModel> findAll() {
        logger.info("[UsuarioService][FIND_ALL] → Obteniendo todos los usuarios");
        Iterable<UsuarioModel> usuarios = usuarioRepository.findAll();
        logger.info("[UsuarioService][FIND_ALL][SUCCESS] → Total de usuarios: {}", ((Collection<?>) usuarios).size());
        return usuarios;
    }

}
