package br.com.motiva.controller;

import br.com.motiva.dto.RelatorioResponse;
import br.com.motiva.service.RelatorioPrioridadeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioPrioridadeController {

    private final RelatorioPrioridadeService service;

    public RelatorioPrioridadeController(RelatorioPrioridadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RelatorioResponse> gerar() {
        RelatorioResponse gerado = service.gerar();
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(gerado.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(gerado);
    }

    @GetMapping
    public ResponseEntity<List<RelatorioResponse>> listarHistorico() {
        return ResponseEntity.ok(service.listarHistorico());
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<RelatorioResponse>> listarPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim) {
        return ResponseEntity.ok(service.listarPorPeriodo(inicio, fim));
    }
}
