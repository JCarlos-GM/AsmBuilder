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

---

## Comandos

| Comando | Descripcion |
|---------|-------------|
| `asmb new` | Crea un nuevo archivo `.asm` a partir de una plantilla |
| `asmb run` | Compila y ejecuta un `.asm` con TASM/TLINK/DOSBox |
| `asmb help` | Muestra la ayuda |

---

## asmb new

```
asmb new <archivo> --template [flags de plantilla] [parametros] [flags de salida]
```

El archivo se crea en la carpeta donde ejecutas el comando. La extension `.asm` se agrega automaticamente si no la escribes.

### Flags de salida

| Flag | Descripcion |
|------|-------------|
| `--path <ruta>` | Guarda el archivo en la ruta indicada. Crea la carpeta si no existe. |
| `--folder` | Crea una subcarpeta con el nombre del programa y copia `tasm.exe` y `tlink.exe` automaticamente. |

### Parametros de personalizacion

| Parametro | Aplica a | Descripcion |
|-----------|----------|-------------|
| `--msg "texto"` | `--print`, `--input` | Texto del mensaje en `.data` |
| `--count N` | `--for`, `--while`, `--dowhile` | Numero de iteraciones del ciclo |
| `--row N` | `--cursor` | Fila del cursor (0-24) |
| `--col N` | `--cursor` | Columna del cursor (0-79) |
| `--str "texto"` | `--string`, `--upper` | Contenido de la cadena |
| `--sec N` | `--delay` | Segundos de espera |
| `--steps N` | `--diagonal` | Pasos del recorrido diagonal |
| `--cycles N` | `--rectswap` | Repeticiones de la animacion |

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

| Flag | Parametros | Descripcion |
|------|------------|-------------|
| `--empty` | | Estructura base sin codigo. |
| `--print` | `--msg` | Imprime un mensaje con `INT 21h` servicio `09h`. |
| `--for` | `--count` | Ciclo FOR con `loop`. Usa `cx` como contador (default 10). |
| `--while` | `--count` | Ciclo WHILE: verifica `cx` antes de ejecutar (default 10). |
| `--dowhile` | `--count` | Ciclo DO-WHILE con `dec cx` + `jnz` (default 10). |
| `--if` | | Condicional con `cmp`/`jz`/`jmp`. Incluye variable `opc`. |
| `--switch` | | Switch con tres `cmp` encadenados y mensajes por opcion. |
| `--vars` | | Variables `numero`, `mensaje` y `bandera` con ejemplo de acceso. |
| `--array` | | Arreglo de 5 elementos recorrido con `SI` como puntero. |

---

## Especificos

Codigo de video, entrada, pila y cadenas.

| Flag | Parametros | Descripcion |
|------|------------|-------------|
| `--cursor` | `--row`, `--col` | Posiciona el cursor con `INT 10h` servicio `02h` (default fila 12, col 40). |
| `--screen` | | Limpia la pantalla con `INT 10h` servicio `06h`. |
| `--color` | | Imprime un caracter con color con `INT 10h` servicio `09h`. |
| `--delay` | `--sec` | Espera N segundos con `INT 15h` servicio `86h` (default 1). |
| `--stack` | | Mete dos valores a la pila con `push` y los saca con `pop`. |
| `--string` | `--str` | Recorre una cadena con `SI` e imprime cada caracter con posicion. |
| `--upper` | `--str` | Recorre una cadena y convierte minusculas a mayusculas. |
| `--input` | `--msg` | Muestra mensaje y espera una tecla con `INT 21h` servicio `0Bh`. |
| `--rect` | | Dibuja un rectangulo con color de fondo con `INT 10h` servicio `06h`. |
| `--diagonal` | `--steps` | Pantalla negra con cursor diagonal dejando rastro amarillo (default 20 pasos). |
| `--rectswap` | `--cycles` | Dos rectangulos que intercambian colores en animacion (default 15 ciclos). |
| `--abc` | | Imprime el abecedario letra por letra, una por linea, con delay de 1 segundo. |

---

## Combinaciones

| Flags | Parametros | Resultado |
|-------|------------|-----------|
| `--for --print` | `--count`, `--msg` | Ciclo FOR que imprime un mensaje en cada iteracion. |
| `--dowhile --print` | `--count`, `--msg` | Ciclo DO-WHILE que imprime un mensaje en cada iteracion. |
| `--cursor --delay` | `--row`, `--col`, `--sec` | Mueve el cursor, espera y regresa al origen. |
| `--screen --cursor` | | Limpia la pantalla y posiciona el cursor en fila 0, columna 0. |

---

## Todos los comandos

### Genericos
```
asmb new prueba --template --empty
asmb new prueba --template --print
asmb new prueba --template --print --msg "Tu mensaje"
asmb new prueba --template --for
asmb new prueba --template --for --count 5
asmb new prueba --template --while
asmb new prueba --template --while --count 5
asmb new prueba --template --dowhile
asmb new prueba --template --dowhile --count 5
asmb new prueba --template --if
asmb new prueba --template --switch
asmb new prueba --template --vars
asmb new prueba --template --array
```

### Especificos
```
asmb new prueba --template --cursor
asmb new prueba --template --cursor --row 5 --col 20
asmb new prueba --template --screen
asmb new prueba --template --color
asmb new prueba --template --delay
asmb new prueba --template --delay --sec 3
asmb new prueba --template --stack
asmb new prueba --template --string
asmb new prueba --template --string --str "mundo"
asmb new prueba --template --upper
asmb new prueba --template --upper --str "hola mundo"
asmb new prueba --template --input
asmb new prueba --template --input --msg "Escribe algo"
asmb new prueba --template --rect
asmb new prueba --template --diagonal
asmb new prueba --template --diagonal --steps 10
asmb new prueba --template --rectswap
asmb new prueba --template --rectswap --cycles 5
asmb new prueba --template --abc
```

### Combinaciones
```
asmb new prueba --template --for --print
asmb new prueba --template --for --print --count 5
asmb new prueba --template --for --print --msg "Iteracion" --count 3
asmb new prueba --template --dowhile --print
asmb new prueba --template --dowhile --print --count 5
asmb new prueba --template --dowhile --print --msg "Vuelta" --count 3
asmb new prueba --template --cursor --delay
asmb new prueba --template --cursor --delay --row 5 --col 20 --sec 2
asmb new prueba --template --screen --cursor
```

### Flags de salida
```
asmb new prueba --template --print --folder
asmb new prueba --template --print --path C:\programas
asmb new prueba --template --for --print --folder
```

### Compilar y ejecutar
```
asmb run prueba.asm
```
