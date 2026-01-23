# AnarchiaCore

## Konfiguracja

### `blockedEndCrystalDimensions`
Lista miejsc, w których blokowane jest stawianie kryształów Endu. Wartości mogą być:

- nazwy środowisk Bukkit (`NETHER`, `END`, `NORMAL`, itp.),
- **lub** nazwy światów (case-insensitive).

Przykład:

```yaml
blockedEndCrystalDimensions:
  - NETHER
  - END
  - spawn
  - pvp_world
```

