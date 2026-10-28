# niktrick-rockets

[![Sprache: Deutsch](https://img.shields.io/badge/Sprache-Deutsch-orange.svg)](#)
[![English](https://img.shields.io/badge/Language-English-blue.svg)](README.md)
[![Plattform: Paper / Purpur / Spigot](https://img.shields.io/badge/Plattform-Paper%20%7C%20Purpur%20%7C%20Spigot-green.svg)](#)
[![Minecraft: 1.8.8 - 1.21.4+](https://img.shields.io/badge/Minecraft-1.8.8--1.21.4%2B-brightgreen.svg)](#)
[![Java: 8+](https://img.shields.io/badge/Java-8%2B-red.svg)](#)

[🇬🇧 Switch to English Version](README.md) | [🇩🇪 Deutsche Version](README_DE.md)

---

Universelles High-Speed Elytra-Antriebs-Plugin für Minecraft **1.8.8 bis 1.21.4+** (Paper, Purpur, Spigot).

Fügt 6 fortschrittliche Flugraketen-Stufen mit individuellen Flugdauern, Beschleunigungsmultiplikatoren, maßgeschneiderten Partikelschweifen (Seelenfeuer, Drachenatem, Sonic Boom) und anhaltendem Schub hinzu. Auf alten Minecraft-Versionen ohne Elytra (z. B. 1.8.8) fungieren die Raketen als direktionales Jetpack!

---

## Features

- **🚀 6 Abgestufte Raketen-Levels**:
  - **Stufe 1 (Basis)**: Standard-Flug (~1.5s Dauer, 1.0x Schub).
  - **Stufe 2 (Verbessert)**: Verlängerter Flug (~2.5s Dauer, 1.15x Schub).
  - **Stufe 3 (Stark)**: Weiter Flug (~3.5s Dauer, 1.30x Schub, Flammenschweif).
  - **Stufe 4 (Turboschub)**: Sehr langer Flug (~5.5s Dauer, 1.55x Schub, Seelenfeuer-Schweif).
  - **Stufe 5 (Mega-Turbine)**: Extrem langer Flug (~8.0s Dauer, 1.85x Schub, Drachenatem-Schweif).
  - **Stufe 6 (Hyperschall MAX)**: Maximale Distanz (~11.0s Dauer, 2.30x Schub, Warden Sonic Boom Sound & Endstab-Schweif!).
- **⚡ Anhaltender Schub (Sustained Propulsion)**:
  - Ab Stufe 4 beschleunigen Triebwerke den Spieler während des Gleitens kontinuierlich vorwärts für atemberaubende Reisegeschwindigkeiten.
- **🛠️ Survival-Crafting-Rezepte**:
  - Natürlich im Survival-Modus über werkbanklose Rezepte herstellbar (passen sich dynamisch der eingestellten Sprache an).
- **🌐 Zweisprachig & Live im Spiel umschaltbar**:
  - Einstellbar in der `config.yml` (`language: "de"` oder `"en"`).
  - Live umschaltbar per In-Game-Befehl `/rocket lang <de|en>`.
- **🔄 Maximale Abwärtskompatibilität**:
  - Funktioniert von 1.8.8 (Jetpack-Modus) bis zu den neuesten 1.21.4+ Versionen nahtlos.

---

## Befehle & Permissions

| Befehl | Beschreibung | Permission |
| :--- | :--- | :--- |
| `/rocket info` | Zeigt Details zu allen 6 Raketen-Stufen an | *Keine* |
| `/rocket get <1-6> [anzahl]` | Gibt dir die gewünschten Raketen | `niktrick.rockets.admin` |
| `/rocket give <spieler> <1-6> [anzahl]` | Gibt einem Spieler Raketen | `niktrick.rockets.admin` |
| `/rocket reload` | Lädt die `config.yml` und Rezepte neu | `niktrick.rockets.admin` |
| `/rocket lang <de\|en>` | Ändert die Sprache des Plugins live | `niktrick.rockets.admin` |

---

## Konfiguration (`config.yml`)

```yaml
# Spracheinstellung: "en" für Englisch, "de" für Deutsch
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

## Kompatibilität

- **Minecraft**: 1.8.8, 1.12.2, 1.16.5, 1.17, 1.18, 1.19, 1.20, 1.21.x+
- **Server-Software**: Paper, Purpur, Spigot
- **Java**: 8 bis 25+ (kompiliert auf Java 8 Bytecode für maximale Kompatibilität)
