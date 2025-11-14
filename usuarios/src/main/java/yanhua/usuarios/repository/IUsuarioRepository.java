package yanhua.usuarios.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import yanhua.usuarios.model.UsuarioModel;

@Repository
public interface IUsuarioRepository extends MongoRepository<UsuarioModel, String> {

    Optional<UsuarioModel> findByNombre(String nombre);

    Optional<UsuarioModel> findByNombreAndClave(String nombre, String clave);

    boolean existsByNombre(String nombre);
}