# MOTIVA — Sprint 3

Sistema acadêmico de monitoramento e priorização de roçada em rodovias. Nesta sprint, o projeto das Sprints 1 e 2 foi organizado em pacotes e passou a persistir equipes, trechos, intervenções e relatórios em um banco Oracle usando JDBC puro.

## Integrantes do grupo

- Pedro Henrique dos Santos Cardoso — RM 563268
- Gabriel Gibin Leoncio — RM 565462
- Rafael do Nascimento Silva — RM 566263
- Rai Augusto Ribeiro — RM 562870
- Guilherme Morais de Assis — RM 564198
- Lucas Werpp Franco — RM 556044

## Requisitos

- JDK 17 ou superior;
- Oracle Database disponível no laboratório;
- driver `ojdbc17.jar` compatível com o Oracle usado;
- usuário com permissão para criar tabelas e sequences no próprio schema.

## Estrutura

```text
src/
├── db/       # conexão singleton com o Oracle
├── dao/      # DAOs com inserir, buscar, listar, atualizar e deletar
├── model/    # classes de domínio, records e enums
├── service/  # geração e persistência do relatório
└── main/     # demonstração completa do sistema
```

Os DTOs de persistência são `record`s imutáveis. `TrechoRodovia` continua sendo uma classe porque mantém o comportamento de crescimento e a herança de `TrechoRodoviaMonitorado` criada na Sprint 2.

## 1. Preparar o Oracle

Os scripts recriam as tabelas do projeto. Portanto, o primeiro script apaga dados anteriores dessas quatro tabelas.

Execute, nesta ordem, usando SQL Developer, SQLcl ou a ferramenta disponibilizada no laboratório:

1. `seu-script-criacao.sql`;
2. `seu-script-dados.sql`.

O segundo script faz `COMMIT` e termina com consultas que permitem conferir a carga de teste.

## 2. Configurar a conexão

1. Copie `ojdbc17.jar` para a pasta `lib/` (o JAR não é versionado no Git).
2. Copie `db.properties.example` para `db.properties`.
3. Preencha URL, usuário e senha fornecidos pela faculdade:

```properties
db.url=jdbc:oracle:thin:@//host:1521/servico
db.usuario=seu_usuario
db.senha=sua_senha
```

O arquivo `db.properties` é ignorado pelo Git para proteger a senha. Também é possível usar as variáveis `MOTIVA_DB_URL`, `MOTIVA_DB_USUARIO` e `MOTIVA_DB_SENHA`, ou as propriedades Java `motiva.db.url`, `motiva.db.usuario` e `motiva.db.senha`.

## 3. Compilar e executar

No PowerShell, a partir da raiz do projeto:

```powershell
$fontes = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object FullName
javac -encoding UTF-8 -d out $fontes
java -cp "out;lib/ojdbc17.jar" main.Main
```

No Linux/macOS, troque o separador de classpath `;` por `:`.

Para executar o teste unitário do gerador sem acessar o Oracle:

```powershell
$fontes = Get-ChildItem -Recurse -Filter *.java src,test | ForEach-Object FullName
javac -encoding UTF-8 -d out $fontes
java -ea -cp out service.GeradorRelatorioTest
```

O `Main`:

1. testa a conexão;
2. demonstra inserir, buscar, listar, atualizar e deletar equipes;
3. demonstra o CRUD de trechos;
4. demonstra o CRUD de intervenções;
5. gera o relatório no console e o salva no Oracle;
6. consulta o histórico de relatórios;
7. encerra a conexão.

## Regras de prioridade

- **Urgente:** altura maior que 50 cm;
- **Crítico:** altura maior que 40 cm e até 50 cm;
- **Atenção:** altura maior que 30 cm e até 40 cm;
- **Normal:** altura de até 30 cm.

Todos os comandos dos DAOs usam `PreparedStatement`, e `PreparedStatement`/`ResultSet` são fechados com `try-with-resources`.
