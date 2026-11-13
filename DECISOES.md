
# Decisões Técnicas

## 1. Tratamento dos dados do CSV

Durante o desenvolvimento, algumas situações exigiram
a definição de regras de tratamento.

### Limites inválidos

Registros com limite de velocidade menor ou igual a
zero são considerados inválidos.

Essa decisão evita divisões por zero e cálculos
percentuais sem significado.

### Linhas em branco

Linhas em branco são contabilizadas como linhas lidas,
mas classificadas como inválidas.

Isso permite manter a numeração original do arquivo
para facilitar a identificação de problemas.

### Números decimais

A aplicação aceita números utilizando ponto ou vírgula
como separador decimal.

Os valores são convertidos para BigDecimal no backend,
permitindo maior controle sobre os cálculos.

### Formatos de data

São aceitos dois formatos:

- 2026-10-05T08:00:00
- 05/10/2026 08:00:00

Datas inválidas são rejeitadas.

### Normalização de placas

As placas são convertidas para letras maiúsculas,
evitando que diferenças de capitalização façam o
mesmo veículo ser tratado como veículos distintos.

### Leituras duplicadas

As leituras duplicadas são mantidas, pois não foi
estabelecida uma regra de remoção de duplicidades.

### Leituras fora de ordem

Antes da identificação dos episódios, as leituras são
agrupadas por placa e ordenadas cronologicamente.

### Episódios que atravessam a meia-noite

Foi utilizado LocalDateTime para permitir o cálculo
de intervalos entre leituras de dias diferentes.

Como o CSV não informa fuso horário, os horários
são tratados como horários locais.

### Arredondamento

O percentual de excesso é calculado com precisão
decimal e classificado antes do arredondamento.

O resultado é apresentado com uma casa decimal.

Essa escolha evita que o arredondamento visual
altere a classificação, mas deve ser revisada
caso a regra do desafio exija a classificação
do percentual já arredondado.

## 2. Organização da aplicação

O backend foi dividido em controllers, services e DTOs.

Os controllers recebem as requisições e retornam as
respostas HTTP.

Os services concentram o processamento do CSV,
a identificação dos episódios e a geração do ranking.

Os DTOs representam os dados utilizados pela aplicação.

No frontend, foi criado um serviço Angular responsável
pela comunicação com a API REST.

As informações são apresentadas por uma interface
com cartões, tabelas e filtro por gravidade.

## 3. Parte mais desafiadora

[Descreva aqui, com suas próprias palavras, qual foi a
parte mais difícil durante o desenvolvimento.]

Uma possibilidade é abordar a identificação dos
episódios, principalmente o agrupamento cronológico,
a tolerância de velocidade e os intervalos de tempo.

Explique como você resolveu essas situações e quais
testes utilizou para verificar o comportamento.

## 4. O que faria diferente com mais tempo

[Revise esta seção de acordo com sua experiência.]

Possíveis melhorias:

- Ampliar a cobertura dos testes automatizados.
- Criar testes de integração da API.
- Melhorar a acessibilidade do frontend.
- Adicionar limites configuráveis de upload.
- Testar arquivos CSV maiores.
- Melhorar o tratamento de erros de entrada.

O projeto não utiliza banco de dados porque o desafio
solicita o processamento direto do arquivo recebido.
