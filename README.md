# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMvYRqDeOrGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4NumqWogVXot3sgY87nae1N+7GWoKDgcTXS7Q97196eNwfD4PohQ+PUY4CH+KT+tbgeC+eL5cHvUexEb+4wp4l7FovFqTtYV9w5t+iWryGkqSz1PsIInnq7QQFWaBgcslwJpQzYphg9ThE4ThZhMIGfDA4HAssUHxDBcEIfsVzoBwpheL4ATQOwjIxCKcARtIcAKDAAAyEBZIUaHME61D+s0bRdL0BjqPkaA4YqnyQiCvz-ICiE1oBgp-v6wFTKBinrPofw7F80KPP+wlUEiMAIHx4oYrx-EEkSYCkuuhibjS24MkyY7yVyF6eVec7CjAYoSm6MpygaulzDApgqsGGpujAaAQMwABmvgSi0aQwMpOwwDM2THGAID6gFvJBRZVkdl2MA5Su7raHFT7uRZ-rMtMp7QEgABeKAcFGMZxoUGlJpUglpk4ACMOE5qoebzIshbFvUPhdXqPX9bsVF1hVM4CuNLaWS6jUym5gqtfUNRIOl8r5QChVyGVahxRdHmVfy9S7nIKD3vEx6nue-YOsF9S3gGQPNRiaAoJsxqnmR6Cua1mlmf6Dnihkqg-nFWnwr61TabhMVLWpkGI7B1aQlcY0TcgqYwJhTgwHJeFkxBxGU+RNNxdRpieN4fj+F4KDoDEcSJKL4sOb4WCCZdRMlg00gRtxEbtBG3Q9FJqgycMJFbXkBT1CRSNoC1Sto7C-pm1TFuYPjiutvUNn2HL9l8XLTlqC5piox9B3eWA-2A9B9vEnFIOzpU86heKd5Q-IsrynbcFvQl6owAAkmgVAmkgy5p+L+3bgBzptilnYIJbrblyJJadYbUB9QNQ0oLGMm14myYM+hMDprNoxjPNi0LMsK3QGtG3xFtA01vzpdVYTLuQw+zXvVSl5fUYKDcPup5h6REdL6DschdIe9MoY-3ne9L7o0Bpk2-C7VP8hr-0yUYAYVhWZXLWGigt6L+BRMufw2BxQam4miGAABxJUGgFZv3qA0OBGttb2CVAbbqLderGzQKbbmJc6YPBfkQ8O6dHaP2didSuyAcgIJzPZNETC1A+xJP7Cugpo6DhgIyEOh9i5oCnIFM+Qp6hhUTuvZOUVhEZ1VBqXO+dkBF2IQ7Xh9c6Htmrt3XsKDJQzznoNaMHcRrdxQkdSaA8ZpzX5GPcCk8SzrWbq3HagDT6zmqqdW+G8A5bzEfSayrDEEYlEZ9GOEj4FMlLDXNh51NFHSujxNEAAeeJ2hyhcLrkdfG9QYGMMQdjXG1CX5aOJlgnMCwGguBcJ0WmVsrF9x-kzP+w9KlqGqbU+pHjgHCw4AAdjcE4FALN-ARmCHANiAA2eAI5DBsLisgleNRUGtA6Jg7BRi8EEIocfOCOEOkADk9JqQaQ3a2b49nmxgAAahgOMY5pzKKlLfLQqyP10RsIxHAeZbCOF+0wJnDUABZbIesHD8KVCc2KGI9DLhRISX2pInZJKVvUAAQiGAF7dO4FAsZ-eAzSprYWHqPfMjiixT1lCGGAiLnILzrP4mQ28gkCNDsI8JB1rwhSkWvM8zU5HqIUYlHOecC5qMoSXRJ3jK61RrvfFZzwm64Lcbi8xH9ULEpsUPbM9iKVjCcdPVx21GVAJlSsqyvjk6KpZYEvhnyUDfI6cDVlh1z71FzgMZgGTk4wzhlCuY2dpBcrLmi1eHTg3ZLoc2PJDyxiRukOPfwKbn5vPDas+Nibk2ps1U07+v9SWPKVMGnN-hdpALosLSwe8bLw1iEgBIYAa1dggPDAAUhAcU8ClQxGSKANURRmm0NEk0ZkEkegdJwZtHZI1rn2xwtgBAwAa1QDgBAGyUA9gAHUWDZ01j0DF3EFBwAANLfGzfUFNubSFxvkfc8YS6V2UHXZu8enME0lqTVe3NqLLWnQAFZdrQN8zt4p-koCRZwoFiiYBgukg4Ho4oVGFzysu1dr7oAwCw4mmAcLaX0uRa88yBisUcBxaYvFDsxq9wLTYotI99UcyNTShFkGGUVujb2AJES+HsqEeoyOnj3VRL5da4AKcEZSodjB0VyiJVSf2dKt1Wiaq6NtYBV02y1WUZGnm2jjNB52NzBSieVLnHadNRcPme0LXHStUnCTm87W8dqAI51X7Q1BQ9fHCUvqJNRUTfFWDizpAQFeqlZgVpYHCdU6deVejnxKpLGRijw0u76a-oZtpeqTPMfM-UeFdL2O+zNaYOzyT-OJfcv+yuL0QAakI7iZcIAN1YYMHIGARxYzymC6KignWTRmnlGGOCGn0UBjLKGcMaB1UZZo1l-u6ZMxkqYwWArpZhswErNWTjzLeFrWwFoL5Sp2RHd+hwfzXnxFx2ZOd9EPa5gyifaugUXGktkKuQ8tNJHksvHOT3RbLTmb-16X0gIXgV0S0bVLKAUPEDBlgMAbAS7CAEKWcOgxKs1Yay1r0YwebKhxpANwPAYTiOWNq99UnUBye2oOzAEniPyeJJ85ffehgTQ1zeKoNYHTeeKfNm921caRg-csQYsXmWiV0ZB6MTjQA)

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

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
