# 🚕 App 100 - Aplicativo de Viagens Curtas (ED II)
[![Status](https://img.shields.io/badge/Status-Conclu%C3%ADdo%20(ED%20II)-brightgreen)]() 
[![Linguagem](https://img.shields.io/badge/Linguagem-Java-blue)]()

## 📝 Sobre o Projeto

O **Projeto 100** simula a lógica de inteligência interna de um aplicativo de transporte por aplicativo (como 99 ou Uber). O foco principal é demonstrar como estruturas de dados avançadas e algoritmos de otimização são utilizados para tomar decisões em tempo real, como calcular a rota mais rápida, selecionar o melhor motorista e otimizar corridas com múltiplas paradas.

Este projeto foi desenvolvido como requisito para a disciplina de Estrutura de Dados II.

## ⚙️ Tecnologias Utilizadas

* **Linguagem Principal:** Java

## 🧠 Aplicações de Estruturas de Dados e Algoritmos

A inteligência do projeto está na implementação pura das seguintes estruturas e algoritmos:

### 1. Otimização e Roteamento (Grafos e Programação Dinâmica)

| Conceito | Aplicação no Projeto |
| :--- | :--- |
| **Grafo Ponderado & Dijkstra** | Modelagem da malha viária e cálculo da **rota mais rápida** (caminho de menor custo). |
| **Fila de Prioridade (Min Heap)** | Implementação de **Algoritmos Gulosos** para a seleção do motorista com o menor ETA (Tempo Estimado de Chegada). |
| **Programação Dinâmica** | Otimização de **corridas com múltiplas paradas**, garantindo a sequência mais eficiente de *stops* (variações do Caixeiro Viajante). |
| **Algoritmo de Huffman** | Simulação da **compressão do histórico de logs** e rotas para otimizar o armazenamento. |

### 2. Algoritmos de Busca Implementados (Foco Acadêmico)

Aqui demonstro a aplicação direta de diferentes tipos de busca, conforme o contexto do dado:

* **Algoritmo Rabin-Karp (Busca em Texto):**
    * Utilizado no módulo de comunicação para **censura de mensagens sensíveis**. O algoritmo busca rapidamente por padrões de texto proibidos dentro das mensagens de chat trocadas entre motorista e passageiro.

* **Busca Sequencial:**
    * Aplicada na rotina que **lista os tipos de corridas disponíveis** (Ex: Econômico, Comfort, Premium). Como esta é uma lista pequena e de baixo acesso, a busca sequencial é a forma mais simples e direta de implementá-la.

* **Busca Binária:**
    * Implementada para **buscar dados de um passageiro**. É utilizada em cenários onde a lista de dados (passageiros, histórico de corridas) é **mantida ordenada por ID ou data**, garantindo um tempo de busca eficiente (logarítmico).

### 3. Análise de Complexidade

**Busca Sequencial (Tipos de Corrida):**
* **Complexidade:** $O(n)$ (Pior/Caso Médio).
* **Propósito no Sistema:** É a busca mais lenta, mas a forma mais simples de listar elementos em listas **pequenas e não ordenadas** no projeto (como os tipos de serviço).

**Busca Binária (Dados do Passageiro por ID):**
* **Complexidade:** $O(\log n)$ (Pior/Caso Médio).
* **Propósito no Sistema:** É a busca mais rápida. É crucial para capturar dados de um **passageiro específico pelo ID** em uma lista de usuários que está **mantida ordenada**, pois a cada passo, metade dos dados é eliminada.

**Algoritmo Rabin-Karp (Filtragem de Chat):**
* **Complexidade:** $O(n + m)$ (Caso Médio).
* **Propósito no Sistema:** É o algoritmo de **Busca de Padrão** mais eficiente. Utiliza *hashing* para detectar rapidamente a presença de palavras sensíveis (o padrão, $m$) dentro de longas mensagens de chat (o texto, $n$).

## 🛠️ Como Executar o Projeto

1.  Clone este repositório:
    ```bash
    git clone [LINK_DO_REPOSITORIO]
    ```
2.  Compile o projeto.
3.  Execute a classe principal.

## 👤 Autor

* [Raphael Muniz Varela]
