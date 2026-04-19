; TAREA 1 - UNIDAD 2
; Ing. Juan Carlos Govea Magaña

; Código que dibuja dos cuadrados en pantalla e intercambia
; su color en cada ciclo.
; Utiliza la interrupción de video INT 10h para dibujar,
; INT 15h para los delays, e INT 21h para terminar.

.MODEL SMALL

.STACK 100h

.DATA

.CODE

main PROC
    ; Limpiamos la pantala
    MOV AH, 06h
    MOV AL, 00h
    MOV BH, 07h     ; fondo negro  
    MOV CH, 0       ; fila superior
    MOV CL, 0       ; columna izquierda
    MOV DH, 24      ; fila inferior
    MOV DL, 79      ; columna derecha
    INT 10h

    ; Guardamos la posición actual del cursor
    MOV AH, 03h
    MOV BH, 0
    INT 10h
    PUSH DX     
    
    ; Cambiamos la apariencia del cursor
    MOV AH, 01h
    MOV CH, 00h
    MOV CL, 07h
    INT 10h

    ; Movemos el cursor al centro de la pantalla
    MOV AH, 02h
    MOV BH, 0
    MOV DH, 12      ; fila centro
    MOV DL, 39      ; columna centro
    INT 10h

    ; Pausa de 3 segundos 
    MOV AH, 86h
    MOV CX, 002Dh  
    MOV DX, 0C6C0h  
    INT 15h

    ; Restauramos la posición original del cursor
    POP DX
    MOV AH, 02h
    MOV BH, 0
    INT 10h

    ; Iniciamos la animación de intercambio de colores
    MOV CX, 000Fh
    swap:
        PUSH CX

        ; Primer cuadrado rojo
        MOV AH, 06h
        MOV AL, 00h
        MOV BH, 40h     ; fondo rojo
        MOV CL, 5
        MOV CH, 5
        MOV DL, 25
        MOV DH, 15
        INT 10h

        ; Segundo cuadrado blanco
        MOV AH, 06h
        MOV AL, 00h
        MOV BH, 0F0h    ; fondo blanco
        MOV CL, 35
        MOV CH, 5
        MOV DL, 55
        MOV DH, 15
        INT 10h

        ; Delay 1 entre cada cambio de color 
        MOV AH, 86h
        MOV CX, 0007h
        MOV DX, 0A120h
        INT 15h

        ; Intercambiamos los colores de los cuadrados entre si
        MOV AH, 06h
        MOV AL, 00h
        MOV BH, 0F0h    ; fondo blanco
        MOV CL, 5
        MOV CH, 5
        MOV DL, 25
        MOV DH, 15
        INT 10h

        MOV AH, 06h
        MOV AL, 00h
        MOV BH, 40h     ; fondo rojo
        MOV CL, 35
        MOV CH, 5
        MOV DL, 55
        MOV DH, 15
        INT 10h

        ; Delay 2 antes del siguiente ciclo
        MOV AH, 86h
        MOV CX, 0007h
        MOV DX, 0A120h
        INT 15h

        POP CX          ; recuperar contador del ciclo principal
        LOOP swap
    
    ;Finalizamos el programa
    MOV AX, 4C00h
    INT 21h
main ENDP
END main