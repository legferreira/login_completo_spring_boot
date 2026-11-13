package com.loginseguro.controller;

import com.loginseguro.entity.Chamado;
import com.loginseguro.service.ChamadoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @GetMapping
    public List<Chamado> listarChamados(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String tecnico) {
        return chamadoService.listarChamados(inicio, fim, status, tipo, tecnico);
    }

    @GetMapping("/{id}")
    public Chamado buscarChamado(@PathVariable String id) {
        return chamadoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Chamado criarChamado(@Valid @RequestBody Chamado chamado) {
        return chamadoService.criarChamado(chamado);
    }

    @PutMapping("/{id}")
    public Chamado atualizarChamado(@PathVariable String id, @Valid @RequestBody Chamado chamado) {
        return chamadoService.atualizarChamado(id, chamado);
    }
}
