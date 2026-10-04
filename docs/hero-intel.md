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

1. Descargue `hero-intel-1.1.0.jar` desde la página de [Releases](https://github.com/clase-programacion-avanzada/JAVA-Project/releases) del repositorio.
2. Cópielo en `libs/`, al mismo nivel de `build.gradle`.
3. Agregue la versión entregada:

```gradle
dependencies {
    implementation files('libs/hero-intel-1.1.0.jar')
}
```

La versión 1.1.0 conserva las operaciones públicas de 1.0.0 y agrega el despacho de misiones.

## Token de acceso

Cada equipo recibe por un canal privado un **token opaco, único, revocable y temporal**, junto con **la ciudad asignada a su equipo**. El servidor usa el token para autenticar al equipo y separar su mundo de juego: villanos activos, capturas y despachos. Las ciudades que consulte deben ser las de su equipo, escritas exactamente como se las entregaron (las tildes cuentan).

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

- `UUID getDispatchId()`: identificador del despacho. Es el que se usa para consultar el desenlace.
- `UUID getMissionId()`: el mismo UUID de misión que se envió.
- `int getEffectiveThreatLevel()`: amenaza oficial de la ciudad más los villanos activos, con tope 10.
- `Instant getCompletesAt()`: momento a partir del cual se puede resolver la misión.

La misión dura la amenaza efectiva por 30 segundos. Una amenaza efectiva de 8 tarda 240 segundos.

### Idempotencia del despacho

Despachar es idempotente por misión. Repetir la llamada con el mismo `missionId` y los mismos datos devuelve el despacho existente, con el mismo `dispatchId` y la misma hora de finalización. El orden de los héroes dentro del escuadrón no cuenta como un cambio.

Si se repite el mismo `missionId` con datos diferentes, la llamada falla con el mensaje _"La misión ya fue despachada con datos diferentes"_. Una misión ya despachada no se puede modificar: use un `missionId` nuevo.

Gracias a esto, reintentar después de un error de red no crea dos misiones ni duplica capturas.

### Resolver

```java
MissionOutcome outcome = intel.getMissionOutcome(dispatch.getDispatchId());

if (outcome.isSuccess()) {
    System.out.println("Misión cumplida");
}

outcome.getCapturedVillain().ifPresent(villain ->
    System.out.println("Villano capturado: " + villain.getName()));
```

Consultar antes de `getCompletesAt()` falla con el mensaje _"La misión sigue en curso"_ y no cambia nada. Consultar después devuelve siempre el mismo desenlace, sin importar cuántas veces se pregunte.

`MissionOutcome` expone:

- `UUID getOutcomeId()`: identificador del desenlace. Sirve para registrar localmente si ya se aplicó.
- `UUID getDispatchId()`: el despacho al que corresponde.
- `boolean isSuccess()`: si la misión se ganó.
- `Map<UUID, HeroOutcome> getHeroOutcomes()`: resultado de cada héroe, indexado por su UUID. El mapa es inmutable.
- `Optional<VillainIntel> getCapturedVillain()`: villano capturado, si lo hubo.

Los resultados se aplican **por UUID, nunca por nombre**. Dos héroes pueden llamarse igual.

### Resultado de cada héroe

`HeroOutcome` expone:

- `HeroStatus getStatus()`.
- `long getRecoverySeconds()`.

`HeroStatus` admite tres valores:

| Estado | Significado | `getRecoverySeconds()` |
|--------|-------------|------------------------|
| `UNHARMED` | El héroe salió ileso | `0` |
| `INJURED` | El héroe resultó herido | Entre 60 y 300 |
| `DECEASED` | El héroe murió en la misión | `0` |

Un escuadrón que gana con holgura rara vez sufre bajas. Una derrota aumenta la probabilidad de heridas y muerte, y los Novatos mueren con el doble de frecuencia que los demás rangos.

### Capturas

Cuando se gana una misión y quedan villanos activos en la ciudad, la Agencia Central captura uno y lo reporta en `getCapturedVillain()`. Ese villano deja de aparecer en `getVillainActivity(city)` desde ese momento.

La captura ocurre al **resolver** la misión, no al despacharla: mientras la misión está en curso, la lista de villanos activos no cambia.

## Errores

Todas las fallas de la librería son `IntelAccessException`, una excepción no verificada. Basta con capturar ese tipo:

```java
try {
    MissionDispatch dispatch = intel.dispatchMission(missionId, city, profiles, synergyBonus);
} catch (IntelAccessException e) {
    System.out.println("No se pudo despachar la misión: " + e.getMessage());
}
```

### Errores detectados antes de llamar a la Agencia

La librería valida los datos localmente y no gasta una llamada de red si algo está mal:

| Mensaje | Causa |
|---------|-------|
| `La variable de entorno HERO_INTEL_TOKEN no está configurada...` | Falta el token; configúrelo y reinicie el IDE |
| `La ciudad no puede estar vacía` | Ciudad nula, vacía o en blanco |
| `El escuadrón debe tener entre 1 y 8 héroes` | Escuadrón vacío o de más de 8 |
| `El escuadrón repite al héroe <uuid>` | El mismo UUID aparece dos veces |
| `La sinergia debe estar entre 0 y 15` | Bono de sinergia fuera de rango |
| `La estadística de <nombre> debe estar entre 1 y 10` | Estadística efectiva fuera de rango |
| `El héroe necesita un identificador` / `un nombre` / `un rango` | Falta un dato obligatorio del perfil |

### Errores reportados por la Agencia

| Mensaje | Qué hacer |
|---------|-----------|
| `La misión sigue en curso` | Esperar hasta `getCompletesAt()` |
| `La misión ya fue despachada con datos diferentes` | Usar un `missionId` nuevo o reenviar los mismos datos |
| `El token del equipo es inválido, está vencido o fue revocado: ...` | Pedir un token nuevo al profesor |
| `La Agencia Central no reconoce el recurso consultado: ...` | Ciudad inexistente o despacho que no pertenece al equipo |
| `La Agencia Central rechazó los datos enviados: ...` | Revisar el cuerpo de la solicitud |
| `No fue posible contactar a la Agencia Central` | Sin red o servidor caído; no cambie ningún estado local |
| `Se superó el límite de consultas del equipo...` | Esperar los segundos indicados antes de reintentar |

> [!IMPORTANT]
> Si la Agencia Central no está disponible al despachar, **no** marque la misión como iniciada ni los héroes como desplegados. El estado local solo cambia cuando la llamada devuelve un `MissionDispatch`.

## Reglas de seguridad

1. Use únicamente la librería. No haga peticiones HTTP a la Agencia Central ni intente averiguar su dirección.
2. No escriba el token en el código, en archivos de configuración del proyecto, en capturas de pantalla ni en la entrega.
3. No suba el token a Git, aunque el repositorio sea privado. Agregue a `.gitignore` cualquier archivo local donde lo guarde.
4. No incluya el token en URLs, parámetros, logs ni mensajes de error. La librería nunca lo expone de vuelta.
5. No comparta el token con otros equipos ni use el de otro equipo. Cada mundo de juego está aislado por token.
6. Informe de inmediato al profesor si sospecha que el token se filtró, para revocarlo y emitir uno nuevo.
7. La Agencia Central es la autoridad sobre villanos activos, capturas y desenlaces. El proyecto guarda lo que ya observó, pero no inventa ni corrige esos datos.
8. Registre localmente qué desenlaces ya aplicó. Volver a consultar, recargar una partida o reintentar tras un error no puede duplicar experiencia, lesiones, muertes ni capturas.
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