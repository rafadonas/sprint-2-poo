# MOTIVA — Sprint 4

API REST para monitoramento e priorização de roçada em rodovias. Nesta sprint, o
projeto foi migrado do JDBC puro para Spring Boot, Spring Data JPA e uma arquitetura
em camadas.

## Integrantes do grupo

- Pedro Henrique dos Santos Cardoso — RM 563268
- Gabriel Gibin Leoncio — RM 565462
- Rafael do Nascimento Silva — RM 566263
- Rai Augusto Ribeiro — RM 562870
- Guilherme Morais de Assis — RM 564198
- Lucas Werpp Franco — RM 556044

## Tecnologias

- Java 17;
- Spring Boot 4.1.1;
- Spring Web;
- Spring Data JPA;
- Bean Validation;
- Oracle Database e driver Maven `ojdbc17`;
- Maven Wrapper;
- JUnit 5, MockMvc e H2 em modo Oracle para testes.

## Arquitetura

```text
src/main/java/br/com/motiva/
├── MotivaApplication.java
├── controller/    # endpoints HTTP e códigos de resposta
├── dto/           # contratos de entrada e saída da API
├── exception/     # erros de negócio e tratamento global
├── model/         # entidades JPA e enums
├── repository/    # interfaces JpaRepository e derived queries
└── service/       # validações e regras de negócio
```

O fluxo das requisições é `Controller → Service → Repository → Oracle`. Não existem
`Connection`, `PreparedStatement`, `ResultSet` ou SQL manual no código Java.

## Preparar o banco Oracle

O mapeamento JPA reutiliza exatamente as tabelas e sequences da Sprint 3. Execute no
seu schema Oracle, nesta ordem:

1. `seu-script-criacao.sql`;
2. `seu-script-dados.sql`.

O script de criação remove e recria as quatro tabelas. Não o execute em um schema que
contenha dados que devam ser preservados.

## Configurar a conexão

As credenciais nunca devem ser gravadas em arquivos versionados. No PowerShell,
configure as variáveis apenas na sessão atual:

```powershell
$env:MOTIVA_DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
$env:MOTIVA_DB_USERNAME="RM000000"
$env:MOTIVA_DB_PASSWORD="SUA_SENHA"
```

Substitua os dois últimos valores pelas suas credenciais. O arquivo `.env.example`
também documenta os nomes das variáveis, mas a aplicação não lê arquivos `.env`
automaticamente.

## Executar

Não é necessário instalar o Maven. Na raiz do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`. Na inicialização, o Hibernate usa
`ddl-auto=validate`: ele verifica as tabelas existentes, mas não altera o schema.

## Testar

Os testes sobem o contexto completo da API com um banco H2 isolado em modo Oracle e
exercitam os endpoints com MockMvc:

```powershell
.\mvnw.cmd test
```

O teste integrado cobre mais de seis requisições, incluindo criação, consulta,
atualização, remoção, validação, derived queries e geração de relatório.

Os resultados de nove requisições HTTP e a validação de leitura no Oracle FIAP estão
registrados em `docs/evidencias-requisicoes.md`.

## Endpoints

| Método | Rota | Resultado esperado |
|---|---|---|
| `GET` | `/api/equipes` | Lista equipes — `200` |
| `GET` | `/api/equipes?ativa=true` | Filtra equipes ativas — `200` |
| `GET` | `/api/equipes/{id}` | Busca equipe — `200` ou `404` |
| `POST` | `/api/equipes` | Cria equipe — `201` |
| `PUT` | `/api/equipes/{id}` | Atualiza equipe — `200` ou `404` |
| `DELETE` | `/api/equipes/{id}` | Remove equipe — `204` ou `404` |
| `GET` | `/api/trechos` | Lista trechos — `200` |
| `GET` | `/api/trechos?alturaMinima=40` | Filtra por altura mínima — `200` |
| `GET` | `/api/trechos/{id}` | Busca trecho — `200` ou `404` |
| `POST` | `/api/trechos` | Cria trecho — `201` |
| `PUT` | `/api/trechos/{id}` | Atualiza trecho — `200` ou `404` |
| `DELETE` | `/api/trechos/{id}` | Remove trecho — `204` ou `404` |
| `GET` | `/api/intervencoes` | Lista intervenções — `200` |
| `GET` | `/api/intervencoes?tipo=Roçada%20Mecanizada` | Filtra por tipo — `200` |
| `GET` | `/api/intervencoes/{id}` | Busca intervenção — `200` ou `404` |
| `POST` | `/api/intervencoes` | Cria intervenção — `201` |
| `PUT` | `/api/intervencoes/{id}` | Atualiza intervenção — `200` ou `404` |
| `DELETE` | `/api/intervencoes/{id}` | Remove intervenção — `204` ou `404` |
| `POST` | `/api/relatorios` | Gera e persiste relatório — `201` |
| `GET` | `/api/relatorios` | Lista o histórico — `200` |
| `GET` | `/api/relatorios/periodo?inicio=...&fim=...` | Filtra o histórico — `200` |

Entradas inválidas retornam `400`; tentativas de remover registros referenciados
retornam `409`.

## Exemplos cURL

### 1. Criar equipe

```bash
curl -i -X POST http://localhost:8080/api/equipes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Equipe Norte","especialidade":"Roçada mecanizada","ativa":true}'
```

### 2. Criar trecho

```bash
curl -i -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d '{"km":42,"alturaVegetacao":56.75,"clima":"UMIDO","monitorado":true}'
```

Os valores aceitos para `clima` são `UMIDO`, `SECO` e `NORMAL`.

### 3. Consultar trechos com vegetação alta

```bash
curl -i "http://localhost:8080/api/trechos?alturaMinima=40"
```

### 4. Criar intervenção

Use os identificadores retornados nas duas criações anteriores:

```bash
curl -i -X POST http://localhost:8080/api/intervencoes \
  -H "Content-Type: application/json" \
  -d '{"trechoId":1,"equipeId":1,"tipo":"Roçada Mecanizada","descricao":"Vegetação acima do limite urgente.","dataAgendada":"2030-10-15","status":"PLANEJADA"}'
```

Os status aceitos são `PLANEJADA`, `EM_ANDAMENTO`, `CONCLUIDA` e `CANCELADA`.

### 5. Atualizar intervenção

```bash
curl -i -X PUT http://localhost:8080/api/intervencoes/1 \
  -H "Content-Type: application/json" \
  -d '{"trechoId":1,"equipeId":1,"tipo":"Roçada Mecanizada","descricao":"Serviço iniciado.","dataAgendada":"2030-10-15","status":"EM_ANDAMENTO"}'
```

### 6. Gerar relatório

```bash
curl -i -X POST http://localhost:8080/api/relatorios
```

### 7. Consultar histórico

```bash
curl -i http://localhost:8080/api/relatorios
```

### 8. Consultar histórico por período

```bash
curl -i "http://localhost:8080/api/relatorios/periodo?inicio=2026-01-01T00:00:00&fim=2026-12-31T23:59:59"
```

### 9. Remover uma intervenção

```bash
curl -i -X DELETE http://localhost:8080/api/intervencoes/1
```

## Regras de prioridade

Foram preservados os limiares da Sprint 3:

- `URGENTE`: altura maior que 50 cm;
- `CRITICO`: altura maior que 40 cm e até 50 cm;
- `ATENCAO`: altura maior que 30 cm e até 40 cm;
- `NORMAL`: altura de até 30 cm.

A classificação está em `RelatorioPrioridadeService`, nunca no controller. Cada
execução de `POST /api/relatorios` consulta os trechos atuais, calcula as quantidades e
salva um novo registro em `RELATORIO_PRIORIDADE`.

## Derived queries utilizadas

Os repositories demonstram geração automática de consultas por nome de método:

- `findByAlturaVegetacaoGreaterThanEqualOrderByKmAsc`;
- `findByTipoIgnoreCaseOrderByDataAgendadaAsc`;
- `findByAtivaOrderByNomeAsc`;
- `findByDataGeracaoBetweenOrderByDataGeracaoDesc`.

## Perguntas de reflexão

### Por que o Repository é uma interface? Quem escreve a implementação e quando?

O repository declara o contrato de acesso aos dados. Durante a inicialização, o Spring
Data analisa essa interface e cria uma implementação proxy em tempo de execução. Essa
implementação recebe as operações prontas do `JpaRepository` e as consultas derivadas
dos nomes dos métodos.

### O pattern DAO da Sprint 3 morreu?

Não. A responsabilidade de isolar o acesso ao banco continua existindo, mas passou do
DAO escrito manualmente para o Repository gerado pelo framework. O código repetitivo de
abrir conexão, montar SQL, preencher parâmetros e fechar recursos foi absorvido pelo
Spring Data JPA e pelo Hibernate.

### Por que `alturaVegetacao >= 0` deve ser validado no Service?

O Service representa a regra do sistema independentemente da interface usada. Se a
validação existisse apenas no Controller, uma chamada interna, um teste ou um futuro
consumidor que acessasse o Service poderia salvar um valor inválido. O DTO também usa
Bean Validation para rejeitar cedo uma requisição HTTP ruim, enquanto o Service protege
a regra de negócio.

### Onde está o SQL de `findByTipo()`? Quem o gerou?

Não existe SQL escrito no projeto para essa operação. O Spring Data interpreta o nome
do método declarado no Repository, monta uma consulta de persistência e o Hibernate a
traduz para o dialeto SQL do Oracle durante a execução.

## Segurança e Git

- Senhas e credenciais reais não são versionadas;
- `target/`, configurações de IDE e arquivos locais estão no `.gitignore`;
- o driver Oracle é obtido pelo Maven, sem JAR manual em `lib/`;
- a migração foi dividida em commits incrementais de estrutura, persistência, serviços,
  API, testes e documentação.
