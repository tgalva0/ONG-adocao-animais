package com.project.adocao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = System.getenv("DB_URL") != null 
        ? System.getenv("DB_URL") 
        : "jdbc:mysql://localhost:3306/Projeto?useSSL=false&allowPublicKeyRetrieval=true";
    
    private static final String USER = System.getenv("DB_USER") != null 
        ? System.getenv("DB_USER") 
        : "root";
    
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static DatabaseConnection instance;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL não encontrado!");
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if (PASSWORD == null) {
            System.err.println("AVISO: Variável de ambiente DB_PASSWORD não definida!");
        }
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}