package dao;

import model.RelatorioPrioridade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** CRUD e operação de gravação do histórico de relatórios. */
public class RelatorioPrioridadeDAO extends DaoSupport {
    private static final String PROXIMO_ID =
            "SELECT SEQ_RELATORIO_PRIORIDADE.NEXTVAL FROM DUAL";
    private static final String INSERIR =
            "INSERT INTO RELATORIO_PRIORIDADE "
                    + "(ID, DATA_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String BUSCAR_POR_ID =
            "SELECT ID, DATA_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO "
                    + "FROM RELATORIO_PRIORIDADE WHERE ID = ?";
    private static final String LISTAR_TODAS =
            "SELECT ID, DATA_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO "
                    + "FROM RELATORIO_PRIORIDADE ORDER BY DATA_GERACAO DESC, ID DESC";
    private static final String ATUALIZAR =
            "UPDATE RELATORIO_PRIORIDADE SET DATA_GERACAO = ?, QT_URGENTE = ?, "
                    + "QT_CRITICO = ?, QT_ATENCAO = ?, QT_NORMAL = ?, RESUMO = ? WHERE ID = ?";
    private static final String DELETAR =
            "DELETE FROM RELATORIO_PRIORIDADE WHERE ID = ?";

    public RelatorioPrioridadeDAO() {
    }

    public RelatorioPrioridade salvarRelatorio(int quantidadeUrgente, int quantidadeCritica,
                                                int quantidadeAtencao, int quantidadeNormal,
                                                String resumo) throws SQLException {
        RelatorioPrioridade relatorio = new RelatorioPrioridade(
                0, LocalDateTime.now(), quantidadeUrgente, quantidadeCritica,
                quantidadeAtencao, quantidadeNormal, resumo);
        return inserir(relatorio);
    }

    public RelatorioPrioridade inserir(RelatorioPrioridade relatorio) throws SQLException {
        long id = proximoId(PROXIMO_ID);
        try (PreparedStatement stmt = conexao().prepareStatement(INSERIR)) {
            stmt.setLong(1, id);
            preencherDados(stmt, relatorio, 2);
            stmt.executeUpdate();
        }
        return relatorio.comId(id);
    }

    public Optional<RelatorioPrioridade> buscarPorId(long id) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(BUSCAR_POR_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    public List<RelatorioPrioridade> listarTodas() throws SQLException {
        List<RelatorioPrioridade> relatorios = new ArrayList<>();
        try (PreparedStatement stmt = conexao().prepareStatement(LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                relatorios.add(mapear(rs));
            }
        }
        return relatorios;
    }

    public boolean atualizar(RelatorioPrioridade relatorio) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(ATUALIZAR)) {
            preencherDados(stmt, relatorio, 1);
            stmt.setLong(7, relatorio.id());
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean deletar(long id) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(DELETAR)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() == 1;
        }
    }

    private void preencherDados(PreparedStatement stmt, RelatorioPrioridade relatorio, int inicio)
            throws SQLException {
        stmt.setTimestamp(inicio, Timestamp.valueOf(relatorio.dataGeracao()));
        stmt.setInt(inicio + 1, relatorio.quantidadeUrgente());
        stmt.setInt(inicio + 2, relatorio.quantidadeCritica());
        stmt.setInt(inicio + 3, relatorio.quantidadeAtencao());
        stmt.setInt(inicio + 4, relatorio.quantidadeNormal());
        stmt.setString(inicio + 5, relatorio.resumo());
    }

    private RelatorioPrioridade mapear(ResultSet rs) throws SQLException {
        return new RelatorioPrioridade(
                rs.getLong("ID"),
                rs.getTimestamp("DATA_GERACAO").toLocalDateTime(),
                rs.getInt("QT_URGENTE"),
                rs.getInt("QT_CRITICO"),
                rs.getInt("QT_ATENCAO"),
                rs.getInt("QT_NORMAL"),
                rs.getString("RESUMO"));
    }
}
