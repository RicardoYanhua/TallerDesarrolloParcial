package com.yanhua.ms.usuario.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.yanhua.ms.usuario.model.UsuarioModel;

@Repository
public interface IUsuarioRepository extends MongoRepository<UsuarioModel, String> {

    Optional<UsuarioModel> findByNombre(String nombre);

    Optional<UsuarioModel> findByNombreAndClave(String nombre, String clave);

    boolean existsByNombre(String nombre);
}