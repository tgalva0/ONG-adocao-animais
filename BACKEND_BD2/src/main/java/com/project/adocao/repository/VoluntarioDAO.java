package com.project.adocao.repository;

import com.project.adocao.dto.VoluntarioAtividadeDTO;
import com.project.adocao.model.Voluntario;

import java.util.List;
import java.util.Optional;

public interface VoluntarioDAO {

    void salvar(Voluntario voluntario);
    Optional<Voluntario> buscarPorId(int id);
    List<Voluntario> buscarTodos();
    void atualizar(Voluntario voluntario);
    void deletar(int id);

    Optional<VoluntarioAtividadeDTO> buscarAtividadePorNome(String nomeVoluntario);
}