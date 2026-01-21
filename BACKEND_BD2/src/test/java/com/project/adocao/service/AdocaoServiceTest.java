package com.project.adocao.service;

import com.project.adocao.model.Adocao;
import com.project.adocao.repository.AdocaoDAO;
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
class AdocaoServiceTest {

    @Mock
    private AdocaoDAO adocaoDAOMock;

    @InjectMocks
    private AdocaoService adocaoService;

    private Adocao adocaoValida;

    @BeforeEach
    void setUp() {
        adocaoValida = new Adocao();
        adocaoValida.setIdAdocao(1);
        adocaoValida.setIdUsuario(10);
        adocaoValida.setIdAnimal(5);
        adocaoValida.setDataAdocao(LocalDate.now());
    }

    @Test
    void deveRegistrarAdocaoComSucesso() {
        adocaoService.registrarAdocao(adocaoValida);
        verify(adocaoDAOMock, times(1)).salvar(adocaoValida);
    }

    @Test
    void deveUsarDataAtualSeDataForNula() {
        Adocao adocaoSemData = new Adocao();
        adocaoSemData.setIdUsuario(1);
        adocaoSemData.setIdAnimal(1);
        adocaoSemData.setDataAdocao(null);

        adocaoService.registrarAdocao(adocaoSemData);

        assertNotNull(adocaoSemData.getDataAdocao());
        assertEquals(LocalDate.now(), adocaoSemData.getDataAdocao());
        verify(adocaoDAOMock, times(1)).salvar(adocaoSemData);
    }

    @Test
    void naoDeveRegistrarAdocaoNoFuturo() {
        adocaoValida.setDataAdocao(LocalDate.now().plusDays(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adocaoService.registrarAdocao(adocaoValida);
        });

        assertEquals("A data de adoção não pode ser no futuro.", exception.getMessage());
        verify(adocaoDAOMock, never()).salvar(any());
    }

    @Test
    void naoDeveRegistrarSemUsuario() {
        adocaoValida.setIdUsuario(0);

        assertThrows(IllegalArgumentException.class, () -> {
            adocaoService.registrarAdocao(adocaoValida);
        });
    }
}