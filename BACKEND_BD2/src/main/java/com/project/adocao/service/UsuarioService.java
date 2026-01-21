package com.project.adocao.service;

import com.project.adocao.dto.AnimalDonoDTO;
import com.project.adocao.exception.CpfDuplicadoException;
import com.project.adocao.exception.EmailDuplicadoException; // Import da nova exceção
import com.project.adocao.model.Usuario;
import com.project.adocao.repository.UsuarioDAO;
import com.project.adocao.repository.impl.UsuarioDAOImpl;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void criarUsuario(Usuario usuario) throws CpfDuplicadoException, EmailDuplicadoException {

        if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
        if (usuario.getCpf() == null || usuario.getCpf().length() != 11) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        usuarioDAO.salvar(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioDAO.buscarTodos();
    }

    public void atualizarUsuario(Usuario usuario) throws CpfDuplicadoException, EmailDuplicadoException {
        if (!usuarioDAO.buscarPorId(usuario.getIdUsuario()).isPresent()) {
            throw new RuntimeException("Usuário não encontrado para atualização");
        }
        usuarioDAO.atualizar(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return usuarioDAO.buscarPorEmail(email);
    }

    public Usuario autenticar(String email, String senha) {
        return usuarioDAO.buscarPorEmailESenha(email, senha)
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));
    }

    public void deletarUsuario(int id) {
        usuarioDAO.deletar(id);
    }

    public int totalAnimaisDoUsuario(int idUsuario) {
        return usuarioDAO.contarAnimaisPorUsuario(idUsuario);
    }

    public List<AnimalDonoDTO> getAnimaisComDonos() {
        return usuarioDAO.listarAnimaisComDonos();
    }
}