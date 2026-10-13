package br.com.motiva.service;

import br.com.motiva.dto.EquipeRequest;
import br.com.motiva.dto.EquipeResponse;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.repository.EquipeManutencaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipeManutencaoService {

    private final EquipeManutencaoRepository repository;

    public EquipeManutencaoService(EquipeManutencaoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EquipeResponse> listar(Boolean ativa) {
        List<EquipeManutencao> equipes = ativa == null
                ? repository.findAllByOrderByNomeAsc()
                : repository.findByAtivaOrderByNomeAsc(ativa);
        return equipes.stream().map(EquipeResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public EquipeResponse buscarPorId(Long id) {
        return EquipeResponse.de(buscarEntidade(id));
    }

    @Transactional
    public EquipeResponse criar(EquipeRequest dados) {
        validarDados(dados);
        EquipeManutencao equipe = new EquipeManutencao(
                dados.nome().trim(),
                dados.especialidade().trim(),
                dados.ativa());
        return EquipeResponse.de(repository.save(equipe));
    }

    @Transactional
    public EquipeResponse atualizar(Long id, EquipeRequest dados) {
        validarDados(dados);
        EquipeManutencao equipe = buscarEntidade(id);
        equipe.atualizar(
                dados.nome().trim(),
                dados.especialidade().trim(),
                dados.ativa());
        return EquipeResponse.de(equipe);
    }

    @Transactional
    public void deletar(Long id) {
        repository.delete(buscarEntidade(id));
    }

    private EquipeManutencao buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe", id));
    }

    private void validarDados(EquipeRequest dados) {
        if (dados.nome() == null || dados.nome().isBlank()) {
            throw new RegraNegocioException("O nome da equipe é obrigatório.");
        }
        if (dados.especialidade() == null || dados.especialidade().isBlank()) {
            throw new RegraNegocioException("A especialidade da equipe é obrigatória.");
        }
        if (dados.ativa() == null) {
            throw new RegraNegocioException("Informe se a equipe está ativa.");
        }
    }
}
