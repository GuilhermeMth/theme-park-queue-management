# 🎡 Sistema de Gerenciamento de Parque Temático

Sistema completo para gerenciamento de filas virtuais, reservas e atrações de um parque temático.

## 📋 Índice
- [Funcionalidades](#funcionalidades)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Instalação](#instalação)
- [Como Usar](#como-usar)
- [Arquitetura](#arquitetura)

## ✨ Funcionalidades

### 🎢 Gerenciamento de Atrações
- Cadastro completo de atrações com:
    - Nome, tipo, capacidade por sessão
    - Idade mínima exigida
    - Nível de prioridade (tipos de ingresso aceitos)
    - Múltiplos horários de sessão
- Visualização de todas as atrações cadastradas
- Remoção de atrações

### 👥 Gerenciamento de Visitantes
- Cadastro de visitantes com:
    - Nome, CPF (com validação)
    - Data de nascimento
    - Email (com validação)
    - Tipo de ingresso (COMUM, PREMIUM, ELITE)
- Cálculo automático de idade
- Validação de disponibilidade para atrações

### ⏳ Filas Virtuais
- Sistema de fila com prioridade baseada no tipo de ingresso
- Visitantes com ingresso ELITE têm prioridade máxima
- Visitantes com ingresso PREMIUM têm prioridade média
- Visitantes com ingresso COMUM entram por ordem de chegada
- Processamento de sessões (atendimento em lote)
- Estimativa de tempo de espera
- Consulta de posição na fila

### 🎟 Sistema de Reservas
- Criação automática de reservas ao entrar na fila
- Status de reservas: ATIVA, CONCLUÍDA, CANCELADA
- Histórico completo de reservas por visitante
- Cancelamento de reservas

### 📊 Painel de Controle
- Dashboard com estatísticas em tempo real:
    - Total de visitantes cadastrados
    - Total de atrações
    - Reservas ativas
    - Reservas do dia
    - Pessoas em filas
- Métricas do dia:
    - Atração mais disputada
    - Visitante mais ativo
- Visualização de filas em tempo real
- Processamento manual de sessões
- Geração de relatórios completos

### 🎪 Portal do Visitante
- Interface dedicada para visitantes:
    - Busca por CPF
    - Visualização de atrações disponíveis (com base na idade e tipo de ingresso)
    - Entrada e saída de filas
    - Consulta de posição e tempo de espera
    - Histórico de reservas

## 📁 Estrutura do Projeto

```
src/main/java/com/themepark/
├── App.java                              # Classe principal
├── SystemInfo.java                       # Informações do sistema
│
├── controller/
│   ├── MainController.java               # Dashboard principal
│   ├── CadastroAtracaoController.java    # Cadastro de atrações
│   ├── CadastroVisitanteController.java  # Cadastro de visitantes
│   └── PortalVisitanteController.java    # Portal do visitante
│
├── model/
│   ├── Atracao.java                      # Modelo de atração
│   ├── Visitante.java                    # Modelo de visitante
│   ├── FilaVirtual.java                  # Sistema de fila virtual
│   ├── Reserva.java                      # Modelo de reserva
│   ├── HorarioSessao.java                # Horário de sessão
│   ├── SistemaParque.java                # Gerenciador central (Singleton)
│   ├── Estatisticas.java                 # Módulo de estatísticas
│   ├── TipoAtracao.java                  # Enum tipos de atração
│   ├── TipoIngresso.java                 # Enum tipos de ingresso
│   ├── NivelPrioridade.java              # Enum níveis de prioridade
│   ├── StatusReserva.java                # Enum status de reserva
│   │
│   └── datastructures/
│       ├── LinkedList.java               # Lista encadeada customizada
│       └── Node.java                     # Nó da lista encadeada
│
└── resources/com/themepark/view/
    ├── main-dashboard-view.fxml          # Interface do dashboard
    ├── cadastro-atracao-view.fxml        # Interface cadastro atração
    ├── cadastro-visitante-view.fxml      # Interface cadastro visitante
    └── portal-visitante-view.fxml        # Interface portal visitante
```

## 🚀 Instalação

### Pré-requisitos
- Java 11 ou superior
- JavaFX 11 ou superior
- Maven (para gerenciamento de dependências)

### Passos

1. Clone o repositório ou copie os arquivos do projeto

2. Configure o JavaFX no seu `pom.xml`:
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>17.0.2</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>17.0.2</version>
</dependency>
```

3. Execute o projeto:
```bash
mvn clean javafx:run
```

## 🎯 Como Usar

### Inicialização
Ao iniciar o sistema, dados de exemplo são carregados automaticamente:
- 5 atrações variadas
- 4 visitantes com diferentes tipos de ingresso
- Algumas reservas ativas para demonstração

### Dashboard Principal

#### Visualização de Estatísticas
- Cards coloridos mostram métricas em tempo real
- Destaques do dia indicam atração mais disputada e visitante mais ativo

#### Gerenciar Atrações
1. Clique em "➕ Nova Atração" ou use o menu "Cadastros > Nova Atração"
2. Preencha os dados da atração
3. Adicione horários de sessão
4. Clique em "✅ Salvar"

#### Gerenciar Visitantes
1. Clique em "Portal do Visitante" ou use o menu
2. Para cadastrar: use "Cadastros > Novo Visitante"
3. Preencha CPF, nome, data de nascimento, email
4. Selecione o tipo de ingresso
5. Clique em "✅ Salvar"

#### Processar Sessões
1. Vá para aba "Filas Virtuais"
2. Selecione uma fila com pessoas aguardando
3. Clique em "▶️ Processar Sessão"
4. Confirme o processamento
5. Os visitantes atendidos serão removidos da fila e suas reservas marcadas como CONCLUÍDAS

### Portal do Visitante

#### Acessar como Visitante
1. Abra o "Portal do Visitante"
2. Digite o CPF de um visitante cadastrado
3. Clique em "🔍 Buscar"

#### Entrar em Filas
1. Vá para aba "🎢 Atrações Disponíveis"
2. Selecione uma atração
3. Clique em "➕ Entrar na Fila"
4. Você verá sua posição e tempo estimado

#### Consultar Posição
1. Vá para aba "⏳ Minhas Filas"
2. Selecione uma fila
3. Clique em "📊 Ver Detalhes"
4. Visualize informações detalhadas

#### Sair da Fila
1. Na aba "⏳ Minhas Filas"
2. Selecione a fila que deseja sair
3. Clique em "❌ Sair da Fila"
4. Confirme a ação

#### Ver Histórico
1. Vá para aba "📜 Histórico"
2. Veja todas as suas reservas (ativas, concluídas e canceladas)

## 🏗 Arquitetura

### Padrões Utilizados

#### Singleton Pattern
- `SistemaParque` é implementado como Singleton para garantir uma única instância gerenciando todos os dados

#### MVC (Model-View-Controller)
- **Model**: Classes de domínio (Atracao, Visitante, FilaVirtual, etc.)
- **View**: Arquivos FXML com a interface
- **Controller**: Classes Controller gerenciam a lógica de apresentação

### Estrutura de Dados Customizada
O projeto utiliza uma **LinkedList customizada** implementada do zero, sem usar `java.util.LinkedList`:
- Implementação completa de lista encadeada
- Operações: add, remove, get, indexOf
- Usada para gerenciar todas as coleções (atrações, visitantes, filas, reservas)

### Sistema de Prioridades
```
ELITE (3)    → Acessa tudo, prioridade máxima na fila
    ↑
PREMIUM (2)  → Acessa atrações PREMIUM e COMUM, prioridade média
    ↑
COMUM (1)    → Acessa apenas atrações COMUM, sem prioridade
```

### Fluxo de Dados

1. **Cadastro de Atração** → `SistemaParque` → Cria `FilaVirtual` automaticamente
2. **Cadastro de Visitante** → `SistemaParque` → Adiciona à lista de visitantes
3. **Entrar na Fila** → Cria `Reserva` → Adiciona à `FilaVirtual` (respeitando prioridade)
4. **Processar Sessão** → Remove visitantes da `FilaVirtual` → Marca `Reserva` como CONCLUÍDA
5. **Sair da Fila** → Remove da `FilaVirtual` → Marca `Reserva` como CANCELADA

## 📈 Módulo de Estatísticas

O sistema oferece métricas detalhadas:

- **Contadores em tempo real**: visitantes, atrações, reservas ativas
- **Métricas diárias**: reservas do dia, atração mais disputada, visitante mais ativo
- **Rankings**: distribuição de tipos de ingresso, ranking de atrações
- **Taxas**: taxa de conclusão de reservas
- **Relatórios**: geração de relatório completo em texto

## 🔧 Validações Implementadas

### Visitante
- CPF com 11 dígitos
- Email em formato válido
- Data de nascimento não pode ser futura
- Cálculo automático de idade

### Atração
- Capacidade maior que zero
- Idade mínima não negativa
- Nome não vazio
- Horários com início antes do fim
- Não permite horários duplicados

### Fila Virtual
- Verifica tipo de ingresso antes de adicionar
- Respeita prioridades ao inserir na fila
- Valida capacidade ao processar sessão

## 💡 Dicas de Uso

1. **Teste com dados de exemplo**: O sistema já vem com dados pré-carregados para facilitar os testes

2. **Diferentes tipos de ingresso**: Crie visitantes com diferentes tipos para ver a diferença na prioridade

3. **Processe sessões**: Experimente processar sessões para ver as filas diminuindo e reservas sendo concluídas

4. **Verifique restrições**: Tente fazer um visitante de 10 anos entrar em uma atração com idade mínima de 12 anos

5. **Estatísticas em tempo real**: Use o botão "🔄 Atualizar" para ver as métricas atualizadas

## 🐛 Bugs Corrigidos

- ✅ Parâmetros invertidos no construtor de `HorarioSessao`
- ✅ Inconsistência na comparação de prioridades em `FilaVirtual`
- ✅ Validações de CPF e email
- ✅ Tipo de data de nascimento (`String` → `LocalDate`)

## 📝 Requisitos Atendidos

- ✅ Sistema de filas virtuais por atração
- ✅ Prioridade baseada em tipo de ingresso
- ✅ Cadastro completo de atrações e visitantes
- ✅ Sistema de reservas com status
- ✅ Múltiplas interfaces (Dashboard, Cadastros, Portal)
- ✅ Módulo de estatísticas e métricas
- ✅ **Uso de LinkedList customizada** (não usa java.util.LinkedList)
- ✅ Estrutura de dados própria implementada do zero

## 🎓 Autor

Sistema desenvolvido como projeto acadêmico para gerenciamento de parques temáticos.

---

**Versão**: 1.0  
**Data**: 2025  
**Licença**: Acadêmica