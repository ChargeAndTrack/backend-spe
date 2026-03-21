---

title: Requirement Specification
nav_order: 1
parent: Report

---

# Requirement Specification

## Description

**ChargeAndTrack** is a system designed to manage and monitor the charging of electric cars. The user is able to
view all the charging stations present in an area and their availability. When a user plugs into a charging station,
its state (available/in-use) is updated in real-time inside the application, and he will be notified with battery
level updates. In addition, an admin can manage the charging stations, meaning that he can add new stations, update
existing ones, and remove them if necessary.

## Functional Requirements

We defined the [ubiquitous language](./ddd.md#ubiquitous-language) that can be used as reference for the concepts of the
functional requirements.

### User Requirements

1. A user can access the application by logging in with his username and password;
2. a user can manage his electric cars, meaning that he can add new cars, update existing ones, and remove them;
3. a user can search for charging stations near an address and asking for the closest one;
4. a user can view the details of a charging station, such as its location, availability, and power;
5. a user can plug into a charging station selecting one of his cars;
6. a user can view the battery level of his cars while they are charging;
7. a user can stop a recharge;
8. a user can ask an LLM to search for the charging stations near an address or asking for the closest one, in addition
he can specify a minimum power filter for the stations;
9. an admin user can also manage the charging stations, meaning that he can add new stations, update existing ones, and
remove them.

### System Requirements

1. The system must persist all the data related to users, cars, charging stations, and recharges;
2. the system must update the state of a charging station in real-time when a user plugs into it or stops a recharge;
3. the system must update the battery level of a car in real-time while it is charging;
4. the system must handle the view of the charging stations in an interactive map;
5. the system must provide the possibility to make free-text searches for charging stations integrating with an LLM.

## Non-Functional Requirements

1. **Portability**: the system should be accessible from a web browsers, through both mobile and desktop devices;
2. **Usability**: the system should have an intuitive and user-friendly interface, adapting to the device resolution.

## Implementation Requirements

1. The system should be implemented following this stack:
   - frontend: VueJS;
   - backend: JVM;
   - database: MongoDB;
2. the system should be implemented following the DDD approach;
3. the system backend should be containerized.
