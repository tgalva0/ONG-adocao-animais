package com.project.adocao.dto;

import java.util.Objects;

public class VoluntarioAtividadeDTO {

    private String atividade;
    private String diasDisponiveis;

    public VoluntarioAtividadeDTO() {
    }

    public VoluntarioAtividadeDTO(String atividade, String diasDisponiveis) {
        this.atividade = atividade;
        this.diasDisponiveis = diasDisponiveis;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getDiasDisponiveis() {
        return diasDisponiveis;
    }

    public void setDiasDisponiveis(String diasDisponiveis) {
        this.diasDisponiveis = diasDisponiveis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoluntarioAtividadeDTO that = (VoluntarioAtividadeDTO) o;
        return Objects.equals(atividade, that.atividade) &&
                Objects.equals(diasDisponiveis, that.diasDisponiveis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atividade, diasDisponiveis);
    }

    @Override
    public String toString() {
        return "VoluntarioAtividadeDTO{" +
                "atividade='" + atividade + '\'' +
                ", diasDisponiveis='" + diasDisponiveis + '\'' +
                '}';
    }
}