package br.com.motiva.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " de id " + id + " não encontrado(a).");
    }
}
