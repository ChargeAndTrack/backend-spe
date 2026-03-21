---

title: Architecture
nav_order: 3
parent: Report

---

# Architecture

```mermaid
flowchart LR
    rest_api((REST API))
    frontend -- external network --- rest_api
    rest_api -- expose --- jvm_container
    subgraph Backend
        subgraph jvm_container
            direction TB
            domain_layer --- application_layer
            application_layer --- infrastructure_layer
        end
        subgraph MongoDB_container
            mongodb[(mongoDB)]
        end
        jvm_container -- internal network --- MongoDB_container 
    end
```

## Backend architecture
We chose to implement the system backend following the **hexagonal architecture** (_ports and adapters_ architecture),
in which the application is divided into three main layers:
- **domain layer**: contains the domain model, which contains the business logic of the application;
- **application layer**: contains the application services, which orchestrate the use cases of the application, and the
ports that define the contracts for the infrastructure layer;
- **infrastructure layer**: contains the adapters, which are the implementations of the ports defined in the application
layer, and the technical details of the application, such as the web server.
