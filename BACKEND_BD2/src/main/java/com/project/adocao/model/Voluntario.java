package com.project.adocao.model;

import java.util.Objects;

public class Voluntario {

    private int idVoluntario;
    private String nomeVoluntario;
    private String rg;
    private String cpf;
    private String atividade;
    private String diasDisponiveis;
    private int idUsuario;

    public Voluntario() {
    }

    public Voluntario(int idVoluntario, String nomeVoluntario, String rg, String cpf,
                      String atividade, String diasDisponiveis, int idUsuario) {
        this.idVoluntario = idVoluntario;
        this.nomeVoluntario = nomeVoluntario;
        this.rg = rg;
        this.cpf = cpf;
        this.atividade = atividade;
        this.diasDisponiveis = diasDisponiveis;
        this.idUsuario = idUsuario;
    }

    public int getIdVoluntario() {
        return idVoluntario;
    }

    public void setIdVoluntario(int idVoluntario) {
        this.idVoluntario = idVoluntario;
    }

    public String getNomeVoluntario() {
        return nomeVoluntario;
    }

    public void setNomeVoluntario(String nomeVoluntario) {
        this.nomeVoluntario = nomeVoluntario;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voluntario that = (Voluntario) o;
        return idVoluntario == that.idVoluntario &&
                idUsuario == that.idUsuario &&
                Objects.equals(nomeVoluntario, that.nomeVoluntario) &&
                Objects.equals(rg, that.rg) &&
                Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVoluntario, nomeVoluntario, rg, cpf, idUsuario);
    }

    @Override
    public String toString() {
        return "Voluntario{" +
                "idVoluntario=" + idVoluntario +
                ", nomeVoluntario='" + nomeVoluntario + '\'' +
                ", cpf='" + cpf + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
}