# Event Sourced Bank Service
## Introduction
This project aims to provide examples of how to use Event Sourcing and CQRS applied to a minimalistic bank context.  

This project assumes that the reader has basic knowledge about the Event Sourcing and CQRS concepts.  
If you want to brush up on the subject we suggest reading:  
- [https://martinfowler.com/eaaDev/EventSourcing.html](https://martinfowler.com/eaaDev/EventSourcing.html)
- [https://martinfowler.com/bliki/CQRS.html](https://martinfowler.com/eaaDev/EventSourcing.html)

## How to use it
### Requirements
- Java 8
- Maven

### Building the application
``` mvn clean verify ```

### Starting the application
``` java -jar target/event-sourced-bank-service-1.0-SNAPSHOT.jar server src/environments/development.yml ```

### Examples of use
#### Create a client
``` curl -vvv -X POST -H "Content-Type: application/json" -d '{"name":"Jane Doe", "email":"jane.doe@example.com"}' http://localhost:8080/clients ```

Check the created client from the response 'Location' header

#### Create an account for the client
``` curl -vvv -X POST -H "Content-Type: application/json" http://localhost:8080/clients/{clientId}/accounts ```

Check the created account from the response 'Location' header

#### Make a deposit to the account
``` curl -vvv -X POST -H "Content-Type: application/json" -d '{"amount":1000000}' http://localhost:8080/accounts/{accountId}/deposits ```

#### Check that you created a millionare!
``` curl -vvv http://localhost:8080/accounts/{accountId} ```

## Design choices
### Domain overview
In this minimalistic bank, a _client_ can _open_ one or more _accounts_.  
On each _account_, the _client_ can _deposit_ or _withdraw_ money.  
A view of an _account transaction history_ is available to the _client_ along with a _summary of each of the client's accounts_.

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

For more information read [here](http://www.dossier-andreas.net/software_architecture/ports_and_adapters.html).

#### Package by Feature
For the Read Models, we chose the Package by Feature structure because we would not benefit from isolating the layers
and instead we put all feature related parts close together. 

For more information read [here](http://www.javapractices.com/topic/TopicAction.do?Id=205).

### DDD and REST
There has been quite a buzz about DDD and REST not beeing suited for each other because DDD is all about behaviour
whereas REST is all about state.  
In this project we followed both techniques quite strictly and hope that the result shows that they can be well combined.

### Event Sourcing and CQRS (finally!)
We have taken a pragmatic approach when combining Event Sourcing and CQRS. 
By the book, CQRS proposes a complete separation between the read/query and the write/command sides,
but that's not what we have here.
The approach we've taken instead:
- The writes/commands are all on the domain model side and executed by aggregates;
- The reads/queries are both in the domain model side and in the read model side.
  - The queries in the domain model side are only allowed when the data we need is a single aggregate itself.
    The reason being that we can only query the event store by aggregate id
    and we can actually fulfill those queries by replaying that single aggregate events.
  - For any other kind of query, we don't want to compromise the domain model.
    Therefore, we create read models to fulfill those queries.
    They are basically projections, potentially built from different events and aggregates
    that can be queried by more appropriate fields. 
    
#### Events
Events are a thing from the past. It communicates a significant change that has happened. 

##### Idempotency when replaying events
When replaying events, we don't want to execute any business logic because we can't change history. We only want to do assignments.  
A simple example would be with a deposit event: instead of adding the deposited amount to the balance when replaying, we want 
the updated balance already available so that we can just assign it. This makes it possible to replay the event multiple times with the same outcome.

##### Ordering of events
In a distributed world, event timestamps are unreliable for ordering - machines have their own clocks.  
Instead we can make the ordering explicit with an event version.
In this project we use event versioning in two ways:
- In the write/command side, we use it when protecting ourselves from race conditions via optimistic locking;
- In the read/query side, we use it for commutative reasons, meaning events can come out of order and we can still handle them properly.

## Contact information
If you have any questions or suggestions, please ping us!
- André Schaffer ([https://github.com/andreschaffer](https://github.com/andreschaffer), [https://twitter.com/andreschaffer](https://twitter.com/andreschaffer))
- Dan Eidmark ([https://github.com/daneidmark](https://github.com/daneidmark), [https://twitter.com/daneidmark](https://twitter.com/daneidmark))
