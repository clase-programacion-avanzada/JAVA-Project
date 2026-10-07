# Iteración 3 - Strings y excepciones: el fanático y la Agencia Central

Este documento es el enunciado detallado de la iteración 3. La [hoja de ruta del README](../README.md#hoja-de-ruta-del-proyecto) explica el resultado esperado en general; aquí están las reglas exactas, incluidos los mensajes que debe mostrar el programa. **Cómo** diseñarlo (qué clases, qué capas, qué validaciones en cuál capa) lo deciden ustedes en su propuesta.

## Índice

1. [Qué cambia respecto a la iteración 2](#qué-cambia-respecto-a-la-iteración-2)
2. [Reglas generales](#reglas-generales)
3. [Parte 1 - Taller en clase (semana 12): inicio de sesión y registro de fanáticos](#parte-1---taller-en-clase-semana-12-inicio-de-sesión-y-registro-de-fanáticos)
4. [Parte 2 - En casa (semana 13): seguir héroes, administrador y Agencia Central](#parte-2---en-casa-semana-13-seguir-héroes-administrador-y-agencia-central)
5. [Entrega y calificación](#entrega-y-calificación)
6. [Qué debe incluir la propuesta del lunes](#qué-debe-incluir-la-propuesta-del-lunes)

## Qué cambia respecto a la iteración 2

- El programa tiene **dos módulos**: el del administrador (ya existe) y el del **fanático**, con inicio de sesión. El módulo del fanático hace solo dos cosas: autenticarse y seguir o dejar de seguir héroes.
- Los héroes ya traen experiencia, estadísticas y estado desde el código base. Ahora deben **calcular su nivel y su rango**, y con ellos se aplica una regla nueva sobre los equipos.
- Todas las operaciones validan sus datos y responden con **excepciones propias** y mensajes claros.
- **Ninguna entrada inválida termina el programa**: el menú sigue funcionando.
- Al registrar una misión, el administrador consulta a la **Agencia Central** (librería `hero-intel`) el nivel de amenaza oficial de la ciudad.

Antes de empezar, configuren la librería y su token siguiendo la sección [La librería de la Agencia](../README.md#la-librería-de-la-agencia) del README.

## Reglas generales

1. Crean el paquete `exception` con dos excepciones propias, ambas **verificadas** (extienden `Exception`): `NotFoundException` (algo no existe) y `AlreadyExistException` (algo ya existe). Los datos inválidos usan `IllegalArgumentException` de Java.
2. **La lógica lanza la excepción y la vista la captura**, muestra su mensaje y vuelve al menú. Ninguna excepción esperada puede terminar el programa.
3. Los mensajes se muestran **exactamente** como aparecen en este documento. Donde dice `${id}`, `${name}`, `${username}`, `${teamName}` o `${codeName}` se reemplaza por el valor real.
4. **Identificadores.** Todo id que digite el usuario se valida antes de buscarlo:
    - Vacío o `null` → `IllegalArgumentException` con el mensaje que corresponda:
        - _"El id del fanático no puede estar vacío o ser null"_
        - _"El id del héroe no puede estar vacío o ser null"_
        - _"El id del equipo no puede estar vacío o ser null"_
        - _"El id de la misión no puede estar vacío o ser null"_
    - Con formato inválido (no es un UUID) → `IllegalArgumentException`: _"El id ${id} no tiene un formato válido"_.
5. **Números.** Si el usuario digita letras donde se espera un número (una opción del menú, la edad, las cinco estadísticas y la experiencia de un héroe, el nivel de amenaza, la duración), el programa no termina: muestra un mensaje y vuelve a pedir el dato.
6. **Espacios y mayúsculas.** Antes de validar y de comparar, a todo texto digitado se le quitan los espacios del inicio y del final. Los nombres de usuario se comparan **exactamente** (las mayúsculas cuentan, igual que en el inicio de sesión); los nombres de héroes y de equipos se comparan **sin distinguir mayúsculas de minúsculas**.
7. **Listas.** Antes de pedir un id se muestra la lista de donde se elige. Si una lista está vacía se muestra _"No hay elementos."_ (el código base ya lo hace).
8. **Contraseñas.** La contraseña de un fanático no aparece en ninguna lista ni en ningún mensaje.
9. El menú principal queda así: `1. Módulo administrador`, `2. Módulo fanático`, `0. Salir`.

## Parte 1 - Taller en clase (semana 12): inicio de sesión y registro de fanáticos

El fanático es un **espectador** de la agencia: no crea equipos ni gestiona nada. Sigue a sus héroes favoritos (no a equipos). En el taller se construyen la puerta de entrada del fanático y las validaciones de su registro.

**Antes del taller** registren, con el módulo del administrador, al menos un fanático y varios héroes para tener con qué probar.

**Iniciar y cerrar sesión.** El fanático se autentica con su nombre de usuario y su contraseña; ambos deben coincidir exactamente (las mayúsculas cuentan). Si no coinciden (o alguno de los dos está vacío) se muestra _"Usuario o contraseña incorrectos"_, no se muestra el menú del fanático y se vuelve al menú principal. Con la sesión iniciada, el menú del fanático ofrece cerrar sesión y volver al menú principal. Las demás opciones de su menú (seguir héroes) se completan en la parte 2.

**Registrar fanático.** Se validan los datos en este orden; la primera regla que falla produce el error:

1. Ningún campo de texto puede ser `null`, vacío o solo espacios → `IllegalArgumentException`: _"Los campos no pueden estar vacíos o ser null"_.
2. El nombre de usuario tiene entre 8 y 10 caracteres y solo contiene letras (mayúsculas o minúsculas), números o los caracteres `_` y `-` → `IllegalArgumentException`: _"El nombre de usuario debe tener entre 8 y 10 caracteres y debe contener solo letras (mayúsculas o minúsculas), números o los caracteres '_' y '-'"_.
3. El nombre de usuario no existe todavía (comparación exacta) → `AlreadyExistException`: _"El fanático con nombre de usuario ${username} ya existe"_.
4. La contraseña tiene mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial de este conjunto: `#?!@$%^&*-` → `IllegalArgumentException`: _"La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (#?!@$%^&*-)"_.
5. La edad es **mayor** a 14 años (14 no se acepta) → `IllegalArgumentException`: _"La edad debe ser mayor a 14 años"_.

## Parte 2 - En casa (semana 13): seguir héroes, administrador y Agencia Central

### Módulo del fanático: seguir héroes

Con la sesión iniciada, el menú del fanático es exactamente este:

```
1. Seguir a un héroe.
2. Dejar de seguir a un héroe.
3. Ver los héroes que sigo.
0. Cerrar sesión.
```

Sus opciones:

1. **Seguir a un héroe.** El sistema muestra primero la lista de héroes.
    - Si el id no existe → `NotFoundException`: _"El héroe con id ${id} no existe"_.
    - Si ya lo sigue → `AlreadyExistException`: _"El fanático ya sigue al héroe ${name}"_.
2. **Dejar de seguir a un héroe.** El sistema muestra primero los héroes que sigue. Si el id no está en su lista → `NotFoundException`: _"El héroe con id ${id} no existe en la lista de héroes seguidos del fanático"_.
3. **Ver los héroes que sigue.**
0. **Cerrar sesión.** Vuelve al menú principal.

El fanático no consulta equipos ni misiones: en esta iteración las misiones todavía no se despachan.

### Eliminar fanáticos

**Eliminar fanático.** Si el id no existe → `NotFoundException`: _"El fanático con id ${id} no existe"_.

### Héroes

Desde el código base, cada héroe tiene experiencia (`experience`) y estado (`state`). **`Hero` debe tener dos métodos nuevos que escriben ustedes**:

- `getLevel()`: el nivel es la experiencia dividida entre 100, sin decimales (999 de experiencia es nivel 9; 1000 es nivel 10).
- `getRank()`: el rango se calcula con el nivel y se devuelve como texto: `intern` (nivel 0 a 9), `junior` (10 a 19), `veteran` (20 a 39) o `elite` (40 en adelante). El rango no se guarda: siempre se calcula.

Reglas de los héroes:

- **Registrar héroe.** El poder principal y la ciudad de origen no se validan en esta iteración. Se validan los datos en este orden:
    1. Nombre vacío o `null` → `IllegalArgumentException`: _"El nombre del héroe no puede estar vacío o ser null"_.
    2. Alguna de las cinco estadísticas fuera de 1 a 10 → `IllegalArgumentException`: _"Las estadísticas deben estar entre 1 y 10"_.
    3. Experiencia negativa → `IllegalArgumentException`: _"La experiencia no puede ser negativa"_.
    4. Nombre repetido (sin distinguir mayúsculas de minúsculas) → `AlreadyExistException`: _"El héroe con nombre ${name} ya existe"_.
- **Retirar héroe.** Si el id no existe → `NotFoundException`: _"El héroe con id ${id} no existe"_. Si se retira con éxito, siguen aplicando las reglas de la iteración 2 sobre equipos y fanáticos, junto con la regla del veterano de la sección siguiente.

### Equipos

- **Registrar equipo.** Nombre vacío o `null` → `IllegalArgumentException`: _"El nombre del equipo no puede estar vacío o ser null"_. Nombre repetido → `AlreadyExistException`: _"El equipo con nombre ${name} ya existe"_.
- **Disolver equipo.** Si el id no existe → `NotFoundException`: _"El equipo con id ${id} no existe"_.
- **Agregar o retirar héroes de un equipo.**
    - Equipo inexistente → `NotFoundException`: _"El equipo con id ${id} no existe"_.
    - Héroe inexistente → `NotFoundException`: _"El héroe con id ${id} no existe"_.
    - Héroe que ya pertenece a un equipo → `AlreadyExistException`: _"El héroe ${name} ya está en el equipo ${teamName}"_. Cada héroe pertenece como máximo a un equipo, así que esto aplica tanto si intentan agregarlo otra vez al mismo equipo como si intentan agregarlo a uno distinto; `${teamName}` es el equipo al que ya pertenece.
    - Retirar un héroe que no está en el equipo → `NotFoundException`: _"El héroe con id ${id} no existe en el equipo ${teamName}"_.
- **Regla del veterano.** Un equipo con integrantes debe tener siempre al menos un héroe de rango `veteran` o superior (`veteran` o `elite`). Un equipo vacío es válido. La regla se revisa en cada cambio, después de las demás validaciones, y si no se cumple → `IllegalArgumentException`: _"El equipo ${teamName} necesita al menos un héroe de rango veterano o superior"_:
    - Agregar un héroe a un equipo vacío: el primero debe ser veterano o superior.
    - Retirar de un equipo al único veterano mientras quedan otros integrantes.
    - Retirar de la base de datos a un héroe que es el único veterano de un equipo con más integrantes (el héroe no se retira).
    - Retirar al último integrante de un equipo, o disolver el equipo, siempre es posible.

### Misiones

**Registrar misión.** Se validan los datos en este orden:

1. Los campos de texto no pueden estar vacíos ni ser `null` → `IllegalArgumentException`: _"Los campos no pueden estar vacíos o ser null"_.
2. El nivel de amenaza está entre 1 y 10 → `IllegalArgumentException`: _"El nivel de amenaza debe estar entre 1 y 10"_.
3. La duración no es menor a 0 (0 es válido) → `IllegalArgumentException`: _"La duración de la misión no puede ser menor a 0"_.

**Consulta a la Agencia Central.** Con los datos válidos, antes de registrar la misión:

- El sistema consulta con la librería `hero-intel` el nivel de amenaza oficial de la ciudad de la misión y **lo muestra** (un número entre 1 y 10).
- Si el nivel que digitó el administrador **difiere del oficial en 4 o más niveles** (en cualquier sentido, por ejemplo oficial 9 y digitado 5, u oficial 3 y digitado 8), el sistema le advierte que se aleja del informe oficial y le pide confirmar. Confirmar es digitar `s` (en mayúscula o minúscula); cualquier otra respuesta cancela y la misión no se registra. Los textos del nivel oficial, de la advertencia y de la pregunta son libres; solo el mensaje _"No se pudo consultar el informe de la Agencia Central"_ debe ser exacto.
- Si la librería lanza `IntelAccessException` (token no configurado o inválido, ciudad desconocida o Agencia Central no disponible), el programa **no termina**: captura la excepción, muestra _"No se pudo consultar el informe de la Agencia Central"_ y **registra la misión igual**. El informe es una ayuda, no un requisito.
- Cada equipo recibe, junto con su token, la **ciudad asignada a su equipo**. Su equipo **no puede consultar otra ciudad**: para cualquier otra, la Agencia responde que no la reconoce y el programa muestra el mensaje de arriba. Para ver el flujo completo (nivel oficial, advertencia y confirmación), registren la misión en la ciudad asignada a su equipo, escrita exactamente como la recibieron (las tildes cuentan).
- El token nunca se escribe en el código: la librería lo lee de la variable de entorno `HERO_INTEL_TOKEN`.

**Retirar misión.** Si el id no existe → `NotFoundException`: _"La misión con id ${id} no existe"_.

**Asignar o retirar el equipo de una misión.**

- Misión inexistente → `NotFoundException`: _"La misión con id ${id} no existe"_.
- Equipo inexistente → `NotFoundException`: _"El equipo con id ${id} no existe"_.
- Equipo ya asignado a una misión → `AlreadyExistException`: _"El equipo ${teamName} ya está asignado a la misión ${codeName}"_ (`${codeName}` es la misión en la que ya está asignado).
- Retirar el equipo de una misión que no tiene equipo → `NotFoundException`: _"La misión ${codeName} no tiene equipo asignado"_.

## Entrega y calificación

Cada parte se califica sobre 5.0 y se **verifica en persona, en clase**, con el equipo ejecutando su programa. Un integrante ausente en la verificación obtiene 0.0 en esa parte, salvo excusa válida, y la parte 1 no se recibe después del taller.

**Parte 1 - Taller en clase, miércoles de la semana 12 (0.5 puntos de la nota del proyecto):**

| Criterio | Puntos |
|----------|--------|
| El paquete `exception` existe y la lógica lanza mientras la vista captura | 1.0 |
| Inicio y cierre de sesión del fanático, con su mensaje de error | 1.5 |
| Registro de fanáticos con sus validaciones y mensajes exactos | 2.0 |
| Un dato inválido (letras en la edad o en una opción del menú, campos vacíos) no termina el programa en el registro ni en el inicio de sesión | 0.5 |

**Parte 2 - En casa, miércoles de la semana 13 (0.5 puntos de la nota del proyecto):**

| Criterio | Puntos |
|----------|--------|
| Seguir y dejar de seguir héroes y ver los que sigue, con excepciones y mensajes exactos | 1.0 |
| Eliminación de fanáticos y excepciones del resto del módulo del administrador (héroes, equipos, misiones) con mensajes exactos | 1.0 |
| `getLevel()`, `getRank()` y la regla del veterano en los equipos | 1.0 |
| Consulta a la Agencia Central en una sola capa: muestra el nivel oficial, advierte y confirma, y sigue funcionando si la Agencia no responde; el token sale de `HERO_INTEL_TOKEN` | 1.25 |
| Entradas inválidas (letras en campos numéricos, ids mal escritos) no terminan el programa en ningún módulo | 0.75 |

## Qué debe incluir la propuesta del lunes

Una propuesta corta, de alto nivel, con:

- La lista de excepciones propias, en qué paquete viven y por qué son verificadas.
- En qué capa se valida cada regla y en cuál se captura el error.
- Dónde se guarda quién tiene la sesión iniciada.
- Cómo evitan que una entrada inválida termine el programa.
- Dónde viven el nivel, el rango y la regla del veterano, y qué cambios de equipo la disparan.
- En qué capa usan la librería `hero-intel` y en qué momento la crean (si la crean al arrancar el programa y no hay token, el programa no arranca).
- El diagrama de clases actualizado.
