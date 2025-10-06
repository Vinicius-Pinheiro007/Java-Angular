package com.sat.pedidos.domain.repository;

import com.sat.pedidos.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsByClienteId(Long clienteId);
    boolean existsByItens_Produto_Id(Long produtoId);
}





