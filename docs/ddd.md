---

title: Domain-Driven Design (DDD)
nav_order: 2
parent: Report

---

# Domain-Driven Design (DDD)

## Ubiquitous language

First of all we defined the ubiquitous language:

- **User**: a registered person who logins to use the system;
- **Role**: the role of the user (base user or administrator);
- **Location**: a pair of coordinates (longitude, latitude);
- **Charging station**: a device in a specified location used to charge electric cars;
- **Car**: an electric car that needs its battery to be charged periodically;
- **Recharge**: the process of charging a car at a charging station.

## Context map

We identified two **bounded contexts**:
- **user context**: contains the entities related to the users, such as their cars and roles;
- **charging station context**: contains the entities related to the charging stations, such as their location.
The two contexts are connected by the car entity, because of the recharge concept.

```mermaid
flowchart TB
    subgraph user context
        user --- car
        user --- role
    end

    subgraph charging station context
        chargingStation --- location
    end

    chargingStation --- car
```

## Domain model

We modeled the concepts of the domain, already described in the ubiquitous language, assigning to each of them the
appropriate building block, also identifying their properties and the relationships between them.

```mermaid
---
config:
  class:
    hideEmptyMembersBox: true
---
classDiagram
    class User {
        <<interface, entity>>
        +id: String
        +username: String
        +password: String
        +role: Role
        +cars: Collection~Car~
    }
    class Role {
        <<enumeration, value-object>>
        BASE_USER
        ADMIN
    }
    class Car {
        <<interface, entity>>
        +id: String
        +plate: String
        +maxBattery: Int
        +currentBattery: Int?
    }
    class ChargingStation {
        <<interface, entity>>
        +id: String
        +power: Int
        +available: Boolean
        +enabled: Boolean
        +location: Location
    }
    class Location {
        <<interface, value-object>>
        +longitude: Double
        +latitude: Double
    }
    class Recharge {
        <<interface, value-object>>
        +carId: String
        +chargingStationId: String
    }
    Role --* User
    Car --o User
    Location --* ChargingStation
    Car --> Recharge
    ChargingStation --> Recharge
```
