package com.project.adocao.service;

import com.project.adocao.model.Animal;
import com.project.adocao.repository.AnimalDAO;
import com.project.adocao.repository.impl.AnimalDAOImpl;

import java.util.List;
import java.util.Optional;

public class AnimalService {

    private AnimalDAO animalDAO;

    public AnimalService() {
        this.animalDAO = new AnimalDAOImpl();
    }

    public AnimalService(AnimalDAO animalDAO) {
        this.animalDAO = animalDAO;
    }

    public void criarAnimal(Animal animal) {
        if (animal.getNomeAnimal() == null || animal.getNomeAnimal().isEmpty()) {
            throw new IllegalArgumentException("Nome do animal é obrigatório");
        }
        animalDAO.salvar(animal);
    }

    public Optional<Animal> buscarAnimalPorId(int id) {
        return animalDAO.buscarPorId(id);
    }

    public List<Animal> listarTodosAnimais() {
        return animalDAO.buscarTodos();
    }

    public void atualizarAnimal(Animal animal) {
        if (!animalDAO.buscarPorId(animal.getIdAnimal()).isPresent()) {
            throw new RuntimeException("Animal não encontrado para atualização");
        }
        animalDAO.atualizar(animal);
    }

    public List<Animal> listarAnimaisDisponiveis() {
        return animalDAO.buscarAnimaisDisponiveis();
    }

    public List<Animal> buscarAnimaisPorFiltro(String nome, String especie) {
        // Note: Esta chamada pressupõe que o DAO foi atualizado com a lógica de filtros
        return animalDAO.buscarPorFiltros(nome, especie);
    }

    public void deletarAnimal(int id) {
        animalDAO.deletar(id);
    }
}