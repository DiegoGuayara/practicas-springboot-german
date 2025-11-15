package com.example.practica1.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.practica1.dto.ItemRequestDTO;
import com.example.practica1.dto.PedidoRequestDTO;
import com.example.practica1.models.ItemPedido;
import com.example.practica1.models.Pedido;
import com.example.practica1.models.Producto;
import com.example.practica1.models.Usuario;
import com.example.practica1.repositorios.PedidoRepository;
import com.example.practica1.repositorios.ProductoRepository;
import com.example.practica1.repositorios.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> getPedidoById(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido realizarPedido(PedidoRequestDTO pedidoRequest) {

        Usuario usuario = usuarioRepository.findById(pedidoRequest.getUsuarioId())
                .orElseThrow(
                        () -> new RuntimeException("Usuario no encontrado con id: " + pedidoRequest.getUsuarioId()));

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUsuario(usuario);
        nuevoPedido.setStatus("PENDIENTE");

        List<ItemPedido> items = new ArrayList<>();
        // 3. Procesar cada item del carrito (DTO)
        for (ItemRequestDTO itemDTO : pedidoRequest.getItems()) {

            // 3.1 Validar que el producto exista
            Producto producto = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " +
                            itemDTO.getProductoId()));

            ItemPedido item = new ItemPedido();
            item.setProducto(producto);
            item.setCantidad(itemDTO.getCantidad());
            item.setPrecioUnitario(producto.getPrecio());
            item.setPedido(nuevoPedido);

            items.add(item);
        }

        nuevoPedido.setItems(items);
        return pedidoRepository.save(nuevoPedido);
    }

    public Optional<Pedido> updatePedidoStatus(Integer id, String status) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatus(status.toUpperCase());
            return pedidoRepository.save(pedido);
        });
    }

    public boolean deletePedido(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
