package com.project.adocao.repository;

import com.project.adocao.dto.AnimalDonoDTO;
import com.project.adocao.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {

    void salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(int id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> buscarTodos();
    void atualizar(Usuario usuario);
    void deletar(int id);
    int contarTotalUsuarios();

    int contarAnimaisPorUsuario(int idUsuario);

    List<AnimalDonoDTO> listarAnimaisComDonos();

    Optional<Usuario> buscarPorEmailESenha(String email, String senha);
}