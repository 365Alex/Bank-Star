@echo off
echo ============================================
echo Star Bank Recommendations - Тестирование
echo ============================================

echo.
echo 1. Проверка сервиса...
curl -X GET "http://localhost:8080/management/info"

echo.
echo 2. Telegram Bot Help...
curl -X GET "http://localhost:8080/bot/help"

echo.
echo 3. Тестирование рекомендаций...
echo.
echo Тест 1: sheron.berge
curl -X GET "http://localhost:8080/bot/recommend/sheron.berge"

echo.
echo Тест 2: test.user
curl -X GET "http://localhost:8080/bot/recommend/test.user"

echo.
echo 4. Проверка через браузер...
echo.
echo Откройте в браузере:
echo - H2 Console: http://localhost:8080/h2-console
echo - Информация о сервисе: http://localhost:8080/management/info
echo - Рекомендации: http://localhost:8080/recommendation/123e4567-e89b-12d3-a456-426614174000

pause