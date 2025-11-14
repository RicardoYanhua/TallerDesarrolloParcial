package com.yanhua.ms.Operacion.repository;
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yanhua.ms.Operacion.model.OperacionModel;

 
@Repository
public interface IOperacionRepository extends CrudRepository<OperacionModel, String>{
   
}

