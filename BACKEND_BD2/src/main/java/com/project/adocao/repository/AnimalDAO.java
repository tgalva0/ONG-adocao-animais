package com.project.adocao.repository;

import com.project.adocao.model.Animal;
import java.util.List;
import java.util.Optional;

public interface AnimalDAO {
    void salvar(Animal animal);
    Optional<Animal> buscarPorId(int id);
    List<Animal> buscarTodos();
    void atualizar(Animal animal);
    void deletar(int id);
    List<Animal> buscarPorFiltros(String nome, String especie);
    int contarAnimaisDisponiveis();
    List<Animal> buscarAnimaisDisponiveis();
}