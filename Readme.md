# 🎥 **Indicator Service**

> Providing movie award interval insights for cinema enthusiasts!

---

## 🌟 **Introduction**

The **Indicator Service** dives deep into the world of cinema, uncovering fascinating insights about movie awards. Built with Spring Boot, this powerhouse unlocks the ability to visualize intervals between awards won by producers, all fetched from our carefully curated movie database.

---

## 🚀 **Features**

1. **📊 Award Intervals Extravaganza**: Dive into intriguing award-winning intervals for movie producers. Discover who had the shortest and longest wait between accolades!
2. **📁 CSV Auto-Loader**: Say goodbye to manual data entries! Our service identifies and ingests data from `movies.csv` on startup, ensuring the database is always primed.
3. **⬆️ CSV Manual Uploader**: Need to update the dataset on the go? Our API endpoint makes this a breeze. Just hit, upload, and voilà!

---

## 🔗 **API Quick Links**

- **🔍 [Fetch Award Intervals](http://localhost:8080/api/award-intervals)**
    - Dive deep into award intervals!
- **📤 [Manual CSV Upload](http://localhost:8080/api/movies/upload)**
    - Use Postman to call manually.
    - http://localhost:8080/api/movies/upload

---

## 🔧 **Tech Deep Dive**

- **💼 Core Components**:
    - **Controllers**: The API maestros! Directing traffic and ensuring data flows smoothly.
    - **Services**: The brain behind the operations! Crunching numbers, weaving magic.
    - **Repositories**: The unsung heroes! Interacting with databases to feed in or fetch data.

- **📚 Stack Highlights**:
    - **Spring Boot**: Crafting microservices never felt this good!
    - **JPA & Spring Data**: Befriending databases with finesse.
    - **Swagger with OpenAPI**: API documentation that's not just accurate but looks good!
    - **Databases**: Flexible for connections with H2, PostgreSQL, MySQL, and more. Check application settings for more details.

---

## 🚀 **Get Started**

1. **Tools in the Kit**:
    - Java 17
    - Maven
2. **Crafting the Build**:
    ```bash
    mvn clean install
    ```
3. **Ignite the Service**:
    ```bash
    java -jar target/indicator-service-0.0.1-SNAPSHOT.jar
    ```
4. **Swagger Documentation**: Begin your exploration [here](http://localhost:8080/swagger-ui.html).
5. **H2 Database Console**: Peek into the database [here](http://localhost:8080/h2-console).

---

## 💌 **Feedback & Support**

Dive into the code, explore, and let the movie magic envelop you! For any queries, or if you stumble upon something unexpected, reach out to our ever-enthusiastic dev team.

Enjoy the cinematic journey! 🎬

---
