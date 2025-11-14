package com.yanhua.ms.usuario.service;


import com.yanhua.ms.usuario.dto.LoginRequest;
import com.yanhua.ms.usuario.dto.LoginResponse;
import com.yanhua.ms.usuario.model.UsuarioModel;

public interface IUsuarioService {
	public LoginResponse login(LoginRequest login);
	public Iterable<UsuarioModel>  findAll() ;

    

}