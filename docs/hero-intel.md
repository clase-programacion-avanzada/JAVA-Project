# Librería oficial de la Agencia Central: `hero-intel`

`hero-intel` permite que HeroHub consulte información oficial y, desde la versión 1.1.0, despache misiones para que la Agencia Central determine su duración y desenlace.

La librería es la única interfaz que deben usar los estudiantes. No deben realizar peticiones HTTP directamente ni conocer la dirección del servidor.

## Índice

1. [Agregar la librería](#agregar-la-librería)
2. [Token de acceso](#token-de-acceso)
3. [Configurar el token](#configurar-el-token)
4. [API 1.0.0](#api-100-informes)
5. [API 1.1.0](#api-110-despacho-de-misiones)
6. [Errores](#errores)
7. [Reglas de seguridad](#reglas-de-seguridad)

## Agregar la librería

1. Descargue el `.jar` publicado por el profesor.
2. Cópielo en `libs/`, al mismo nivel de `build.gradle`.
3. Agregue la versión entregada:

```gradle
dependencies {
    implementation files('libs/hero-intel-1.1.0.jar')
}
```

La versión 1.1.0 conserva las operaciones públicas de 1.0.0 y agrega el despacho de misiones.

## Token de acceso

Cada equipo recibe por un canal privado un **token opaco, único, revocable y temporal**. El servidor usa el token para autenticar al equipo y separar su mundo de juego: villanos activos, capturas y despachos.

El token funciona como una contraseña:

- Solo los integrantes del equipo deben conocerlo.
- No se escribe en código, archivos de configuración, entregas ni capturas de pantalla.
- No se incluye en URLs, parámetros ni mensajes de error.
- No se sube a Git, incluso si el repositorio es privado.
- Expira al terminar el curso y el profesor puede revocarlo o rotarlo.

La librería lee `HERO_INTEL_TOKEN` y envía internamente el encabezado `Authorization: Bearer <token>` exclusivamente mediante HTTPS. El programa del estudiante nunca recibe ni pasa el token como argumento.

> [!IMPORTANT]
> Quien posea el token puede usar la identidad del equipo. Si sospecha una filtración, informe inmediatamente al profesor para revocarlo y recibir uno nuevo.

## Configurar el token

### Windows

En `cmd`:

```bat
setx HERO_INTEL_TOKEN "token-entregado-por-el-profesor"
```

Cierre y vuelva a abrir el IDE y las terminales. Para comprobar que existe sin imprimirlo:

```bat
if defined HERO_INTEL_TOKEN (echo Token configurado) else (echo Token ausente)
```

### macOS y Linux

Si usa zsh:

```bash
echo 'export HERO_INTEL_TOKEN="token-entregado-por-el-profesor"' >> ~/.zshrc
source ~/.zshrc
```

Si usa bash, reemplace `~/.zshrc` por `~/.bashrc`. Compruebe la configuración sin mostrar el token:

```bash
[[ -n "$HERO_INTEL_TOKEN" ]] && echo "Token configurado" || echo "Token ausente"
```

### Configuración del IDE

Como alternativa, agregue `HERO_INTEL_TOKEN` a las variables de entorno de la configuración de ejecución. No la guarde en un archivo compartido del proyecto.

## API 1.0.0: informes

La clase principal es `io.github.dmorav1.herointel.IntelService`:

```java
IntelService intel = IntelService.create();

int threat = intel.getCityThreatLevel("Metrópolis");

for (VillainIntel villain : intel.getVillainActivity("Metrópolis")) {
    System.out.println(villain.getName() + " - " + villain.getCity());
}
```

| Método | Retorna | Descripción |
|--------|---------|-------------|
| `IntelService.create()` | `IntelService` | Lee `HERO_INTEL_TOKEN`. Falla si está ausente, vacío, vencido o revocado. |
| `getCityThreatLevel(String city)` | `int` | Amenaza oficial base de la ciudad, entre 1 y 10. |
| `getVillainActivity(String city)` | `List<VillainIntel>` | Villanos activos para el equipo en esa ciudad. Los capturados dejan de aparecer. |

`VillainIntel` es inmutable y expone:

- `UUID getId()`.
- `String getName()`.
- `String getCity()`.

Los UUID de villano son estables. El mismo villano conserva su identidad entre consultas.

## API 1.1.0: despacho de misiones

### Perfiles

```java
HeroProfile profile = new HeroProfile(
    hero.getId(),
    hero.getName(),
    Rank.VETERAN,
    hero.getEffectiveCombat(),
    hero.getEffectiveIntellect(),
    hero.getEffectiveVigor(),
    hero.getEffectiveCharisma(),
    hero.getEffectiveMobility()
);
```

`Rank` admite `ROOKIE`, `VETERAN` y `ELITE`. Corresponden a `RookieHero`, `VeteranHero` y `EliteHero` del proyecto. Las estadísticas efectivas deben estar entre 1 y 10.

### Despachar

```java
MissionDispatch dispatch = intel.dispatchMission(
    mission.getId(),
    mission.getCity(),
    profiles,
    synergyBonus
);

System.out.println("Amenaza efectiva: " + dispatch.getEffectiveThreatLevel());
System.out.println("Finaliza: " + dispatch.getCompletesAt());
```

La firma pública es:

```java
MissionDispatch dispatchMission(
    UUID missionId,
    String city,
    List<HeroProfile> squad,
    int synergyBonus
);
```

La Agencia Central calcula la amenaza efectiva, duración y desenlace. `MissionDispatch` expone:

- `UUID getDispatchId()`.
- `UUID getMissionId()`.
- `int getEffectiveThreatLevel()`.
- `Instant getCompletesAt()`.

El despacho es **idempotente** por equipo y `missionId`. Si la respuesta se pierde, repetir exactamente la misma solicitud devuelve el despacho existente. Reutilizar el mismo `missionId` con datos diferentes produce un error.

### Consultar el desenlace

```java
MissionOutcome outcome = intel.getMissionOutcome(dispatch.getDispatchId());

if (outcome.isSuccess()) {
    System.out.println("Misión cumplida");
}

for (Map.Entry<UUID, HeroOutcome> entry : outcome.getHeroOutcomes().entrySet()) {
    System.out.println(entry.getKey() + " -> " + entry.getValue().getStatus());
}
```

`MissionOutcome` expone:

- `UUID getOutcomeId()`.
- `UUID getDispatchId()`.
- `boolean isSuccess()`.
- `Map<UUID, HeroOutcome> getHeroOutcomes()`.
- `Optional<VillainIntel> getCapturedVillain()`.

`HeroOutcome` expone `getStatus()` (`UNHARMED`, `INJURED` o `DECEASED`) y `getRecoverySeconds()`. La duración entregada corresponde a una lesión reportada por la Agencia; HeroHub decide localmente cuándo una segunda lesión pasa a `OUT_OF_SERVICE`. Los resultados se relacionan por UUID de héroe, nunca por nombre.

Consultar antes de `completesAt` produce `IntelAccessException`. Una vez disponible, el mismo `dispatchId` siempre retorna el mismo `outcomeId` y desenlace. La aplicación debe guardar si ya lo aplicó para no duplicar recompensas o consecuencias.

### Autoridad de la Agencia Central

El servidor conserva por equipo:

- Villanos activos y capturados.
- Misiones despachadas.
- Hora de finalización.
- Desenlace definitivo.

En una victoria puede capturarse un villano activo de la ciudad. El villano aparece en `getCapturedVillain()` y deja de aparecer en consultas posteriores. El cliente no elimina villanos directamente.

## Errores

Todas las fallas se reportan como `IntelAccessException`. El menú nunca debe terminar abruptamente por una falla externa.

| Situación | Significado | Acción |
|-----------|-------------|--------|
| Token ausente | `HERO_INTEL_TOKEN` no está configurado | Configure la variable y reinicie el IDE. |
| `401` | Token inválido, vencido o revocado | Solicite revisión o rotación al profesor. |
| `403` | El token no tiene permiso para la operación | No reintente; informe al profesor. |
| `409` | Misión repetida con datos diferentes o desenlace aún no disponible | Corrija la solicitud o espere hasta `completesAt`. |
| `429` | Se superó el límite por token | Espere antes de reintentar; no consulte en ciclos. |
| Error de red | La Agencia Central no está disponible | Conserve el estado local y permita continuar usando otras opciones. |

Los mensajes y logs nunca incluyen el token completo.

## Reglas de seguridad

1. Use únicamente `IntelService.create()`; no pida ni reciba el token dentro del programa.
2. No imprima variables de entorno ni adjunte el token al solicitar ayuda.
3. No comparta el token con otros equipos.
4. No automatice consultas repetitivas. El servidor aplica límites por token.
5. Ante una filtración, deje de usar el token y solicite su revocación.
6. El profesor administra las operaciones de reinicio y datos de prueba con credenciales diferentes, que nunca se distribuyen con la librería.