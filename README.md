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
- [ ] Tarefa Bônus 4 - Versionamento da API
- [ ] Adicionar Testes
- [ ] Adicionar Swagger
