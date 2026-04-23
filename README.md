# MedBook — Медицинское приложение для записи к врачам

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![Room](https://img.shields.io/badge/Room-3DDC84?style=for-the-badge&logo=android&logoColor=white)

Мобильное приложение на **Kotlin + Jetpack Compose** для поиска врачей, просмотра их профилей и записи на приём.

---

## Ключевые возможности

- **Полный CRUD** для профиля пользователя (создание, чтение, обновление)
- **Поиск врачей** по имени и специальности с дебаунсом ввода (300 мс)
- **Просмотр профиля врача**: фото, рейтинг, статистика, описание, кнопка записи
- **Управление профилем**: редактирование имени и email с сохранением в Room Database
- **Мои записи**: список с фильтрами по статусу (Scheduled / Completed / Cancelled)
- **Локальное хранение**: Room Database с реактивными Flow-потоками для авто-обновления UI
- **Чистый UI**: Material Design 3, адаптивная вёрстка, продуманная цветовая схема

---

## Технологический стек

| Категория       | Технологии                                                                 |
|-----------------|----------------------------------------------------------------------------|
| **Язык**        | Kotlin                                                                     |
| **UI**          | Jetpack Compose                                         |
| **База данных** | Room (SQLite), Coroutines, Flow                                            |
| **Навигация**   | Navigation Compose                                                         |
| **Сборка**      | Gradle Kotlin DSL, KSP                                                     |
| **Мин. SDK**    | 24 (Android 7.0)                                                           |

---

## Структура проекта
```bash
MedBook/
├── app/
│ ├── src/main/java/com/example/figmaproject/
│ │ ├── data/
│ │ │ ├── local/
│ │ │ │ ├── dao/ 
│ │ │ │ ├── entity/ 
│ │ │ │ └── AppDatabase.kt
│ │ │ └── repository/ 
│ │ ├── ui/
│ │ │ ├── screens/ 
│ │ │ ├── theme/ 
│ │ │ └── viewmodel/ 
│ │ ├── navigation/ 
│ │ ├── MainActivity.kt
│ │ └── MedBookApp.kt
│ └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## Установка и запуск

### Требования
- Android Studio 
- Эмулятор или устройство Pixel 9 Pro XL

### 1. Клонирование репозитория
```bash
git https://github.com/MiraWUSE/GameLibraryDjango.git
