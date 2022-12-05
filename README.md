# Teste Dev Backend

## Problemas

---

Durante 3 dias a SWAPI esteve indisponível, para evitar problemas com a execução adicionei um profile na aplicação do 
desafio 1 com o nome "mock", este perfil ao invés de recuperar os dados da SWAPI irá carregá-los a partir de arquivos
JSON locais. Além disso, este perfil seria o ideal para testar a paginação, já que conta com mais de 10 filmes, mesmo
que repetidos.

## Instruções de Execução - Alternativas

---

### Maven spring-boot:run
````bash
# Acessar a raiz do repositório
# Executar a aplicação do desafio 1, neste caso com o profile "mock", basta removê-lo para utilizar a real API
mvn spring-boot:run -D"spring-boot.run.profiles=mock" -f ./desafio_1/pom.xml

# Após a aplicação anterior iniciar, basta executar a aplicação do desafio 2
mvn spring-boot:run -f ./desafio_2/pom.xml
````

### Maven Package
````bash
# Acessar a raiz do repositório
# Compilar a aplicação do desafio 1, neste caso com o profile "mock", basta removê-lo para utilizar a SWAPI
mvn package -f ./desafio_1/pom.xml

# Compilar a aplicação do desafio 2
mvn package -f ./desafio_2/pom.xml

# Executar a aplicação do desafio 1
java -jar -D"spring.profiles.active=mock" .\desafio_1\target\test.dev.backend.desafio_1-0.0.1-SNAPSHOT.jar

# Após a aplicação anterior iniciar, basta executar a aplicação do desafio 2
java -jar .\desafio_2\target\test.dev.backend.desafio_2-0.0.1-SNAPSHOT.jar
````


## Desafio 1

---
Para o desafio 1 foi proposta realizar uma consulta na [SWAPI](https://swapi.dev) para listar os filmes em que
o personagem "Luke Skywalker" aparece. Para isso são necessárias duas chamadas, primeiramente utilizar a API de
"/people" com o campo de pesquisa utilizando o nome do personagem, e então teremos como resposta várias informações
sendo que a de interesse é uma lista com todas as URIs dos filmes em que o personagem aparece na API "/films".
Essa lista foi então utilizada para apresentar todos os filmes do personagem "Luke Skywalker".

Exemplo de requisição:

```console
curl -sb -H "Accept: application/json" http://localhost:8080/luke/films
```

Exemplo de resposta:

```json
{
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8080/luke/films?page=1"
        }
    ],
    "content": [
        {
            "title": "A New Hope",
            "episode_id": 4,
            "director": "George Lucas",
            "release_date": "25/05/1977"
        },
        {
            "title": "The Empire Strikes Back",
            "episode_id": 5,
            "director": "Irvin Kershner",
            "release_date": "17/05/1980"
        },
        {
            "title": "Return of the Jedi",
            "episode_id": 6,
            "director": "Richard Marquand",
            "release_date": "25/05/1983"
        },
        {
            "title": "Revenge of the Sith",
            "episode_id": 3,
            "director": "George Lucas",
            "release_date": "19/05/2005"
        }
    ]
}
```

### Diferenciais alcançados:


- Paginação com 10 itens por página:
  - Como a forma de se listar os filmes de um personagem não provê um mecanismo para paginação, foi necessário realizar essa paginação manualmente. Ela pode ser melhor verificar rodando o desafio 1 com o profile ativo "Mock", que diferente da resposta verdadeira, retornará mais de 10 filmes.
- Filtros de "title" e "episode_id"
- Campo "release_date" no formato dd/MM/yyyy

---

## Desafio 2

---
O desafio 2 pede que seja criada uma segunda aplicação que consuma a API construída no desafio anterior, a lista de 
filmes deve então ser persistida em um banco de dados em memória (H2), inserindo em seguida um novo filme criado, e 
registrar no log da aplicação todos os filmes. Caso o resultado esteja paginado, todas as páginas serão carregadas 
iterativamente.

### Diferenciais alcançados:

- Registro dos filmes no console da aplicação
  - Diversas informações são registradas no log, como os filmes recuperados da API desenvolvido, o novo filme inserido, e ao final, todos os filmes registrados no log
- Salvar os filmes em um banco de dados H2
  - Todos os filmes são persistidos ao iniciar a aplicação e é possível acessar o console do H2 para confirmar isto, basta acessar "http://localhost:8081/h2-console" e logar com as seguintes credenciais:
    - Driver Class: org.h2.Driver
    - JDBC URL: jdbc:h2:mem:testdb
    - User Name: sa
    - Password: password