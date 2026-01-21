package com.project.adocao.service;

import com.project.adocao.model.Evento;
import com.project.adocao.repository.EventoDAO;
import com.project.adocao.repository.impl.EventoDAOImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EventoService {

    private EventoDAO eventoDAO;

    public EventoService() {
        this.eventoDAO = new EventoDAOImpl();
    }

    public EventoService(EventoDAO eventoDAO) {
        this.eventoDAO = eventoDAO;
    }

    public void criarEvento(Evento evento) {
        if (evento.getNomeEvento() == null || evento.getNomeEvento().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do evento é obrigatório.");
        }
        if (evento.getDataEvento() == null) {
            throw new IllegalArgumentException("Data do evento é obrigatória.");
        }
        if (evento.getDataEvento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data do evento não pode ser no passado.");
        }

        eventoDAO.salvar(evento);
    }

    public Optional<Evento> buscarEventoPorId(int id) {
        return eventoDAO.buscarPorId(id);
    }

    public List<Evento> listarTodosEventos() {
        return eventoDAO.buscarTodos();
    }

    public void atualizarEvento(Evento evento) {
        if (!eventoDAO.buscarPorId(evento.getIdEvento()).isPresent()) {
            throw new RuntimeException("Evento não encontrado para atualização.");
        }
        if (evento.getNomeEvento() == null || evento.getNomeEvento().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do evento é obrigatório.");
        }
        if (evento.getDataEvento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data do evento não pode ser no passado.");
        }
        eventoDAO.atualizar(evento);
    }

    public void deletarEvento(int id) {
        eventoDAO.deletar(id);
    }
}