package com.example.practica1.dto;

import java.util.List;

import lombok.Data;

@Data
public class PedidoRequestDTO {
    private Integer usuarioId;
    private List<ItemRequestDTO> items;
}
