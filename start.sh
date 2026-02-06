#!/bin/bash

echo "Starting Star Bank Recommendations Service..."

# Проверяем наличие базы данных
if [ ! -f "./data/transaction.mv.db" ]; then
    echo "Warning: Database file not found at ./data/transaction.mv.db"
    echo "Please ensure the H2 database file exists for recommendations."
fi

# Собираем приложение
echo "Building application..."
mvn clean package -DskipTests

# Запускаем приложение
echo "Starting application..."
java -jar target/star-recommendations-1.0.0.jar