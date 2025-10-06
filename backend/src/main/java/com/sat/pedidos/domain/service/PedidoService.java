package com.sat.pedidos.domain.service;

import com.sat.pedidos.api.dto.PedidoDTO;
import com.sat.pedidos.domain.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Long criar(PedidoDTO dto);
    Long atualizar(Long id, PedidoDTO dto);
    List<Pedido> listar();
    Optional<Pedido> buscarPorId(Long id);
    void remover(Long id);
}




