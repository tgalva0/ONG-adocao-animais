package com.project.adocao.service;

import com.project.adocao.model.Evento;
import com.project.adocao.repository.EventoDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoDAO eventoDAOMock;

    @InjectMocks
    private EventoService eventoService;

    private Evento eventoValido;

    @BeforeEach
    void setUp() {
        eventoValido = new Evento();
        eventoValido.setIdEvento(1);
        eventoValido.setNomeEvento("Feira de Adoção");
        eventoValido.setDescricao("Evento no parque");
        eventoValido.setDataEvento(LocalDate.now().plusDays(10));
        eventoValido.setIdUsuario(1);
    }

    @Test
    void deveCriarEventoComSucesso() {
        eventoService.criarEvento(eventoValido);
        verify(eventoDAOMock, times(1)).salvar(eventoValido);
    }

    @Test
    void naoDeveCriarEventoNoPassado() {
        eventoValido.setDataEvento(LocalDate.now().minusDays(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            eventoService.criarEvento(eventoValido);
        });

        assertEquals("A data do evento não pode ser no passado.", exception.getMessage());
        verify(eventoDAOMock, never()).salvar(any());
    }

    @Test
    void naoDeveCriarEventoSemNome() {
        eventoValido.setNomeEvento("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            eventoService.criarEvento(eventoValido);
        });

        assertEquals("Nome do evento é obrigatório.", exception.getMessage());
        verify(eventoDAOMock, never()).salvar(any());
    }
}