package com.sat.pedidos.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sat.pedidos.domain.model.Produto;
import com.sat.pedidos.domain.service.ProdutoService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProdutoController.class)
@org.springframework.context.annotation.Import(com.sat.pedidos.api.handler.ApiExceptionHandler.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService produtoService;

    @Test
    void postProdutoValido_deveRetornar201() throws Exception {
        Produto p = new Produto();
        p.setId(1L);
        p.setNome("Celular");
        p.setPreco(new BigDecimal("1000.00"));
        when(produtoService.salvar(any(Produto.class))).thenReturn(p);

        Produto req = new Produto();
        req.setNome("Celular");
        req.setPreco(new BigDecimal("1000.00"));

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated());
    }

    @Test
    void getProdutos_deveRetornar200() throws Exception {
        when(produtoService.listar()).thenReturn(List.of());
        mockMvc.perform(get("/produtos"))
            .andExpect(status().isOk());
    }
}




