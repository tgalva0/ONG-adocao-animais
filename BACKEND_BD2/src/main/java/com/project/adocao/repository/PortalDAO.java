package com.project.adocao.repository;

import com.project.adocao.model.Portal;
import java.util.List;
import java.util.Optional;

public interface PortalDAO {
    void salvar(Portal portal);
    Optional<Portal> buscarPorId(int id);
    List<Portal> buscarTodos();
    void atualizar(Portal portal);
    void deletar(int id);
}