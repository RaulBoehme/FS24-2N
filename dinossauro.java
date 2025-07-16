
import java.sql.*;

public class dinossauro {


        public static final String URL = "jdbc:postgresql://localhost:5432/JurassicWorld";
        public static final String USUARIO = "postgres";
        public static final String SENHA = "root";

        public static Connection conectar() {
            Connection Conexao = null; // Inicializa a conexão como nula
            try {
                Conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com o banco de dados estabelecida com sucesso");
            }catch(SQLException error){
                System.err.println("Erro ao conectar ao bd:" + error.getMessage());

            } return Conexao;
        }
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
        public static void setDinossauro(int id, String nome, String especie, String dieta, int idade, String status) {
            String sql = "INSERT INTO dinossauros (id_dinossauro, nome, especie, dieta, idade_estimada_ano, status_cercado) VALUES (?, ?, ?, ?)";
            Connection conexao = null;
            PreparedStatement stmt = null;
            try {
                conexao = conectar(); // Usa nosso método de conexão
                if (conexao != null) {
                    stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, id);
                    stmt.setString(2, nome);
                    stmt.setString(3, especie );
                    stmt.setString(4, dieta);
                    stmt.setInt(5, idade );
                    stmt.setString(6,status );
                    int linhasAfetadas = stmt.executeUpdate(); // Executa o INSERT
                    if (linhasAfetadas > 0) {
                        System.out.println("Dinossauro " + nome + " inserido no BD com sucesso!");
                    }
                }
            } catch (SQLException e) {
                System.err.printf("Erro ao inserir dinossauro no PostGreSQL: " + e.getMessage());
            } finally {
                try {
                    if (stmt !=null) stmt.close();
                    if (conexao!= null) fecharConexao(conexao); // Usa nosso método de fechar conexão
                } catch (SQLException e){
                    System.err.printf("Erro ao fechar recursos após inserção: " + e.getMessage());
                }
            }
        }

        public static void getDinossauro(){
            String sql = "SELECT id_dinossauro, nome, especie, dieta, idade_estimada_ano,status_cercado FROM dinossauro ORDER BY id_dinossauro" ; //Após a palavra "FROM" é o nome da tabela
            Connection conexao = null;
            PreparedStatement stmt = null;
            ResultSet rs = null ; // Objeto para armazenar os resultados da consulta
            try {
                conexao = conectar();
                if (conexao != null) {
                    stmt = conexao.prepareStatement(sql);
                    rs = stmt.executeQuery(); // Executa a consulta SELECT
                    System.out.println("\n--- Dinossauros Cadastrados no BD ---");
                    boolean encontrouDinossauro = false;
                    while (rs.next()) { //Loop enquanto houver próximas linhas no resultado
                        encontrouDinossauro = true;
                        int id = rs.getInt("id_dinosssauro");
                        String nome = rs.getString("nome");
                        String especie = rs.getString("especie");
                        String dieta = rs.getString("dieta");
                        String idade = rs.getString("idade_estimada_ano");
                        String status = rs.getString("status_cercado");

                        System.out.println("");
                    }
                    if (!encontrouDinossauro) {
                        System.out.println("Nenhum dinossauro encontrado.");
                    }
                    System.out.println("----------------------------------\n");
                }
            }catch (SQLException e) {
                System.err.println("Erro ao consultar dinossauros no DB: " + e.getMessage());
            }finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conexao != null) fecharConexao(conexao);
                }catch (SQLException e) {System.err.println("Erro ao fechar recursos após consulta: " + e.getMessage());}
            }
        }

        public static void main(String[] args){
            Connection testeConexao = dinossauro.conectar();
            if(testeConexao != null){
                //    ConexaoPostgresDB.fecharConexao(testeConexao);
                setDinossauro(1, "Mosassauro", "Mosassauride", "Herbívero", 68, "Cercado");
                getDinossauro();
            }
        }
    }



