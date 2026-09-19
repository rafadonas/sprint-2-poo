package dao;

import db.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Operações JDBC compartilhadas pelos DAOs do projeto. */
abstract class DaoSupport {
    protected Connection conexao() throws SQLException {
        return ConexaoBD.getInstancia().conectar();
    }

    protected long proximoId(String sqlSequence) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(sqlSequence);
             ResultSet rs = stmt.executeQuery()) {
            if (!rs.next()) {
                throw new SQLException("O Oracle não retornou o próximo identificador.");
            }
            return rs.getLong(1);
        }
    }
}
