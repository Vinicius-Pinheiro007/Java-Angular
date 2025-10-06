package com.sat.pedidos.domain.service;

import com.sat.pedidos.domain.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente salvar(Cliente cliente);
    List<Cliente> listar();
    Optional<Cliente> buscarPorId(Long id);
    void remover(Long id);
}




