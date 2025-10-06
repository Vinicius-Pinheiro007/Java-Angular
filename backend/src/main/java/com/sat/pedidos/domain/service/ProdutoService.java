package com.sat.pedidos.domain.service;

import com.sat.pedidos.domain.model.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoService {
    Produto salvar(Produto produto);
    List<Produto> listar();
    Optional<Produto> buscarPorId(Long id);
    void remover(Long id);
}




