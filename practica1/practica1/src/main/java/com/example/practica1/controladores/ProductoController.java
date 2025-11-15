package com.example.practica1.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.practica1.models.Producto;
import com.example.practica1.servicios.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    // CREATE
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        return new ResponseEntity<>(productoService.createProducto(producto),
                HttpStatus.CREATED); // 201
    }

    // READ (All)
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos()); // 200
    }

    // READ (One)
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Integer id) {
        return productoService.getProductoById(id)
                .map(ResponseEntity::ok) // 200
                .orElse(ResponseEntity.notFound().build()); // 404
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        return productoService.updateProducto(id, producto)
                .map(ResponseEntity::ok) // 200
                .orElse(ResponseEntity.notFound().build()); // 404
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        if (productoService.deleteProducto(id)) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}