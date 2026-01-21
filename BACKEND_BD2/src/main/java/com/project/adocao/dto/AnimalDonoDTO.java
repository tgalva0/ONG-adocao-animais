package com.project.adocao.dto;

import java.util.Objects;

public class AnimalDonoDTO {

    private String nomeUsuario;
    private String emailUsuario;
    private String tipoUsuario;
    private String nomeAnimal;
    private String racaAnimal;
    private String doencaAnimal;
    private boolean vacinadoGripe;
    private boolean vermifugado;

    public AnimalDonoDTO() {
    }

    public AnimalDonoDTO(String nomeUsuario, String emailUsuario, String tipoUsuario,
                         String nomeAnimal, String racaAnimal, String doencaAnimal,
                         boolean vacinadoGripe, boolean vermifugado) {
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.tipoUsuario = tipoUsuario;
        this.nomeAnimal = nomeAnimal;
        this.racaAnimal = racaAnimal;
        this.doencaAnimal = doencaAnimal;
        this.vacinadoGripe = vacinadoGripe;
        this.vermifugado = vermifugado;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getRacaAnimal() {
        return racaAnimal;
    }

    public void setRacaAnimal(String racaAnimal) {
        this.racaAnimal = racaAnimal;
    }

    public String getDoencaAnimal() {
        return doencaAnimal;
    }

    public void setDoencaAnimal(String doencaAnimal) {
        this.doencaAnimal = doencaAnimal;
    }

    public boolean isVacinadoGripe() {
        return vacinadoGripe;
    }

    public void setVacinadoGripe(boolean vacinadoGripe) {
        this.vacinadoGripe = vacinadoGripe;
    }

    public boolean isVermifugado() {
        return vermifugado;
    }

    public void setVermifugado(boolean vermifugado) {
        this.vermifugado = vermifugado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalDonoDTO that = (AnimalDonoDTO) o;
        return vacinadoGripe == that.vacinadoGripe &&
                vermifugado == that.vermifugado &&
                Objects.equals(nomeUsuario, that.nomeUsuario) &&
                Objects.equals(emailUsuario, that.emailUsuario) &&
                Objects.equals(tipoUsuario, that.tipoUsuario) &&
                Objects.equals(nomeAnimal, that.nomeAnimal) &&
                Objects.equals(racaAnimal, that.racaAnimal) &&
                Objects.equals(doencaAnimal, that.doencaAnimal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeUsuario, emailUsuario, tipoUsuario, nomeAnimal,
                racaAnimal, doencaAnimal, vacinadoGripe, vermifugado);
    }

    @Override
    public String toString() {
        return "AnimalDonoDTO{" +
                "nomeUsuario='" + nomeUsuario + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", nomeAnimal='" + nomeAnimal + '\'' +
                ", racaAnimal='" + racaAnimal + '\'' +
                '}';
    }
}