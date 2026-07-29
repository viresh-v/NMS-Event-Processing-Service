# Fault Alert Engine

A Spring Boot based Fault Alert Engine that consumes device telemetry events, evaluates configurable threshold-based rules, generates alerts, manages alert lifecycle, and exposes REST APIs for monitoring and alert management.
 
---

## Overview

Fault Alert Engine is designed to monitor device health metrics such as:

- CPU Usage
- Memory Usage
- Active Connections
- Device Health Status

Incoming device events are evaluated against configurable thresholds using the Strategy Pattern. When thresholds are exceeded, alerts are generated, updated, escalated, acknowledged, resolved, and automatically recovered when metrics return to normal.
 
---

## Features

- Kafka Device Event Consumer
- Configurable Threshold Rules
- Strategy Pattern based Rule Engine
- Alert Generation
- Duplicate Alert Detection
- Alert Escalation
- Auto Recovery
- Alert Acknowledgement
- Alert Resolution
- Search & Filtering
- Pagination & Sorting
- REST APIs
- Swagger Documentation
- Global Exception Handling
- DTO Layer
- Factory Pattern
- Mapper Layer
- PostgreSQL Persistence
- JaCoCo Code Coverage
- Unit & Controller Tests

---

## Technology Stack

| Technology | Version |
|------------|----------|
| Java | 17 |
| Spring Boot | 3.x |
| Spring Data JPA | Latest |
| Spring Kafka | Latest |
| PostgreSQL | 16 |
| Maven | 3.9+ |
| Swagger OpenAPI | springdoc-openapi |
| Lombok | Latest |
| JUnit 5 | Latest |
| Mockito | Latest |
| JaCoCo | Latest |
 
---

# Project Architecture

Kafka Device Event

↓

Kafka Consumer

↓

RuleEngineService

↓

Strategy Rules

• CpuRule

• MemoryRule

• ActiveConnectionsRule

• HealthRule

↓

AlertMetadata

↓

AlertService

↓

AlertFactory

↓

Alert Entity

↓

AlertRepository

↓

PostgreSQL

↓

REST APIs
 
---

# Design Patterns Used

- Strategy Pattern
- Factory Pattern
- Repository Pattern
- DTO Pattern
- Builder Pattern
- Dependency Injection

---

# Alert Lifecycle

OPEN

↓

WARNING

↓

MAJOR

↓

CRITICAL

↓

ACKNOWLEDGED

↓

RESOLVED

↓

AUTO RECOVERY
 
---

# Project Structure

```
src
├── config
├── consumer
├── controller
├── dto
├── entity
├── enums
├── exception
├── factory
├── mapper
├── repository
├── rule
├── service
├── specification
└── util
```
 
---

# Threshold Configuration

Configured through application.properties

```
rules.cpu.warning=70
rules.cpu.major=80
rules.cpu.critical=90
 
rules.memory.warning=70
rules.memory.major=85
rules.memory.critical=95
 
rules.connection.warning=1000
rules.connection.major=5000
rules.connection.critical=10000
```
 
---

# REST APIs

## Alerts

| Method | Endpoint |
|----------|----------------------------|
| GET | /api/v1/alerts |
| GET | /api/v1/alerts/open |
| GET | /api/v1/alerts/{id} |
| PUT | /api/v1/alerts/{id}/acknowledge |
| PUT | /api/v1/alerts/{id}/resolve |
| GET | /api/v1/alerts/search |
 
---

# Search Parameters

- deviceId
- alertType
- severity
- status
- page
- size
- sort
- direction

---

# Swagger

```
http://localhost:8083/swagger-ui/index.html
```
 
---

# Build

```
mvn clean install
```
 
---

# Run

```
mvn spring-boot:run
```
 
---

# Testing

Run all tests

```
mvn test
```

Generate JaCoCo Report

```
mvn clean verify
```

Coverage Report

```
target/site/jacoco/index.html
```
 
---

# Database

PostgreSQL

Stores

- Alert Details
- Alert Status
- Severity
- Occurrence Count
- Created Time
- Updated Time
- Resolved Time

---

# Sample Device Event

```json
{
  "deviceId": "DEVICE-001",
  "cpuUsage": 92.5,
  "memoryUsage": 88.1,
  "activeConnections": 6500,
  "healthStatus": "DOWN"
}
```
 
---

# Future Enhancements

- Email Notifications
- Slack Notifications
- SMS Alerts
- Dashboard
- Alert Analytics
- Prometheus Metrics
- Grafana Integration
- Docker Deployment
- Kubernetes Deployment

---

# Author

Developed as a Proof of Concept (POC) for Network Management System (NMS).

```