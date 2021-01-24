<h1 align="center" >:books: Sky Library API</h1>

<p align="center" >Projeto de API para bibliotecas criada com Spring Boot.</p>

<p align="center" >
    <img src="https://user-images.githubusercontent.com/59753526/105465822-316a2880-5c72-11eb-97b8-d1309b8f31b1.png" />
</p>

---

## :rocket: Tecnologias

* [Spring Boot](https://spring.io/projects/spring-boot) - Framework Java para criação de APIs REST.

* [Project Lombok](https://projectlombok.org/) - Biblioteca Java que ajuda a remover a verbosidade do nosso código usando anotações.

* [Map Struct](https://mapstruct.org/) - Framerowk Java para mapear DTOs.

* [JUnit5](https://junit.org/junit5/) - Framework Java para testes unitários.

* [H2](http://www.h2database.com/html/features.html) - Banco de dados em memória.

* [Postgres](https://www.postgresql.org/) - Banco de dados relacional.

## :muscle: Motivação

Este é um projeto simples, criado apenas para exercitar o que foi estudado sobre Spring Boot e algumas outras tecnologias.

Caso goste do projeto, e queira contribuir de algum modo, você tem total liberdade para abrir um pull request.

O projeto é Open Source, então você pode usar o código como bem entender.

## :wrench: Como executar

Antes de tudo, tenha certeza de ter instalado o Postgres em sua máquina.

- Faça o download do projeto

- Execute o comando: `./gradlew bootRun`

- Caso não queira utilizar outro login no postges execute o comando: `./gradlew bootRun --args="--spring.datasource.username=${SEU_USUARIO},--spring.datasource.password=${SUA_SENHA}"`

## :heart: Contribuições

Caso queria contribuir com o projeto, por favor, abra um pull request.
