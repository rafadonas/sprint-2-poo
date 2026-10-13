# Evidências de requisições — Sprint 4

Registro gerado em 13/10/2026 com a API Spring Boot 4.1.1 executando na porta
`18080` e banco H2 isolado em modo de compatibilidade Oracle. O mesmo código de
controllers, services, repositories e entidades é usado com o Oracle da FIAP.

## Resultado automatizado

```text
[1] POST /api/equipes -> HTTP 201
{"id":2,"nome":"Equipe Registro","especialidade":"Roçada mecanizada","ativa":true}

[2] POST /api/trechos -> HTTP 201
{"id":2,"km":84,"alturaVegetacao":45.5,"clima":"NORMAL","monitorado":true}

[3] GET /api/trechos?alturaMinima=40 -> HTTP 200
[{"id":1,"km":42,"alturaVegetacao":56.75,"clima":"UMIDO","monitorado":true},{"id":2,"km":84,"alturaVegetacao":45.5,"clima":"NORMAL","monitorado":true}]

[4] POST /api/intervencoes -> HTTP 201
{"id":2,"trechoId":2,"trechoKm":84,"equipeId":2,"equipeNome":"Equipe Registro","tipo":"Roçada Mecanizada","descricao":"Registro funcional da API.","dataAgendada":"2030-10-15","status":"PLANEJADA"}

[5] PUT /api/intervencoes/2 -> HTTP 200
{"id":2,"trechoId":2,"trechoKm":84,"equipeId":2,"equipeNome":"Equipe Registro","tipo":"Roçada Mecanizada","descricao":"Serviço iniciado.","dataAgendada":"2030-10-15","status":"EM_ANDAMENTO"}

[6] POST /api/relatorios -> HTTP 201
{"id":2,"dataGeracao":"2026-10-13T13:38:17.781351","quantidadeUrgente":1,"quantidadeCritica":1,"quantidadeAtencao":0,"quantidadeNormal":0,"resumo":"Total: 2 | Urgente: 1 | Crítico: 1 | Atenção: 0 | Normal: 0"}

[7] GET /api/relatorios -> HTTP 200
[{"id":2,"dataGeracao":"2026-10-13T13:38:17.781351","quantidadeUrgente":1,"quantidadeCritica":1,"quantidadeAtencao":0,"quantidadeNormal":0,"resumo":"Total: 2 | Urgente: 1 | Crítico: 1 | Atenção: 0 | Normal: 0"}]

[8] GET /api/relatorios/periodo?inicio=2026-01-01T00:00:00&fim=2031-12-31T23:59:59 -> HTTP 200
[{"id":2,"dataGeracao":"2026-10-13T13:38:17.781351","quantidadeUrgente":1,"quantidadeCritica":1,"quantidadeAtencao":0,"quantidadeNormal":0,"resumo":"Total: 2 | Urgente: 1 | Crítico: 1 | Atenção: 0 | Normal: 0"}]

[9] DELETE /api/intervencoes/2 -> HTTP 204
```

Além deste registro, `MotivaApiIntegrationTest` executa quatro cenários automatizados
com MockMvc, cobrindo códigos `200`, `201`, `204`, `400` e `404`.

## Validação com o Oracle FIAP

A aplicação também foi iniciada com `ddl-auto=validate` contra o schema Oracle real.
O Hibernate validou as quatro tabelas e as quatro sequences sem alterar o banco. Em
seguida, consultas somente leitura confirmaram os endpoints:

```text
/api/equipes -> HTTP 200, registros: 2
/api/trechos -> HTTP 200, registros: 3
/api/intervencoes -> HTTP 200, registros: 1
/api/relatorios -> HTTP 200, registros: 3
```

As credenciais usadas nessa conferência foram mantidas somente em variáveis de ambiente
da sessão e não foram gravadas no projeto.
