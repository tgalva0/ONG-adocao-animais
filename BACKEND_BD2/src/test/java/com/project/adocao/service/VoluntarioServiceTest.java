package com.project.adocao.service;

import com.project.adocao.dto.VoluntarioAtividadeDTO;
import com.project.adocao.model.Voluntario;
import com.project.adocao.repository.VoluntarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoluntarioServiceTest {

    @Mock
    private VoluntarioDAO voluntarioDAOMock;

    @InjectMocks
    private VoluntarioService voluntarioService;

    private Voluntario voluntarioValido;

    @BeforeEach
    void setUp() {
        voluntarioValido = new Voluntario();
        voluntarioValido.setIdVoluntario(1);
        voluntarioValido.setNomeVoluntario("Voluntario Teste");
        voluntarioValido.setCpf("11122233344");
        voluntarioValido.setRg("123456789");
        voluntarioValido.setAtividade("Limpeza");
        voluntarioValido.setDiasDisponiveis("Seg, Qua");
        voluntarioValido.setIdUsuario(1);
    }

    @Test
    void deveCriarVoluntarioComSucesso() {
        voluntarioService.criarVoluntario(voluntarioValido);
        verify(voluntarioDAOMock, times(1)).salvar(voluntarioValido);
    }

    @Test
    void naoDeveCriarVoluntarioComCpfInvalido() {
        voluntarioValido.setCpf("123");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            voluntarioService.criarVoluntario(voluntarioValido);
        });

        assertEquals("CPF inválido. Deve conter 11 dígitos.", exception.getMessage());
        verify(voluntarioDAOMock, never()).salvar(any());
    }

    @Test
    void naoDeveCriarVoluntarioComRgInvalido() {
        voluntarioValido.setRg("123");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            voluntarioService.criarVoluntario(voluntarioValido);
        });

        assertEquals("RG inválido. Deve conter 9 dígitos.", exception.getMessage());
        verify(voluntarioDAOMock, never()).salvar(any());
    }

    @Test
    void deveBuscarAtividadeVoluntario() {
        String nome = "Voluntario Teste";
        VoluntarioAtividadeDTO dtoEsperado = new VoluntarioAtividadeDTO("Limpeza", "Seg, Qua");

        when(voluntarioDAOMock.buscarAtividadePorNome(nome)).thenReturn(Optional.of(dtoEsperado));

        Optional<VoluntarioAtividadeDTO> resultado = voluntarioService.getAtividadeVoluntario(nome);

        assertTrue(resultado.isPresent());
        assertEquals("Limpeza", resultado.get().getAtividade());
        assertEquals("Seg, Qua", resultado.get().getDiasDisponiveis());
        verify(voluntarioDAOMock, times(1)).buscarAtividadePorNome(nome);
    }

    @Test
    void deveRetornarVazioSeAtividadeNaoEncontrada() {
        String nome = "Inexistente";
        when(voluntarioDAOMock.buscarAtividadePorNome(nome)).thenReturn(Optional.empty());

        Optional<VoluntarioAtividadeDTO> resultado = voluntarioService.getAtividadeVoluntario(nome);

        assertFalse(resultado.isPresent());
    }

    @Test
    void naoDeveBuscarAtividadeComNomeVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            voluntarioService.getAtividadeVoluntario("");
        });

        assertEquals("Nome do voluntário não pode ser vazio.", exception.getMessage());
        verify(voluntarioDAOMock, never()).buscarAtividadePorNome(anyString());
    }
}