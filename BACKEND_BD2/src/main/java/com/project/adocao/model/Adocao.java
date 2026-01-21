package com.project.adocao.model;

import java.time.LocalDate;
import java.util.Objects;

public class Adocao {

    private int idAdocao;
    private LocalDate dataAdocao;
    private String status;
    private int idUsuario;
    private int idAnimal;

    public Adocao() {
    }

    public Adocao(int idAdocao, LocalDate dataAdocao, String status, int idUsuario, int idAnimal) {
        this.idAdocao = idAdocao;
        this.dataAdocao = dataAdocao;
        this.status = status;
        this.idUsuario = idUsuario;
        this.idAnimal = idAnimal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdAdocao() {
        return idAdocao;
    }

    public void setIdAdocao(int idAdocao) {
        this.idAdocao = idAdocao;
    }

    public LocalDate getDataAdocao() {
        return dataAdocao;
    }

    public void setDataAdocao(LocalDate dataAdocao) {
        this.dataAdocao = dataAdocao;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adocao adocao = (Adocao) o;
        return idAdocao == adocao.idAdocao &&
                idUsuario == adocao.idUsuario &&
                idAnimal == adocao.idAnimal &&
                Objects.equals(status, adocao.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAdocao, idUsuario, idAnimal, status);
    }

    @Override
    public String toString() {
        return "Adocao{" +
                "idAdocao=" + idAdocao +
                ", dataAdocao=" + dataAdocao +
                ", status='" + status + '\'' +
                ", idUsuario=" + idUsuario +
                ", idAnimal=" + idAnimal +
                '}';
    }
}