package com.yanhua.ms.producto.service;

import java.util.List;

import com.yanhua.ms.producto.dto.ProductoRequestCreate;
import com.yanhua.ms.producto.dto.ProductoRequestUpdate;
import com.yanhua.ms.producto.model.ProductoModel;

public interface IProductoService {
    
    /**
     * Recupera todos los productos del sistema.
     * @return lista de productos (vacía si no existen)
     */
    public List<ProductoModel> findAll();

    /**
     * Actualiza un producto existente.
     * @param idProducto identificador del producto a actualizar
     * @param productoModel datos a actualizar
     * @return el producto actualizado, o null si no existe
     */
    public ProductoModel update(Long idProducto, ProductoRequestUpdate productoModel);
    /**
     * Crea un nuevo producto en el sistema.
     * @param productoRequest datos del producto a crear
     * @return el producto creado con su id
     */
    public ProductoModel create(ProductoRequestCreate productoRequest);

    /**
     * Elimina un producto por su id.
     * @param idProducto identificador del producto a eliminar
     * @return true si fue eliminado, false si no existía
     */
    public boolean delete(Long idProducto);

}



