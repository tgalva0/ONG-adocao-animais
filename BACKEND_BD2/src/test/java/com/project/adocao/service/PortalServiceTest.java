package com.project.adocao.service;

import com.project.adocao.model.Portal;
import com.project.adocao.repository.PortalDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortalServiceTest {

    @Mock
    private PortalDAO portalDAOMock;

    @InjectMocks
    private PortalService portalService;

    private Portal portalValido;

    @BeforeEach
    void setUp() {
        portalValido = new Portal();
        portalValido.setIdPortal(1);
        portalValido.setDataRegistro(LocalDate.now());
        portalValido.setIdUsuario(1);

        // 🛠️ Novas propriedades financeiras
        portalValido.setValorRacao(500.00);
        portalValido.setValorAgua(20.00);
        portalValido.setValorVacina(150.50);
    }

    @Test
    void deveCriarRegistroComSucesso() {
        portalService.criarRegistroPortal(portalValido);
        verify(portalDAOMock, times(1)).salvar(portalValido);
    }

    @Test
    void deveAdicionarDataDeHojeSeNaoForInformada() {
        Portal portalSemData = new Portal();
        // 🛠️ Inicializa os novos campos para que o objeto seja válido
        portalSemData.setValorRacao(100.00);
        portalSemData.setValorAgua(10.00);
        portalSemData.setValorVacina(0.00);

        portalService.criarRegistroPortal(portalSemData);

        ArgumentCaptor<Portal> captor = ArgumentCaptor.forClass(Portal.class);
        verify(portalDAOMock).salvar(captor.capture());

        Portal portalSalvo = captor.getValue();
        assertNotNull(portalSalvo.getDataRegistro());
        assertEquals(LocalDate.now(), portalSalvo.getDataRegistro());
    }

    /**
     * Novo Teste: Verifica se o Service impede valores negativos (Regra de Negócio)
     */
    @Test
    void naoDeveAceitarValoresNegativos() {
        // Preparação: Define um valor negativo
        portalValido.setValorRacao(-10.00);

        // Ação e Verificação
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            portalService.criarRegistroPortal(portalValido);
        });

        assertEquals("Os valores de gasto (Ração, Água, Vacina) não podem ser negativos.", exception.getMessage());

        // Garante que o DAO nunca foi chamado
        verify(portalDAOMock, never()).salvar(any());
    }
}