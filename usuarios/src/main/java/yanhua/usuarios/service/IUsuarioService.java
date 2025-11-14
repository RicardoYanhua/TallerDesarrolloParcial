package yanhua.usuarios.service;


import yanhua.usuarios.dto.LoginRequest;
import yanhua.usuarios.dto.LoginResponse;
import yanhua.usuarios.model.UsuarioModel;

public interface IUsuarioService {
	public LoginResponse login(LoginRequest login);
	public Iterable<UsuarioModel>  findAll() ;

    

}