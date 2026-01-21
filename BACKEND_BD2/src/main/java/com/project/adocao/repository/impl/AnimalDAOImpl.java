package com.project.adocao.repository.impl;

import com.project.adocao.model.Animal;
import com.project.adocao.repository.AnimalDAO;
import com.project.adocao.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimalDAOImpl implements AnimalDAO {

    @Override
    public void salvar(Animal animal) {
        String sql = "INSERT INTO tb_animal (nome_animal, peso, porte, raca, status, especie, idade, sexo, vermifugado, vacinado_gripe, doenca) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, animal.getNomeAnimal());
            ps.setInt(2, animal.getPeso());
            ps.setString(3, animal.getPorte());
            ps.setString(4, animal.getRaca());
            ps.setBoolean(5, animal.isStatus());
            ps.setString(6, animal.getEspecie());
            ps.setInt(7, animal.getIdade());
            ps.setString(8, animal.getSexo());
            ps.setBoolean(9, animal.isVermifugado());
            ps.setBoolean(10, animal.isVacinadoGripe());
            ps.setString(11, animal.getDoenca());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    animal.setIdAnimal(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar animal: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Animal> buscarPorId(int id) {
        String sql = "SELECT * FROM tb_animal WHERE id_animal = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAnimal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar animal por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Animal> buscarTodos() {
        List<Animal> animais = new ArrayList<>();
        String sql = "SELECT * FROM tb_animal";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                animais.add(mapResultSetToAnimal(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os animais: " + e.getMessage(), e);
        }
        return animais;
    }

    @Override
    public void atualizar(Animal animal) {
        String sql = "UPDATE tb_animal SET nome_animal = ?, peso = ?, porte = ?, raca = ?, " +
                "status = ?, especie = ?, idade = ?, sexo = ?, vermifugado = ?, " +
                "vacinado_gripe = ?, doenca = ? WHERE id_animal = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, animal.getNomeAnimal());
            ps.setInt(2, animal.getPeso());
            ps.setString(3, animal.getPorte());
            ps.setString(4, animal.getRaca());
            ps.setBoolean(5, animal.isStatus());
            ps.setString(6, animal.getEspecie());
            ps.setInt(7, animal.getIdade());
            ps.setString(8, animal.getSexo());
            ps.setBoolean(9, animal.isVermifugado());
            ps.setBoolean(10, animal.isVacinadoGripe());
            ps.setString(11, animal.getDoenca());
            ps.setInt(12, animal.getIdAnimal());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar animal: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM tb_animal WHERE id_animal = ?";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar animal: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Animal> buscarPorFiltros(String nome, String especie) {
        List<Animal> animais = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_animal WHERE 1=1 ");

        List<String> parametros = new ArrayList<>();

        if (nome != null && !nome.trim().isEmpty()) {
            sql.append(" AND nome_animal LIKE ? ");
            parametros.add("%" + nome + "%");
        }
        if (especie != null && !especie.trim().isEmpty()) {
            sql.append(" AND especie = ? ");
            parametros.add(especie);
        }

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setString(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    animais.add(mapResultSetToAnimal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar animais por filtro: " + e.getMessage(), e);
        }
        return animais;
    }

    @Override
    public int contarAnimaisDisponiveis() {
        String sql = "SELECT COUNT(*) AS total FROM tb_animal WHERE status = TRUE";
        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar animais disponíveis: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public List<Animal> buscarAnimaisDisponiveis() {
        List<Animal> animais = new ArrayList<>();
        // Query para filtrar apenas os animais com status TRUE
        String sql = "SELECT * FROM tb_animal WHERE status = TRUE";

        try (Connection conexao = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                animais.add(mapResultSetToAnimal(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar animais disponíveis: " + e.getMessage(), e);
        }
        return animais;
    }

    private Animal mapResultSetToAnimal(ResultSet rs) throws SQLException {
        Animal animal = new Animal();
        animal.setIdAnimal(rs.getInt("id_animal"));
        animal.setNomeAnimal(rs.getString("nome_animal"));
        animal.setPeso(rs.getInt("peso"));
        animal.setPorte(rs.getString("porte"));
        animal.setRaca(rs.getString("raca"));
        animal.setStatus(rs.getBoolean("status"));
        animal.setEspecie(rs.getString("especie"));
        animal.setIdade(rs.getInt("idade"));
        animal.setSexo(rs.getString("sexo"));
        animal.setVermifugado(rs.getBoolean("vermifugado"));
        animal.setVacinadoGripe(rs.getBoolean("vacinado_gripe"));
        animal.setDoenca(rs.getString("doenca"));
        return animal;
    }
}