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
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYFXBjUAIRDMBRhLUzAAZt4ZmWoJg7Xz1D6ql6UK6Bh7Ed6k77qv6AJJoKgmpAcdfYHxgKMxuOFP3w5PIVMwdNOYYwbP8vPzepjIsl+p6DhdlA9nIN9AcZtVQ1U8YFfaBR20e0JyXKcqCRTVpW0BMKRg096lPMCRxAmCUwweoAFYnBvNA7zGHNVEfAsX2LaB6gxDg1BAKBiGPGAID7GBKBLP8mzHOkBVQ50ZxgNAfAQBBkMXKlIPHVRaiY4N0Tddk520bkZP4wVjFqBQOE-N15yEwU+P5eTVXRBR93iDFgCsnl1JpWStOFXTP0svVDOnScYSeUtsTRPE1DErAfLhSc0NIxVPhgZ9gWWWy9XaCA6zQJZYobbDzxKMB6nCIiswmQ0lTS5Y4rGBL4iSlKSv2K5-1MLxfACaB2EZGIRTgCNpDgBQYAAGQgLJClw5gnWof1mjaLpegMdR8jQLMopQNZfn+DgrmwypQv9EYlpW-Q-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-DyU2A5GGF7yRdPK2s9o+-aLETWrAf-bi0FfVx9DVJfmZt2NNH8sjGZ40-+Xh5v1ca2-+UiBgwF+wLNwgPIAoMdQZUjadBBJBFBNBYwGZCLDdO8ZZU6QfEAbgPABQf-ZAEAIAtAM9VAqAdAgArA48K9UkX9d5HGBXfHYfSPcpPRCDHGajS8CnejCXdvWnGFFDemNDJnBpdlUfSHDhLhWLOYAVFPPzURIQwLXjWRQ-MQPgxGeoBiIQ5SVfFeEJMRdEYcaQ+VZ5I-FXfgtXQQ8RU5VQ4VaQDQiRDnbQ9HRVY-Q1KLPAggzA7A+LK-RLWgl4O-apQmR-ZnIAA

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
