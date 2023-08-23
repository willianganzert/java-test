# Vote Cooperative  - API

Um sistema de votação que permite que os membros votem em tópicos durante as sessões de votação.

## Características
- Pautas podem ser criadas para a votação dos membros.
- Pautas podem ser abertas para votação por um período de tempo especificado.
- Quando as pautas estão abertas para votação, membros podem votar em diversos tópicos durante sessões especificadas.
- Agregação e exibição de resultados para sessões de votação concluídas.
- Integração com sistemas externos para verificar a elegibilidade do membro com base em seu CPF.
- Resultados das pautas são publicados em tempo real e notificações usando Apache Kafka.

### Pré-requisitos:
- Java 17 ou mais recente
- Opcional
    - Docker (para executar PostgreSQL, Kafka e Zookeeper )

### Configuração:
#### Clone o repositório
```shell
git clone git@github.com:willianganzert/java-test.git
cd java-test
```
#### Inicialize o Postgres, Kafka e o Zookeeper
```shell
docker-compose up -d
```
#### Rodar testes unitários com Gradle:
```shell
./gradlew test
```
#### Gerar relatório de cobertura de código e validar cobertura mínima de 80% com Gradle:
```shell
./gradlew jacocoTestReport jacocoTestCoverageVerification
```

#### Construa o projeto com Gradle:
```shell
./gradlew build
```

#### Inicialize a aplicação
```shell
./gradlew bootRun
```

### Documentação da API
### Versão 1 da API
A seguir estão os serviços oferecidos pela versão 1 da API.
#### Controller de Tópicos (TopicController)
- **Criar Pauta**:
    - **Endpoint**:/api/v1/topics
    - **Método**: POST
    - **Descrição**: Cria uma nova pauta.
    - **Curl**:
      ```shell
      curl --request POST --url http://localhost:8080/api/v1/topics \
      --header 'Content-Type: application/json' \
      --data '{
        "title":"Second Topic",
        "description":"Second Topic Description"
      }'
      ```
- **Iniciar Sessão para Pauta**:
    - **Endpoint**: /api/v1/topics/{topicId}/startSession
    - **Método**: POST
    - **Descrição**: Inicia uma nova sessão de votação para uma pauta específica. A duração da sessão é opcional e pode ser fornecida.
    - **Curl**:
        ```shell
        curl --request POST --url http://localhost:8080/api/v1/topics/1/startSession \
        --header 'Content-Type: application/json' \
        --data '{
          "duration": 300
        }'
        ```
- **Obter Todas as Pautas**:
    - **Endpoint**: /api/v1/topics
    - **Método**: GET
    - **Descrição**: Retorna uma lista de todas as pautas.
    - **Curl**:
        ```shell
        curl --request GET --url http://localhost:8080/api/v1/topics
        ```
#### Controller de Votos (VoteController)
- **Votar em uma Sessão**:
    - **Endpoint**: /api/v1/votes/sessions/{sessionId}/vote
    - **Método**: POST
    - **Descrição**: Permite que um membro vote em uma sessão específica.
    - **Curl**:
        ```shell
        curl --request POST  --url http://localhost:8080/api/v1/votes/sessions/1/vote \
        --header 'Content-Type: application/json' \
        --data '{
          "memberCpf":"88487711499",
          "voteValue": true
        }'
        ```
### Versão 2 da API

A seguir estão os serviços oferecidos pela versão 2 da API.

#### Controller de Tópicos (TopicController)

- **Obter todas as Pautas sem informações de sessões**:
  - **Endpoint**: /api/v2/topics
  - **Método**: GET
  - **Descrição**: Retorna uma lista de todas as pautas.
  - **Curl**:
    ```shell
    curl --request GET --url http://localhost:8080/api/v2/topics
    ```

- **Obter tópico com informação de sessões**:
  - **Endpoint**: /api/v2/{topicId}/sessions
  - **Método**: GET
  - **Descrição**: Retorna um tópico específico juntamente com suas informações de sessão.
  - **Curl**:
    ```shell
    curl --request GET --url http://localhost:8080/api/v2/1/sessions
    ```

- **Criar pauta**:
  - **Endpoint**:/api/v2/topics
  - **Método**: POST
  - **Descrição**: Cria uma nova pauta.
  - **Curl**:
    ```shell
    curl --request POST --url http://localhost:8080/api/v2/topics \
    --header 'Content-Type: application/json' \
    --data '{
      "title":"Topic",
      "description":"Topic Description"
    }'
    ```

- **Iniciar sessão para pauta**:
  - **Endpoint**: /api/v2/topics/{topicId}/startSession
  - **Método**: POST
  - **Descrição**: Inicia uma nova sessão de votação para uma pauta específica. A duração da sessão é opcional e pode ser fornecida.
  - **Curl**:
    ```shell
    curl --request POST --url http://localhost:8080/api/v2/topics/1/startSession \
    --header 'Content-Type: application/json' \
    --data '{
      "duration": 300
    }'
    ```

---

### Performance (Jmeter)
Com base nos testes realizados com o Jmeter (Ambiente local), a aplicação consegue suportar aproximadamente 2144 requisições por segundo. O teste foi realizado com 1000 usuários simultâneos e processou um total de 1.000.001 requisições. O tempo de resposta médio foi de 462 ms, e a mediana do tempo de resposta foi de 435 ms. Adicionalmente, 90% das solicitações foram atendidas em 544 ms ou menos, enquanto que 95% das solicitações foram atendidas em menos de 586 ms e 99% em menos de 673 ms. O tempo de resposta variou entre 1 ms e 1506 ms.

É essencial destacar que o teste apresentou uma taxa de erro de 54,51%. Os erros são esperados, visto que os CPFs com final par utilizados nos testes não são válidos.

Agora é necessário entender se esse tempo de resposta é aceitável para o negócio. Caso não seja, é imperativo realizar uma análise mais aprofundada para identificar possíveis gargalos da aplicação e buscar estratégias de otimização.

##### O teste foi realizado bem como os artefatos produzidos se encontram nos links abaixo: 
- [Jmeter Test Plan](https://raw.githubusercontent.com/willianganzert/java-test/main/Voting_Load_Test.jmx)
- [Jmeter Test Results](https://raw.githubusercontent.com/willianganzert/java-test/main/Voting_Load_Test_Results.7z)
- [Jmeter HTML Report](https://raw.githubusercontent.com/willianganzert/java-test/main/html_report.7z)


### TODO
- [X] Tarefa Bônus 1 - Integração com sistemas externos
- [X] Tarefa Bônus 2 - Mensageria e filas (Kafka)
- [X] Tarefa Bônus 3 - Performance (Jmeter)
- [X] Tarefa Bônus 4 - Versionamento da API
- [ ] Adicionar Testes
- [ ] Adicionar Swagger
