package com.yanhua.ms.usuario.model;

import java.io.Serializable;

import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Document(collection = "usuario")
public class UsuarioModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
    private String idUsuario;

    private String nombre;
    private String clave;

    public UsuarioModel(String idUsuario, String nombre, String clave) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.clave = clave;
    }
}