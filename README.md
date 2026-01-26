# AnarchiaCore

## Wymagania
- Paper 1.21.10 (API 1.21).  
- Java 21.  
- WorldGuard (opcjonalnie, wymagany do reguł regionów dla eventowych itemów).  
- PlaceholderAPI (opcjonalnie, wymagany do placeholderów PAPI).  

## Instalacja
1. Zbuduj plugin (`mvn clean package`) lub skopiuj gotowy plik `.jar`.  
2. Wrzuć `AnarchiaCore.jar` do `plugins/` i uruchom serwer.  
3. Po pierwszym starcie powstaną katalogi:
   - `plugins/AnarchiaCore/config.yml` (główna konfiguracja).  
   - `plugins/AnarchiaCore/combatlog/` (config i message dla AntyLogout).  
   - `plugins/AnarchiaCore/configs/STORMITEMY/` + `plugins/AnarchiaCore/custom-items/` (pliki StormItemy).  

## Reload
- ` /anarchiacore reload` — przeładowuje: `config.yml`, storage, serca, kosz, custom items, statystyki, combatlog + prefix, instalację configów StormItemy oraz rejestrację placeholderów.  
- ` /antylogout reload` — przeładowuje wyłącznie konfiguracje combatlog (`combatlog/config.yml` + `combatlog/message.yml`).  
- ` /stormitemy reload` — przeładowuje konfiguracje StormItemy (z `configs/STORMITEMY/`).  

## Konfiguracja

### `plugins/AnarchiaCore/config.yml`
Sekcje i klucze:
- `defaultHearts`, `maxHearts`  
- `anarchiczneSerce` (definicja itemu) + `anarchiczneSerceCustomModelData`  
- `blockedEndCrystalEnvironments`, `blockedEndCrystalWorlds`  
- `stats.topCacheSeconds`, `stats.topLimit`, `stats.killCredit.*`  
- `trash.title`, `trash.rows`, `trash.clearMessage`  
- `messages.*` (prefix, permission, serca, kosz, custom items, blokady kryształów)  

Przykład:

```yaml
blockedEndCrystalEnvironments:
  - NETHER
  - END
blockedEndCrystalWorlds:
  - spawn
  - pvp_world
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

### `plugins/AnarchiaCore/custom-items/`
Eventowe itemy i schematy kopiowane z paczki StormItemy (np. `bombardamaxima.yml`, `turbotrap.yml`, `totemulaskawienia.yml`, `trapanarchia.schem`, `turbodomek.schem`).  

## Komendy i permisje

| Komenda | Opis | Permisja |
| --- | --- | --- |
| `/anarchiacore reload` | Reload całego pluginu | `anarchiacore.admin` |
| `/anarchiacore heart give <player> [amount]` | Daje Anarchiczne Serce | `anarchiacore.admin` |
| `/anarchiacore heart setitem` | Zapisuje definicję Anarchicznego Serca | `anarchiacore.admin` |
| `/anarchiacore customitems give <item> [player]` | Daje custom item | `anarchiacore.admin` |
| `/anarchiacore customitems list` | Lista custom itemów | `anarchiacore.admin` |
| `/antylogout reload` | Reload AntyLogout | `dream.antylogout.command.reload` |
| `/antylogout pvp on|off` | Przełączanie PVP | `dream.antylogout.command.pvp` |
| `/antylogout autorespawn on|off` | Przełączanie autorespawn | `dream.antylogout.command.autorespawn` |
| `/antylogout protection-off` | Wyłączenie ochrony | `dream.antylogout.command.protection-off` |
| `/stormitemy ...` | Komendy StormItemy (give/reload/panel/itp.) | `stormitemy.admin` |
| `/kosz` | Otwiera kosz | brak |
| `/menuprzedmioty` | Menu przedmiotów StormItemy | brak |

Dodatkowe bypassy:
- `dream.antylogout.bypass` — omija blokady combatlog.  

## Placeholdery (PlaceholderAPI)
Identyfikator: `anarchiacore`.

### Podstawowe
- `%anarchiacore_hearts%`  
- `%anarchiacore_defaulthearts%`  
- `%anarchiacore_maxhearts%`  
- `%anarchiacore_combat_tagged%`  
- `%anarchiacore_combat_timeleft%`  
- `%anarchiacore_combat_timeleft_mmss%`  
- `%anarchiacore_kills%`  
- `%anarchiacore_deaths%`  
- `%anarchiacore_killstreak%`  
- `%anarchiacore_bestkillstreak%`  
- `%anarchiacore_kdr%`  

### Topki
Format:
- `%anarchiacore_<typ>_top_<pozycja>_name%`  
- `%anarchiacore_<typ>_top_<pozycja>_value%`  

Typy (`<typ>`):
- `kills`, `deaths`, `kdr`, `killstreak`, `bestkillstreak`  

Przykłady:
- `%anarchiacore_kills_top_1_name%`  
- `%anarchiacore_kdr_top_3_value%`  

Pozycja (`<pozycja>`) jest liczona od `1` do limitu `stats.topLimit`.  

## Checklist testów
- [ ] `mvn clean package`  
- [ ] Uruchom serwer Paper 1.21.10 z Java 21  
- [ ] Sprawdź `/anarchiacore reload` oraz `/antylogout reload`  
- [ ] Sprawdź `/stormitemy reload` (jeśli używasz StormItemy)  
- [ ] Zweryfikuj działanie placeholderów PAPI  
- [ ] Zweryfikuj topki (`%anarchiacore_<typ>_top_<pozycja>_...%`)  
