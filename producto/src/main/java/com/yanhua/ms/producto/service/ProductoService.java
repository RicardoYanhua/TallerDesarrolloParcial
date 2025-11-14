package com.yanhua.ms.producto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yanhua.ms.producto.dto.ProductoRequestCreate;
import com.yanhua.ms.producto.dto.ProductoRequestUpdate;
import com.yanhua.ms.producto.model.ProductoModel;
import com.yanhua.ms.producto.repository.IProductoRepository;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    IProductoRepository productoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    /**
     * Recupera todos los productos del sistema.
     * @return lista de productos (vacía si no existen)
     */
    @Override
    public List<ProductoModel> findAll() {
        logger.info("[ProductoService][FINDALL][INICIO] → Obteniendo listado de productos");
        List<ProductoModel> list = (List<ProductoModel>) productoRepository.findAll();
        logger.info("[ProductoService][FINDALL][SUCCESS] → Total productos encontrados={}", list.size());
        return list;
    }

    /**
     * Actualiza un producto existente.
     * @param idProducto identificador del producto a actualizar
     * @param productoModel datos a actualizar
     * @return el producto actualizado, o null si no existe
     */
    @Override
    public ProductoModel update(Long idProducto, ProductoRequestUpdate accountModel) {
        logger.info("[ProductoService][UPDATE][INICIO] → Actualizando producto idProducto={}", idProducto);
        ProductoModel producto = productoRepository.findByIdProducto(idProducto);

        if (producto == null) {
            logger.warn("[ProductoService][UPDATE][NOT_FOUND] → No se encontró producto con idProducto={}", idProducto);
            return null;
        }

        producto.setNombre(accountModel.getNombre());
        producto.setPrecio(accountModel.getPrecio());

        ProductoModel saved = productoRepository.save(producto);
        logger.info("[ProductoService][UPDATE][SUCCESS] → Producto actualizado idProducto={}", saved.getIdProducto());
        return saved;
    }

    /**
     * Crea un nuevo producto en el sistema.
     * @param productoRequest datos del producto a crear
     * @return el producto creado con su id
     */
    @Override
    public ProductoModel create(ProductoRequestCreate productoRequest) {
        logger.info("[ProductoService][CREATE][INICIO] → Creando producto nombre={}", productoRequest.getNombre());
        ProductoModel nuevo = new ProductoModel(productoRequest.getNombre(), productoRequest.getPrecio());
        ProductoModel saved = productoRepository.save(nuevo);
        logger.info("[ProductoService][CREATE][SUCCESS] → Producto creado idProducto={}", saved.getIdProducto());
        return saved;
    }

    /**
     * Elimina un producto por su id.
     * @param idProducto identificador del producto a eliminar
     * @return true si fue eliminado, false si no existía
     */
    @Override
    public boolean delete(Long idProducto) {
        logger.info("[ProductoService][DELETE][INICIO] → Eliminando producto idProducto={}", idProducto);
        ProductoModel producto = productoRepository.findByIdProducto(idProducto);
        if (producto == null) {
            logger.warn("[ProductoService][DELETE][NOT_FOUND] → No existe producto con idProducto={}", idProducto);
            return false;
        }
        productoRepository.delete(producto);
        logger.info("[ProductoService][DELETE][SUCCESS] → Producto eliminado idProducto={}", idProducto);
        return true;
    }

}



