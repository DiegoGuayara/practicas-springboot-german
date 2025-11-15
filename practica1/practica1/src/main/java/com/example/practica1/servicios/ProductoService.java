package com.example.practica1.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.practica1.models.Producto;
import com.example.practica1.repositorios.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto createProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> updateProducto(Integer id, Producto productoDetails) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(productoDetails.getNombre());
            producto.setPrecio(productoDetails.getPrecio());
            return productoRepository.save(producto);
        });
    }

    public boolean deleteProducto(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
