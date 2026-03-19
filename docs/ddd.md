---

title: Domain-Driven Design (DDD)
nav_order: 2
parent: Report

---

# Domain-Driven Design (DDD)

## Ubiquitous language

- **User**: a registered person who logins to use the system;
- **Role**: the role of the user (base user or administrator);
- **Location**: a pair of coordinates (longitude, latitude);
- **Charging station**: a device in a specified location used to charge electric cars;
- **Car**: an electric car that needs its battery to be charged periodically.

## Context map

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