# Decisões Técnicas

## 1. Que situações o enunciado não cobria e o que decidi?

Durante o desenvolvimento, encontrei algumas situações que não estavam totalmente definidas no enunciado e precisei estabelecer critérios para tratá-las.

- **Limite de velocidade inválido:** decidi considerar inválidas as leituras com limite menor ou igual a zero, evitando cálculos incorretos e divisões por zero.
- **Linhas em branco:** optei por contabilizá-las como linhas lidas, mas registrá-las como inválidas, mantendo a numeração original do CSV.
- **Formatos diferentes:** implementei suporte a datas nos formatos ISO e brasileiro, além de números decimais com ponto ou vírgula.
- **Placas:** normalizei as placas para letras maiúsculas, evitando que diferenças de capitalização fossem interpretadas como veículos distintos.
- **Leituras duplicadas:** optei por mantê-las, já que o enunciado não determinava a remoção de duplicidades.
- **Cálculo percentual:** utilizei `BigDecimal` para controlar a precisão e o arredondamento dos valores.

Também optei por manter o processamento em memória, sem banco de dados, já que o objetivo era receber o arquivo, processá-lo e devolver o resultado.

## 2. Qual foi a parte mais difícil e como resolvi?

A parte que exigiu mais atenção foi a identificação dos episódios de excesso de velocidade, principalmente por precisar considerar leituras fora de ordem, intervalos entre registros e mudanças no limite da via.

Para resolver isso, organizei as leituras por veículo e por data/hora antes de processá-las. Depois, implementei as regras para iniciar, continuar e encerrar cada episódio.

Os testes automatizados ajudaram a verificar situações importantes, como intervalos exatamente iguais a cinco minutos, leituras sem excesso e os limites de classificação da gravidade.

## 3. O que faria diferente com mais tempo?

Com mais tempo, ampliaria os testes automatizados, principalmente os testes de integração entre a API e o frontend, além de melhorar o tratamento de arquivos maiores e de situações inesperadas.

Também investiria em melhorias de acessibilidade e na experiência do usuário, mantendo a aplicação simples e organizada, como solicitado no desafio.