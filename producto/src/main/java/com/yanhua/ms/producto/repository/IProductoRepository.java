package com.yanhua.ms.producto.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yanhua.ms.producto.model.ProductoModel;

@Repository
public interface IProductoRepository extends CrudRepository<ProductoModel,Long> {

}



