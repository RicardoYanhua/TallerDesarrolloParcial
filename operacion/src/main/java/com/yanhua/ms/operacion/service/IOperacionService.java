package com.yanhua.ms.operacion.service;

import com.yanhua.ms.operacion.model.OperacionModel;
import java.util.List;

public interface IOperacionService {
    /**
     * Registra (guarda) una operación en el sistema.
     *
     * Implementación típica: validaciones ligeras -> persistencia via repositorio.
     *
     * @param operacionModel entidad de operación a persistir
     * @return la entidad persistida, normalmente con campos generados por la BD
     */
    public OperacionModel Registrar (OperacionModel operacionModel);

    /**
     * Lista todas las operaciones registradas en el sistema.
     *
     * @return lista de operaciones (vacía si no hay registros)
     */
    public List<OperacionModel> Listar();

}