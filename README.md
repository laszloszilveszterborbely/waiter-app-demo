# WaiterApp

A simple Java + Spring Boot web application that I developed both as a learning project and for practical use at the restaurant where I work.

Its purpose is to support waiters in managing table reservations and preorders, and to automate the processing of reservation requests received via email.

The application handles small but practical tasks that arise in the restaurant’s daily operation—tasks that are not covered by the existing hospitality software.

> This application is actively used in our restaurant.

-----

## Features

- **Table Reservation & Preorder Management**
  - Create new reservations or preorders
  - Search by date or guest name
  - Edit, delete, and print reservations

- **Daily Summaries**
  - Clear overview of daily reservations and preorders
  - **Printable handouts** for:
    - **Kitchen**: meal details, arrival time, special requests, etc.
    - **Bar**: number of guests, arrival time orders, comments, etc.
  - All organized in chronological order

- **Message Generator**
  - Extracts information from reservation request emails (standard format)
  - Generates a ready-to-send confirmation message with personalized data
  - Can be copied and sent via the restaurant's email client

---

## Technologies

- Java
- Spring Boot
- HTML + CSS
- Thymeleaf
- H2 Database
- Maven

---

## GitHub

[github.com/laszloszilveszterborbely/waiter-app-demo](https://github.com/laszloszilveszterborbely/waiter-app-demo)
