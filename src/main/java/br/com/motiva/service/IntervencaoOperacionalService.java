package br.com.motiva.service;

import br.com.motiva.dto.IntervencaoRequest;
import br.com.motiva.dto.IntervencaoResponse;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.EquipeManutencaoRepository;
import br.com.motiva.repository.IntervencaoOperacionalRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IntervencaoOperacionalService {

    private final IntervencaoOperacionalRepository repository;
    private final TrechoRodoviaRepository trechoRepository;
    private final EquipeManutencaoRepository equipeRepository;

    public IntervencaoOperacionalService(
            IntervencaoOperacionalRepository repository,
            TrechoRodoviaRepository trechoRepository,
            EquipeManutencaoRepository equipeRepository) {
        this.repository = repository;
        this.trechoRepository = trechoRepository;
        this.equipeRepository = equipeRepository;
    }

    @Transactional(readOnly = true)
    public List<IntervencaoResponse> listar(String tipo) {
        List<IntervencaoOperacional> intervencoes = tipo == null || tipo.isBlank()
                ? repository.findAllByOrderByDataAgendadaAscIdAsc()
                : repository.findByTipoIgnoreCaseOrderByDataAgendadaAsc(tipo.trim());
        return intervencoes.stream().map(IntervencaoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public IntervencaoResponse buscarPorId(Long id) {
        return IntervencaoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public IntervencaoResponse criar(IntervencaoRequest dados) {
        Referencias referencias = validarEBuscarReferencias(dados);
        IntervencaoOperacional intervencao = new IntervencaoOperacional(
                referencias.trecho(),
                referencias.equipe(),
                dados.tipo().trim(),
                normalizarDescricao(dados.descricao()),
                dados.dataAgendada(),
                dados.status());
        return IntervencaoResponse.de(repository.save(intervencao));
    }

    @Transactional
    public IntervencaoResponse atualizar(Long id, IntervencaoRequest dados) {
        IntervencaoOperacional intervencao = buscarEntidade(id);
        Referencias referencias = validarEBuscarReferencias(dados);
        intervencao.atualizar(
                referencias.trecho(),
                referencias.equipe(),
                dados.tipo().trim(),
                normalizarDescricao(dados.descricao()),
                dados.dataAgendada(),
                dados.status());
        return IntervencaoResponse.de(intervencao);
    }

    @Transactional
    public void deletar(Long id) {
        repository.delete(buscarEntidade(id));
    }

    private IntervencaoOperacional buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Intervenção", id));
    }

    private Referencias validarEBuscarReferencias(IntervencaoRequest dados) {
        if (dados.trechoId() == null || dados.trechoId() <= 0
                || dados.equipeId() == null || dados.equipeId() <= 0) {
            throw new RegraNegocioException("Trecho e equipe devem possuir identificadores válidos.");
        }
        if (dados.tipo() == null || dados.tipo().isBlank()) {
            throw new RegraNegocioException("O tipo da intervenção é obrigatório.");
        }
        if (dados.dataAgendada() == null || dados.status() == null) {
            throw new RegraNegocioException("Data agendada e status são obrigatórios.");
        }

        TrechoRodovia trecho = trechoRepository.findById(dados.trechoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Trecho", dados.trechoId()));
        EquipeManutencao equipe = equipeRepository.findById(dados.equipeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe", dados.equipeId()));
        if (!equipe.isAtiva()) {
            throw new RegraNegocioException("Não é possível atribuir uma equipe inativa.");
        }
        return new Referencias(trecho, equipe);
    }

    private String normalizarDescricao(String descricao) {
        return descricao == null || descricao.isBlank() ? null : descricao.trim();
    }

    private record Referencias(TrechoRodovia trecho, EquipeManutencao equipe) {
    }
}
