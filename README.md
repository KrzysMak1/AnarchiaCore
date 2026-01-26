# AnarchiaCore

## Wymagania
- Paper 1.21.10 (API 1.21).
- Java 21.
- Maven.
- WorldGuard (opcjonalnie, wymagany do reguł regionów eventowych itemów).
- PlaceholderAPI (opcjonalnie, wymagany do placeholderów PAPI).
- MySQL (opcjonalnie, wymagany przy `storage.type: MYSQL`).

## Instalacja
1. Zbuduj plugin (`mvn clean package`) lub skopiuj gotowy plik `.jar`.
2. Wrzuć `AnarchiaCore.jar` do `plugins/` i uruchom serwer.
3. Po pierwszym starcie powstaną katalogi:
   - `plugins/AnarchiaCore/config.yml` (główna konfiguracja).
   - `plugins/AnarchiaCore/combatlog/` (config i message dla AntyLogout).
   - `plugins/AnarchiaCore/configs/STORMITEMY/` (pliki StormItemy).
   - `plugins/AnarchiaCore/configs/customitems/` (pojedyncze pliki konfiguracji custom itemów).

## Reload
- `/anarchiacore reload` — przeładowuje: `config.yml`, storage (YAML/MYSQL), serca, kosz, custom items, statystyki, top cache, combatlog + prefix, instalację configów StormItemy, StormItemy configs oraz rejestrację placeholderów.
- `/anarchiacore combatlog ...` — podkomendy combatlog (alias: `/antylogout ...`).
- `/stormitemy reload` — przeładowuje konfiguracje StormItemy (z `configs/STORMITEMY/`).

## Konfiguracja

### `plugins/AnarchiaCore/config.yml`
Sekcje i klucze:
- `defaultHearts`, `maxHearts`
- `anarchiczneSerce` (definicja itemu) + `anarchiczneSerceCustomModelData`
- `blockedEndCrystalDimensions` (lista wymiarów, np. `NETHER`)
- `stats.topCacheSeconds`, `stats.topLimit`, `stats.killCredit.*`
- `storage.*` (YAML/MYSQL, dane połączenia)
- `trash.title`, `trash.rows`, `trash.clearMessage`
- `messages.*` (prefix, permission, serca, kosz, custom items, blokady kryształów)
- `customItems.worldguard.*` (reguły regionów WorldGuard)

Przykład:

```yaml
blockedEndCrystalDimensions:
  - NETHER

stats:
  topCacheSeconds: 60
  topLimit: 10
  killCredit:
    sameVictimCooldownSeconds: 30
    ignoreSameIp: true
    onMissingIp: count

storage:
  type: "MYSQL"
  mysql:
    host: "localhost"
    port: 3306
    database: "anarchiacore"
    user: "root"
    password: "pass"
    useSSL: false
    poolSize: 10
```

### `plugins/AnarchiaCore/combatlog/config.yml`
Główne sekcje AntyLogout:
- `storage-config.*`
- `antylogout.*` (czas, blokady, display, whitelist komend, regiony, ender-perła, void, itp.)
- `protection.*` (ochrona po starcie/śmierci i komunikaty)
- `barrier.*` (wall/knockback)
- `misc.*` (pvp, autorespawn, creative)

### `plugins/AnarchiaCore/combatlog/message.yml`
Wiadomości AntyLogout (wszystkie klucze z `message.yml`, np. `command-no-permission`, `combat-start`, `combat-ended`, `pvp-status-changed`, itd.).

### `plugins/AnarchiaCore/configs/STORMITEMY/`
Pliki konfiguracyjne StormItemy:
- `config.yml` (główne ustawienia, actionbar, zaczarowania, regiony, język).
- `lang/POL.yml`, `lang/ENG.yml`
- `books/*.yml` (definicje ksiąg)
- `items/*.yml` (konfiguracja eventowych itemów)
- `regions.yml`, `texturepack_preferences.yml`, `data.yml`, `data.db`

### `plugins/AnarchiaCore/configs/customitems/`
Custom items (każdy item w osobnym pliku `.yml`).
- Każdy plik ma dokładnie jedną definicję itemu.
- W tej lokalizacji instalują się również eventowe itemy z paczki StormItemy (np. `bombardamaxima.yml`, `turbotrap.yml`, `totemulaskawienia.yml`).

### Storage (YAML/MYSQL)
- `storage.type: YAML` — dane graczy zapisują się w plikach `.yml` (`data.yml`, `stats.yml`).
- `storage.type: MYSQL` — dane graczy zapisują się do bazy (HikariCP).
- Tabele:
  - `hearts` (`uuid`, `hearts`, `spawn_on_join`)
  - `stats` (`uuid`, `name`, `kills`, `deaths`, `killstreak`, `bestKillstreak`)

## Komendy i permisje

| Komenda | Opis | Permisja |
| --- | --- | --- |
| `/anarchiacore reload` | Reload całego pluginu | `anarchiacore.admin` |
| `/anarchiacore heart give <player> [amount]` | Daje Anarchiczne Serce | `anarchiacore.admin` |
| `/anarchiacore heart setitem` | Zapisuje definicję Anarchicznego Serca | `anarchiacore.admin` |
| `/anarchiacore customitems give <item> [player]` | Daje custom item | `anarchiacore.admin` |
| `/anarchiacore customitems list` | Lista custom itemów | `anarchiacore.admin` |
| `/anarchiacore combatlog reload` | Reload AntyLogout | `dream.antylogout.command.reload` |
| `/anarchiacore combatlog pvp on|off` | Przełączanie PVP | `dream.antylogout.command.pvp` |
| `/anarchiacore combatlog autorespawn on|off` | Przełączanie autorespawn | `dream.antylogout.command.autorespawn` |
| `/anarchiacore combatlog protection-off` | Wyłączenie ochrony | `dream.antylogout.command.protection-off` |
| `/antylogout ...` | Alias dla `/anarchiacore combatlog ...` | jak wyżej |
| `/stormitemy ...` | Komendy StormItemy (give/reload/panel/itp.) | `stormitemy.admin` |
| `/kosz` | Otwiera kosz | brak |
| `/menuprzedmioty` | Menu przedmiotów StormItemy | brak |

Dodatkowe bypassy:
- `dream.antylogout.bypass` — omija blokady combatlog.

## Placeholdery (PlaceholderAPI)
Identyfikator: `anarchiacore`.

### Serca (3)
1. `%anarchiacore_hearts%`
2. `%anarchiacore_defaulthearts%`
3. `%anarchiacore_maxhearts%`

### Combatlog (3)
4. `%anarchiacore_combat_tagged%`
5. `%anarchiacore_combat_timeleft%`
6. `%anarchiacore_combat_timeleft_mmss%`

### Statystyki (5)
7. `%anarchiacore_kills%`
8. `%anarchiacore_deaths%`
9. `%anarchiacore_kdr%`
10. `%anarchiacore_killstreak%`
11. `%anarchiacore_bestkillstreak%`

### TOP10 — KILLS (20)
12. `%anarchiacore_kills_top_1_name%`
13. `%anarchiacore_kills_top_1_value%`
14. `%anarchiacore_kills_top_2_name%`
15. `%anarchiacore_kills_top_2_value%`
16. `%anarchiacore_kills_top_3_name%`
17. `%anarchiacore_kills_top_3_value%`
18. `%anarchiacore_kills_top_4_name%`
19. `%anarchiacore_kills_top_4_value%`
20. `%anarchiacore_kills_top_5_name%`
21. `%anarchiacore_kills_top_5_value%`
22. `%anarchiacore_kills_top_6_name%`
23. `%anarchiacore_kills_top_6_value%`
24. `%anarchiacore_kills_top_7_name%`
25. `%anarchiacore_kills_top_7_value%`
26. `%anarchiacore_kills_top_8_name%`
27. `%anarchiacore_kills_top_8_value%`
28. `%anarchiacore_kills_top_9_name%`
29. `%anarchiacore_kills_top_9_value%`
30. `%anarchiacore_kills_top_10_name%`
31. `%anarchiacore_kills_top_10_value%`

### TOP10 — DEATHS (20)
32. `%anarchiacore_deaths_top_1_name%`
33. `%anarchiacore_deaths_top_1_value%`
34. `%anarchiacore_deaths_top_2_name%`
35. `%anarchiacore_deaths_top_2_value%`
36. `%anarchiacore_deaths_top_3_name%`
37. `%anarchiacore_deaths_top_3_value%`
38. `%anarchiacore_deaths_top_4_name%`
39. `%anarchiacore_deaths_top_4_value%`
40. `%anarchiacore_deaths_top_5_name%`
41. `%anarchiacore_deaths_top_5_value%`
42. `%anarchiacore_deaths_top_6_name%`
43. `%anarchiacore_deaths_top_6_value%`
44. `%anarchiacore_deaths_top_7_name%`
45. `%anarchiacore_deaths_top_7_value%`
46. `%anarchiacore_deaths_top_8_name%`
47. `%anarchiacore_deaths_top_8_value%`
48. `%anarchiacore_deaths_top_9_name%`
49. `%anarchiacore_deaths_top_9_value%`
50. `%anarchiacore_deaths_top_10_name%`
51. `%anarchiacore_deaths_top_10_value%`

### TOP10 — KDR (20)
52. `%anarchiacore_kdr_top_1_name%`
53. `%anarchiacore_kdr_top_1_value%`
54. `%anarchiacore_kdr_top_2_name%`
55. `%anarchiacore_kdr_top_2_value%`
56. `%anarchiacore_kdr_top_3_name%`
57. `%anarchiacore_kdr_top_3_value%`
58. `%anarchiacore_kdr_top_4_name%`
59. `%anarchiacore_kdr_top_4_value%`
60. `%anarchiacore_kdr_top_5_name%`
61. `%anarchiacore_kdr_top_5_value%`
62. `%anarchiacore_kdr_top_6_name%`
63. `%anarchiacore_kdr_top_6_value%`
64. `%anarchiacore_kdr_top_7_name%`
65. `%anarchiacore_kdr_top_7_value%`
66. `%anarchiacore_kdr_top_8_name%`
67. `%anarchiacore_kdr_top_8_value%`
68. `%anarchiacore_kdr_top_9_name%`
69. `%anarchiacore_kdr_top_9_value%`
70. `%anarchiacore_kdr_top_10_name%`
71. `%anarchiacore_kdr_top_10_value%`

### TOP10 — KILLSTREAK (20)
72. `%anarchiacore_killstreak_top_1_name%`
73. `%anarchiacore_killstreak_top_1_value%`
74. `%anarchiacore_killstreak_top_2_name%`
75. `%anarchiacore_killstreak_top_2_value%`
76. `%anarchiacore_killstreak_top_3_name%`
77. `%anarchiacore_killstreak_top_3_value%`
78. `%anarchiacore_killstreak_top_4_name%`
79. `%anarchiacore_killstreak_top_4_value%`
80. `%anarchiacore_killstreak_top_5_name%`
81. `%anarchiacore_killstreak_top_5_value%`
82. `%anarchiacore_killstreak_top_6_name%`
83. `%anarchiacore_killstreak_top_6_value%`
84. `%anarchiacore_killstreak_top_7_name%`
85. `%anarchiacore_killstreak_top_7_value%`
86. `%anarchiacore_killstreak_top_8_name%`
87. `%anarchiacore_killstreak_top_8_value%`
88. `%anarchiacore_killstreak_top_9_name%`
89. `%anarchiacore_killstreak_top_9_value%`
90. `%anarchiacore_killstreak_top_10_name%`
91. `%anarchiacore_killstreak_top_10_value%`

### TOP10 — BESTKILLSTREAK (20)
92. `%anarchiacore_bestkillstreak_top_1_name%`
93. `%anarchiacore_bestkillstreak_top_1_value%`
94. `%anarchiacore_bestkillstreak_top_2_name%`
95. `%anarchiacore_bestkillstreak_top_2_value%`
96. `%anarchiacore_bestkillstreak_top_3_name%`
97. `%anarchiacore_bestkillstreak_top_3_value%`
98. `%anarchiacore_bestkillstreak_top_4_name%`
99. `%anarchiacore_bestkillstreak_top_4_value%`
100. `%anarchiacore_bestkillstreak_top_5_name%`
101. `%anarchiacore_bestkillstreak_top_5_value%`
102. `%anarchiacore_bestkillstreak_top_6_name%`
103. `%anarchiacore_bestkillstreak_top_6_value%`
104. `%anarchiacore_bestkillstreak_top_7_name%`
105. `%anarchiacore_bestkillstreak_top_7_value%`
106. `%anarchiacore_bestkillstreak_top_8_name%`
107. `%anarchiacore_bestkillstreak_top_8_value%`
108. `%anarchiacore_bestkillstreak_top_9_name%`
109. `%anarchiacore_bestkillstreak_top_9_value%`
110. `%anarchiacore_bestkillstreak_top_10_name%`
111. `%anarchiacore_bestkillstreak_top_10_value%`

### Cache topów
- Cache topów jest odświeżany co `stats.topCacheSeconds`.
- Placeholdery TOP zwracają dane wyłącznie z cache (O(1)).
- Brak danych: `*_name` = `"-"`, `*_value` = `"0"`.

## Checklist testów
- [ ] `mvn clean package`
- [ ] Uruchom serwer Paper 1.21.10 z Java 21
- [ ] Sprawdź `/anarchiacore reload` oraz `/anarchiacore combatlog reload`
- [ ] Sprawdź `/stormitemy reload` (jeśli używasz StormItemy)
- [ ] Zweryfikuj działanie placeholderów PAPI
- [ ] Zweryfikuj topki (`%anarchiacore_<typ>_top_<pozycja>_...%`)
