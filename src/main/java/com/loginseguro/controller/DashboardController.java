package com.loginseguro.controller;

import com.loginseguro.entity.Chamado;
import com.loginseguro.service.ChamadoService;
import com.loginseguro.service.DashboardService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private ChamadoService chamadoService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/indicadores")
    public Map<String, Object> indicadores(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String tecnico) {
        return dashboardService.indicadores(filtrar(inicio, fim, status, tipo, tecnico));
    }

    @GetMapping("/status")
    public Map<String, Long> porStatus(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String tecnico) {
        return dashboardService.porStatus(filtrar(inicio, fim, status, tipo, tecnico));
    }

    @GetMapping("/tipos")
    public Map<String, Long> rankingTipos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String tecnico) {
        return dashboardService.rankingTipos(filtrar(inicio, fim, status, tipo, tecnico));
    }

    @GetMapping("/atendidos-por-periodo")
    public Map<String, Long> atendidosPorPeriodo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String tecnico) {
        return dashboardService.atendidosPorPeriodo(filtrar(inicio, fim, status, tipo, tecnico));
    }

    private List<Chamado> filtrar(LocalDate inicio, LocalDate fim, String status, String tipo, String tecnico) {
        return chamadoService.listarChamados(inicio, fim, status, tipo, tecnico);
    }
}
