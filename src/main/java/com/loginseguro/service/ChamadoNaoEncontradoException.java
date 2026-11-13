package com.loginseguro.service;

public class ChamadoNaoEncontradoException extends RuntimeException {

    public ChamadoNaoEncontradoException(String id) {
        super("Chamado não encontrado com id: " + id);
    }
}
