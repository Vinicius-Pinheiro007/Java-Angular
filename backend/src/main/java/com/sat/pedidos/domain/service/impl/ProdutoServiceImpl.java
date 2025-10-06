package com.sat.pedidos.domain.service.impl;

import com.sat.pedidos.domain.model.Produto;
import com.sat.pedidos.domain.repository.ProdutoRepository;
import com.sat.pedidos.domain.repository.PedidoRepository;
import com.sat.pedidos.domain.service.ProdutoService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    public Produto salvar(Produto produto) {
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço inválido, informe valor > 0");
        }
        return produtoRepository.save(produto);
    }

    @Override
    public List<Produto> listar() { return produtoRepository.findAll(); }

    @Override
    public Optional<Produto> buscarPorId(Long id) { return produtoRepository.findById(id); }

    @Override
    public void remover(Long id) {
        if (pedidoRepository.existsByItens_Produto_Id(id)) {
            throw new IllegalArgumentException("Produto está sendo usado em pedidos e não pode ser excluído");
        }
        produtoRepository.deleteById(id);
    }
}



