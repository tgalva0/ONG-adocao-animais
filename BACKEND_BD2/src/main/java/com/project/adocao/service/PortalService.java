package com.project.adocao.service;

import com.project.adocao.model.Portal;
import com.project.adocao.repository.PortalDAO;
import com.project.adocao.repository.impl.PortalDAOImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PortalService {

    private PortalDAO portalDAO;

    public PortalService() {
        this.portalDAO = new PortalDAOImpl();
    }

    public PortalService(PortalDAO portalDAO) {
        this.portalDAO = portalDAO;
    }

    private void validarValores(Portal portal) {
        if (portal.getValorRacao() < 0 || portal.getValorAgua() < 0 || portal.getValorVacina() < 0) {
            throw new IllegalArgumentException("Os valores de gasto (Ração, Água, Vacina) não podem ser negativos.");
        }
    }

    public void criarRegistroPortal(Portal portal) {
        validarValores(portal);

        if (portal.getDataRegistro() == null) {
            portal.setDataRegistro(LocalDate.now());
        }

        portalDAO.salvar(portal);
    }

    public Optional<Portal> buscarPortalPorId(int id) {
        return portalDAO.buscarPorId(id);
    }

    public List<Portal> listarTodosRegistros() {
        return portalDAO.buscarTodos();
    }

    public void atualizarPortal(Portal portal) {
        if (!portalDAO.buscarPorId(portal.getIdPortal()).isPresent()) {
            throw new RuntimeException("Registro não encontrado para atualização.");
        }
        validarValores(portal);
        portalDAO.atualizar(portal);
    }

    public void deletarPortal(int id) {
        portalDAO.deletar(id);
    }
}