package com.yanhua.ms.producto.service;

import java.util.List;

import com.yanhua.ms.producto.dto.ProductoRequestUpdate;
import com.yanhua.ms.producto.model.ProductoModel;

public interface IProductoService {

    public ProductoModel add(ProductoModel invoice);

    public List<ProductoModel> findAll();

    public ProductoModel findById(Long idProducto);

    public ProductoModel update(Long idProducto, ProductoRequestUpdate productoModel);

    public void deleteById(Long id);

}



