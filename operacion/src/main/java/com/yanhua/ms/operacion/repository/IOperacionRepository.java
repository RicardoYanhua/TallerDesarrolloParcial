package com.yanhua.ms.operacion.repository;
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yanhua.ms.operacion.model.OperacionModel;

 
@Repository
public interface IOperacionRepository extends CrudRepository<OperacionModel, Integer>{
   
}

