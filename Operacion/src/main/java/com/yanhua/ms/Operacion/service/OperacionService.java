package com.yanhua.ms.Operacion.service;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanhua.ms.Operacion.model.OperacionModel;
import com.yanhua.ms.Operacion.repository.IOperacionRepository;



@Service
public class OperacionService  implements IOperacionService {
 
    private static final Logger logger = LoggerFactory.getLogger(OperacionService.class);
    
    @Autowired
    private IOperacionRepository operacionRepositoy;
 
    @SuppressWarnings("null")
    @Override
    public OperacionModel Registrar(OperacionModel operacionModel) {
        logger.info("Iniciando proceso de registro en base de datos");
        logger.debug("Operación - Tipo: {}, Cliente ID: {}, Total: {}", 
                operacionModel.getTipoOperacion(), 
                operacionModel.getIdCliente(), 
                operacionModel.getTotal());
        
        OperacionModel saved = operacionRepositoy.save(operacionModel);
        
        logger.info("Operación guardada exitosamente en base de datos con ID: {}", saved.getIdOperacion());
        logger.debug("Operación completada");
        
        return saved;
    }

   
   
}
