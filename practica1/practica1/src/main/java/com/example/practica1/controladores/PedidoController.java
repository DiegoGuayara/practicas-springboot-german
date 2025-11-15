package com.example.practica1.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.practica1.dto.PedidoRequestDTO;
import com.example.practica1.models.Pedido;
import com.example.practica1.servicios.PedidoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody PedidoRequestDTO pedidoRequest) {
        try {
            Pedido nuevoPedido = pedidoService.realizarPedido(pedidoRequest);
            return new ResponseEntity<>(nuevoPedido,
                    HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Integer id) {
        return pedidoService.getPedidoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> updatePedidoStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> statusUpdate) {
        String newStatus = statusUpdate.get("status");
        if (newStatus == null) {
            return ResponseEntity.badRequest().build();
        }
        return pedidoService.updatePedidoStatus(id,
                newStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Integer id) {
        if (pedidoService.deletePedido(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}