package com.sat.pedidos.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import com.sat.pedidos.domain.model.StatusPedido;
import java.util.List;

public class PedidoDTO {

    @NotNull
    private Long clienteId;

    private BigDecimal valor; // calculado no serviço

    @Valid
    private List<PedidoItemDTO> itens;

    private StatusPedido status;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<PedidoItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItemDTO> itens) {
        this.itens = itens;
    }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
}




