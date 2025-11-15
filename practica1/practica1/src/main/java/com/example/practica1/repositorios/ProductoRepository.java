package com.example.practica1.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.practica1.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
