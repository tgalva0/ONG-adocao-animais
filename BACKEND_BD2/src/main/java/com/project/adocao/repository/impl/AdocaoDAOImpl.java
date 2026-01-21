package com.project.adocao.repository.impl;

import com.project.adocao.exception.AnimalIndisponivelException;
import com.project.adocao.model.Adocao;
import com.project.adocao.repository.AdocaoDAO;
import com.project.adocao.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdocaoDAOImpl implements AdocaoDAO {
    @Override
    public void salvar(Adocao adocao) {
        String sql = "INSERT INTO tb_adocao (data_adocao, status, id_usuario, id_animal) VALUES (?, ?, ?, ?)";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // ... (setters dos parâmetros continuam iguais) ...
            if (adocao.getDataAdocao() != null) {
                ps.setDate(1, Date.valueOf(adocao.getDataAdocao()));
            } else {
                ps.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            }
            ps.setString(2, adocao.getStatus());
            ps.setInt(3, adocao.getIdUsuario());
            ps.setInt(4, adocao.getIdAnimal());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    adocao.setIdAdocao(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            // 🚨 CAPTURA O ERRO DO TRIGGER AQUI
            if ("45000".equals(e.getSQLState()) && e.getMessage().contains("disponível")) {
                throw new AnimalIndisponivelException("Este animal já foi adotado ou está em processo de adoção.");
            }
            throw new RuntimeException("Erro ao registrar adoção: " + e.getMessage(), e);
        }
    }

    @Override
    public int contarTotalAdocoes() {
        String sql = "SELECT COUNT(*) AS total FROM tb_adocao";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar total de adoções: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public Optional<Adocao> buscarPorId(int id) {
        String sql = "SELECT * FROM tb_adocao WHERE id_adocao = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAdocao(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar adoção por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Adocao> buscarTodos() {
        List<Adocao> adocoes = new ArrayList<>();
        String sql = "SELECT * FROM tb_adocao";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                adocoes.add(mapResultSetToAdocao(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as adoções: " + e.getMessage(), e);
        }
        return adocoes;
    }

    @Override
    public List<Adocao> buscarPorUsuario(int idUsuario) {
        List<Adocao> adocoes = new ArrayList<>();
        String sql = "SELECT * FROM tb_adocao WHERE id_usuario = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    adocoes.add(mapResultSetToAdocao(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar adoções do usuário: " + e.getMessage(), e);
        }
        return adocoes;
    }

    @Override
    public void atualizar(Adocao adocao) {
        // Atualiza data, status, usuario e animal
        String sql = "UPDATE tb_adocao SET data_adocao = ?, status = ?, id_usuario = ?, id_animal = ? WHERE id_adocao = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            if (adocao.getDataAdocao() != null) {
                ps.setDate(1, Date.valueOf(adocao.getDataAdocao()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }

            ps.setString(2, adocao.getStatus()); // Status
            ps.setInt(3, adocao.getIdUsuario());
            ps.setInt(4, adocao.getIdAnimal());
            ps.setInt(5, adocao.getIdAdocao()); // WHERE id

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar adoção: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tb_adocao WHERE id_adocao = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar adoção: " + e.getMessage(), e);
        }
    }

    private Adocao mapResultSetToAdocao(ResultSet rs) throws SQLException {
        Adocao adocao = new Adocao();
        adocao.setIdAdocao(rs.getInt("id_adocao"));

        Date sqlDate = rs.getDate("data_adocao");
        if (sqlDate != null) {
            adocao.setDataAdocao(sqlDate.toLocalDate());
        }

        adocao.setStatus(rs.getString("status")); // Mapeia o novo campo
        adocao.setIdUsuario(rs.getInt("id_usuario"));
        adocao.setIdAnimal(rs.getInt("id_animal"));
        return adocao;
    }
}