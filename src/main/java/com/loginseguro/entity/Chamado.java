package com.loginseguro.entity;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chamados")
public class Chamado {

    @Id
    private String id;

    @NotBlank(message = "Informe a descrição")
    private String descricao;

    @NotBlank(message = "Informe o status")
    private String status;

    @NotBlank(message = "Informe o tipo")
    private String tipo;

    private String tecnico;
    private String usuario;
    private String observacaoTecnica;
    private LocalDate dataAbertura;
    private LocalDate dataConclusao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getObservacaoTecnica() {
        return observacaoTecnica;
    }

    public void setObservacaoTecnica(String observacaoTecnica) {
        this.observacaoTecnica = observacaoTecnica;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
}
