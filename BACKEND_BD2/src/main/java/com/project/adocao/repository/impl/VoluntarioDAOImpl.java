package com.project.adocao.repository.impl;

import com.project.adocao.dto.VoluntarioAtividadeDTO;
import com.project.adocao.model.Voluntario;
import com.project.adocao.repository.VoluntarioDAO;
import com.project.adocao.util.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VoluntarioDAOImpl implements VoluntarioDAO {

    @Override
    public void salvar(Voluntario voluntario) {
        String sql = "INSERT INTO tb_voluntario (nome_voluntario, RG, CPF, atividade, dias_disponiveis, id_usuario) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, voluntario.getNomeVoluntario());
            ps.setString(2, voluntario.getRg());
            ps.setString(3, voluntario.getCpf());
            ps.setString(4, voluntario.getAtividade());
            ps.setString(5, voluntario.getDiasDisponiveis());
            ps.setInt(6, voluntario.getIdUsuario());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    voluntario.setIdVoluntario(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar voluntário: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Voluntario> buscarPorId(int id) {
        String sql = "SELECT * FROM tb_voluntario WHERE id_voluntario = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToVoluntario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar voluntário por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Voluntario> buscarTodos() {
        List<Voluntario> voluntarios = new ArrayList<>();
        String sql = "SELECT * FROM tb_voluntario";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                voluntarios.add(mapResultSetToVoluntario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os voluntários: " + e.getMessage(), e);
        }
        return voluntarios;
    }

    @Override
    public void atualizar(Voluntario voluntario) {
        String sql = "UPDATE tb_voluntario SET nome_voluntario = ?, RG = ?, CPF = ?, " +
                "atividade = ?, dias_disponiveis = ?, id_usuario = ? WHERE id_voluntario = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, voluntario.getNomeVoluntario());
            ps.setString(2, voluntario.getRg());
            ps.setString(3, voluntario.getCpf());
            ps.setString(4, voluntario.getAtividade());
            ps.setString(5, voluntario.getDiasDisponiveis());
            ps.setInt(6, voluntario.getIdUsuario());
            ps.setInt(7, voluntario.getIdVoluntario());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar voluntário: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tb_voluntario WHERE id_voluntario = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar voluntário: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<VoluntarioAtividadeDTO> buscarAtividadePorNome(String nomeVoluntario) {
        String sql = "{CALL sp_mostrar_atividade_voluntario(?)}";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             CallableStatement cs = conexao.prepareCall(sql)) {

            cs.setString(1, nomeVoluntario);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    String atividade = rs.getString("atividade");
                    String dias = rs.getString("dias_disponiveis");
                    VoluntarioAtividadeDTO dto = new VoluntarioAtividadeDTO(atividade, dias);
                    return Optional.of(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar procedure sp_mostrar_atividade_voluntario: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    private Voluntario mapResultSetToVoluntario(ResultSet rs) throws SQLException {
        Voluntario voluntario = new Voluntario();
        voluntario.setIdVoluntario(rs.getInt("id_voluntario"));
        voluntario.setNomeVoluntario(rs.getString("nome_voluntario"));
        voluntario.setRg(rs.getString("RG"));
        voluntario.setCpf(rs.getString("CPF"));
        voluntario.setAtividade(rs.getString("atividade"));
        voluntario.setDiasDisponiveis(rs.getString("dias_disponiveis"));
        voluntario.setIdUsuario(rs.getInt("id_usuario"));
        return voluntario;
    }
}