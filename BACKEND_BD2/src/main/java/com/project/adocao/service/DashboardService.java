package com.project.adocao.service;

import com.project.adocao.dto.DashboardStatsDTO;
import com.project.adocao.repository.AnimalDAO;
import com.project.adocao.repository.AdocaoDAO;
import com.project.adocao.repository.UsuarioDAO;
import com.project.adocao.repository.impl.AnimalDAOImpl;
import com.project.adocao.repository.impl.AdocaoDAOImpl;
import com.project.adocao.repository.impl.UsuarioDAOImpl;

public class DashboardService {

    private final AnimalDAO animalDAO;
    private final AdocaoDAO adocaoDAO;
    private final UsuarioDAO usuarioDAO;

    public DashboardService() {
        this.animalDAO = new AnimalDAOImpl();
        this.adocaoDAO = new AdocaoDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public DashboardService(AnimalDAO animalDAO, AdocaoDAO adocaoDAO, UsuarioDAO usuarioDAO) {
        this.animalDAO = animalDAO;
        this.adocaoDAO = adocaoDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public DashboardStatsDTO getEstatisticasGerais() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalAnimais(animalDAO.contarAnimaisDisponiveis());
        stats.setTotalAdocoes(adocaoDAO.contarTotalAdocoes());
        stats.setTotalUsuarios(usuarioDAO.contarTotalUsuarios());

        return stats;
    }
}