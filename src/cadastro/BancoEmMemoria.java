package cadastro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BancoEmMemoria {

    public static void main(String[] args) {
        String url = "jdbc:h2:mem:meubanco;DB_CLOSE_DELAY=-1"; // banco em memória
        String usuario = "sa";
        String senha = "";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("Conexão com o banco estabelecida.");

            // 1. Criar a tabela usuario
            String sqlCriarTabela = """
                CREATE TABLE usuario (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nome VARCHAR(100),
                    email VARCHAR(100)
                );
                """;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlCriarTabela);
                System.out.println("Tabela 'usuario' criada com sucesso.");
            }

            // 2. Inserir dados
            String sqlInserir = "INSERT INTO usuario (nome, email) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInserir)) {
                pstmt.setString(1, "Ana Silva");
                pstmt.setString(2, "ana@ifpb.edu.br");
                pstmt.executeUpdate();

                pstmt.setString(1, "Carlos Souza");
                pstmt.setString(2, "carlos@ifpb.edu.br");
                pstmt.executeUpdate();

                System.out.println("Usuários inseridos.");
            }

            // 3. Consultar dados
            String sqlConsulta = "SELECT * FROM usuario";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlConsulta)) {

                System.out.println("\nLista de usuários:");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    System.out.printf("ID: %d | Nome: %s | Email: %s%n", id, nome, email);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
