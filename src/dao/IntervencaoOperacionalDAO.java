package dao;

import model.IntervencaoOperacionalRegistro;
import model.StatusIntervencao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** CRUD da entidade INTERVENCAO_OPERACIONAL. */
public class IntervencaoOperacionalDAO extends DaoSupport {
    private static final String PROXIMO_ID =
            "SELECT SEQ_INTERVENCAO_OPERACIONAL.NEXTVAL FROM DUAL";
    private static final String INSERIR =
            "INSERT INTO INTERVENCAO_OPERACIONAL "
                    + "(ID, TRECHO_ID, EQUIPE_ID, TIPO, DESCRICAO, DATA_AGENDADA, STATUS) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String BUSCAR_POR_ID =
            "SELECT ID, TRECHO_ID, EQUIPE_ID, TIPO, DESCRICAO, DATA_AGENDADA, STATUS "
                    + "FROM INTERVENCAO_OPERACIONAL WHERE ID = ?";
    private static final String LISTAR_TODAS =
            "SELECT ID, TRECHO_ID, EQUIPE_ID, TIPO, DESCRICAO, DATA_AGENDADA, STATUS "
                    + "FROM INTERVENCAO_OPERACIONAL ORDER BY DATA_AGENDADA, ID";
    private static final String ATUALIZAR =
            "UPDATE INTERVENCAO_OPERACIONAL SET TRECHO_ID = ?, EQUIPE_ID = ?, TIPO = ?, "
                    + "DESCRICAO = ?, DATA_AGENDADA = ?, STATUS = ? WHERE ID = ?";
    private static final String DELETAR =
            "DELETE FROM INTERVENCAO_OPERACIONAL WHERE ID = ?";

    public IntervencaoOperacionalDAO() {
    }

    public IntervencaoOperacionalRegistro inserir(IntervencaoOperacionalRegistro intervencao)
            throws SQLException {
        long id = proximoId(PROXIMO_ID);
        try (PreparedStatement stmt = conexao().prepareStatement(INSERIR)) {
            preencherDados(stmt, intervencao);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
        return intervencao.comId(id);
    }

    public Optional<IntervencaoOperacionalRegistro> buscarPorId(long id) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(BUSCAR_POR_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    public List<IntervencaoOperacionalRegistro> listarTodas() throws SQLException {
        List<IntervencaoOperacionalRegistro> intervencoes = new ArrayList<>();
        try (PreparedStatement stmt = conexao().prepareStatement(LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                intervencoes.add(mapear(rs));
            }
        }
        return intervencoes;
    }

    public boolean atualizar(IntervencaoOperacionalRegistro intervencao) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(ATUALIZAR)) {
            stmt.setLong(1, intervencao.trechoId());
            stmt.setLong(2, intervencao.equipeId());
            stmt.setString(3, intervencao.tipo());
            stmt.setString(4, intervencao.descricao());
            stmt.setDate(5, Date.valueOf(intervencao.dataAgendada()));
            stmt.setString(6, intervencao.status().name());
            stmt.setLong(7, intervencao.id());
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean deletar(long id) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(DELETAR)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() == 1;
        }
    }

    private void preencherDados(PreparedStatement stmt, IntervencaoOperacionalRegistro intervencao)
            throws SQLException {
        stmt.setLong(2, intervencao.trechoId());
        stmt.setLong(3, intervencao.equipeId());
        stmt.setString(4, intervencao.tipo());
        stmt.setString(5, intervencao.descricao());
        stmt.setDate(6, Date.valueOf(intervencao.dataAgendada()));
        stmt.setString(7, intervencao.status().name());
    }

    private IntervencaoOperacionalRegistro mapear(ResultSet rs) throws SQLException {
        return new IntervencaoOperacionalRegistro(
                rs.getLong("ID"),
                rs.getLong("TRECHO_ID"),
                rs.getLong("EQUIPE_ID"),
                rs.getString("TIPO"),
                rs.getString("DESCRICAO"),
                rs.getDate("DATA_AGENDADA").toLocalDate(),
                StatusIntervencao.valueOf(rs.getString("STATUS")));
    }
}
