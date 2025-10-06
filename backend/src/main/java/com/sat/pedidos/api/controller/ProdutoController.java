package com.sat.pedidos.api.controller;

import com.sat.pedidos.domain.model.Produto;
import com.sat.pedidos.domain.service.ProdutoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto, UriComponentsBuilder uri) {
        Produto salvo = produtoService.salvar(produto);
        URI location = uri.path("/produtos/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        produto.setId(id);
        return ResponseEntity.ok(produtoService.salvar(produto));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() { return ResponseEntity.ok(produtoService.listar()); }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        return produtoService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) { produtoService.remover(id); return ResponseEntity.noContent().build(); }
}




