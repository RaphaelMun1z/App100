# 🚕 App 100 - Aplicativo de Viagens Curtas (ED II)

## 📝 Sobre o Projeto

O **Projeto 100** simula a lógica de inteligência interna de um aplicativo de transporte por aplicativo (como 99 ou Uber). O foco principal é demonstrar como estruturas de dados avançadas e algoritmos de otimização são utilizados para tomar decisões em tempo real, como calcular a rota mais rápida, garantir a segurança do chat e oferecer uma interface resiliente a erros de digitação.

Este projeto foi desenvolvido como requisito para a disciplina de Estrutura de Dados II, apresentando uma arquitetura modularizada e limpa.

## ⚙️ Tecnologias Utilizadas

* **Linguagem Principal:** Java 25
* **Arquitetura:** Modular (Controllers/Serviços)

## 🧠 Aplicações de Estruturas de Dados e Algoritmos

A inteligência do projeto está na implementação pura das seguintes estruturas e algoritmos:

### 1. Otimização de Mapas e Resiliência (Grafos e Programação Dinâmica)

| Conceito | Aplicação no Projeto |
| ----- | ----- |
| **Grafo Ponderado & Dijkstra** | Modelagem da malha viária e cálculo da **rota mais rápida** (caminho de menor custo) entre cidades. |
| **Programação Dinâmica (Levenshtein)** | Implementação de **Busca Aproximada (Fuzzy Search)** via PD (Bottom-Up) para corrigir nomes de cidades digitados pelo usuário. |
| **Buscas em Grafo (BFS/DFS)** | Utilizadas para verificar o alcance de notificações e a conectividade entre rotas. |
| **Algoritmo de Prim (AGM)** | Otimização da rede de infraestrutura e abastecimento com o menor custo total. |
| **Ordenação Topológica** | Sequenciamento lógico de tarefas de manutenção de frota (DAG). |

### 2. Segurança e Eficiência de Dados

* **Algoritmo Rabin-Karp (Busca em Texto):**
  * Utilizado no módulo de comunicação para **monitoramento e segurança do chat**. O algoritmo utiliza *Rolling Hash* para detectar padrões de palavras sensíveis (golpes/fraudes) em tempo real.

* **Algoritmo de Huffman (Compressão):**
  * Simulação da **compressão de logs e históricos de mensagens**. Reduz o espaço em disco necessário para armazenar grandes volumes de dados de viagens.

### 3. Algoritmos de Busca (Foco Acadêmico)

* **Busca Sequencial:**
  * Aplicada na listagem de **tipos de corridas disponíveis** (Econômico, Comfort, Premium). Ideal para listas pequenas e fixas.

* **Busca Binária:**
  * Implementada para **localizar passageiros por ID**. Garante tempo logarítmico O(log n) em bases de usuários ordenadas.

## 🛠️ Como Executar o Projeto

1. Clone este repositório:
   git clone [LINK_DO_REPOSITORIO]

2. Compile o projeto através da sua IDE ou via terminal:
   javac -d bin -sourcepath src src/raphaelmun1z/Main.java

3. Execute a classe principal:
   java -cp bin raphaelmun1z.Main

## 📂 Organização do Código (Modularização)

O sistema foi refatorado para garantir a separação de responsabilidades:

* **ui**: Contém os Controladores (GrafoController, ModulosGeraisController) e o InputHandler (que integra a PD).
* **servicos**: Onde residem os algoritmos puros (Dijkstra, Rabin-Karp, Huffman, CorrecaoEndereco).
* **entidades**: Estruturas fundamentais de dados do sistema.

## 👤 Autor

* **Raphael Muniz Varela**