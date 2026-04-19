# AsmBuilder

Generador de plantillas `.asm` para TASM/DOS desde la terminal.

## Instalacion

1. Compilar el proyecto:
   ```
   mvn package
   ```

2. Colocar `tasm.exe` y `tlink.exe` en la carpeta `bin/` del proyecto:
   ```
   AsmBuilder\bin\tasm.exe
   AsmBuilder\bin\tlink.exe
   ```

3. Instalar DOSBox desde https://www.dosbox.com/download.php

4. Agregar al PATH de Windows las siguientes rutas (ajusta segun donde instalaste cada cosa):

   | Programa | Ruta ejemplo |
   |----------|--------------|
   | AsmBuilder | `C:\Users\TuUsuario\Documents\proyects\AsmBuilder` |
   | DOSBox | `C:\Program Files (x86)\DOSBox-0.74-3` |

   **Como agregar al PATH:**
   - Abre **Inicio** → busca **"Variables de entorno"** → selecciona **"Editar las variables de entorno del sistema"**
   - Clic en **Variables de entorno...**
   - En **Variables del usuario**, selecciona **Path** → **Editar** → **Nuevo**
   - Pega cada ruta y acepta
   - Abre una **nueva terminal** para que los cambios tomen efecto

5. Verificar que todo funciona:
   ```
   asmb help
   dosbox --version
   ```

## Comandos

| Comando | Descripcion |
|---------|-------------|
| `asmb new` | Crea un nuevo archivo `.asm` a partir de una plantilla |
| `asmb run` | Compila y ejecuta un `.asm` con TASM/TLINK/DOSBox |
| `asmb help` | Muestra la ayuda |

---

## asmb new

```
asmb new <archivo> --template [flags de plantilla] [flags de salida]
```

El archivo se crea en la carpeta donde ejecutas el comando. La extension `.asm` se agrega automaticamente si no la escribes.

### Flags de salida

| Flag | Descripcion |
|------|-------------|
| `--path <ruta>` | Guarda el archivo en la ruta indicada en lugar del directorio actual. Crea la carpeta si no existe. |
| `--folder` | Crea una subcarpeta con el nombre del programa, guarda el `.asm` dentro y copia `tasm.exe` y `tlink.exe` automaticamente. |

---

## asmb run

```
asmb run <archivo.asm>
```

Ensambla el archivo con TASM, lo enlaza con TLINK y abre DOSBox automaticamente para ejecutarlo.

Requisitos:
- `tasm.exe` y `tlink.exe` en la misma carpeta del `.asm`, o en `bin/` del proyecto.
- DOSBox instalado en el sistema.

---

## Genericos

Estructura y logica basica del programa.

| Flag | Descripcion |
|------|-------------|
| `--empty` | Estructura base sin codigo. Solo `.data`, `.code` y salida del programa. |
| `--print` | Declara `msg` en `.data` e imprime con `INT 21h` servicio `09h`. |
| `--for` | Ciclo FOR con la instruccion `loop`. Usa `cx` como contador (10 repeticiones). |
| `--while` | Ciclo WHILE: verifica `cx` contra `00h` antes de ejecutar. Si la condicion no se cumple, salta a `fin_mientras`. |
| `--dowhile` | Ciclo DO-WHILE con `dec cx` + `jnz`. El cuerpo corre al menos una vez antes de verificar. |
| `--if` | Condicional `if/else` con `cmp` y saltos `jz`/`jmp`. Incluye variable `opc` para la condicion. |
| `--switch` | Switch con multiples `cmp` encadenados. Tres opciones, cada una con su mensaje. |
| `--vars` | Declara `numero`, `mensaje` y `bandera` en `.data` con ejemplo de carga y modificacion. |
| `--array` | Declara `arr` con 5 elementos en `.data` y los recorre con `SI` como puntero, elemento en `AL`. |

---

## Especificos

Codigo de video, entrada, pila y cadenas.

| Flag | Descripcion |
|------|-------------|
| `--cursor` | Posiciona el cursor en una fila y columna con `INT 10h` servicio `02h`. |
| `--screen` | Limpia la pantalla completa con `INT 10h` servicio `06h`. |
| `--color` | Imprime un caracter con color usando `INT 10h` servicio `09h`. |
| `--delay` | Espera 1 segundo con `INT 15h` servicio `86h` (tiempo en microsegundos). |
| `--stack` | Mete dos valores a la pila con `push` y los saca con `pop`. |
| `--string` | Recorre una cadena con el registro `SI` e imprime cada caracter en pantalla con posicion. |
| `--upper` | Recorre una cadena y convierte cada letra minuscula a mayuscula restando `20h` del ASCII. |
| `--input` | Imprime un mensaje y espera a que el usuario presione una tecla con `INT 21h` servicio `0Bh`. |
| `--rect` | Dibuja un rectangulo con color de fondo usando `INT 10h` servicio `06h`. |
| `--diagonal` | Limpia la pantalla a negro y mueve el cursor en diagonal imprimiendo `█` en amarillo con delay en cada paso. |
| `--rectswap` | Dos rectangulos (rojo y blanco) que intercambian colores en un ciclo animado de 15 repeticiones. |
| `--abc` | Imprime el abecedario en mayusculas letra por letra, una por linea, con delay de 1 segundo entre cada una. |

---

## Combinaciones

| Flags | Resultado |
|-------|-----------|
| `--for --print` | Ciclo FOR que imprime `msg` en cada iteracion. |
| `--dowhile --print` | Ciclo DO-WHILE que imprime `msg` en cada iteracion. |
| `--cursor --delay` | Mueve el cursor al centro, espera 1 segundo y regresa al origen. |
| `--screen --cursor` | Limpia la pantalla y posiciona el cursor en fila 0, columna 0. |

---

## Ejemplos

```
asmb new hola --template --empty
```
Estructura base sin codigo, lista para completar.

```
asmb new hola --template --print --folder
```
Crea la carpeta `hola/`, genera `hola.asm` dentro y copia `tasm.exe` y `tlink.exe` junto al archivo.

```
asmb new hola --template --print --path C:\programas
```
Genera `hola.asm` en `C:\programas\` (la crea si no existe).

```
asmb new hola --template --for --print --folder
```
Genera plantilla for+print en su propia carpeta lista para compilar.

```
asmb run hola.asm
```
Compila `hola.asm` con TASM/TLINK y abre DOSBox para ejecutarlo.

```
asmb new hola --template --print
```
Imprime `Hola Mundo` una vez y termina.

```
asmb new hola --template --for
```
Ciclo de 10 repeticiones con el cuerpo vacio.

```
asmb new hola --template --for --print
```
Ciclo de 10 iteraciones que imprime `Hola Mundo` en cada una.

```
asmb new hola --template --dowhile --print
```
Ciclo DO-WHILE que imprime `Hola Mundo` en cada iteracion.

```
asmb new hola --template --if
```
Condicional que compara `opc` contra `01`. Salta a `verdadero` o `falso` segun el resultado.

```
asmb new hola --template --switch
```
Switch de tres opciones con `cmp` encadenados. Imprime un mensaje distinto segun `opc`.

```
asmb new hola --template --vars
```
Variables `numero`, `mensaje` y `bandera` en `.data` con ejemplo de acceso.

```
asmb new hola --template --while
```
Ciclo que verifica `cx` al inicio. Si `cx` es `00h` salta a `fin_mientras`, si no ejecuta el cuerpo, decrementa y repite.

```
asmb new hola --template --array
```
Declara `arr db 01h, 02h, 03h, 04h, 05h` en `.data` y lo recorre con `SI`. Cada iteracion deja el elemento actual en `AL`.

```
asmb new hola --template --cursor
```
Posiciona el cursor en fila 12, columna 40 (centro de pantalla).

```
asmb new hola --template --screen
```
Limpia la pantalla completa con fondo gris.

```
asmb new hola --template --color
```
Imprime el caracter `A` en amarillo con `INT 10h`.

```
asmb new hola --template --delay
```
Espera exactamente 1 segundo antes de continuar.

```
asmb new hola --template --stack
```
Mete `1234h` y `00ffh` a la pila y los saca con `pop`.

```
asmb new hola --template --string
```
Recorre la cadena `hola` con `SI` e imprime cada caracter en su posicion en pantalla.

```
asmb new hola --template --upper
```
Recorre `hola mundo` y la imprime en mayusculas (`HOLA MUNDO`).

```
asmb new hola --template --input
```
Imprime `Presiona una tecla` y espera hasta que el usuario presione algo.

```
asmb new hola --template --rect
```
Dibuja un rectangulo rojo en pantalla entre las coordenadas (5,5) y (15,25).

```
asmb new hola --template --cursor --delay
```
Mueve el cursor al centro, espera 1 segundo y lo regresa a la posicion 0,0.

```
asmb new hola --template --screen --cursor
```
Limpia la pantalla y deja el cursor en la esquina superior izquierda.

```
asmb new hola --template --diagonal
```
Pantalla negra con cursor moviendose en diagonal dejando rastro de bloques amarillos.

```
asmb new hola --template --rectswap
```
Animacion de dos rectangulos que intercambian colores entre rojo y blanco en bucle.

```
asmb new hola --template --abc
```
Imprime A, B, C... Z una letra por linea con 1 segundo de pausa entre cada una.
