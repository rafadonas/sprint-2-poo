package dao;

import model.TipoClima;
import model.TrechoRodoviaRegistro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** CRUD da entidade TRECHO_RODOVIA. */
public class TrechoRodoviaDAO extends DaoSupport {
    private static final String PROXIMO_ID =
            "SELECT SEQ_TRECHO_RODOVIA.NEXTVAL FROM DUAL";
    private static final String INSERIR =
            "INSERT INTO TRECHO_RODOVIA "
                    + "(ID, KM, ALTURA_VEGETACAO, CLIMA, MONITORADO) VALUES (?, ?, ?, ?, ?)";
    private static final String BUSCAR_POR_ID =
            "SELECT ID, KM, ALTURA_VEGETACAO, CLIMA, MONITORADO "
                    + "FROM TRECHO_RODOVIA WHERE ID = ?";
    private static final String LISTAR_TODAS =
            "SELECT ID, KM, ALTURA_VEGETACAO, CLIMA, MONITORADO "
                    + "FROM TRECHO_RODOVIA ORDER BY KM";
    private static final String ATUALIZAR =
            "UPDATE TRECHO_RODOVIA SET KM = ?, ALTURA_VEGETACAO = ?, "
                    + "CLIMA = ?, MONITORADO = ? WHERE ID = ?";
    private static final String DELETAR =
            "DELETE FROM TRECHO_RODOVIA WHERE ID = ?";

    public TrechoRodoviaDAO() {
    }

    public TrechoRodoviaRegistro inserir(TrechoRodoviaRegistro trecho) throws SQLException {
        long id = proximoId(PROXIMO_ID);
        try (PreparedStatement stmt = conexao().prepareStatement(INSERIR)) {
            stmt.setLong(1, id);
            stmt.setInt(2, trecho.km());
            stmt.setDouble(3, trecho.alturaVegetacao());
            stmt.setString(4, trecho.clima().name());
            stmt.setString(5, trecho.monitorado() ? "S" : "N");
            stmt.executeUpdate();
        }
        return trecho.comId(id);
    }

    public Optional<TrechoRodoviaRegistro> buscarPorId(long id) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(BUSCAR_POR_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    public List<TrechoRodoviaRegistro> listarTodas() throws SQLException {
        List<TrechoRodoviaRegistro> trechos = new ArrayList<>();
        try (PreparedStatement stmt = conexao().prepareStatement(LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                trechos.add(mapear(rs));
            }
        }
        return trechos;
    }

    public boolean atualizar(TrechoRodoviaRegistro trecho) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(ATUALIZAR)) {
            stmt.setInt(1, trecho.km());
            stmt.setDouble(2, trecho.alturaVegetacao());
            stmt.setString(3, trecho.clima().name());
            stmt.setString(4, trecho.monitorado() ? "S" : "N");
            stmt.setLong(5, trecho.id());
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean deletar(long id) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(DELETAR)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() == 1;
        }
    }

    private TrechoRodoviaRegistro mapear(ResultSet rs) throws SQLException {
        return new TrechoRodoviaRegistro(
                rs.getLong("ID"),
                rs.getInt("KM"),
                rs.getDouble("ALTURA_VEGETACAO"),
                TipoClima.valueOf(rs.getString("CLIMA")),
                "S".equals(rs.getString("MONITORADO")));
    }
}
