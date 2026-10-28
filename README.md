# NT-rockets

[![Language: English](https://img.shields.io/badge/Language-English-blue.svg)](#)
[![Deutsch](https://img.shields.io/badge/Sprache-Deutsch-orange.svg)](README_DE.md)
[![Platform: Paper / Purpur / Spigot](https://img.shields.io/badge/Platform-Paper%20%7C%20Purpur%20%7C%20Spigot-green.svg)](#)
[![Minecraft: 1.8.8 - 1.21.4+](https://img.shields.io/badge/Minecraft-1.8.8--1.21.4%2B-brightgreen.svg)](#)
[![Java: 8+](https://img.shields.io/badge/Java-8%2B-red.svg)](#)
[![Donate with PayPal](https://img.shields.io/badge/Donate-PayPal-00457C.svg?logo=paypal&logoColor=white)](https://paypal.me/niktrick)

[🇩🇪 Zur deutschen Version wechseln](README_DE.md) | [🇬🇧 English Version](README.md)

---

Universal, high-performance Elytra propulsion plugin for Minecraft **1.8.8 to 1.21.4+** (Paper, Purpur, Spigot).

Adds 6 distinct tiers of flight rockets with custom flight durations, acceleration multipliers, custom particle trails (Soul Flame, Dragon Breath, Sonic Boom), and sustained propulsion. On legacy versions (such as 1.8.8 without Elytra), rockets function as directional jetpacks!

---

## Features

- **🚀 6 Tiered Flight Levels**:
  - **Tier 1 (Basic)**: Standard flight (~1.5s duration, 1.0x boost).
  - **Tier 2 (Enhanced)**: Extended flight (~2.5s duration, 1.15x boost).
  - **Tier 3 (Powerful)**: Long flight (~3.5s duration, 1.30x boost, flame trail).
  - **Tier 4 (Turbo Boost)**: Very long flight (~5.5s duration, 1.55x boost, soul flame trail).
  - **Tier 5 (Mega Turbine)**: Extremely long flight (~8.0s duration, 1.85x boost, dragon breath trail).
  - **Tier 6 (Hypersonic MAX)**: Maximum flight (~11.0s duration, 2.30x boost, Warden Sonic Boom sound & End Rod sparks!).
- **⚡ Sustained Propulsion**:
  - Tiers 4 to 6 feature sustained thrusters that actively propel the player forward while gliding, making long-distance traversal fast and thrilling.
- **🛠️ Survival Crafting Recipes**:
  - Naturally craftable in vanilla survival using shapeless crafting recipes (recipes update dynamically when language changes).
- **🌐 Bilingual & In-Game Language Switching**:
  - Configurable in `config.yml` (`language: "en"` or `"de"`).
  - Toggle live anytime with `/rocket lang <de|en>`.
- **🔄 Cross-Version Compatibility**:
  - Backward compatible down to Minecraft 1.8.8 (directional jetpack propulsion fallback) all the way up to modern 1.21.4+ Elytra aerodynamics.

---

## Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/rocket info` | Displays rocket tiers and flight specs | *None* |
| `/rocket get <1-6> [amount]` | Gives yourself custom tiered rockets | `niktrick.rockets.admin` |
| `/rocket give <player> <1-6> [amount]` | Gives tiered rockets to a player | `niktrick.rockets.admin` |
| `/rocket reload` | Reloads `config.yml` and re-registers crafting recipes | `niktrick.rockets.admin` |
| `/rocket lang <de\|en>` | Changes plugin language live (Global) | `niktrick.rockets.admin` |

---

## Configuration (`config.yml`)

```yaml
# Language setting: "en" for English, "de" for German
language: "en"

rockets:
  recipes-enabled: true

  tier-1:
    duration-ticks: 25
    boost-multiplier: 1.0
    sustained-propulsion: false
    trail-particle: FIREWORK

  tier-2:
    duration-ticks: 45
    boost-multiplier: 1.15
    sustained-propulsion: false
    trail-particle: FIREWORK

  tier-3:
    duration-ticks: 70
    boost-multiplier: 1.30
    sustained-propulsion: false
    trail-particle: FLAME

  tier-4:
    duration-ticks: 110
    boost-multiplier: 1.55
    sustained-propulsion: true
    trail-particle: SOUL_FIRE_FLAME

  tier-5:
    duration-ticks: 160
    boost-multiplier: 1.85
    sustained-propulsion: true
    trail-particle: DRAGON_BREATH

  tier-6:
    duration-ticks: 220
    boost-multiplier: 2.30
    sustained-propulsion: true
    trail-particle: END_ROD
```

---

## Compatibility

- **Minecraft**: 1.8.8, 1.12.2, 1.16.5, 1.17, 1.18, 1.19, 1.20, 1.21.x+
- **Server Software**: Paper, Purpur, Spigot
- **Java**: 8 to 25+ (compiled to Java 8 bytecode for maximum compatibility)
---

## 💖 Support & Donations

If you enjoy this plugin, consider supporting future development:
- [PayPal Donation](https://paypal.me/niktrick)
- Donations are voluntary and directly help maintain multi-version compatibility!
