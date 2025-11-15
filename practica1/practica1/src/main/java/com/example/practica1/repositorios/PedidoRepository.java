package com.example.practica1.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.practica1.models.Pedido;
import com.example.practica1.models.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuario(Usuario usuario);
}
