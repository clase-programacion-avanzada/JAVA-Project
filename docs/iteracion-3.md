# Iteración 3 - Strings y excepciones: el fanático y la Agencia Central

Este documento es el enunciado detallado de la iteración 3. La [hoja de ruta del README](../README.md#hoja-de-ruta-del-proyecto) explica el resultado esperado en general; aquí están las reglas exactas, incluidos los mensajes que debe mostrar el programa. **Cómo** diseñarlo (qué clases, qué capas, qué validaciones en cuál capa) lo deciden ustedes en su propuesta.

## Índice

1. [Qué cambia respecto a la iteración 2](#qué-cambia-respecto-a-la-iteración-2)
2. [Reglas generales](#reglas-generales)
3. [Parte 1 - Taller en clase (semana 12): módulo del fanático](#parte-1---taller-en-clase-semana-12-módulo-del-fanático)
4. [Parte 2 - En casa (semana 13): administrador y Agencia Central](#parte-2---en-casa-semana-13-administrador-y-agencia-central)
5. [Entrega y calificación](#entrega-y-calificación)
6. [Qué debe incluir la propuesta del lunes](#qué-debe-incluir-la-propuesta-del-lunes)

## Qué cambia respecto a la iteración 2

- El programa tiene **dos módulos**: el del administrador (ya existe) y el del **fanático**, con inicio de sesión.
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
5. **Números.** Si el usuario digita letras donde se espera un número (una opción del menú, la edad, el nivel de amenaza, la duración), el programa no termina: muestra un mensaje y vuelve a pedir el dato.
6. El menú principal queda así: `1. Módulo administrador`, `2. Módulo fanático`, `0. Salir`.

## Parte 1 - Taller en clase (semana 12): módulo del fanático

El fanático es un **espectador** de la agencia: no crea equipos ni gestiona nada. Sigue a sus héroes favoritos (no a equipos) y vive las misiones desde la grada.

**Antes del taller** registren, con el módulo del administrador, al menos un fanático, varios héroes y un equipo con héroes, para tener con qué probar.

Operaciones del módulo del fanático:

1. **Iniciar sesión.** El fanático se autentica con su nombre de usuario y su contraseña; ambos deben coincidir exactamente (las mayúsculas cuentan). Si no coinciden se muestra _"Usuario o contraseña incorrectos"_, no se muestra el menú del fanático y se vuelve al menú principal.
2. **Seguir a un héroe.**
    - Si el id no existe → `NotFoundException`: _"El héroe con id ${id} no existe"_.
    - Si ya lo sigue → `AlreadyExistException`: _"El fanático ya sigue al héroe ${name}"_.
3. **Dejar de seguir a un héroe.** El sistema muestra primero los héroes que sigue. Si el id no está en su lista → `NotFoundException`: _"El héroe con id ${id} no existe en la lista de héroes seguidos del fanático"_.
4. **Ver los integrantes de un equipo** (cualquier equipo). Si el equipo no existe → `NotFoundException`: _"El equipo con id ${id} no existe"_.
5. **Ver las misiones registradas**, con nombre clave, ciudad, nivel de amenaza estimado, duración estimada y equipo asignado (si lo tiene).
6. **Ver los héroes que sigue.**
7. **Cerrar sesión.**

En esta iteración las misiones todavía no se despachan: el fanático solo consulta el catálogo. El despacho llega en la iteración 5.

## Parte 2 - En casa (semana 13): administrador y Agencia Central

### Registrar y eliminar fanáticos

**Registrar fanático.** Se validan los datos en este orden; la primera regla que falla produce el error:

1. Ningún campo de texto puede ser `null`, vacío o solo espacios → `IllegalArgumentException`: _"Los campos no pueden estar vacíos o ser null"_.
2. El nombre de usuario tiene entre 8 y 10 caracteres y solo contiene letras (mayúsculas o minúsculas), números o los caracteres `_` y `-` → `IllegalArgumentException`: _"El nombre de usuario debe tener entre 8 y 10 caracteres y debe contener solo letras (mayúsculas o minúsculas), números o los caracteres '_' y '-'"_.
3. El nombre de usuario no existe todavía → `AlreadyExistException`: _"El fanático con nombre de usuario ${username} ya existe"_.
4. La contraseña tiene mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial de este conjunto: `#?!@$%^&*-` → `IllegalArgumentException`: _"La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (#?!@$%^&*-)"_.
5. La edad es **mayor** a 14 años (14 no se acepta) → `IllegalArgumentException`: _"La edad debe ser mayor a 14 años"_.

**Eliminar fanático.** Si el id no existe → `NotFoundException`: _"El fanático con id ${id} no existe"_.

### Héroes

- **Registrar héroe.** Nombre vacío o `null` → `IllegalArgumentException`: _"El nombre del héroe no puede estar vacío o ser null"_. Nombre repetido (sin distinguir mayúsculas de minúsculas) → `AlreadyExistException`: _"El héroe con nombre ${name} ya existe"_.
- **Retirar héroe.** Si el id no existe → `NotFoundException`: _"El héroe con id ${id} no existe"_. Si se retira con éxito, siguen aplicando las reglas de la iteración 2 sobre equipos y fanáticos.

### Equipos

- **Registrar equipo.** Nombre vacío o `null` → `IllegalArgumentException`: _"El nombre del equipo no puede estar vacío o ser null"_. Nombre repetido → `AlreadyExistException`: _"El equipo con nombre ${name} ya existe"_.
- **Disolver equipo.** Si el id no existe → `NotFoundException`: _"El equipo con id ${id} no existe"_.
- **Agregar o retirar héroes de un equipo.**
    - Equipo inexistente → `NotFoundException`: _"El equipo con id ${id} no existe"_.
    - Héroe inexistente → `NotFoundException`: _"El héroe con id ${id} no existe"_.
    - Héroe que ya pertenece a un equipo → `AlreadyExistException`: _"El héroe ${name} ya está en el equipo ${teamName}"_. Cada héroe pertenece como máximo a un equipo, así que esto aplica tanto si intentan agregarlo otra vez al mismo equipo como si intentan agregarlo a uno distinto; `${teamName}` es el equipo al que ya pertenece.
    - Retirar un héroe que no está en el equipo → `NotFoundException`: _"El héroe con id ${id} no existe en el equipo ${teamName}"_.

### Misiones

**Registrar misión.** Se validan los datos en este orden:

1. Los campos de texto no pueden estar vacíos ni ser `null` → `IllegalArgumentException`: _"Los campos no pueden estar vacíos o ser null"_.
2. El nivel de amenaza está entre 1 y 10 → `IllegalArgumentException`: _"El nivel de amenaza debe estar entre 1 y 10"_.
3. La duración no es menor a 0 (0 es válido) → `IllegalArgumentException`: _"La duración de la misión no puede ser menor a 0"_.

**Consulta a la Agencia Central.** Con los datos válidos, antes de registrar la misión:

- El sistema consulta con la librería `hero-intel` el nivel de amenaza oficial de la ciudad de la misión y **lo muestra** (un número entre 1 y 10).
- Si el nivel que digitó el administrador **difiere del oficial en 4 o más niveles** (en cualquier sentido, por ejemplo oficial 9 y digitado 5, u oficial 3 y digitado 8), el sistema le advierte que se aleja del informe oficial y le pide confirmar. Si no confirma, la misión no se registra.
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
| Seguir y dejar de seguir héroes con sus excepciones y mensajes exactos; ver integrantes de un equipo, misiones y héroes que sigue | 1.5 |
| Un id vacío o con formato inválido en el módulo del fanático no termina el programa | 1.0 |

**Parte 2 - En casa, miércoles de la semana 13 (0.5 puntos de la nota del proyecto):**

| Criterio | Puntos |
|----------|--------|
| Registro y eliminación de fanáticos con sus validaciones y mensajes exactos | 1.25 |
| Excepciones del resto del módulo del administrador con mensajes exactos | 1.25 |
| Consulta a la Agencia Central en una sola capa: muestra el nivel oficial, advierte y confirma, y sigue funcionando si la Agencia no responde; el token sale de `HERO_INTEL_TOKEN` | 1.5 |
| Entradas inválidas (letras en campos numéricos, ids mal escritos) no terminan el programa en ningún módulo | 1.0 |

## Qué debe incluir la propuesta del lunes

Una propuesta corta, de alto nivel, con:

- La lista de excepciones propias, en qué paquete viven y por qué son verificadas.
- En qué capa se valida cada regla y en cuál se captura el error.
- Dónde se guarda quién tiene la sesión iniciada.
- Cómo evitan que una entrada inválida termine el programa.
- En qué capa usan la librería `hero-intel` y en qué momento la crean (si la crean al arrancar el programa y no hay token, el programa no arranca).
- El diagrama de clases actualizado.
