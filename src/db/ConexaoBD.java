package db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Gerencia uma única conexão JDBC com o Oracle durante a execução. */
public final class ConexaoBD {
    private static final String ARQUIVO_CONFIGURACAO = "db.properties";
    private static final ConexaoBD INSTANCIA = new ConexaoBD();

    private Connection conexao;

    private ConexaoBD() {
    }

    public static ConexaoBD getInstancia() {
        return INSTANCIA;
    }

    public synchronized Connection conectar() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            return conexao;
        }

        Properties arquivo = carregarArquivoConfiguracao();
        String url = obterConfiguracao("motiva.db.url", "MOTIVA_DB_URL", "db.url", arquivo);
        String usuario = obterConfiguracao(
                "motiva.db.usuario", "MOTIVA_DB_USUARIO", "db.usuario", arquivo);
        String senha = obterConfiguracao(
                "motiva.db.senha", "MOTIVA_DB_SENHA", "db.senha", arquivo);

        if (url == null || usuario == null || senha == null) {
            throw new SQLException(
                    "Configuração incompleta. Defina db.url, db.usuario e db.senha "
                            + "em db.properties ou use as variáveis MOTIVA_DB_URL, "
                            + "MOTIVA_DB_USUARIO e MOTIVA_DB_SENHA.");
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver Oracle não encontrado. Copie ojdbc17.jar para a pasta lib/ "
                            + "e inclua-o no classpath.", e);
        }

        conexao = DriverManager.getConnection(url, usuario, senha);
        return conexao;
    }

    public synchronized void desconectar() throws SQLException {
        if (conexao != null) {
            try {
                if (!conexao.isClosed()) {
                    conexao.close();
                }
            } finally {
                conexao = null;
            }
        }
    }

    public synchronized boolean estaConectado() throws SQLException {
        return conexao != null && !conexao.isClosed();
    }

    private Properties carregarArquivoConfiguracao() throws SQLException {
        Properties propriedades = new Properties();
        Path caminho = Path.of(ARQUIVO_CONFIGURACAO);
        if (!Files.exists(caminho)) {
            return propriedades;
        }

        try (InputStream entrada = Files.newInputStream(caminho)) {
            propriedades.load(entrada);
            return propriedades;
        } catch (IOException e) {
            throw new SQLException("Não foi possível ler " + ARQUIVO_CONFIGURACAO + '.', e);
        }
    }

    private String obterConfiguracao(String propriedadeSistema, String variavelAmbiente,
                                     String propriedadeArquivo, Properties arquivo) {
        String valor = System.getProperty(propriedadeSistema);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(variavelAmbiente);
        }
        if (valor == null || valor.isBlank()) {
            valor = arquivo.getProperty(propriedadeArquivo);
        }
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
