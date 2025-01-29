# API для отслеживание переходов

## Методы

1) Генерация QR-кода `http://localhost:8080/generate/qr/flamingo?repurl=https://www.youtube.com/watch?v=yup8gIXxWDU`

![image](https://github.com/user-attachments/assets/5faed3d5-7137-4cb8-9a2a-e8c99adc35bf)

2) Перенаправление `http://localhost:8080/generate/rep/flamingo770383`

3) Получить число перешедших `http://localhost:8080/generate/count/flamingo770383`

4) Метод удаление `http://localhost:8080/generate/delete/flamingo770383`

## пометка
`/qr/{user}?repurl=url` где user имя qr кода, repurl = url адрес qr кода
`/rep/{user}` где user имя qr кода
`/delete/{user}` где user имя qr кода
`/count/{user}` где user имя qr кода