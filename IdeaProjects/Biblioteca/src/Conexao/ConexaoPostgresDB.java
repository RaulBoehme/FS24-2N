package Conexao;

import java.sql.PreparedStatement; // Importa .
import java.sql.ResultSet; // Importa ResultSet para consultas
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoPostgresDB {
    public static final String URL = "jdbc:postgresql://localhost:5432/Mydb";
    public static final String USUARIO = "postgres";
    public static final String SENHA = "root";

    public static void fecharConexao(Connection conexao){
        if(conexao != null){
            try{
                conexao.close();
                System.out.println("Conexao com bd fechada!");

            }catch (SQLException e){
                System.err.println("Erro ao fechar a conexão com bd:" + e.getMessage());
            }

        }
    }

    public static Connection conectar() {
        Connection Conexao = null; // Inicializa a conexão como nula
        try {
            Conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso");
        }catch(SQLException error){
            System.err.println("Erro ao conectar ao bd:" + error.getMessage());

        } return Conexao;
    }

    public static void setAluno(int id, String nome, int idade, String telefone) {
        String sql = "INSERT INTO alunos (idAlunos, nomeAluno, idadeAluno, telefoneAluno) VALUES (?, ?, ?, ?)";
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = conectar(); // Usa nosso método de conexão
            if (conexao != null) {
                stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.setString(2, nome);
                stmt.setInt(3, idade);
                stmt.setString(4, telefone);
                int linhasAfetadas = stmt.executeUpdate(); // Executa o INSERT
                if (linhasAfetadas > 0) {
                    System.out.println("Aluno " + nome + " inserido no BD com sucesso!");
                }
            }
        } catch (SQLException e) {
            System.err.printf("Erro ao inserir aluno no PostGreSQL: " + e.getMessage());
        } finally {
            try {
                if (stmt !=null) stmt.close();
                if (conexao!= null) fecharConexao(conexao); // Usa nosso método de fechar conexão
            } catch (SQLException e){
                System.err.printf("Erro ao fechar recursos após inserção: " + e.getMessage());
            }
        }
    }

    public static void getAlunos(){
    String sql = "SELECT idAlunos, nomeAluno, idadeAluno, telefoneAluno FROM Alunos ORDER BY idAlunos" ; //Após a palavra "FROM" é o nome da tabela
    Connection conexao = null;
    PreparedStatement stmt = null;
    ResultSet rs = null ; // Objeto para armazenar os resultados da consulta
        try {
            conexao = conectar();
            if (conexao != null) {
                stmt = conexao.prepareStatement(sql);
                rs = stmt.executeQuery(); // Executa a consulta SELECT
                System.out.println("\n--- Alunos Cadastrados no BD ---");
                boolean encontrouAluno = false;
                while (rs.next()) { //Loop enquanto houver próximas linhas no resultado
                    encontrouAluno = true;
                    int id = rs.getInt("idAlunos");
                    String nome = rs.getString("nomeAluno");
                    Number idade = rs.getInt("idadeAluno");
                    String telefone = rs.getString("telefoneAluno");
                    System.out.println("ID: " + id + ", Nome: " + nome + ", Idade: " + idade + ", Telefone" + telefone);
                }
                if (!encontrouAluno) {
                    System.out.println("Nenhum aluno encontrado.");
                }
                System.out.println("----------------------------------\n");
            }
        }catch (SQLException e) {
            System.err.println("Erro ao consultar alunos no DB: " + e.getMessage());
        }finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexao != null) fecharConexao(conexao);
            }catch (SQLException e) {System.err.println("Erro ao fechar recursos após consulta: " + e.getMessage());}
        }
    }

    public static void atualizarAluno(int idAlunos, String novoNome, int novaIdade, String novoTelefone) {
        String sql = "UPDATE alunos SET nome = ?, idade = ?, telefone = ? WHERE idAlunos = ?";
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = conectar();
            if (conexao != null) {
                stmt = conexao.prepareStatement(sql);
                stmt.setString(1, novoNome);
                stmt.setInt(2, novaIdade);
                stmt.setString(3, novoTelefone);
                stmt.setInt(4, idAlunos); //O ID do aluno que queremos atualizar
                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Aluno com ID " + idAlunos + " atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum aluno encontrado com o ID: " + idAlunos + " para atualização. ");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar aluno no PostgreSQL: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexao != null) fecharConexao(conexao);
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recurso após atualização: " + e.getMessage());
            }
        }
    }

    public static void removerAluno(int idAlunos) {
        String sql = "DELETE FROM alunos WHERE idAlunos = ?";
        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            conexao = conectar();
            if (conexao != null) {
                stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, idAlunos); // O ID do aluno que queremos remover

                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Aluno com ID " + idAlunos + " removido com sucesso!");
                } else {
                    System.out.println("Nenhum aluno encontrado com ID " + idAlunos + " para remoção.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover aluno no PostgreSQL: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexao != null) fecharConexao(conexao);
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos após remoção: " + e.getMessage());
            }
        }
    }

        public static void main (String[]args){
            Connection testeConexao = ConexaoPostgresDB.conectar();
            if (testeConexao != null) {
                //    ConexaoPostgresDB.fecharConexao(testeConexao);
                // setAluno(1, "raul", 31, "765643246");
                getAlunos();
            }
        }
    }


