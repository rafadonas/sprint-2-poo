package br.com.motiva.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiErro(
        LocalDateTime instante,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos) {
}
