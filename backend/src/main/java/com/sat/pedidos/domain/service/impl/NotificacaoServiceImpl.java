package com.sat.pedidos.domain.service.impl;

import com.sat.pedidos.domain.service.NotificacaoService;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoServiceImpl implements NotificacaoService {
    @Override
    public void enviarEmail(Long clienteId, String mensagem) {
        System.out.printf("Email para cliente %d: %s%n", clienteId, mensagem);
    }
}






