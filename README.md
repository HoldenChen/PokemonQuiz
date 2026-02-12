# PokemonQuiz

一个基于 **Redux (Per-Page Pattern)** 架构设计的 Android 演示项目，展示了如何使用 Jetpack Compose 和 GraphQL 构建高性能、可维护的移动应用。

## 🏛 核心架构：Per-Page Redux

本项目借鉴了高性能大型库的架构设计，采用“按页隔离”的 Redux 模式。

- **模块化状态管理**：每个页面（如 `Search` 和 `Detail`）都拥有独立的 `State`、`Action`、`Reducer` 和 `Effect`。
- **Store 视图化 (Scoped Store)**：通过中心化的 `StoreManager` 利用 `Store.view()` 为各 UI 组件分发局部 Store，确保 UI 组件只感知其相关的状态，降低耦合度。
- **副作用解耦 (Effect Classes)**：所有网络请求（GraphQL 查询）和异步逻辑都被封装在独立的 `Effect` 类中，使 Reducer 保持纯净且易于测试。

## ✨ 功能亮点

- **智能搜索**：基于 GraphQL 的模糊匹配查询，支持高效的分页加载（Load More）交互。
- **详情展示**：深度利用 PokéAPI 数据展示 Pokémon 的属性、ID 及技能列表。
- **极致交互体验**：解决 Redux 状态流转中常见的 `TextField` 光标跳变问题，通过 `TextFieldValue` 确保录入流畅。
- **现代 UI 栈**：全量使用 Jetpack Compose 构建，遵循 Material 3 设计规范。

## 🛠 技术栈

- **Core**: Kotlin 2.1.0 + Jetpack Compose
- **Architecture**: [Komposable Architecture](https://github.com/toggl/komposable-architecture) (Redux)
- **Networking**: GraphQL (OkHttp 驱动)
- **Dependency Injection**: 简洁高效的手动 DI 容器 (`AppContainer`)
- **JSON**: Kotlinx Serialization

## 📂 项目结构

```text
com.holden.pokemonquiz
├── architecture    # 核心接口与通用逻辑 (UseCase, Mapper)
├── data            # 数据层 (GraphQL API, Repository 实现, DTOs)
├── domain          # 领域层 (实体模型, 业务用例接口)
├── redux           # 状态管理层 (按功能模块分包)
│   ├── search/     # 搜索页 Redux 组合
│   ├── detail/     # 详情页 Redux 组合
│   └── PokemonQuizStoreManager.kt # Scoped Store 分发器
├── ui              # UI 层 (Compose Screens & Components)
└── di              # 依赖注入配置
```

## 🚀 开发环境要求

- **JDK**: 17
- **Android Studio**: 最新稳定版
- **Gradle**: 8.x
- **Compose BOM**: 2026.01.01

---

*本项目旨在展示现代 Android 架构的最佳实践，欢迎参考。*
