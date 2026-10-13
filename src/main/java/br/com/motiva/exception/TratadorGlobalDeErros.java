package br.com.motiva.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class TratadorGlobalDeErros {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErro> tratarNaoEncontrado(
            RecursoNaoEncontradoException excecao,
            HttpServletRequest requisicao) {
        return resposta(HttpStatus.NOT_FOUND, excecao.getMessage(), requisicao, Map.of());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiErro> tratarRegraNegocio(
            RegraNegocioException excecao,
            HttpServletRequest requisicao) {
        return resposta(HttpStatus.BAD_REQUEST, excecao.getMessage(), requisicao, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErro> tratarValidacao(
            MethodArgumentNotValidException excecao,
            HttpServletRequest requisicao) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : excecao.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Existem campos inválidos na requisição.",
                requisicao,
                campos);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiErro> tratarRequisicaoInvalida(
            Exception excecao,
            HttpServletRequest requisicao) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "A requisição contém um valor ausente ou inválido.",
                requisicao,
                Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErro> tratarIntegridade(
            DataIntegrityViolationException excecao,
            HttpServletRequest requisicao) {
        return resposta(
                HttpStatus.CONFLICT,
                "A operação viola um relacionamento ou restrição do banco de dados.",
                requisicao,
                Map.of());
    }

    private ResponseEntity<ApiErro> resposta(
            HttpStatus status,
            String mensagem,
            HttpServletRequest requisicao,
            Map<String, String> campos) {
        ApiErro erro = new ApiErro(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                requisicao.getRequestURI(),
                campos);
        return ResponseEntity.status(status).body(erro);
    }
}
