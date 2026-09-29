# Librería oficial de la Agencia Central: `hero-intel`

`hero-intel` es la librería oficial de Java mediante la cual la **Agencia Central** comparte información actualizada con los sistemas de HeroHub: el nivel de amenaza oficial de una ciudad y la actividad villanesca reciente.

> **Confidencial:** la clave de acceso identifica a su equipo ante la Agencia Central. No la comparta con otros equipos ni la suba a repositorios públicos. Si la Agencia detecta un uso indebido, la clave será revocada.

## Índice

1. [Agregar la librería al proyecto](#agregar-la-librería-al-proyecto)
2. [Configurar la clave de acceso](#configurar-la-clave-de-acceso)
    - [Windows](#windows)
    - [macOS](#macos)
    - [Verificar la configuración](#verificar-la-configuración)
3. [Uso de la librería](#uso-de-la-librería)
4. [Errores y manejo de excepciones](#errores-y-manejo-de-excepciones)
5. [Reglas de la Agencia](#reglas-de-la-agencia)

## Agregar la librería al proyecto

1. Descargue el archivo `hero-intel-1.0.0.jar` que el profesor publicará.
2. Cree una carpeta llamada `libs` en la raíz de su proyecto (al mismo nivel de `build.gradle`) y copie el archivo allí.
3. Agregue la dependencia en el archivo `build.gradle`:

```gradle
dependencies {
    implementation files('libs/hero-intel-1.0.0.jar')
    // ... resto de dependencias
}
```

4. Sincronice el proyecto con Gradle (en IntelliJ: el botón del elefante 🐘, o _Reload All Gradle Projects_).

[Volver al índice](#índice)

## Configurar la clave de acceso

La librería lee la clave de la **variable de entorno** `HERO_INTEL_API_KEY`. Así, la clave nunca aparece en su código fuente. El profesor le entregará la clave de su equipo en privado.

### Windows

1. Abra una terminal (`cmd`) y ejecute:

    ```
    setx HERO_INTEL_API_KEY "la-clave-de-su-equipo"
    ```

2. **Cierre y vuelva a abrir IntelliJ** (y cualquier terminal que tenga abierta): las variables definidas con `setx` solo aplican a programas abiertos después del comando.

También puede hacerlo desde _Configuración → Sistema → Acerca de → Configuración avanzada del sistema → Variables de entorno… → Nueva…_ con nombre `HERO_INTEL_API_KEY` y el valor de su clave.

### macOS

1. Abra la aplicación Terminal y ejecute:

    ```bash
    echo 'export HERO_INTEL_API_KEY="la-clave-de-su-equipo"' >> ~/.zshrc
    ```

2. Recargue la configuración:

    ```bash
    source ~/.zshrc
    ```

3. Si abre IntelliJ desde el _Dock_, ciérrelo y ábralo de nuevo para que tome la nueva variable. (Alternativa: abrir IntelliJ desde la terminal con `open -a "IntelliJ IDEA"`).

> **Alternativa para ambos sistemas (útil para pruebas):** en IntelliJ, _Run → Edit Configurations… → su configuración de ejecución → Environment variables_ y agregue `HERO_INTEL_API_KEY=la-clave-de-su-equipo`. Esto aplica solo a esa configuración de ejecución.

### Verificar la configuración

- Windows (`cmd`): `echo %HERO_INTEL_API_KEY%`
- macOS (Terminal): `echo $HERO_INTEL_API_KEY`

Debe imprimir su clave. Si imprime vacío (o la variable literal en Windows), repita los pasos y recuerde reiniciar el IDE.

[Volver al índice](#índice)

## Uso de la librería

La clase principal es `com.javeriana.herointel.IntelService`. Se obtiene una instancia con el método de fábrica `create()`, que lee la clave de la variable de entorno:

```java
import com.javeriana.herointel.IntelService;

public class Main {
    public static void main(String[] args) {
        IntelService intel = IntelService.create();

        // Nivel de amenaza oficial de una ciudad (entero de 1 a 10)
        int threat = intel.getCityThreatLevel("Metrópolis");
        System.out.println("Amenaza oficial: " + threat);

        // Actividad villanesca reciente en una ciudad
        for (String villain : intel.getVillainActivity("Metrópolis")) {
            System.out.println("Villano reportado: " + villain);
        }
    }
}
```

| Método | Retorna | Descripción |
|--------|---------|-------------|
| `IntelService.create()` | `IntelService` | Crea el servicio leyendo `HERO_INTEL_API_KEY`. Lanza `IntelAccessException` si la variable no está definida. |
| `getCityThreatLevel(String city)` | `int` | Nivel de amenaza oficial de la ciudad (1–10). |
| `getVillainActivity(String city)` | `List<String>` | Villanos activos reportados en la ciudad (lista vacía si no hay reportes). |

Ustedes no necesitan saber _cómo_ la librería obtiene la información: es un asunto clasificado de la Agencia Central. Lo importante es decidir **dónde tiene sentido usarla** en el sistema y **manejar sus errores correctamente**.

### Versión 1.1.0: la Agencia simula misiones

Durante el semestre, la Agencia Central publicará la versión **1.1.0** del `.jar` con dos nuevas operaciones. Para actualizar, reemplace el archivo en `libs/` y ajuste la versión en su `build.gradle`:

```java
import com.javeriana.herointel.*;
```

| Método / tipo | Descripción |
|---------------|-------------|
| `dispatchMission(String city, List<HeroProfile> squad, int synergyBonus)` | Despacha un escuadrón a una ciudad. `synergyBonus` es el bono de sinergia del escuadrón (0–15). Retorna un `MissionDispatch` con el `dispatchId` y la hora exacta de finalización (`getCompletesAt()`). |
| `getMissionOutcome(String dispatchId)` | Retorna el `MissionOutcome` de la misión: si fue ganada (`isSuccess()`) y el resultado de cada héroe (`getHeroOutcomes()`). **Si la misión aún está en curso, lanza `IntelAccessException`** — la paciencia también es una virtud heroica. |
| `HeroProfile(name, rank, combat, intellect, vigor, charisma, mobility)` | El perfil de un héroe al despacharlo. `rank` es el enum `Rank` (`ROOKIE`, `VETERAN`, `ELITE`) y cada una de las cinco estadísticas va de 1 a 10. |
| `HeroOutcome` | Resultado de un héroe: `getStatus()` retorna `UNHARMED` (ileso), `INJURED` (herido, con `getRecoverySeconds()` segundos de recuperación) o `DECEASED` (caído en el deber 🕯️). |

```java
IntelService intel = IntelService.create();

List<HeroProfile> squad = List.of(
    new HeroProfile("Capitán Trueno", Rank.VETERAN, 8, 5, 7, 4, 6),
    new HeroProfile("Centella", Rank.ROOKIE, 4, 6, 5, 7, 9)
);

MissionDispatch dispatch = intel.dispatchMission("Metrópolis", squad, 10);
System.out.println("La misión terminará a las: " + dispatch.getCompletesAt());

// ... cuando ya haya pasado la hora de finalización ...
MissionOutcome outcome = intel.getMissionOutcome(dispatch.getDispatchId());
System.out.println(outcome.isSuccess() ? "¡Misión cumplida!" : "Misión fallida...");
outcome.getHeroOutcomes().forEach((hero, result) ->
    System.out.println(hero + " -> " + result.getStatus())
);
```

La Agencia Central decide los desenlaces con criterios que no siempre coinciden con las estimaciones locales de la agencia. Esa es la gracia: **planifique bien sus escuadrones**.

[Volver al índice](#índice)

## Errores y manejo de excepciones

Todas las fallas de la librería se reportan con la excepción `com.javeriana.herointel.IntelAccessException`. **Su programa nunca debe terminar abruptamente por causa de la Agencia Central**: capture la excepción y muestre un mensaje amigable.

```java
import com.javeriana.herointel.IntelAccessException;
import com.javeriana.herointel.IntelService;

try {
    IntelService intel = IntelService.create();
    int threat = intel.getCityThreatLevel("Metrópolis");
    System.out.println("Amenaza oficial: " + threat);
} catch (IntelAccessException e) {
    System.out.println("No se pudo consultar el informe de la Agencia Central");
}
```

| Situación | Causa probable | Qué hacer |
|-----------|----------------|-----------|
| `IntelAccessException` al llamar `create()` | La variable `HERO_INTEL_API_KEY` no está definida | Repita la [configuración](#configurar-la-clave-de-acceso) y reinicie el IDE |
| `IntelAccessException`: clave rechazada | La clave es incorrecta o fue revocada | Verifique que copió la clave completa; si persiste, hable con el profesor |
| `IntelAccessException`: Agencia saturada | Demasiadas consultas seguidas | Espere unos segundos y reintente; no consulte en ciclos innecesarios |
| `IntelAccessException`: no se pudo contactar | Problema de red o la Agencia no está disponible | Verifique su conexión; su programa debe seguir funcionando sin el informe |
| `IntelAccessException`: la misión sigue en curso | Consultó el desenlace antes de la hora de finalización | Espere a que pase la hora indicada por `getCompletesAt()` y reintente |

[Volver al índice](#índice)

## Reglas de la Agencia

1. Una clave por equipo. **No la comparta ni la publique** (ni siquiera en su repositorio: si usa git, la clave nunca debe estar en el código — por eso se usa una variable de entorno).
2. Consulte a la Agencia solo cuando el informe sea necesario; no haga consultas en ciclos repetitivos.
3. La Agencia Central lleva registro de las interacciones con sus sistemas de información. Comportamientos anómalos serán investigados. 🕵️

[Volver al índice](#índice)
