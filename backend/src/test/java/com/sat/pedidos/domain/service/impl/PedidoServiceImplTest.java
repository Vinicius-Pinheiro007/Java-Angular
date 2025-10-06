package com.sat.pedidos.domain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sat.pedidos.api.dto.PedidoDTO;
import com.sat.pedidos.api.dto.PedidoItemDTO;
import com.sat.pedidos.domain.model.Pedido;
import com.sat.pedidos.domain.repository.PedidoRepository;
import com.sat.pedidos.domain.service.NotificacaoService;
import com.sat.pedidos.domain.model.Produto;
import com.sat.pedidos.domain.repository.ProdutoRepository;
import com.sat.pedidos.domain.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class PedidoServiceImplTest {

    private PedidoRepository pedidoRepository;
    private NotificacaoService notificacaoService;
    private ProdutoRepository produtoRepository;
    private PedidoServiceImpl service;

    @BeforeEach
    void setup() {
        pedidoRepository = mock(PedidoRepository.class);
        notificacaoService = mock(NotificacaoService.class);
        produtoRepository = mock(ProdutoRepository.class);
        service = new PedidoServiceImpl(pedidoRepository, notificacaoService, produtoRepository);
    }

    @Test
    void criar_devePersistir_eRetornarId_quandoValorMaiorQueZero() {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(10L);
        dto.setValor(new BigDecimal("100.00"));
        PedidoItemDTO item = new PedidoItemDTO();
        item.setProdutoId(5L);
        item.setQuantidade(1);
        item.setValorUnitario(new BigDecimal("100.00"));
        dto.setItens(java.util.List.of(item));

        Pedido salvo = new Pedido();
        salvo.setId(1L);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(salvo);
        Produto produto = new Produto();
        produto.setId(5L);
        produto.setNome("Teste");
        produto.setPreco(new BigDecimal("100.00"));
        when(produtoRepository.findById(5L)).thenReturn(java.util.Optional.of(produto));

        Long id = service.criar(dto);

        assertEquals(1L, id);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(notificacaoService, times(1)).enviarEmail(eq(10L), anyString());
    }

    @Test
    void criar_deveLancarExcecao_quandoValorInvalido() {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(10L);
        dto.setValor(new BigDecimal("0.00"));

        assertThrows(IllegalArgumentException.class, () -> service.criar(dto));
        verify(pedidoRepository, never()).save(any());
        verify(notificacaoService, never()).enviarEmail(any(), any());
    }

    @Test
    void listar_deveDelegarParaRepository() {
        when(pedidoRepository.findAll()).thenReturn(List.of());
        assertNotNull(service.listar());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_deveDelegarParaRepository() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(service.buscarPorId(1L).isEmpty());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void remover_deveDelegarParaRepository() {
        service.remover(1L);
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void atualizar_devePersistirMudancas() {
        Pedido existente = new Pedido();
        existente.setId(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(existente);
        Produto produto = new Produto(); produto.setId(5L); produto.setNome("P"); produto.setPreco(new BigDecimal("10"));
        when(produtoRepository.findById(5L)).thenReturn(Optional.of(produto));

        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(1L);
        dto.setValor(new BigDecimal("10.00"));
        PedidoItemDTO item = new PedidoItemDTO();
        item.setProdutoId(5L); item.setQuantidade(1); item.setValorUnitario(new BigDecimal("10.00"));
        dto.setItens(java.util.List.of(item));

        Long id = service.atualizar(1L, dto);
        assertEquals(1L, id);
        verify(pedidoRepository).save(any(Pedido.class));
    }
}




