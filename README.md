<h1 align="center">📰 App_News - Android News Application</h1>

<p align="center">  
App_News is a modern Android application for browsing and reading news articles from a public API. It leverages the latest Android development practices including **Jetpack Compose**, **MVVM architecture**, **Hilt for DI**, **Retrofit**, **Coroutines**, and **Paging 3** to deliver a scalable and responsive user experience.
</p>

## 🚀 Features

- 🔍 **Browse News by Category**: View top headlines filtered by categories (e.g., Technology, Health, Sports, etc.)
- 📰 **Detailed Article View**: Read full articles with clean, formatted content.
- 🌐 **Language Switcher**: Easily switch between English, Vietnamese, and Traditional Chinese within the app.
- 🧭 **Navigation Component**: Seamless navigation between screens using Jetpack Navigation.
- ⚡ **Pagination Support**: Infinite scrolling using Paging 3 for efficient data loading.
- 🌙 **Dark Mode Support**: Fully responsive to system-wide theme changes.
- 🔄 **Refresh News**: Pull to refresh functionality for latest updates.
- 🧪 **Unit Testing**: Structured testing using coroutine test rules and fake repositories.

## 🧱 Architecture

The app follows **Clean MVVM architecture**, ensuring testability and separation of concerns.

```
Data (Retrofit, PagingSource)
↑
Repository (ResultWrapper, Error Handling)
↑
ViewModel (StateFlow, UI State)
↑
UI (Jetpack Compose)
```


## Tech stack & Open-source libraries

| Layer        | Technology                          |
|--------------|--------------------------------------|
| UI           | Jetpack Compose, Material 3         |
| State Mgmt   | ViewModel, StateFlow                |
| DI           | Hilt                                |
| Networking   | Retrofit, OkHttp                    |
| Async        | Kotlin Coroutines                   |
| Paging       | Paging 3                            |
| Testing      | JUnit, Coroutine Test, MockK        |

- Minimum SDK level 24.
- [Kotlin](https://kotlinlang.org/) based, utilizing [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous operations.
- Jetpack Libraries:
    - Jetpack Compose: Android’s modern toolkit for declarative UI development.
    - Lifecycle: Observes Android lifecycles and manages UI states upon lifecycle changes.
    - ViewModel: Manages UI-related data and is lifecycle-aware, ensuring data survival through configuration changes.
    - Navigation: Facilitates screen navigation.
    - SharedPreferences: Used to locally store small pieces of data such as the user's last search query or app settings. Ideal for simple key-value data without the need for a full database.
    - [Hilt](https://dagger.dev/hilt/): Facilitates dependency injection.
- Architecture:
    - MVVM Architecture (View - ViewModel - Model): Facilitates separation of concerns and promotes maintainability.
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Constructs REST APIs and facilitates paging network data retrieval.
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization): Kotlin multiplatform / multi-format reflectionless serialization.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API for code generation and analysis.

## 🏗️ Project Modules

- **data**: Remote data source, API service, DTOs.
- **domain** *(optional)*: Business logic (can be added for larger apps).
- **presentation**: ViewModels and Compose UI.
- **di**: Hilt module providers.
- **util**: Helper functions, ResultWrapper for error handling.

## 🚀 How to Run the App
1. Clone this repository:
   ```bash
   git clone https://github.com/TraVi-1801/Search-Image.git
   cd Search-Image
   ```
2. Open the project in Android Studio
3. Add your API key to local.properties
- Create or open the local.properties file in the root of your project and add:
```properties
API_KEY = your_api_key_here
```
4. Sync Gradle to download all necessary dependencies.
5. Build and run the app on an emulator or a physical device running Android 8.0 (API level 26) or higher.

