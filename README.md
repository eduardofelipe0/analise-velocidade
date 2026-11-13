
# Análise de Velocidade — Desafio Full Stack

Aplicação Full Stack desenvolvida com Java, Spring Boot e Angular
para analisar registros de velocidade de veículos a partir de um
arquivo CSV.

O sistema valida os dados, identifica episódios de excesso de
velocidade, classifica a gravidade das ocorrências e gera um
ranking de veículos por pontuação.

## Tecnologias utilizadas

### Backend
- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Maven
- JUnit 5

### Frontend
- Angular 21
- TypeScript
- HTML e CSS

## Funcionalidades

- Upload de arquivos CSV
- Validação individual das leituras
- Identificação de linhas inválidas
- Normalização de placas
- Identificação de episódios de excesso de velocidade
- Classificação da gravidade
- Ranking dos veículos
- Filtro de episódios por gravidade
- Interface responsiva
- Mensagens de carregamento e erro

## Estrutura do projeto

```text
analise-velocidade/
├── backend/
├── frontend/
├── exemplos/
├── README.md
└── DECISOES.md
```

## Pré-requisitos

- Java 21
- Node.js 22
- npm
- Git

O backend possui Maven Wrapper, portanto não é necessário
instalar o Maven globalmente.

## Como executar

### 1. Backend

Abra um terminal na pasta backend:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

A API será iniciada em:

http://localhost:8080

### 2. Frontend

Em outro terminal:

```powershell
cd frontend
npm install
npm start
```

Acesse:

http://localhost:4200

Durante o desenvolvimento, o Angular utiliza um proxy
para encaminhar as requisições /api ao Spring Boot.

## Como utilizar

1. Abra a aplicação no navegador.
2. Selecione um arquivo CSV.
3. Clique em "Analisar CSV".
4. Visualize o resumo do processamento.
5. Consulte o ranking dos veículos.
6. Visualize os episódios identificados.
7. Utilize o filtro por gravidade.
8. Consulte as linhas inválidas.

## Formato do CSV

O arquivo utiliza ponto e vírgula como separador.

```csv
placa;dataHora;velocidadeKmh;limiteKmh
ABC1D23;2026-10-05T08:00:00;72;60
DEF2G34;2026-10-05T08:05:00;75;60
```

Também são aceitos números decimais com vírgula e datas no
formato brasileiro.

## API REST

### POST /api/analises

Recebe um arquivo CSV utilizando multipart/form-data.

Nome do campo: arquivo

Exemplo:

```powershell
curl.exe -X POST "http://localhost:8080/api/analises" -F "arquivo=@exemplos/leituras.csv"
```

A resposta contém:

- resumo
- episodios
- ranking
- linhasInvalidas

## Regras de negócio

Uma leitura é considerada excesso quando a velocidade
ultrapassa o limite da via em mais de 5 km/h.

Leituras consecutivas em excesso do mesmo veículo são
agrupadas quando o intervalo entre elas é de até 5 minutos.

Uma leitura sem excesso encerra o episódio.

### Classificação

| Gravidade | Excesso | Pontos |
|-----------|---------|--------|
| Leve | Até 20% | 1 |
| Grave | Acima de 20% até 50% | 3 |
| Gravíssima | Acima de 50% | 5 |

### Ranking

Os veículos são ordenados por:

1. Maior pontuação total.
2. Maior velocidade registrada em episódio.
3. Placa em ordem alfabética.

## Testes automatizados

Execute na pasta backend:

```powershell
.\mvnw.cmd test
```

Os testes cobrem leitura e validação do CSV,
identificação de episódios, classificação da gravidade,
intervalos de tempo e critérios de ordenação.

## Build do frontend

Na pasta frontend:

```powershell
npm run build
```

## Resultado com o CSV de exemplo

- 43 linhas lidas
- 36 linhas válidas
- 7 linhas inválidas
- 13 episódios
- 8 veículos no ranking

## Decisões técnicas

As decisões tomadas durante o desenvolvimento estão
documentadas em [DECISOES.md](DECISOES.md).