# AsmBuilder

Generador de plantillas `.asm` para TASM/DOS desde la terminal.

## Instalacion

1. Compilar el proyecto:
   ```
   mvn package
   ```
2. Agregar la carpeta del proyecto al PATH de Windows para usar `asmb` desde cualquier lugar.

## Uso

```
asmb new <archivo> --template [opciones]
```

El archivo se crea en la carpeta donde ejecutas el comando. La extension `.asm` se agrega automaticamente si no la escribes.

---

## Comandos

### `asmb new`

Crea un nuevo archivo `.asm` en el directorio actual.

```
asmb new <nombre> --template [opciones]
```

---

## Opciones de plantilla

| Flag | Descripcion |
|------|-------------|
| `--empty` | Plantilla base sin codigo. Solo la estructura minima con `.DATA` vacio y salida del programa. |
| `--print` | Agrega una cadena `MSG` en `.DATA` y el codigo para imprimirla con `INT 21H`. |
| `--for` | Ciclo FOR usando la instruccion `LOOP`. Usa `CX` como contador (10 repeticiones por defecto). |
| `--dowhile` | Ciclo DO-WHILE con `DEC CX` + `JNZ`. El cuerpo se ejecuta al menos una vez antes de verificar. |
| `--if` | Condicional IF con `CMP` y saltos `JZ`/`JMP`. Incluye variable `OPC` para la condicion. |
| `--switch` | Switch con multiples `CMP` encadenados. Tres opciones con sus mensajes, patron de salto a etiqueta de impresion. |
| `--vars` | Variables en `.DATA` con ejemplos de como cargarlas y modificarlas desde el codigo. |

---

## Combinaciones soportadas

| Comando | Resultado |
|---------|-----------|
| `--for --print` | Ciclo FOR que imprime el mensaje en cada iteracion. |
| `--dowhile --print` | Ciclo DO-WHILE que imprime el mensaje en cada iteracion. |

---

## Ejemplos

```
asmb new hola --template --empty
```
Estructura base, sin ningun codigo adicional.

```
asmb new hola --template --print
```
Imprime `Hola Mundo` una vez y termina.

```
asmb new hola --template --for
```
Ciclo que se repite 10 veces con el cuerpo vacio listo para completar.

```
asmb new hola --template --for --print
```
Ciclo de 10 iteraciones que imprime `Hola Mundo` en cada una.

```
asmb new hola --template --dowhile
```
Ciclo DO-WHILE de 10 iteraciones con cuerpo vacio.

```
asmb new hola --template --dowhile --print
```
Ciclo DO-WHILE que imprime `Hola Mundo` en cada iteracion.

```
asmb new hola --template --if
```
Condicional que compara `OPC` contra `01`. Si se cumple salta a `verdadero`, si no a `falso`.

```
asmb new hola --template --switch
```
Switch de tres opciones usando `CMP` encadenados. Imprime un mensaje distinto segun el valor de `OPC`.

```
asmb new hola --template --vars
```
Declara variables `NUMERO`, `MENSAJE` y `BANDERA` en `.DATA` con ejemplos de acceso desde el codigo.
