package com.yanhua.ms.producto.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yanhua.ms.producto.constans.ApiRoutes;
import com.yanhua.ms.producto.dto.ProductoRequestCreate;
import com.yanhua.ms.producto.dto.ProductoRequestUpdate;
import com.yanhua.ms.producto.model.ProductoModel;
import com.yanhua.ms.producto.service.IProductoService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(ApiRoutes.Producto.BASE)
@Tag(name = "Productos", description = "Endpoints para la gestión y consulta de productos del sistema.")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    /**
     * LISTAR - Devuelve todas las facturas.
     */
    @GetMapping(ApiRoutes.Producto.LISTAR)
    public List<ProductoModel> getAll() {
        logger.info("[ProductoController][GET][LIST] → Solicitando listado completo de productos");
        List<ProductoModel> productos = productoService.findAll();
        logger.info("[ProductoController][GET][LIST][SUCCESS] → Total de productos encontrados: {}", productos.size());
        return productos;
    }

    /**
     * UPDATE - Actualiza una factura existente.
     */
    @PutMapping(ApiRoutes.Producto.UPDATE)
    public ResponseEntity<ProductoModel> update(@PathVariable Long idProducto, @RequestBody ProductoRequestUpdate producto) {
        logger.info("[ProductoController][PUT][UPDATE] → Actualizando producto idProducto={}", idProducto);

        ProductoModel updatedProducto = productoService.update(idProducto, producto);
        if (updatedProducto == null) {
            logger.warn("[ProductoController][PUT][UPDATE][NOT_FOUND] → No se encontró producto con idProducto={}", idProducto);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("[ProductoController][PUT][UPDATE][SUCCESS] → Producto actualizado idProducto={}", idProducto);
        return ResponseEntity.ok(updatedProducto);
    }

    /**
     * CREATE - Crea un nuevo producto.
     */
    @PostMapping(ApiRoutes.Producto.CREAR)
    public ResponseEntity<ProductoModel> create(@RequestBody ProductoRequestCreate productoRequest) {
        logger.info("[ProductoController][POST][CREATE] → Creando producto nombre={}", productoRequest.getNombre());
        ProductoModel created = productoService.create(productoRequest);
        logger.info("[ProductoController][POST][CREATE][SUCCESS] → Producto creado idProducto={}", created.getIdProducto());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * DELETE - Elimina un producto por id.
     */
    @DeleteMapping(ApiRoutes.Producto.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Long idProducto) {
        logger.info("[ProductoController][DELETE] → Eliminando producto idProducto={}", idProducto);
        boolean removed = productoService.delete(idProducto);
        if (!removed) {
            logger.warn("[ProductoController][DELETE][NOT_FOUND] → No existe producto idProducto={}", idProducto);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("[ProductoController][DELETE][SUCCESS] → Producto eliminado idProducto={}", idProducto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

