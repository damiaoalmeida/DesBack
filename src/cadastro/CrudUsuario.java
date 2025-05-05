package cadastro;

import java.sql.*;
import java.util.Scanner;

public class CrudUsuario {

    //private static final String URL = "jdbc:h2:mem:meubanco;DB_CLOSE_DELAY=-1";
	private static final String URL = "jdbc:h2:file:C:/Users/dacio/eclipse-workspace/DesBack/bd/meubanco";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Scanner scanner = new Scanner(System.in)) {

            criarTabela(conn);

            while (true) {
                System.out.println("\n=== MENU ===");
                System.out.println("1 - Inserir usuário");
                System.out.println("2 - Listar usuários");
                System.out.println("3 - Atualizar usuário");
                System.out.println("4 - Deletar usuário");
                System.out.println("0 - Sair");
                System.out.print("Escolha: ");
                int opcao = scanner.nextInt();
                scanner.nextLine(); // limpar buffer

                switch (opcao) {
                    case 1 -> inserirUsuario(conn, scanner);
                    case 2 -> listarUsuarios(conn);
                    case 3 -> atualizarUsuario(conn, scanner);
                    case 4 -> deletarUsuario(conn, scanner);
                    case 0 -> {
                        System.out.println("Encerrando...");
                        return;
                    }
                    default -> System.out.println("Opção inválida!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void criarTabela(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE usuario (
                id INT PRIMARY KEY AUTO_INCREMENT,
                nome VARCHAR(100),
                email VARCHAR(100)
            );
            """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void inserirUsuario(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO usuario (nome, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("Usuário inserido com sucesso.");
        }
    }

    private static void listarUsuarios(Connection conn) throws SQLException {
        String sql = "SELECT * FROM usuario";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Lista de Usuários ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Email: %s%n",
                        rs.getInt("id"), rs.getString("nome"), rs.getString("email"));
            }
        }
    }

    private static void atualizarUsuario(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID do usuário a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo email: ");
        String email = scanner.nextLine();

        String sql = "UPDATE usuario SET nome = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setInt(3, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Usuário atualizado.");
            } else {
                System.out.println("Usuário não encontrado.");
            }
        }
    }

    private static void deletarUsuario(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("ID do usuário a deletar: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Usuário deletado.");
            } else {
                System.out.println("Usuário não encontrado.");
            }
        }
    }
}
