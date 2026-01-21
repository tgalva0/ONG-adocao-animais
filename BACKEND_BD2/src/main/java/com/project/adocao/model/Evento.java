package com.project.adocao.model;

import java.time.LocalDate;
import java.util.Objects;

public class Evento {

    private int idEvento;
    private String nomeEvento;
    private LocalDate dataEvento;
    private String descricao;
    private int idUsuario;


    public Evento() {
    }

    public Evento(int idEvento, String nomeEvento, LocalDate dataEvento, String descricao, int idUsuario) {
        this.idEvento = idEvento;
        this.nomeEvento = nomeEvento;
        this.dataEvento = dataEvento;
        this.descricao = descricao;
        this.idUsuario = idUsuario;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        Evento evento = (Evento) o;
        return idEvento == evento.idEvento &&
                idUsuario == evento.idUsuario &&
                Objects.equals(nomeEvento, evento.nomeEvento) &&
                Objects.equals(dataEvento, evento.dataEvento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvento, nomeEvento, dataEvento, idUsuario);
    }

    @Override
    public String toString() {
        return "Evento{" +
                "idEvento=" + idEvento +
                ", nomeEvento='" + nomeEvento + '\'' +
                ", dataEvento=" + dataEvento +
                ", idUsuario=" + idUsuario +
                '}';
    }
}