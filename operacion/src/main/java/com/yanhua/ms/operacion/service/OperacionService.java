package com.yanhua.ms.operacion.service;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanhua.ms.operacion.model.OperacionModel;
import com.yanhua.ms.operacion.repository.IOperacionRepository;

@Service
public class OperacionService  implements IOperacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(OperacionService.class);

    @Autowired
    private IOperacionRepository operacionRepositoy;

    /**
     * Persiste una operación en el repositorio.
     *
     * @param operacionModel entidad de operación a guardar
     * @return la entidad guardada (con campos generados por BD, p.ej. id_operacion)
     */
    @Override
    public OperacionModel Registrar(OperacionModel operacionModel) {
        logger.info("[OperacionService][REGISTRAR][INICIO] → Guardando operación idCliente={}, tipo={}, total={}", operacionModel.getIdCliente(), operacionModel.getTipoOperacion(), operacionModel.getTotal());
        OperacionModel saved = operacionRepositoy.save(operacionModel);
        logger.info("[OperacionService][REGISTRAR][SUCCESS] → Operación guardada con id_operacion={}", saved.getIdOperacion());
        return saved;
    }

    /**
     * Recupera todas las operaciones desde el repositorio.
     *
     * @return lista de operaciones
     */
    @Override
    public java.util.List<OperacionModel> Listar() {
        logger.info("[OperacionService][LISTAR][INICIO] → Obteniendo listado de operaciones");
        java.util.List<OperacionModel> list = new java.util.ArrayList<>();
        operacionRepositoy.findAll().forEach(list::add);
        logger.info("[OperacionService][LISTAR][SUCCESS] → Total operaciones encontradas={}", list.size());
        return list;
    }

   
   
}
