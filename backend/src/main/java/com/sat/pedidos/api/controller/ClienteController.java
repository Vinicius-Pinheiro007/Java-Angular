package com.sat.pedidos.api.controller;

import com.sat.pedidos.domain.model.Cliente;
import com.sat.pedidos.domain.service.ClienteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente, UriComponentsBuilder uri) {
        Cliente salvo = clienteService.salvar(cliente);
        URI location = uri.path("/clientes/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        cliente.setId(id);
        return ResponseEntity.ok(clienteService.salvar(cliente));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() { return ResponseEntity.ok(clienteService.listar()); }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return clienteService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) { clienteService.remover(id); return ResponseEntity.noContent().build(); }
}




