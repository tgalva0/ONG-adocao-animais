package com.project.adocao.model;

import java.time.LocalDate;
import java.util.Objects;

public class Portal {

    private int idPortal;
    private LocalDate dataRegistro;
    private double valorRacao;
    private double valorAgua;
    private double valorVacina;
    private int idUsuario;

    public Portal() {
    }

    public Portal(int idPortal, LocalDate dataRegistro, double valorRacao, double valorAgua, double valorVacina, int idUsuario) {
        this.idPortal = idPortal;
        this.dataRegistro = dataRegistro;
        this.valorRacao = valorRacao;
        this.valorAgua = valorAgua;
        this.valorVacina = valorVacina;
        this.idUsuario = idUsuario;
    }

    public int getIdPortal() { return idPortal; }
    public void setIdPortal(int idPortal) { this.idPortal = idPortal; }
    public LocalDate getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDate dataRegistro) { this.dataRegistro = dataRegistro; }

    public double getValorRacao() { return valorRacao; }
    public void setValorRacao(double valorRacao) { this.valorRacao = valorRacao; }
    public double getValorAgua() { return valorAgua; }
    public void setValorAgua(double valorAgua) { this.valorAgua = valorAgua; }
    public double getValorVacina() { return valorVacina; }
    public void setValorVacina(double valorVacina) { this.valorVacina = valorVacina; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portal portal = (Portal) o;
        return idPortal == portal.idPortal &&
                Double.compare(portal.valorRacao, valorRacao) == 0 &&
                Double.compare(portal.valorAgua, valorAgua) == 0 &&
                Double.compare(portal.valorVacina, valorVacina) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPortal, valorRacao, valorAgua, valorVacina);
    }

    @Override
    public String toString() {
        return "Portal{" +
                "idPortal=" + idPortal +
                ", dataRegistro=" + dataRegistro +
                ", valorRacao=" + valorRacao +
                '}';
    }
}