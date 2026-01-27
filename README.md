# 🌙 Lunatic (LuaBot Localization Engine)

> 🌌 A high-performance I18n microservice API for the **LuaBot** ecosystem, engineered in **Kotlin** to centralize translations using **YAML** for maximum readability.

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-orange.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-blue.svg?logo=kotlin)](https://kotlinlang.org/)
[![YAML](https://img.shields.io/badge/Format-YAML-green.svg)](https://yaml.org/)

**Lunatic** is the "Source of Truth" for all strings across the LuaBot infrastructure. It parses developer-friendly **YAML** files and serves them as lightning-fast JSON to the **Discord Bot (Python)** and the **Web Site (Flask)**.

---

## 🛠️ Ecosystem Integration

1.  **Lunatic (Kotlin):** Manages, validates, and parses `.yml` locale files.
2.  **LuaBot (Python/Discord.py):** Consumes JSON strings for commands and interactions.
3.  **Website (Python/Flask):** Dynamically renders page content via API calls.

## ✨ Features

* **YAML-Powered:** Easy-to-read translation files with support for comments and multi-line strings.
* **Hot-Reloading:** Updates in the `.yml` files are reflected across the ecosystem without reboots.
* **Unified Pipeline:** Convert complex YAML structures into clean JSON responses for Python clients.
* **Type-Safe Validation:** Kotlin ensures your YAML schema is valid before the API goes live.

## 🚀 API Usage

### Fetch Locale (Returns JSON)
```http
GET /lunatic/{lang}
```

Response Example:
```json
{
  "meta": {
    "language": "Português",
    "version": "2.0.0"
  },
  "strings": {
    "welcome": "Olá {user}, bem-vindo à LuaBot! 🌙",
    "errors": {
      "no_perm": "Você não tem permissão para usar isso!"
    }
  }
}
```

## 📲 Installation
If you want to use the Lunatic API, put this on your `build.gradle.kts`

```kotlin
dependencies {
  implementation("net.perfect.tea.lunatic:VERSION")
}
```

## Why not use Crowdin?

 - Crowdin is expensive for our projects. It would be, at least, 50 USD monthly! And that's too dang expensive for a tool like this.

## 📄 License
Distributed under the GNU Affero General Public License v3.0. Copyright (c) 2026 TheLuaBot
