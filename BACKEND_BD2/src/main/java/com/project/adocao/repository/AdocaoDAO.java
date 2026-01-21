package com.project.adocao.repository;

import com.project.adocao.model.Adocao;
import com.project.adocao.model.Animal;

import java.util.List;
import java.util.Optional;

public interface AdocaoDAO {
    void salvar(Adocao adocao);
    Optional<Adocao> buscarPorId(int id);
    List<Adocao> buscarTodos();
    void atualizar(Adocao adocao);
    void deletar(int id);
    int contarTotalAdocoes();
    List<Adocao> buscarPorUsuario(int idUsuario);
}