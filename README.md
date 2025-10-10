# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

## Link to sequence diagram
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYFXBjVSjgwcVUE1IDsAM2gx2YA9girmmDtfPUPqqXpQ9TQPgQCATFKTvuq-oAkmhu8gODvsD4wFGY3HCn74cnkKmYOmnMMYNn+Xn5vUxkWS-U9B2UYS1Mw7KGhW2SdruPb9oOcjEg26AcBO2j2tO66zlQSIwIuy6rt69yTnSqi1CAqrom6QHSto3KIVOAr3MYtQKBwHZuh6iI4VSVH4YRxEoAox7xBiwB8TylE0tRgp0QxHa8XqLHOoYKEwk8pbYmieJqEuWCKXCM6Xi8EzAZ8MDvsCyyCXq7QQHWaBLMZDaXjOKYYPU4ROA+oz6eWhm2SCZnxBZVk2cslyNvBnjeH4-jQOwjIxCKcARtIcAKDAAAyEBZIUjnME61D+s0bRdL0BjqPkaBZmOKBrL8-wcFc9n3Fp-ojBVVX6H8OzQo82k5WhLowAg6XihiaUZQSRJgKSrHyexomcTAjIsmyFUiby+HicKYoSsx2iyvKFXNqqbZajqeplu8FZoBAzDikY2h6AYCGzQ6KFTQuS4rlNOmbqWeH8qeKCxqVCaUA5N5OXeTgAIxZmMOaqK+BYfsW0D1D40x8dASAAF4oB2GJdhBZ2csSwVwY9q3PT16HbfI2HTTIHH8rUC1NG1NVkedcwrUhNGVHRm0dtVOwwCOZ0Ha2MA7nuvYwKzWw7OTPNfXOroDLJc7KzU9S-eo-2A-G9XXiUYBplDMNwwj76fijCro3qmM43jBP7jAQsAiTsFNjrNFU31NPAHTgre1xwbotJ-G+cJitibRwqSQGQnaIHCldf6I3ihkqgaZgjVXhuuWluMvn+fWRlBXZ32g8bzmuTDxeWaXtlXGTpheL4AReCg6AxHEiSd93I2+FgWWCrpjTSBGKURu0EbdD0xWqKVwz11ZwPwpUucvCv6CdbCIO+-O-XpUPw3H8eY0AZNclB4z9JGCg3Bh3xAl8SXaDczHfPCjID9MoY29oBFt4GYt15D3WVIdSW4EXbhzftHNaL05JvSwp9RBBdta31UHrc8a8q63nTNDdyFt8xW2RqWNGvkHa4xgPjaBMsAEwWbl7TBmt0KRyTqghqqdSyD3PupBAmluGayanVSuKEso1zciMK4IVW7hQCCiDs-hsDig1ClNEMAADiSoNAjx6nlTR08572CVMvV+DcLxiI3twre5irK7yUqPJB-U0TaJzMNVxOiL4kmTjNCmd8FrhxfuZCxH8EFf3qALY0dju43S0GA-QECJZS0JrAix8DKb516ofTCH1r5oK1gzJ6utowAxwYbSoEiIaEKfLDF8JDCxkNRnbeIVCnZ0I7Awz28FvasL6uw2mqC-E8wZEyQMCA3FqAxGE56ESYAZAsKgGglYEDzR0UAiAID4m6ESRk5CB8GRKnVtkmcm9UqeJzJnbOudhGF2WCYnMBYGjjAeSgLc0gCyQ3CMEQIIJNjxF1CgN0nI9jfGSKANUwKlSgpBK8gAckqRYUIYCdFEQXPB4MXJSPuTop5LylTvM+d835yx-mAqhZ8JFIJwUgEhQZN8YxvjwsRYyi4KKZEtzbhFDgAB2NwTgUBOBiBGYIcB4oADZ4DcS0RWIoYNspZLyq0DoxjTEtLflmZlcw0WJmsXvQuADYVKgRZS1lOchEHJgERUOKBJkYjgNxSZ3iJq+KKf4gi80xnPwYXs3mQpInik6TEwBcS7q7JbOqKB0spLBt9X0nJ71k76J+pg7BQMKnwHlabGpz5cwNKRl+W2lCoDY1xt0uNBS2GJ0Gfk4Z1F6jWrkLapUGJXlR16bHeo8dJnHLXHWuafhtmTJNSgVtRztBrC1VycWkaABCIYYB-nGhskBbwK2Wp7Rw-Jyb6gpP3IeY8aaDZiKNvg2ugDc3w3zdbUsP5F0oH-DkGhzsZZTsYbIjtcyKDMFeRhCskA1lzBHUmvVSl6gOptU6-hgj9W3JeDiuYhL6hfJ+TAHV+9T2YvPSMBDbyPnIeJWh0wnL5H+EsA-Aamwe5IASGAcjy4IBUYAFIQBupMmINK1RyuNk49BjQmjMkKj0V5ZiQlWSzNgBAwByNQDgBAAaUBJ0EukOh9eDx9W2LE+gEEknpOUDkwpvYAB1FgW4Z49FnSlBQcAADSTLlNEtQw47qWT0IACtWNoDtSx8UUHH0uqGW6kZnqwBBJ9Z+-1MAokALAqAnZD0I0aj3TLNJq8O2WtySB1C-pvZHssei8RWbqmPkvZbRphaKEYxLY7ct6XXP9OrQHQLwcQt2tee8mZyEIlRPY3KQDeGZ0anYxwCAag0AAHJmBWjROu+rCaUHbsVaWZLB40BHhPKU-W+XEyYZNhDNytTiFvnKzbO9S6AI0N-ZdYcvhODvrJnV1C6E21bo1rhTBoywBNCkzJgz0Ax2IekJ1v1-NA1Wvk9AAUoaEkJcgX92AJozQ1nDIUR7r0ZVzF7ThHdAYqyhhR3l3BhXq4Q0zEQ+px2C020R8Gc0MBaz1iYT0lhla+qZeax9hU2B4koG+3p2TEOoAA7w1VH7+nBfA-Wq6bnTb6co-BwpqHgDtngKy2cnzXmlRXIEea2DBSRFE925IrMTPTBhXbv4Lw0nqO0at-KRAwYEfYEk4QPIBQYDcfMLxwpDQJ5TxnnPYwRPQNwheM5-ec2G3cDwAoZ3yAQBu7QPa6PUBY8u4T+eZ1pJXUtY4L-dEdrJedp-o-Qwv6+zALi6rjnxSPV59L6RIvczpD58MG8FdVfdk1-dfUevf8glN8iy30v0StOAIr5szvD1OEh-9A7mPcfXeZ+g7rxx+vC6qYxXtrFJvZFAA

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
