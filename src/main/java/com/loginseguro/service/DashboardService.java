package com.loginseguro.service;

import com.loginseguro.entity.Chamado;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public Map<String, Object> indicadores(List<Chamado> chamados) {
        List<Chamado> concluidos = concluidos(chamados);
        double tempoMedio = concluidos.stream()
                .mapToLong(c -> ChronoUnit.DAYS.between(c.getDataAbertura(), c.getDataConclusao()))
                .average()
                .orElse(0);

        Map<String, Object> indicadores = new LinkedHashMap<>();
        indicadores.put("total", chamados.size());
        indicadores.put("abertos", contarPorStatus(chamados, "Aberto"));
        indicadores.put("emAndamento", contarPorStatus(chamados, "Em andamento"));
        indicadores.put("concluidos", concluidos.size());
        indicadores.put("tempoMedioDias", Math.round(tempoMedio * 10) / 10.0);
        return indicadores;
    }

    public Map<String, Long> porStatus(List<Chamado> chamados) {
        return chamados.stream()
                .collect(Collectors.groupingBy(Chamado::getStatus, TreeMap::new, Collectors.counting()));
    }

    public Map<String, Long> rankingTipos(List<Chamado> chamados) {
        return chamados.stream()
                .collect(Collectors.groupingBy(Chamado::getTipo, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    public Map<String, Long> atendidosPorPeriodo(List<Chamado> chamados) {
        return concluidos(chamados).stream()
                .collect(Collectors.groupingBy(c -> c.getDataConclusao().toString(), TreeMap::new, Collectors.counting()));
    }

    private List<Chamado> concluidos(List<Chamado> chamados) {
        return chamados.stream()
                .filter(c -> ChamadoService.CONCLUIDO.equalsIgnoreCase(c.getStatus()) && c.getDataConclusao() != null)
                .toList();
    }

    private long contarPorStatus(List<Chamado> chamados, String status) {
        return chamados.stream().filter(c -> status.equalsIgnoreCase(c.getStatus())).count();
    }
}
