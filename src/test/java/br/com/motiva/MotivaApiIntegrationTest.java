package br.com.motiva;

import br.com.motiva.model.TipoClima;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.EquipeManutencaoRepository;
import br.com.motiva.repository.IntervencaoOperacionalRepository;
import br.com.motiva.repository.RelatorioPrioridadeRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MotivaApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IntervencaoOperacionalRepository intervencaoRepository;

    @Autowired
    private RelatorioPrioridadeRepository relatorioRepository;

    @Autowired
    private TrechoRodoviaRepository trechoRepository;

    @Autowired
    private EquipeManutencaoRepository equipeRepository;

    @BeforeEach
    void limparBanco() {
        intervencaoRepository.deleteAll();
        relatorioRepository.deleteAll();
        trechoRepository.deleteAll();
        equipeRepository.deleteAll();
    }

    @Test
    void deveExecutarCrudCompletoDeEquipeComCodigosHttpCorretos() throws Exception {
        MvcResult criacao = mockMvc.perform(post("/api/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Equipe Norte",
                                  "especialidade": "Roçada mecanizada",
                                  "ativa": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome", is("Equipe Norte")))
                .andReturn();

        long id = lerId(criacao);

        mockMvc.perform(get("/api/equipes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)));

        mockMvc.perform(put("/api/equipes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Equipe Norte Atualizada",
                                  "especialidade": "Roçada e pulverização",
                                  "ativa": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Equipe Norte Atualizada")));

        mockMvc.perform(get("/api/equipes").param("ativa", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete("/api/equipes/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/equipes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void deveExecutarCrudDeTrechoEIntervencao() throws Exception {
        long equipeId = criarEquipe();
        long trechoId = criarTrecho(42, "56.75");

        MvcResult criacao = mockMvc.perform(post("/api/intervencoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "trechoId": %d,
                                  "equipeId": %d,
                                  "tipo": "Roçada Mecanizada",
                                  "descricao": "Vegetação acima do limite urgente.",
                                  "dataAgendada": "2030-10-15",
                                  "status": "PLANEJADA"
                                }
                                """.formatted(trechoId, equipeId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.trechoId", is((int) trechoId)))
                .andExpect(jsonPath("$.equipeId", is((int) equipeId)))
                .andReturn();

        long intervencaoId = lerId(criacao);

        mockMvc.perform(get("/api/intervencoes/{id}", intervencaoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PLANEJADA")));

        mockMvc.perform(put("/api/intervencoes/{id}", intervencaoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "trechoId": %d,
                                  "equipeId": %d,
                                  "tipo": "Roçada Mecanizada",
                                  "descricao": "Serviço iniciado.",
                                  "dataAgendada": "2030-10-15",
                                  "status": "EM_ANDAMENTO"
                                }
                                """.formatted(trechoId, equipeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("EM_ANDAMENTO")));

        mockMvc.perform(get("/api/intervencoes").param("tipo", "Roçada Mecanizada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete("/api/intervencoes/{id}", intervencaoId))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/trechos/{id}", trechoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveGerarPersistirEConsultarRelatorioPorPeriodo() throws Exception {
        trechoRepository.save(new TrechoRodovia(
                10, new BigDecimal("22.50"), TipoClima.NORMAL, false));
        trechoRepository.save(new TrechoRodovia(
                25, new BigDecimal("38.00"), TipoClima.SECO, true));
        trechoRepository.save(new TrechoRodovia(
                42, new BigDecimal("56.75"), TipoClima.UMIDO, true));

        mockMvc.perform(post("/api/relatorios"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantidadeUrgente", is(1)))
                .andExpect(jsonPath("$.quantidadeCritica", is(0)))
                .andExpect(jsonPath("$.quantidadeAtencao", is(1)))
                .andExpect(jsonPath("$.quantidadeNormal", is(1)));

        mockMvc.perform(get("/api/relatorios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/api/relatorios/periodo")
                        .param("inicio", "2020-01-01T00:00:00")
                        .param("fim", "2040-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deveRetornar400ComDetalhesQuandoTrechoForInvalido() throws Exception {
        mockMvc.perform(post("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "km": -1,
                                  "alturaVegetacao": -2.5,
                                  "clima": null,
                                  "monitorado": true
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.campos.km").exists())
                .andExpect(jsonPath("$.campos.alturaVegetacao").exists())
                .andExpect(jsonPath("$.campos.clima").exists());
    }

    private long criarEquipe() throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Equipe de Teste",
                                  "especialidade": "Roçada mecanizada",
                                  "ativa": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        return lerId(resultado);
    }

    private long criarTrecho(int km, String altura) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/trechos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "km": %d,
                                  "alturaVegetacao": %s,
                                  "clima": "UMIDO",
                                  "monitorado": true
                                }
                                """.formatted(km, altura)))
                .andExpect(status().isCreated())
                .andReturn();
        return lerId(resultado);
    }

    private long lerId(MvcResult resultado) throws Exception {
        Number id = JsonPath.read(resultado.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}
