package com.sat.pedidos.domain.service.impl;

import com.sat.pedidos.api.dto.PedidoDTO;
import com.sat.pedidos.domain.model.ItemPedido;
import com.sat.pedidos.domain.model.Pedido;
import com.sat.pedidos.domain.model.Produto;
import com.sat.pedidos.domain.repository.PedidoRepository;
import com.sat.pedidos.domain.repository.ProdutoRepository;
import com.sat.pedidos.domain.service.NotificacaoService;
import com.sat.pedidos.domain.service.PedidoService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final NotificacaoService notificacaoService;
    private final ProdutoRepository produtoRepository;

    @Override
    public Long criar(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.getClienteId());
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(dto.getStatus() != null ? dto.getStatus() : com.sat.pedidos.domain.model.StatusPedido.LANCADO);

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter ao menos um item");
        }

        BigDecimal total = BigDecimal.ZERO;
        if (dto.getItens() != null) {
            for (var itemDto : dto.getItens()) {
                Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemDto.getProdutoId()));
                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setProduto(produto);
                item.setQuantidade(itemDto.getQuantidade());
                item.setValorUnitario(itemDto.getValorUnitario());
                item.setNumeroSerie(itemDto.getNumeroSerie());
                pedido.getItens().add(item);

                if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                    throw new IllegalArgumentException("Quantidade deve ser maior que 0");
                }
                if (item.getValorUnitario() == null || item.getValorUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Valor unitário deve ser > 0");
                }
                total = total.add(item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
            }
        }
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor total inválido");
        }
        pedido.setValor(total);

        Pedido salvo = pedidoRepository.save(pedido);
        notificacaoService.enviarEmail(dto.getClienteId(), "Pedido criado!");
        return salvo.getId();
    }

    @Override
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public void remover(Long id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public Long atualizar(Long id, PedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        pedido.setClienteId(dto.getClienteId());
        if (dto.getStatus() != null) {
            pedido.setStatus(dto.getStatus());
        }

        // Atualiza itens: remove todos e adiciona novamente (simples para o teste)
        pedido.getItens().clear();
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter ao menos um item");
        }

        BigDecimal total = BigDecimal.ZERO;
        if (dto.getItens() != null) {
            for (var itemDto : dto.getItens()) {
                Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemDto.getProdutoId()));
                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setProduto(produto);
                item.setQuantidade(itemDto.getQuantidade());
                item.setValorUnitario(itemDto.getValorUnitario());
                item.setNumeroSerie(itemDto.getNumeroSerie());
                pedido.getItens().add(item);

                if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                    throw new IllegalArgumentException("Quantidade deve ser maior que 0");
                }
                if (item.getValorUnitario() == null || item.getValorUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Valor unitário deve ser > 0");
                }
                total = total.add(item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
            }
        }
        pedido.setValor(total);
        Pedido salvo = pedidoRepository.save(pedido);
        return salvo.getId();
    }
}




