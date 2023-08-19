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
#### Construa o projeto com Gradle:
```shell
./gradlew build
```

#### Inicialize a aplicação
```shell
./gradlew bootRun
```

### Documentação da API
TODO - Adicionar swagger na aplicação e adicionar link aqui
#### Controller de Tópicos (TopicController)
- **Criar Pauta**:
    - **Endpoint**:/topics
    - **Método**: POST
    - **Descrição**: Cria uma nova pauta.
    - **Curl**:
      ```shell
      curl --request POST --url http://localhost:8080/topics \
      --header 'Content-Type: application/json' \
      --data '{
        "title":"Second Topic",
        "description":"Second Topic Description"
      }'
      ```
- **Iniciar Sessão para Pauta**:
    - **Endpoint**: /topics/{topicId}/startSession
    - **Método**: POST
    - **Descrição**: Inicia uma nova sessão de votação para uma pauta específica. A duração da sessão é opcional e pode ser fornecida.
    - **Curl**:
        ```shell
        curl --request POST --url http://localhost:8080/topics/1/startSession \
        --header 'Content-Type: application/json' \
        --data '{
          "duration": 300
        }'
        ```
- **Obter Todas as Pautas**:
    - **Endpoint**: /topics
    - **Método**: GET
    - **Descrição**: Retorna uma lista de todas as pautas.
    - **Curl**:
        ```shell
        curl --request GET --url http://localhost:8080/topics
        ```
#### Controller de Votos (VoteController)
- **Votar em uma Sessão**:
    - **Endpoint**: /votes/sessions/{sessionId}/vote
    - **Método**: POST
    - **Descrição**: Permite que um membro vote em uma sessão específica.
    - **Curl**:
        ```shell
        curl --request POST  --url http://localhost:8080/votes/sessions/1/vote \
        --header 'Content-Type: application/json' \
        --data '{
          "memberCpf":"88487711499",
          "voteValue": true
        }'
        ```

### TODO
- [X] Tarefa Bônus 1 - Integração com sistemas externos
- [X] Tarefa Bônus 2 - Mensageria e filas (Kafka)
- [ ] Tarefa Bônus 3 - Performance (Jmeter)
- [ ] Tarefa Bônus 4 - Versionamento da API
- [ ] Adicionar Testes
- [ ] Adicionar Swagger
