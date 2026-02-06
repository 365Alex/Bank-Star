@echo off
echo ============================================
echo Star Bank Recommendations - Тестирование
echo ============================================

echo.
echo Ожидаем запуска сервера... (20 секунд)
timeout /t 20 >nul

echo.
echo 1. Проверка сервиса...
curl -X GET "http://localhost:8080/management/info"

echo.
echo 2. Telegram Bot Help...
curl -X GET "http://localhost:8080/bot/help"

echo.
echo 3. Тестирование рекомендаций через Telegram Bot API...
echo Тест 1: sheron.berge
curl -X GET "http://localhost:8080/bot/recommend/sheron.berge"

echo.
echo Тест 2: test.user
curl -X GET "http://localhost:8080/bot/recommend/test.user"

echo.
echo 4. Тестирование REST API...
echo GET all rules:
curl -X GET "http://localhost:8080/dynamic-rules"

echo.
echo 5. Создание динамического правила...
curl -X POST "http://localhost:8080/rule" ^
  -H "Content-Type: application/json" ^
  -d "{\"product_name\":\"Простой кредит\",\"product_id\":\"ab138afb-f3ba-4a93-b74f-0fcee86d447f\",\"product_text\":\"Откройте мир выгодных кредитов с нами!\",\"is_active\":true,\"conditions\":[{\"query\":\"USER_OF\",\"arguments\":[\"CREDIT\"],\"negate\":true},{\"query\":\"TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW\",\"arguments\":[\"DEBIT\"],\"negate\":false},{\"query\":\"TRANSACTION_SUM_COMPARE\",\"arguments\":[\"DEBIT\",\"DEPOSIT\",\"100000\"],\"negate\":false}]}"

echo.
echo 6. Проверка статистики...
curl -X GET "http://localhost:8080/rule/stats"

echo.
echo 7. Очистка кэша...
curl -X POST "http://localhost:8080/management/clear-caches"

echo.
echo 8. Проверка H2 Console...
echo Откройте в браузере:
echo - Primary DB: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:primarydb, User: sa, Password: пусто)
echo - Secondary DB: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:file:./transactiondb, User: sa, Password: пусто)

pause