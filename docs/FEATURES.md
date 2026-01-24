# AnarchiaCore — inventory funkcji (oryginały)

## Combatlog / AntyLogout (Dream-AntyLogout)

### Komendy i permisje
* `/antylogout` — główna komenda zarządzania (admin).  
* `/antylogout pvp on|off` — przełączanie PVP (admin).  
* `/antylogout autorespawn on|off` — przełączanie auto-respawn (admin).  
* `/antylogout reload` — przeładowanie konfiguracji (admin).  
* `/wylaczochrone` — wyłączenie ochrony (admin).  
* Uprawnienia (z komentarzy w konfiguracji):  
  * `dream.antylogout.bypass`  
  * `dream.antylogout.command.pvp`  
  * `dream.antylogout.command.autorespawn`  
  * `dream.antylogout.command.reload`  
  * `dream.antylogout.command.protection-off`

### Funkcje / zachowania (wg konfiguracji i źródeł)
* Tag walki (czas `antylogout.duration`) i blokady w trakcie walki.  
* Nadawanie tagu walki od mobów (`antylogout.monster-antylogout`).  
* Nadawanie tagu po ender-perle (`antylogout.ender-pearl-antylogout`).  
* Blokada latania/glide podczas walki (`antylogout.block-gliding`).  
* Nadawanie tagu za void (`antylogout.voidAntylogout`).  
* Wyłączenia per regiony (WG i własne kuboidy) (`antylogout.disabled-regions`).  
* Blokowanie interakcji z blokami i bytami podczas walki (`antylogout.blocked-blocks`, `antylogout.blockedEntity`).  
* Blokada komend w walce + whitelist (`antylogout.disable-commands`, `antylogout.allowed-commands`).  
* Wyświetlanie timera: BossBar / ActionBar z konfiguracją (`antylogout.display`, `antylogout.notice.*`).  
* Powiadomienie o końcu walki (`antylogout.send-end`, `antylogout.end-notice.*`).  
* Ochrona startowa i ochrona po śmierci (`protection.*`).  
* Suffix ochrony (`protection.suffix`).  
* System barrier (knockback/blocked/wall) (`barrier.*`).  
* Obsługa statusu PVP i autorespawnu (`misc.pvpDisabled`, `misc.autorespawn`).  
* Blokowanie obrażeń na creative (`misc.cancel-creative-damage`).  
* Integracje: WorldGuard + LuckPerms + (opcjonalnie) ProtocolLib.  
* BossBar/ActionBar + komunikaty czatu z `message.yml`.

### Pełny config (klucze i znaczenie)
* `debug`  
* `storage-config.*` (typ zapisu, prefix, uri)  
* `antylogout.*`  
  * `duration`  
  * `monster-antylogout`  
  * `ender-pearl-antylogout`  
  * `block-gliding`  
  * `voidAntylogout`  
  * `disabled-regions`  
  * `blocked-blocks`  
  * `blockedEntity`  
  * `disable-commands`  
  * `allowed-commands`  
  * `display`  
  * `notice.title` / `notice.color` / `notice.style`  
  * `send-end`  
  * `end-notice.title` / `end-notice.color` / `end-notice.style`
* `protection.*`  
  * `duration`  
  * `damage-on-protection`  
  * `suffix`  
  * `display`  
  * `notice.title` / `notice.color` / `notice.style`  
  * `death-duration`  
  * `death-notice.title` / `death-notice.color` / `death-notice.style`  
  * `send-end`  
  * `end-notice.title` / `end-notice.color` / `end-notice.style`
* `barrier.*`  
  * `barrier-type`  
  * `wall-block`  
  * `knockback-strength`  
  * `knockback-y`
* `misc.*`  
  * `cancel-creative-damage`  
  * `pvpDisabled`  
  * `autorespawn`

### Message config (message.yml)
Wszystkie klucze z `message.yml` (komendy, błędy, statusy, komunikaty) muszą być zachowane 1:1, m.in.:
* `command-usage`, `command-usage-help`, `command-usage-not-found`, `command-path-not-found`  
* `command-no-permission`, `command-not-player`, `command-not-console`  
* `command-invalid-format`, `player-not-found`, `world-not-found`  
* `config-reloaded`, `config-reload-error`, `error`  
* `no-protection`, `pvp-is-off`, `player-has-protection`, `you-have-protection`  
* `cant-hurt-on-creative`, `cant-use-commands`, `protection-ended`  
* `combat-ended`, `combat-start`, `combat-logout`  
* `teleport-cancelled`, `interaction-blocked`, `health-left`  
* `protection-added`, `cant-fly-in-combat`  
* `enabled`, `disabled`, `pvp-status-changed`, `autorespawn-status-changed`

---

## CustomItems (StormItemy / Anarchia.gg)

### Komendy i permisje (wg źródeł)
* Komenda główna: `stormitemy` (tab-complete wskazuje `stormitemy`).  
* Permisja admina: `stormitemy.admin`.  
* Z zestawu handlerów komend w źródłach występują funkcje:  
  * rozdawanie itemów (`give`)  
  * przeładowanie konfiguracji (`reload`)  
  * panel/GUI oraz książki (komendy panelu/ksiąg)

### Itemy i eventówki (wszystkie pliki z `items/`)
* bombardaMaxima, dynamit, turbotrap, turbodomek  
* arcusmagnus, balonikzhelem, blokwidmo, boskitopor  
* cudownalatarnia, excalibur, kostkarubika, koronaanarchi  
* krewwampira, kupaanarchi, kosa, lewejajko  
* lizak, lopatagrincha, lukkupidyna, marchewkowakusza  
* marchewkowymiecz, olaf, parawan, piekielnatarcza  
* plecakdrakuli, przeterminowanytrunek, rozga, rozakupidyna  
* rozdzkailuzjonisty, rozgotowanakukurydza, sakiewkadropu  
* siekieragrincha, smoczymiecz, sniezka, sniezynka  
* splesnialakanapka, totemulaskawienia, trojzabposejdona  
* watacukrowa, wampirzejablko, wedkanielota, wedkasurferka  
* wyrzutniahydroklatki, zajeczymiecz, zatrutyolowek  
* zlamaneserce, zmutowanycreeper, wzmocnionaeltra  
* anarchicznyhelm, anarchicznaklata, anarchicznespodnie, anarchicznebuty  
* anarchicznyhelmii, anarchiczna_klata_ii, anarchiczne_spodnie_ii, anarchiczne_buty_ii  
* anarchicznymiecz, anarchicznykilof, anarchicznyluk, antycobweb  
* cieplemleko, piernik

### Księgi/Enchanted Books (pliki `books/`)
* niepodpalenie  
* regeneracja  
* szybkosc  
* spowolnienie  
* dodatkowy_drop  
* pospiech  
* unik  
* wampiryzm  
* ulaskawienie  
* wytrzymalosc  
* zatrucie  
* obrazenia_krytyczne

### GUI
* Menu zaczarowań (sekcja `zaczarowania.menu` w config.yml).  
* Przyciski wyboru itemu, zaczarowania i zamknięcia GUI.  
* Wspierane formaty lore i kolory (MiniMessage + legacy `&`).

### Konfiguracja (pliki i klucze)
* `config.yml` (główny):  
  * `disabled-regions`  
  * `language`  
  * `zaczarowania.*` (item, menu, effects, probability)  
  * `actionbar.*` (włącz/wyłącz, separator, formaty, kolory, display_names)
* `lang/POL.yml` i `lang/ENG.yml`  
* `custom-items/*.yml` (pełne definicje eventówek — wszystkie klucze, cooldowny, efekty, regiony, wiadomości)  
* `books/*.yml` (definicje ksiąg i efektów)  
* `regions.yml`, `texturepack_preferences.yml`  
* `data.yml`, `data.db` (dane pluginu)  
* schematy: `custom-items/trapanarchia.schem`, `custom-items/turbodomek.schem`

---

## ZIP configs parity

### Wykryte ZIP-y
* `configs/Dream-AntyLogout.zip`  
* `configs/STORMITEMY.zip`

### Zawartość ZIP-ów
* **Dream-AntyLogout.zip**  
  * `config.yml`  
  * `message.yml`  
  * `profiles/*.json`
* **STORMITEMY.zip**  
  * `config.yml`  
  * `lang/ENG.yml`  
  * `lang/POL.yml`  
  * `items/*.yml` (kopiowane do `custom-items/*.yml`)  
  * `books/*.yml`  
  * `regions.yml`  
  * `texturepack_preferences.yml`  
  * `data.yml`, `data.db`  
  * schematy: `trapanarchia.schem`, `turbodomek.schem` (kopiowane do `custom-items/`)

### Parzystość konfiguracji
Wszystkie pliki z powyższych ZIP-ów są zachowane 1:1 i rozpakowywane do katalogu danych pluginu (`plugins/AnarchiaCore/configs/...`), a pliki eventowych itemów oraz schematy z paczki STORMITEMY są kopiowane do `plugins/AnarchiaCore/custom-items/`, aby rdzeń mógł je czytać bez utraty struktury lub wartości.
