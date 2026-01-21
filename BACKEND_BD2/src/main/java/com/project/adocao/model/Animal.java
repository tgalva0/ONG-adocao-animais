package com.project.adocao.model;

import java.util.Objects;

public class Animal {
    private int idAnimal;
    private String nomeAnimal;
    private int peso;
    private String porte;
    private String raca;
    private boolean status;
    private String especie;
    private int idade;
    private String sexo;
    private boolean vermifugado;
    private boolean vacinadoGripe;
    private String doenca;

    public Animal() {}

    public Animal(int idAnimal, String nomeAnimal, int peso, String porte, String raca,
                  boolean status, String especie, int idade, String sexo,
                  boolean vermifugado, boolean vacinadoGripe, String doenca) {
        this.idAnimal = idAnimal;
        this.nomeAnimal = nomeAnimal;
        this.peso = peso;
        this.porte = porte;
        this.raca = raca;
        this.status = status;
        this.especie = especie;
        this.idade = idade;
        this.sexo = sexo;
        this.vermifugado = vermifugado;
        this.vacinadoGripe = vacinadoGripe;
        this.doenca = doenca;
    }

    public int getIdAnimal() { return idAnimal; }
    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }
    public String getNomeAnimal() { return nomeAnimal; }
    public void setNomeAnimal(String nomeAnimal) { this.nomeAnimal = nomeAnimal; }
    public int getPeso() { return peso; }
    public void setPeso(int peso) { this.peso = peso; }
    public String getPorte() { return porte; }
    public void setPorte(String porte) { this.porte = porte; }
    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public boolean isVermifugado() { return vermifugado; }
    public void setVermifugado(boolean vermifugado) { this.vermifugado = vermifugado; }
    public boolean isVacinadoGripe() { return vacinadoGripe; }
    public void setVacinadoGripe(boolean vacinadoGripe) { this.vacinadoGripe = vacinadoGripe; }
    public String getDoenca() { return doenca; }
    public void setDoenca(String doenca) { this.doenca = doenca; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return idAnimal == animal.idAnimal &&
                Objects.equals(nomeAnimal, animal.nomeAnimal);
    }
    @Override
    public int hashCode() { return Objects.hash(idAnimal, nomeAnimal); }
    @Override
    public String toString() { return "Animal{idAnimal=" + idAnimal + ", nomeAnimal='" + nomeAnimal + "\'}"; }
}