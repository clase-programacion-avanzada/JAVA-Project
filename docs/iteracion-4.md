# Iteración 4 — Archivos de texto, `Map`/`Set` y archivos binarios

En esta iteración HeroHub debe poder **iniciar una nueva partida desde archivos de texto**, **Guardar/Cargaruna partida existente**

La iteración se suma a lo que ya funciona de las iteraciones anteriores.

## En clase: proponer y aprobar el formato (0.4 del total del proyecto)

En la clase del **miércoles 28 de octubre** cada equipo debe explicar cuál será el formato de sus **archivos de texto** para cargar:

- héroes,
- fanáticos,
- equipos.

**No se cargan misiones desde archivos, ya que las misiones serán lo que finalmente se envíe al servidor central.**

El formato debe permitir representar las clases del modelo y sus relaciones:

- qué héroes sigue cada fanático,
- qué héroes forman cada equipo.

Deben mostrar **archivos de ejemplo con datos**, no solo la descripción del formato. Incluyan al menos un ejemplo por clase.

El profesor revisará la propuesta y dirá si la aprueba. **Si el formato no se aprueba, la parte de clase recibirá una nota de 0.** No empiecen a implementar la carga hasta tener la aprobación del formato.

## En casa 

### Cargar desde archivos de texto (0.6 del total del proyecto)

Con el formato aprobado, implementen la carga de **héroes, fanáticos y equipos**. **No se incluyen misiones.**

Los archivos de texto son el **punto de entrada de una partida**. No se exige exportar a texto el estado dinámico del programa.



### Reportes

Los siguientes reportes se guardan en un archivo de texto y se muestra en pantalla:

`reporte_popularidad.txt` — popularidad de cada héroe, solo para héroes con popularidad mayor que 0:

- +1 si el héroe pertenece a un equipo,
- +2 por cada fanático que siga al héroe.

Formato:

```text
Reporte de popularidad

<Nombre del héroe 1>: <Popularidad>
<Nombre del héroe 2>: <Popularidad>
...
```

`reporte_fanaticos.txt` — por cada fanático: cantidad de héroes seguidos y su héroe favorito (el héroe seguido con mayor popularidad).


### Guardar y Cargar desde un archivo binario

Su programa debe poder guardar el estado de TODO el programa en uno o más archivos serializados y poder cargar los mismos para continuar con una partida. 

#### Supuestos sobre los archivos

**Se asume que los archivos siempre están correctos.** No deben validar campos, referencias ni estadísticas al cargar, ni manejar archivos mal formados.

Los equipos del archivo también se asumen válidos: cada héroe pertenece a un solo equipo y cada equipo tiene al menos un héroe de rango `veteran` o superior. No se exige validar la regla del veterano al cargar.

Aunque los datos sean correctos, capturen las excepciones de entrada/salida: el programa no debe terminar abruptamente si un archivo no se puede leer o escribir.

## Entrega y calificación

Cada parte se califica sobre **5.0** y tiene un peso distinto en la nota de la iteración:

**0.4 × nota de clase + 0.6 × nota de casa**

La iteración 4 vale **1.0** de la nota del proyecto. **Si el formato no se aprueba, la parte de clase vale 0.**

### En clase — 40%

| Criterio | Puntos |
|----------|--------|
| Formato completo: héroes, fanáticos, equipos y sus relaciones; sin misiones | 2.0 |
| Archivos de ejemplo con datos para las tres clases | 2.0 |
| Explicación del formato | 1.0 |
| **Total** | **5.0** |

### En casa — 60%

| Criterio | Puntos |
|----------|--------|
| Carga desde archivos de texto (héroes, fanáticos y equipos; sin misiones) | 2.5 |
| Guardar y reanudar el estado con serialización binaria | 1.5 |
| Reportes de popularidad y de fanáticos | 1.0 |
| **Total** | **5.0** |

La entrega es en la **semana 14**.





