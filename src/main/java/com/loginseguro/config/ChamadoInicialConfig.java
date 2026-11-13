package com.loginseguro.config;

import com.loginseguro.entity.Chamado;
import com.loginseguro.repository.ChamadoRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChamadoInicialConfig implements CommandLineRunner {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (chamadoRepository.count() > 0) {
            return;
        }
        LocalDate hoje = LocalDate.now();
        chamadoRepository.save(novoChamado("Computador não liga", "Concluído", "Manutenção", "João", "Alberto",
                "Fonte de alimentação substituída", hoje.minusDays(5), hoje.minusDays(3)));
        chamadoRepository.save(novoChamado("Instalação de impressora", "Aberto", "Instalação", "Maria", "Carla",
                null, hoje.minusDays(1), null));
    }

    private Chamado novoChamado(String descricao, String status, String tipo, String tecnico, String usuario,
            String observacaoTecnica, LocalDate dataAbertura, LocalDate dataConclusao) {
        Chamado chamado = new Chamado();
        chamado.setDescricao(descricao);
        chamado.setStatus(status);
        chamado.setTipo(tipo);
        chamado.setTecnico(tecnico);
        chamado.setUsuario(usuario);
        chamado.setObservacaoTecnica(observacaoTecnica);
        chamado.setDataAbertura(dataAbertura);
        chamado.setDataConclusao(dataConclusao);
        return chamado;
    }
}
