package br.com.motiva.service;

import br.com.motiva.dto.TrechoRequest;
import br.com.motiva.dto.TrechoResponse;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TrechoRodoviaService {

    private final TrechoRodoviaRepository repository;

    public TrechoRodoviaService(TrechoRodoviaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TrechoResponse> listar(BigDecimal alturaMinima) {
        if (alturaMinima != null && alturaMinima.signum() < 0) {
            throw new RegraNegocioException("A altura mínima não pode ser negativa.");
        }
        List<TrechoRodovia> trechos = alturaMinima == null
                ? repository.findAllByOrderByKmAsc()
                : repository.findByAlturaVegetacaoGreaterThanEqualOrderByKmAsc(alturaMinima);
        return trechos.stream().map(TrechoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public TrechoResponse buscarPorId(Long id) {
        return TrechoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public TrechoResponse criar(TrechoRequest dados) {
        validarDados(dados);
        TrechoRodovia trecho = new TrechoRodovia(
                dados.km(),
                dados.alturaVegetacao(),
                dados.clima(),
                dados.monitorado());
        return TrechoResponse.de(repository.save(trecho));
    }

    @Transactional
    public TrechoResponse atualizar(Long id, TrechoRequest dados) {
        validarDados(dados);
        TrechoRodovia trecho = buscarEntidade(id);
        trecho.atualizar(
                dados.km(),
                dados.alturaVegetacao(),
                dados.clima(),
                dados.monitorado());
        return TrechoResponse.de(trecho);
    }

    @Transactional
    public void deletar(Long id) {
        repository.delete(buscarEntidade(id));
    }

    private TrechoRodovia buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Trecho", id));
    }

    private void validarDados(TrechoRequest dados) {
        if (dados.km() == null || dados.km() < 0) {
            throw new RegraNegocioException("O km não pode ser negativo.");
        }
        if (dados.alturaVegetacao() == null || dados.alturaVegetacao().signum() < 0) {
            throw new RegraNegocioException("A altura da vegetação não pode ser negativa.");
        }
        if (dados.clima() == null) {
            throw new RegraNegocioException("O clima é obrigatório.");
        }
        if (dados.monitorado() == null) {
            throw new RegraNegocioException("Informe se o trecho é monitorado.");
        }
    }
}
