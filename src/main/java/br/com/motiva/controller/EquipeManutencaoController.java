package br.com.motiva.controller;

import br.com.motiva.dto.EquipeRequest;
import br.com.motiva.dto.EquipeResponse;
import br.com.motiva.service.EquipeManutencaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeManutencaoController {

    private final EquipeManutencaoService service;

    public EquipeManutencaoController(EquipeManutencaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EquipeResponse>> listar(
            @RequestParam(required = false) Boolean ativa) {
        return ResponseEntity.ok(service.listar(ativa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EquipeResponse> criar(@Valid @RequestBody EquipeRequest dados) {
        EquipeResponse criada = service.criar(dados);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipeRequest dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
