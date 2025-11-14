package com.yanhua.ms.producto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanhua.ms.producto.dto.ProductoRequestUpdate;
import com.yanhua.ms.producto.model.ProductoModel;
import com.yanhua.ms.producto.repository.IProductoRepository;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    IProductoRepository productoRepository;

    @Override
    public List<ProductoModel> findAll() {
        return (List<ProductoModel>) productoRepository.findAll();
    }

    @Override
    public ProductoModel update(Long idProducto, ProductoRequestUpdate accountModel) {

        ProductoModel producto = productoRepository.findById(idProducto).orElse(null);

        if (producto == null) {
            return null;
        }

        producto.setNombre(accountModel.getNombre());
        producto.setPrecio(accountModel.getPrecio());

        return productoRepository.save(producto);
    }

    @Override
    public ProductoModel findById(Long idProducto) {
        return (ProductoModel) productoRepository.findById(idProducto).orElse(null);

    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public ProductoModel add(ProductoModel producto) {
        return productoRepository.save(producto);
    }

}



