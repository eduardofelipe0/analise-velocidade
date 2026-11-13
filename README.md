# Análise de Velocidade — Desafio Full Stack

Aplicação Full Stack desenvolvida com **Java, Spring Boot e Angular** para analisar registros de velocidade de veículos a partir de arquivos CSV.

O sistema identifica leituras acima do limite permitido, agrupa essas leituras em episódios de excesso de velocidade e gera um ranking dos veículos de acordo com a gravidade das ocorrências.

## Tecnologias utilizadas

**Backend**
- Java 21
- Spring Boot 4.1.2
- Maven
- JUnit 6.0.3

**Frontend**
- Angular 21
- TypeScript 5.9
- HTML e CSS

## Funcionalidades

- Upload e processamento de arquivos CSV.
- Validação individual das leituras, sem interromper o processamento por erros em registros específicos.
- Identificação dos episódios de excesso de velocidade.
- Classificação dos episódios em leves, graves ou gravíssimos.
- Ranking de veículos por pontuação, velocidade máxima e placa.
- Visualização das linhas inválidas e dos motivos de rejeição.
- Filtro de episódios por gravidade, sem novas requisições à API.
- Interface responsiva, com indicadores de carregamento e mensagens de erro.

## Estrutura do projeto

```text
analise-velocidade/
├── backend/       # API REST e regras de negócio
├── frontend/      # Interface Angular
├── exemplos/      # CSV para testes
├── README.md
└── DECISOES.md
```

## Pré-requisitos

- JDK 21
- Node.js 22
- npm
- Git (para clonar o repositório)

## Como executar

### 1. Backend

Abra um terminal na pasta `backend`:

**Windows (PowerShell):**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**
```bash
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

### 2. Frontend

Em outro terminal, na pasta `frontend`, execute:

```bash
npm install
npm start
```

Acesse `http://localhost:4200`.

Durante o desenvolvimento, o Angular utiliza um proxy para encaminhar as requisições `/api` ao backend.

## Como utilizar

1. Abra a interface no navegador.
2. Selecione um arquivo CSV.
3. Clique em **Analisar CSV**.
4. Visualize o resumo do processamento, o ranking dos veículos e os episódios identificados.
5. Utilize o filtro por gravidade para consultar os episódios.
6. Consulte a tabela de linhas inválidas, caso existam.

## Formato do arquivo

O CSV deve conter o cabeçalho:

```csv
placa;dataHora;velocidadeKmh;limiteKmh
ABC1D23;2026-10-05T08:00:00;72;60
DEF2G34;2026-10-05T08:05:00;75;60
```

Os campos são separados por ponto e vírgula (`;`).

Também são aceitos valores decimais com vírgula e datas no formato `dd/MM/yyyy HH:mm:ss`.

## API REST

**POST `/api/analises`**

Recebe um arquivo CSV utilizando `multipart/form-data`, com o nome de campo `arquivo`.

Exemplo, a partir da raiz do repositório:

```powershell
curl.exe -X POST "http://localhost:8080/api/analises" -F "arquivo=@exemplos/leituras.csv"
```

A resposta JSON contém:

- `resumo`: linhas lidas, válidas, inválidas e total de episódios.
- `episodios`: informações e classificação de cada episódio.
- `ranking`: veículos ordenados pela pontuação e critérios de desempate.
- `linhasInvalidas`: número da linha e motivo da rejeição.

Arquivos vazios ou com cabeçalho incorreto retornam HTTP 400.

## Regras de negócio

Uma leitura é considerada excesso quando:

`velocidade > limite + 5 km/h`

As leituras são agrupadas por veículo e ordenadas cronologicamente. Leituras consecutivas em excesso fazem parte do mesmo episódio quando o intervalo entre elas não ultrapassa cinco minutos.

Uma leitura sem excesso interrompe a sequência.

O percentual de excesso é calculado por:

`(velocidade - limite) / limite * 100`

O resultado é arredondado para uma casa decimal.

| Excesso máximo | Gravidade | Pontos |
|---|---|---|
| Até 20% | LEVE | 1 |
| Acima de 20% até 50% | GRAVE | 3 |
| Acima de 50% | GRAVISSIMA | 5 |

O ranking considera, nesta ordem, maior pontuação total, maior velocidade registrada em episódio e placa em ordem alfabética.

Veículos sem episódios não aparecem no ranking.

## Testes automatizados

Na pasta `backend`, execute:

```powershell
.\mvnw.cmd test
```

Os testes verificam validação do CSV, limites de tolerância, agrupamento e separação de episódios, gravidade e ordenação do ranking.

## Build do frontend

Na pasta `frontend`, execute:

```bash
npm run build
```

## Resultado do CSV de exemplo

Com o arquivo fornecido no desafio, obtivemos:

| Indicador | Resultado |
|---|---:|
| Linhas lidas | 43 |
| Linhas válidas | 36 |
| Linhas inválidas | 7 |
| Episódios identificados | 13 |
| Veículos no ranking | 8 |

## Decisões técnicas

As situações não especificadas pelo enunciado e as decisões adotadas durante o desenvolvimento estão descritas em [DECISOES.md](DECISOES.md).
