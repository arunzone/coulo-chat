## Run the application

### Database

Application needs database, so database can be spin up in docker container
`docker run --name oculo-chat -p 5432:5432 -e POSTGRES_PASSWORD=xKeye1RL9yIJf0BBwk40fQsGMjPzmmxtqyrLmZ94B8Sg4hoF8T -d postgres`

1. Import the spring boot project to your IDE(Optional).
2. Run the project backend with IDE or ./gradlew bootrun

### Test

Send message
`curl -XPOST -H "Content-Type: application/json" -d '{"sender": "8d1208fc-f401-496c-9cb8-483fef121234", "recipients": ["e6b920b7-4ac4-4b62-aea7-36f75e3ad610"], "content": "Hey"}' localhost:8080/api/messages`
Receive message
`curl -XGET -H "Content-Type: application/json" localhost:8080/api/messages/senders/8d1208fc-f401-496c-9cb8-483fef121234/recipients/e6b920b7-4ac4-4b62-aea7-36f75e3ad610`
Xml output
`curl -XGET -H "Content-Type: application/json" -H "accept: application/xml" localhost:8080/api/messages/senders/8d1208fc-f401-496c-9cb8-483fef121234/recipients/e6b920b7-4ac4-4b62-aea7-36f75e3ad610`