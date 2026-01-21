package com.project.adocao.repository.impl;

import com.project.adocao.model.Evento;
import com.project.adocao.repository.EventoDAO;
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

public class EventoDAOImpl implements EventoDAO {

    @Override
    public void salvar(Evento evento) {
        String sql = "INSERT INTO tb_evento (nome_evento, data_evento, descricao, id_usuario) VALUES (?, ?, ?, ?)";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, evento.getNomeEvento());

            if (evento.getDataEvento() != null) {
                ps.setDate(2, Date.valueOf(evento.getDataEvento()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            ps.setString(3, evento.getDescricao());
            ps.setInt(4, evento.getIdUsuario());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evento.setIdEvento(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar evento: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Evento> buscarPorId(int id) {
        String sql = "SELECT * FROM tb_evento WHERE id_evento = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEvento(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar evento por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Evento> buscarTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM tb_evento";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                eventos.add(mapResultSetToEvento(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar eventos: " + e.getMessage(), e);
        }
        return eventos;
    }

    @Override
    public void atualizar(Evento evento) {
        String sql = "UPDATE tb_evento SET nome_evento = ?, data_evento = ?, descricao = ?, id_usuario = ? WHERE id_evento = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, evento.getNomeEvento());

            if (evento.getDataEvento() != null) {
                ps.setDate(2, Date.valueOf(evento.getDataEvento()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }

            ps.setString(3, evento.getDescricao());
            ps.setInt(4, evento.getIdUsuario());
            ps.setInt(5, evento.getIdEvento());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar evento: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tb_evento WHERE id_evento = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar evento: " + e.getMessage(), e);
        }
    }

    private Evento mapResultSetToEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("id_evento"));
        evento.setNomeEvento(rs.getString("nome_evento"));

        Date sqlDate = rs.getDate("data_evento");
        if (sqlDate != null) {
            evento.setDataEvento(sqlDate.toLocalDate());
        }

        evento.setDescricao(rs.getString("descricao"));
        evento.setIdUsuario(rs.getInt("id_usuario"));
        return evento;
    }
}