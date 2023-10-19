![Build](https://github.com/andreschaffer/event-sourcing-cqrs-examples/workflows/Build/badge.svg)
[![Test Coverage](https://api.codeclimate.com/v1/badges/299df5b2515003778612/test_coverage)](https://codeclimate.com/github/andreschaffer/event-sourcing-cqrs-examples/test_coverage)
[![Maintainability](https://api.codeclimate.com/v1/badges/299df5b2515003778612/maintainability)](https://codeclimate.com/github/andreschaffer/event-sourcing-cqrs-examples/maintainability)
[![Dependabot](https://img.shields.io/badge/Dependabot-enabled-blue?logo=dependabot)](https://docs.github.com/en/github/administering-a-repository/keeping-your-dependencies-updated-automatically)

# Event Sourcing and CQRS Examples
This project aims to provide examples of how to use Event Sourcing and CQRS applied to a minimalistic bank context.  

We assume the reader has basic knowledge of Event Sourcing and CQRS concepts.  
If you want to brush up on the subject we suggest reading:  
- [https://martinfowler.com/eaaDev/EventSourcing.html](https://martinfowler.com/eaaDev/EventSourcing.html)
- [https://martinfowler.com/bliki/CQRS.html](https://martinfowler.com/bliki/CQRS.html)

## Domain overview
In this minimalistic bank, a _client_ can _open_ one or more _accounts_.  
On each _account_, the _client_ can _deposit_ or _withdraw_ money.  
The history of an _account's transactions_ is available to the _client_ as well as a summary of the _client's accounts_.

## Design choices
### Architecture overview
      Event Store   Projections
        +----+        +----+
        |    |        |    |
        | DB |        | DB |
        +--+-+        +-+--+
          ^             ^
          |             |
    +------------+------------+
    |     |      |      |     |
    |     |    Events   |     |
    |     +------+----+ |     |
    |     |      |    | |     |
    |     +      |    v +     |
    |   Domain   |   Read     |
    |   Model    |   Model    |
    |            |            |
    +------------+------------+
    |                         |
    |           API           |
    |                         |
    +-------------------------+ 

#### Ports and Adapters
For the Domain Model, we chose the Ports and Adapters structure because we wanted to protect the domain logic from
all the technical concerns.

For more information about it read [here](http://www.dossier-andreas.net/software_architecture/ports_and_adapters.html).

#### Package by Feature
For the Read Models, we chose the Package by Feature structure because we would not benefit from isolating the layers
and instead we put all feature related parts close together. 

For more information about it read [here](http://www.javapractices.com/topic/TopicAction.do?Id=205).

### DDD and REST
There has been a myth of DDD and REST being incompatible due to DDD being all about behaviour
whereas REST is all about state.  
In this project we followed both techniques quite strictly and hope that the result shows that they can be well combined.  
Note: We did not include REST hypermedia controls as we believe it is a big subject in itself and didn't want to shift focus from Event Sourcing and CQRS.

### Event Sourcing and CQRS (finally!)
We have taken a pragmatic approach when combining Event Sourcing and CQRS. 
By the book, CQRS proposes a complete separation between the read/query and write/command sides,
but that's not what we have here.
The approach we've taken instead:
- The writes/commands are all on the domain model side and processed by aggregates;
- The reads/queries are both in the domain model side and in the read model side.
  - The queries in the domain model side are only allowed when the data we need is a single aggregate itself.
    The reason being that we can only query the event store by aggregate id
    and we can actually fulfill those queries by replaying that single aggregate events.
  - For any other kind of query, we don't want to compromise the domain model.
    Therefore, we create read models to fulfill those queries.
    They are basically projections, potentially built from different events and aggregates
    that can be queried by more appropriate fields. 
    
#### Events
Events are a thing from the past. It communicates a significant change that _happened_. 

##### Idempotency when replaying events
When replaying events, we don't want to execute any business logic because we can't change history. We only want to do assignments.  
A simple example is with a deposit event: instead of adding the deposited amount to the balance when replaying (business logic), we want 
the updated balance already available so that we can just assign it. This makes it possible to replay the event multiple times with the same outcome.

##### Ordering of events
In a distributed world, event timestamps are unreliable for ordering - machines have their own clocks.  
Instead we can make the ordering explicit with an event version.
In this project we use event versioning in two ways:
- In the write/command side, we use it for protecting ourselves from race conditions via optimistic locking;
- In the read/query side, we use it for commutative reasons, meaning events can come out of order and we can still handle them properly.

If you are interested in this topic, we also recommend reading about [Lamport timestamps](https://en.wikipedia.org/wiki/Lamport_timestamps) and [Vector clocks](https://en.wikipedia.org/wiki/Vector_clock) as alternatives.

## Trying it out
### Requirements
- Java 14
- Maven

### Building the application
` mvn clean verify `

### Starting the application
` java -jar target/bank-service-1.0-SNAPSHOT.jar server src/environments/development.yml `

### Examples of use
#### Create a client
` curl -v -X POST -H "Content-Type: application/json" -d '{"name":"Jane Doe", "email":"jane.doe@example.com"}' http://localhost:8080/clients `

Check the created client in the response's 'Location' header.

#### Create an account for the client
` curl -v -X POST -H "Content-Type: application/json" -d '{"clientId":"{CLIENT_ID}"}' http://localhost:8080/accounts `

Check the created account in the response's 'Location' header.

#### Make a deposit to the account
` curl -v -X POST -H "Content-Type: application/json" -d '{"amount":1000000}' http://localhost:8080/accounts/{ACCOUNT_ID}/deposits `

#### Check that you created a millionaire!
` curl -v http://localhost:8080/accounts/{ACCOUNT_ID} `

#### More operations
Go ahead and check the code! :)

# Contributing
If you would like to help making this project better, see the [CONTRIBUTING.md](CONTRIBUTING.md).  

# Maintainers
Send any other comments, flowers and suggestions to [André Schaffer](https://github.com/andreschaffer) and [Dan Eidmark](https://github.com/daneidmark).

# License
This project is distributed under the [MIT License](LICENSE).
