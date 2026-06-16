# Sistema de Monitoramento e Priorização de Roçada (Sprint 2)

Este projeto faz parte do Challenge Sprint 2 e foca no desenvolvimento de um "Motor de Regras" para o monitoramento de vegetação em rodovias, utilizando conceitos avançados de Programação Orientada a Objetos (POO) em Java.

## 🚀 Objetivo
Automatizar a identificação de trechos de rodovia que necessitam de intervenção (roçada manual ou mecanizada) com base no crescimento da vegetação, que varia conforme o clima de cada região.

## 🛠️ Tecnologias e Conceitos Utilizados
- **Java 17+**: Linguagem base.
- **Classes Abstratas**: Para modelar intervenções genéricas que não podem ser instanciadas.
- **Interfaces**: Para definir o contrato de monitoramento IoT, permitindo o desacoplamento.
- **Herança e Polimorfismo**: Para tratar diferentes tipos de trechos e intervenções de forma uniforme.
- **Enums**: Para gerenciar comportamentos de crescimento baseados no clima (Úmido, Seco, Normal).

## 📂 Estrutura de Arquivos (Pasta `src/`)
- `Main.java`: Ponto de entrada que simula o cenário e executa o motor.
- `IntervencaoOperacional.java`: Classe **Abstrata** base para os serviços.
- `RocadaMecanizada.java`: Implementação de serviço pesado.
- `Pulverizacao.java`: Implementação de serviço químico/preventivo.
- `MonitoravelViaIoT.java`: **Interface** que define o comportamento de sensores.
- `TrechoRodovia.java`: Modelo base de um trecho de estrada.
- `TrechoRodoviaMonitorado.java`: Trecho que herda de rodovia e implementa a interface IoT.
- `TipoClima.java`: Enum com os fatores de crescimento.
- `MotorPriorizacao.java`: O algoritmo que analisa os dados e gera o relatório.

## 🏁 Como Executar
No terminal, dentro da pasta raiz do projeto, utilize os seguintes comandos:

1. **Compilar os arquivos:**
```bash
javac src/*.java
```

2. **Executar o programa:**
```bash
java -cp src Main
```

## 🧠 Perguntas de Reflexão (Respostas)

### 1. Por que não faz sentido para a Motiva que uma equipe execute apenas uma "Intervenção Operacional" genérica sem especificar qual é?
**Resposta:** Na arquitetura do sistema, `IntervencaoOperacional` é uma abstração pura. No mundo real, uma ordem de serviço precisa de especificidade: você não envia uma equipe para "fazer algo genérico", você envia para "fazer roçada mecanizada" ou "pulverizar". Ao marcar a classe como `abstract`, garantimos que o sistema nunca crie uma intervenção vazia ou incompleta, forçando a definição de um tipo concreto que contenha a lógica específica de execução.

### 2. Qual a diferença arquitetural entre fazer um Trecho herdar de uma classe abstrata vs. implementar uma Interface?
**Resposta:** 
- **Herança (Classe Abstrata):** Representa o que o objeto **É** (um TrechoRodovia). Ela permite o reuso de código (como os atributos `km` e `altura`) e estabelece uma relação de "é um".
- **Interface:** Representa o que o objeto **CONSEGUE FAZER** (um contrato de comportamento). Ao usar a interface `MonitoravelViaIoT`, estamos dizendo que aquele trecho tem a *capacidade* de transmitir dados. Isso permite que outros objetos (que talvez não sejam trechos de rodovia, como um caminhão da frota) também possam ser "Monitoráveis" no futuro sem precisar herdar da mesma árvore genealógica de classes.

---
*Projeto desenvolvido para fins acadêmicos - Sprint 2 POO.*
