package yanhua.usuarios.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yanhua.usuarios.dto.UsuarioResponse;
import yanhua.usuarios.model.UsuarioModel;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponse toResponse(UsuarioModel model) {
        if (model == null) {
            return null;
        }
        return UsuarioResponse.builder()
                .idUsuario(model.getIdUsuario())
                .nombre(model.getNombre())
                .build();
    }

    public static List<UsuarioResponse> toResponseList(Iterable<UsuarioModel> models) {
        if (models == null) {
            return Collections.emptyList();
        }
        List<UsuarioResponse> responses = new ArrayList<>();
        for (UsuarioModel model : models) {
            UsuarioResponse response = toResponse(model);
            if (response != null) {
                responses.add(response);
            }
        }
        return responses;
    }
}
