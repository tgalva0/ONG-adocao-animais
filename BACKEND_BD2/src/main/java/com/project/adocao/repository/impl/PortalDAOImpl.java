package com.project.adocao.repository.impl;

import com.project.adocao.model.Portal;
import com.project.adocao.repository.PortalDAO;
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

public class PortalDAOImpl implements PortalDAO {

    /**
     * Salva um novo registro no portal. (Nova estrutura de valores)
     */
    @Override
    public void salvar(Portal portal) {
        // SQL atualizado com 4 parâmetros (data_registro, valor_racao, valor_agua, valor_vacina) + id_usuario
        String sql = "INSERT INTO tb_portal (data_registro, valor_racao, valor_agua, valor_vacina, id_usuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1. data_registro
            if (portal.getDataRegistro() != null) {
                ps.setDate(1, Date.valueOf(portal.getDataRegistro()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }

            // 2, 3, 4. Novos campos de valor (usando setDouble para precisão)
            ps.setDouble(2, portal.getValorRacao());
            ps.setDouble(3, portal.getValorAgua());
            ps.setDouble(4, portal.getValorVacina());

            // 5. id_usuario
            ps.setInt(5, portal.getIdUsuario());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    portal.setIdPortal(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar registro no portal: " + e.getMessage(), e);
        }
    }

    /**
     * Busca um registro pelo ID.
     */
    @Override
    public Optional<Portal> buscarPorId(int id) {
        String sql = "SELECT * FROM tb_portal WHERE id_portal = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPortal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar registro do portal por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Lista todos os registros.
     */
    @Override
    public List<Portal> buscarTodos() {
        List<Portal> portais = new ArrayList<>();
        String sql = "SELECT * FROM tb_portal";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                portais.add(mapResultSetToPortal(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os registros do portal: " + e.getMessage(), e);
        }
        return portais;
    }

    /**
     * Atualiza um registro. (Nova estrutura de valores)
     */
    @Override
    public void atualizar(Portal portal) {
        // SQL atualizado com novos campos de valor
        String sql = "UPDATE tb_portal SET data_registro = ?, valor_racao = ?, valor_agua = ?, valor_vacina = ?, id_usuario = ? WHERE id_portal = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            // 1. data_registro
            if (portal.getDataRegistro() != null) {
                ps.setDate(1, Date.valueOf(portal.getDataRegistro()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }

            // 2, 3, 4. Novos campos de valor
            ps.setDouble(2, portal.getValorRacao());
            ps.setDouble(3, portal.getValorAgua());
            ps.setDouble(4, portal.getValorVacina());

            // 5. id_usuario
            ps.setInt(5, portal.getIdUsuario());

            // 6. WHERE id_portal
            ps.setInt(6, portal.getIdPortal());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar registro do portal: " + e.getMessage(), e);
        }
    }

    /**
     * Deleta um registro.
     */
    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tb_portal WHERE id_portal = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar registro do portal: " + e.getMessage(), e);
        }
    }

    /**
     * Mapeamento do ResultSet. (Nova estrutura de valores)
     */
    private Portal mapResultSetToPortal(ResultSet rs) throws SQLException {
        Portal portal = new Portal();
        portal.setIdPortal(rs.getInt("id_portal"));

        Date sqlDate = rs.getDate("data_registro");
        if (sqlDate != null) {
            portal.setDataRegistro(sqlDate.toLocalDate());
        }

        // Mapeamento dos novos campos
        portal.setValorRacao(rs.getDouble("valor_racao"));
        portal.setValorAgua(rs.getDouble("valor_agua"));
        portal.setValorVacina(rs.getDouble("valor_vacina"));

        // A coluna 'descricao' não é mais lida

        portal.setIdUsuario(rs.getInt("id_usuario"));
        return portal;
    }
}