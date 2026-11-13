package com.loginseguro.service;

import com.loginseguro.entity.Chamado;
import com.loginseguro.repository.ChamadoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChamadoService {

    public static final String CONCLUIDO = "Concluído";

    @Autowired
    private ChamadoRepository chamadoRepository;

    public Chamado criarChamado(Chamado chamado) {
        chamado.setId(null);
        return chamadoRepository.save(tratarDados(chamado));
    }

    public Chamado atualizarChamado(String id, Chamado chamadoAtualizado) {
        Chamado chamado = buscarPorId(id);
        chamado.setDescricao(chamadoAtualizado.getDescricao());
        chamado.setStatus(chamadoAtualizado.getStatus());
        chamado.setTipo(chamadoAtualizado.getTipo());
        chamado.setTecnico(chamadoAtualizado.getTecnico());
        chamado.setUsuario(chamadoAtualizado.getUsuario());
        chamado.setObservacaoTecnica(chamadoAtualizado.getObservacaoTecnica());
        chamado.setDataConclusao(chamadoAtualizado.getDataConclusao());
        return chamadoRepository.save(tratarDados(chamado));
    }

    public Chamado buscarPorId(String id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException(id));
    }

    public List<Chamado> listarChamados(LocalDate inicio, LocalDate fim, String status, String tipo, String tecnico) {
        return chamadoRepository.findAll().stream()
                .filter(c -> inicio == null || (c.getDataAbertura() != null && !c.getDataAbertura().isBefore(inicio)))
                .filter(c -> fim == null || (c.getDataAbertura() != null && !c.getDataAbertura().isAfter(fim)))
                .filter(c -> vazio(status) || status.equalsIgnoreCase(c.getStatus()))
                .filter(c -> vazio(tipo) || tipo.equalsIgnoreCase(c.getTipo()))
                .filter(c -> vazio(tecnico) || tecnico.equalsIgnoreCase(c.getTecnico()))
                .toList();
    }

    private Chamado tratarDados(Chamado chamado) {
        chamado.setDescricao(chamado.getDescricao().trim());
        chamado.setStatus(chamado.getStatus().trim());
        chamado.setTipo(chamado.getTipo().trim());
        if (chamado.getDataAbertura() == null) {
            chamado.setDataAbertura(LocalDate.now());
        }
        if (CONCLUIDO.equalsIgnoreCase(chamado.getStatus()) && chamado.getDataConclusao() == null) {
            chamado.setDataConclusao(LocalDate.now());
        }
        if (!CONCLUIDO.equalsIgnoreCase(chamado.getStatus())) {
            chamado.setDataConclusao(null);
        }
        return chamado;
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
