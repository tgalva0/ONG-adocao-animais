package com.project.adocao.repository.impl;

import com.project.adocao.dto.AnimalDonoDTO;
import com.project.adocao.exception.CpfDuplicadoException;
import com.project.adocao.exception.EmailDuplicadoException;
import com.project.adocao.model.Usuario;
import com.project.adocao.repository.UsuarioDAO;
import com.project.adocao.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO tb_usuario (nome, senha, email, tipo_usuario, cpf) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getTipoUsuario());
            ps.setString(5, usuario.getCpf());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setIdUsuario(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                if (e.getMessage().contains("cpf")) {
                    throw new CpfDuplicadoException("CPF já cadastrado no sistema!");
                }
                if (e.getMessage().contains("email")) {
                    throw new EmailDuplicadoException("E-mail já cadastrado no sistema!");
                }
            }
            throw new RuntimeException("Erro de persistência: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT * FROM tb_usuario WHERE id_usuario = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT * FROM tb_usuario WHERE email = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> buscarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM tb_usuario";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os usuários: " + e.getMessage(), e);
        }
        return usuarios;
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE tb_usuario SET nome = ?, senha = ?, email = ?, tipo_usuario = ?, cpf = ? WHERE id_usuario = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getSenha());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getTipoUsuario());
            ps.setString(5, usuario.getCpf());
            ps.setInt(6, usuario.getIdUsuario());

            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                if (e.getMessage().contains("cpf")) {
                    throw new CpfDuplicadoException("CPF já cadastrado no sistema!");
                }
                if (e.getMessage().contains("email")) {
                    throw new EmailDuplicadoException("E-mail já cadastrado no sistema!");
                }
            }
            throw new RuntimeException("Erro de persistência: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tb_usuario WHERE id_usuario = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public int contarAnimaisPorUsuario(int idUsuario) {
        String sql = "SELECT fn_qtd_animais(?) AS total";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar animais do usuário: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public List<AnimalDonoDTO> listarAnimaisComDonos() {
        List<AnimalDonoDTO> resultados = new ArrayList<>();
        String sql = "SELECT * FROM mostrar_usuario_animal";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AnimalDonoDTO dto = new AnimalDonoDTO();
                dto.setNomeUsuario(rs.getString("nome"));
                dto.setEmailUsuario(rs.getString("email"));
                dto.setTipoUsuario(rs.getString("tipo_usuario"));
                dto.setNomeAnimal(rs.getString("nome_animal"));
                dto.setRacaAnimal(rs.getString("raca"));
                dto.setDoencaAnimal(rs.getString("doenca"));
                dto.setVacinadoGripe(rs.getBoolean("vacinado_gripe"));
                dto.setVermifugado(rs.getBoolean("vermifugado"));
                resultados.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar view de animais e donos: " + e.getMessage(), e);
        }
        return resultados;
    }

    @Override
    public int contarTotalUsuarios() {
        String sql = "SELECT COUNT(*) AS total FROM tb_usuario";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar total de usuários: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public Optional<Usuario> buscarPorEmailESenha(String email, String senha) {
        String sql = "SELECT * FROM tb_usuario WHERE email = ? AND senha = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTipoUsuario(rs.getString("tipo_usuario"));
        usuario.setCpf(rs.getString("cpf"));
        return usuario;
    }
}