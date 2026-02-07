---

title: Context map
nav_order: 2
parent: Report

---

# Context map

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