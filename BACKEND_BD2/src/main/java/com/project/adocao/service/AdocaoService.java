package com.project.adocao.service;

import com.project.adocao.model.Adocao;
import com.project.adocao.repository.AdocaoDAO;
import com.project.adocao.repository.impl.AdocaoDAOImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AdocaoService {

    private AdocaoDAO adocaoDAO;

    public AdocaoService() {
        this.adocaoDAO = new AdocaoDAOImpl();
    }

    public AdocaoService(AdocaoDAO adocaoDAO) {
        this.adocaoDAO = adocaoDAO;
    }

    public void registrarAdocao(Adocao adocao) {
        if (adocao.getIdUsuario() <= 0) throw new IllegalArgumentException("ID do usuário inválido.");
        if (adocao.getIdAnimal() <= 0) throw new IllegalArgumentException("ID do animal inválido.");

        if (adocao.getDataAdocao() == null) {
            adocao.setDataAdocao(LocalDate.now());
        }

        // 🛠️ CORREÇÃO: Reintroduzindo a validação de data futura que o teste exige
        if (adocao.getDataAdocao().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de adoção não pode ser no futuro.");
        }

        // Regra: Nova adoção começa sempre "Em Andamento" se não for especificado
        if (adocao.getStatus() == null || adocao.getStatus().isEmpty()) {
            adocao.setStatus("Em Andamento");
        }

        adocaoDAO.salvar(adocao);
    }

    public Optional<Adocao> buscarAdocaoPorId(int id) {
        return adocaoDAO.buscarPorId(id);
    }

    public List<Adocao> listarTodasAdocoes() {
        return adocaoDAO.buscarTodos();
    }

    public List<Adocao> listarAdocoesPorUsuario(int idUsuario) {
        return adocaoDAO.buscarPorUsuario(idUsuario);
    }

    public void atualizarAdocao(Adocao adocao) {
        if (!adocaoDAO.buscarPorId(adocao.getIdAdocao()).isPresent()) {
            throw new RuntimeException("Registro de adoção não encontrado.");
        }

        // 🛠️ CORREÇÃO: Validação também na atualização
        if (adocao.getDataAdocao() != null && adocao.getDataAdocao().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de adoção não pode ser no futuro.");
        }

        // Validação simples de status
        String s = adocao.getStatus();
        if (s != null && !s.equals("Em Andamento") && !s.equals("Deferido") && !s.equals("Indeferido")) {
            throw new IllegalArgumentException("Status inválido. Use: 'Em Andamento', 'Deferido' ou 'Indeferido'.");
        }

        adocaoDAO.atualizar(adocao);
    }

    public void deletarAdocao(int id) {
        adocaoDAO.deletar(id);
    }
}