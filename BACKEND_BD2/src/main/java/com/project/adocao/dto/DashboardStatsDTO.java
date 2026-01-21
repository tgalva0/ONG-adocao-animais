package com.project.adocao.dto;

import java.util.Objects;

public class DashboardStatsDTO {

    private int totalAnimais;
    private int totalAdocoes;
    private int totalUsuarios;

    public DashboardStatsDTO() {
    }

    public DashboardStatsDTO(int totalAnimais, int totalAdocoes, int totalUsuarios) {
        this.totalAnimais = totalAnimais;
        this.totalAdocoes = totalAdocoes;
        this.totalUsuarios = totalUsuarios;
    }

    public int getTotalAnimais() {
        return totalAnimais;
    }

    public void setTotalAnimais(int totalAnimais) {
        this.totalAnimais = totalAnimais;
    }

    public int getTotalAdocoes() {
        return totalAdocoes;
    }

    public void setTotalAdocoes(int totalAdocoes) {
        this.totalAdocoes = totalAdocoes;
    }

    public int getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(int totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardStatsDTO that = (DashboardStatsDTO) o;
        return totalAnimais == that.totalAnimais &&
                totalAdocoes == that.totalAdocoes &&
                totalUsuarios == that.totalUsuarios;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalAnimais, totalAdocoes, totalUsuarios);
    }

    @Override
    public String toString() {
        return "DashboardStatsDTO{" +
                "totalAnimais=" + totalAnimais +
                ", totalAdocoes=" + totalAdocoes +
                ", totalUsuarios=" + totalUsuarios +
                '}';
    }
}