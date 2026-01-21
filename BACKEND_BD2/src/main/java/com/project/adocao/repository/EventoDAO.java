package com.project.adocao.repository;

import com.project.adocao.model.Evento;
import java.util.List;
import java.util.Optional;

public interface EventoDAO {
    void salvar(Evento evento);
    Optional<Evento> buscarPorId(int id);
    List<Evento> buscarTodos();
    void atualizar(Evento evento);
    void deletar(int id);
}