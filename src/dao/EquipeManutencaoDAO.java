package dao;

import model.EquipeManutencao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** CRUD da entidade EQUIPE_MANUTENCAO. */
public class EquipeManutencaoDAO extends DaoSupport {
    private static final String PROXIMO_ID =
            "SELECT SEQ_EQUIPE_MANUTENCAO.NEXTVAL FROM DUAL";
    private static final String INSERIR =
            "INSERT INTO EQUIPE_MANUTENCAO (ID, NOME, ESPECIALIDADE, ATIVA) VALUES (?, ?, ?, ?)";
    private static final String BUSCAR_POR_ID =
            "SELECT ID, NOME, ESPECIALIDADE, ATIVA FROM EQUIPE_MANUTENCAO WHERE ID = ?";
    private static final String LISTAR_TODAS =
            "SELECT ID, NOME, ESPECIALIDADE, ATIVA FROM EQUIPE_MANUTENCAO ORDER BY ID";
    private static final String ATUALIZAR =
            "UPDATE EQUIPE_MANUTENCAO SET NOME = ?, ESPECIALIDADE = ?, ATIVA = ? WHERE ID = ?";
    private static final String DELETAR =
            "DELETE FROM EQUIPE_MANUTENCAO WHERE ID = ?";

    public EquipeManutencaoDAO() {
    }

    public EquipeManutencao inserir(EquipeManutencao equipe) throws SQLException {
        long id = proximoId(PROXIMO_ID);
        try (PreparedStatement stmt = conexao().prepareStatement(INSERIR)) {
            stmt.setLong(1, id);
            stmt.setString(2, equipe.nome());
            stmt.setString(3, equipe.especialidade());
            stmt.setString(4, equipe.ativa() ? "S" : "N");
            stmt.executeUpdate();
        }
        return equipe.comId(id);
    }

    public Optional<EquipeManutencao> buscarPorId(long id) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(BUSCAR_POR_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    public List<EquipeManutencao> listarTodas() throws SQLException {
        List<EquipeManutencao> equipes = new ArrayList<>();
        try (PreparedStatement stmt = conexao().prepareStatement(LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                equipes.add(mapear(rs));
            }
        }
        return equipes;
    }

    public boolean atualizar(EquipeManutencao equipe) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(ATUALIZAR)) {
            stmt.setString(1, equipe.nome());
            stmt.setString(2, equipe.especialidade());
            stmt.setString(3, equipe.ativa() ? "S" : "N");
            stmt.setLong(4, equipe.id());
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean deletar(long id) throws SQLException {
        try (PreparedStatement stmt = conexao().prepareStatement(DELETAR)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() == 1;
        }
    }

    private EquipeManutencao mapear(ResultSet rs) throws SQLException {
        return new EquipeManutencao(
                rs.getLong("ID"),
                rs.getString("NOME"),
                rs.getString("ESPECIALIDADE"),
                "S".equals(rs.getString("ATIVA")));
    }
}
