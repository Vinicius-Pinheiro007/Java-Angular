package com.sat.pedidos.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sat.pedidos.api.dto.PedidoDTO;
import com.sat.pedidos.api.dto.PedidoItemDTO;
import com.sat.pedidos.domain.model.Pedido;
import com.sat.pedidos.domain.service.PedidoService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PedidoController.class)
@org.springframework.context.annotation.Import(com.sat.pedidos.api.handler.ApiExceptionHandler.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoService pedidoService;

    @BeforeEach
    void setup() {
    }

    @Test
    void postValido_deveRetornar201ELocation() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(1L);
        dto.setValor(new BigDecimal("10.00"));
        PedidoItemDTO item = new PedidoItemDTO();
        item.setProdutoId(1L);
        item.setQuantidade(1);
        item.setValorUnitario(new BigDecimal("10.00"));
        dto.setItens(java.util.List.of(item));

        when(pedidoService.criar(any(PedidoDTO.class))).thenReturn(99L);

        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", endsWith("/pedidos/99")));
    }

    @Test
    void postInvalido_deveRetornar400() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(null);
        dto.setValor(new BigDecimal("0.00"));

        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getLista_deveRetornar200() throws Exception {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setClienteId(2L);
        p.setValor(new BigDecimal("10.00"));
        p.setDataCriacao(LocalDateTime.now());
        when(pedidoService.listar()).thenReturn(List.of(p));

        mockMvc.perform(get("/pedidos"))
            .andExpect(status().isOk());
    }

    @Test
    void getPorId_existente_deveRetornar200() throws Exception {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setClienteId(2L);
        p.setValor(new BigDecimal("10.00"));
        p.setDataCriacao(LocalDateTime.now());
        when(pedidoService.buscarPorId(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/pedidos/1"))
            .andExpect(status().isOk());
    }

    @Test
    void getPorId_inexistente_deveRetornar404() throws Exception {
        when(pedidoService.buscarPorId(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/pedidos/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void delete_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/pedidos/1"))
            .andExpect(status().isNoContent());
    }
}


