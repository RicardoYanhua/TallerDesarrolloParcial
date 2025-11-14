package yanhua.usuarios.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import yanhua.usuarios.constans.ApiRoutes;
import yanhua.usuarios.dto.LoginRequest;
import yanhua.usuarios.dto.LoginResponse;
import yanhua.usuarios.dto.UsuarioResponse;
import yanhua.usuarios.mapper.UsuarioMapper;
import yanhua.usuarios.model.UsuarioModel;
import yanhua.usuarios.service.IUsuarioService;

@RestController
@RequestMapping(ApiRoutes.Usuarios.BASE)
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios y login")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @PostMapping(ApiRoutes.Usuarios.LOGIN)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        logger.info("[UsuarioController][POST][LOGIN] → Login solicitado para: {}", loginRequest.getNombre());
        return usuarioService.login(loginRequest);
    }

    /**
     * LISTAR - Devuelve todos los usuarios registrados.
     */
    @GetMapping(ApiRoutes.Usuarios.LISTAR)
    public List<UsuarioResponse> listar() {
        logger.info("[UsuarioController][GET][LIST] → Solicitando listado completo de usuarios");
        List<UsuarioModel> usuarios = (List<UsuarioModel>) usuarioService.findAll();
        List<UsuarioResponse> response = UsuarioMapper.toResponseList(usuarios);
        logger.info("[UsuarioController][GET][LIST][SUCCESS] → Total de usuarios encontrados: {}", response.size());
        return response;
    }
}
