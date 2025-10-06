package com.sat.pedidos.domain.repository;

import com.sat.pedidos.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}




