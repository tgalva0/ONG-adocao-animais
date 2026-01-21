package com.project.adocao.model;

import java.util.Objects;

public class Usuario {

    private int idUsuario;
    private String nome;
    private String senha;
    private String email;
    private String tipoUsuario;
    private String cpf;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nome, String senha, String email, String tipoUsuario, String cpf) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.cpf = cpf;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return idUsuario == usuario.idUsuario &&
                Objects.equals(cpf, usuario.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, cpf);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}