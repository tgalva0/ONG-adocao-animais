package com.project.adocao.service;

import com.project.adocao.exception.CpfDuplicadoException;
import com.project.adocao.model.Usuario;
import com.project.adocao.repository.UsuarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioDAO usuarioDAOMock;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        usuarioValido = new Usuario();
        usuarioValido.setIdUsuario(1);
        usuarioValido.setNome("Teste");
        usuarioValido.setEmail("teste@teste.com");
        usuarioValido.setCpf("12345678901");
        usuarioValido.setSenha("123");
        usuarioValido.setTipoUsuario("comum");
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        usuarioService.criarUsuario(usuarioValido);
        verify(usuarioDAOMock, times(1)).salvar(usuarioValido);
    }

    @Test
    void naoDeveCriarUsuarioComEmailInvalido() {
        usuarioValido.setEmail("email_invalido");

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.criarUsuario(usuarioValido);
        }, "Email inválido.");

        verify(usuarioDAOMock, never()).salvar(any());
    }

    @Test
    void naoDeveCriarUsuarioComCpfInvalido() {
        usuarioValido.setCpf("123");

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.criarUsuario(usuarioValido);
        }, "CPF inválido.");

        verify(usuarioDAOMock, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoAoCriarUsuarioComCpfDuplicado() {
        doThrow(new CpfDuplicadoException("CPF já cadastrado!"))
                .when(usuarioDAOMock).salvar(any(Usuario.class));

        assertThrows(CpfDuplicadoException.class, () -> {
            usuarioService.criarUsuario(usuarioValido);
        });

        verify(usuarioDAOMock, times(1)).salvar(usuarioValido);
    }

    @Test
    void deveChamarDaoParaContarAnimais() {
        when(usuarioDAOMock.contarAnimaisPorUsuario(1)).thenReturn(5);

        int total = usuarioService.totalAnimaisDoUsuario(1);

        assertEquals(5, total);
        verify(usuarioDAOMock, times(1)).contarAnimaisPorUsuario(1);
    }
}