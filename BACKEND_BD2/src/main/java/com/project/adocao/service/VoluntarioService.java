package com.project.adocao.service;

import com.project.adocao.dto.VoluntarioAtividadeDTO;
import com.project.adocao.model.Voluntario;
import com.project.adocao.repository.VoluntarioDAO;
import com.project.adocao.repository.impl.VoluntarioDAOImpl;

import java.util.List;
import java.util.Optional;

public class VoluntarioService {

    private VoluntarioDAO voluntarioDAO;

    public VoluntarioService() {
        this.voluntarioDAO = new VoluntarioDAOImpl();
    }

    public VoluntarioService(VoluntarioDAO voluntarioDAO) {
        this.voluntarioDAO = voluntarioDAO;
    }

    public Voluntario criarVoluntario(Voluntario voluntario) {
        if (voluntario.getCpf() == null || (voluntario.getCpf().length() != 11)) {
            throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos.");
        }
        if (voluntario.getRg() == null || (voluntario.getRg().length() != 9)) {
            throw new IllegalArgumentException("RG inválido. Deve conter 9 dígitos.");
        }
        voluntarioDAO.salvar(voluntario);
        return voluntario;
    }

    public Optional<Voluntario> buscarVoluntarioPorId(int id) {
        return voluntarioDAO.buscarPorId(id);
    }

    public List<Voluntario> listarTodosVoluntarios() {
        return voluntarioDAO.buscarTodos();
    }

    public void atualizarVoluntario(Voluntario voluntario) {
        if (!voluntarioDAO.buscarPorId(voluntario.getIdVoluntario()).isPresent()) {
            throw new RuntimeException("Voluntário não encontrado para atualização.");
        }
        if (voluntario.getCpf() == null || (voluntario.getCpf().length() != 11)) {
            throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos.");
        }
        if (voluntario.getRg() == null || (voluntario.getRg().length() != 9)) {
            throw new IllegalArgumentException("RG inválido. Deve conter 9 dígitos.");
        }
        voluntarioDAO.atualizar(voluntario);
    }

    public void deletarVoluntario(int id) {
        voluntarioDAO.deletar(id);
    }

    public Optional<VoluntarioAtividadeDTO> getAtividadeVoluntario(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do voluntário não pode ser vazio.");
        }
        return voluntarioDAO.buscarAtividadePorNome(nome);
    }
}