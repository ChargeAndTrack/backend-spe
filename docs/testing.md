---

title: Testing
nav_order: 4
parent: Report

---

# Testing

The tests are implemented using the [Kotest](https://kotest.io/) framework.

## Architectural Testing

We developed two architectural tests to ensure that the code adheres to the layered architecture and the hexagonal
architecture. The tests are implemented using the [ArchUnit](https://www.archunit.org/) library.

## End-to-End Testing

We implemented end-to-end tests to verify the functionality of the application from the user's perspective. The tests
aim to verify that each API behaves as expected, both in success and failure scenarios. To create the HTTP client and
perform API calls we use the [Ktor](https://ktor.io/) library.