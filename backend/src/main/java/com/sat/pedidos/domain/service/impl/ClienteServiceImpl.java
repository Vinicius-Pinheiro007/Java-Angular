package com.sat.pedidos.domain.service.impl;

import com.sat.pedidos.domain.model.Cliente;
import com.sat.pedidos.domain.repository.ClienteRepository;
import com.sat.pedidos.domain.service.ClienteService;
import com.sat.pedidos.domain.repository.PedidoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    public Cliente salvar(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        if (cliente.getDocumento() == null || cliente.getDocumento().isBlank()) {
            throw new IllegalArgumentException("Documento (CPF/CNPJ) é obrigatório");
        }
        String doc = cliente.getDocumento().replaceAll("[^0-9]", "");
        if (!(isCpfValido(doc) || isCnpjValido(doc))) {
            throw new IllegalArgumentException("Documento inválido (CPF/CNPJ)");
        }
        cliente.setDocumento(doc);
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> listar() { return clienteRepository.findAll(); }

    @Override
    public Optional<Cliente> buscarPorId(Long id) { return clienteRepository.findById(id); }

    @Override
    public void remover(Long id) {
        if (pedidoRepository.existsByClienteId(id)) {
            throw new IllegalArgumentException("Cliente possui pedidos e não pode ser excluído");
        }
        clienteRepository.deleteById(id);
    }

    private boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.chars().distinct().count() == 1) return false;
        int d1 = calcularDigito(cpf.substring(0, 9), new int[]{10,9,8,7,6,5,4,3,2});
        int d2 = calcularDigito(cpf.substring(0, 9) + d1, new int[]{11,10,9,8,7,6,5,4,3,2});
        return cpf.equals(cpf.substring(0, 9) + d1 + d2);
    }

    private boolean isCnpjValido(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || cnpj.chars().distinct().count() == 1) return false;
        int d1 = calcularDigito(cnpj.substring(0, 12), new int[]{5,4,3,2,9,8,7,6,5,4,3,2});
        int d2 = calcularDigito(cnpj.substring(0, 12) + d1, new int[]{6,5,4,3,2,9,8,7,6,5,4,3,2});
        return cnpj.equals(cnpj.substring(0, 12) + d1 + d2);
    }

    private int calcularDigito(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (base.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}



