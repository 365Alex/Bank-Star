# Star Bank Recommendations System

[![Java Version](https://img.shields.io/badge/Java-17%2B-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Система персонализированных рекомендаций банковских продуктов на основе транзакционной истории клиентов.

## 🚀 Быстрый старт

### Требования
- Java 17 или выше
- Maven 3.6+
- PostgreSQL (для продакшн) или H2 (для разработки)

### Запуск для разработки
```bash
# Клонирование репозитория
git clone https://github.com/star-bank/recommendations-system.git
cd recommendations-system

# Запуск с H2 базой данных
mvn spring-boot:run