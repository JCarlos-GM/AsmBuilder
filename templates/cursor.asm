.MODEL SMALL
.STACK 100H
.DATA

.CODE
MAIN PROC
    MOV AX, @DATA
    MOV DS, AX

    ; Borrar toda la pantalla
    MOV AH, 06H
    MOV AL, 00H
    MOV BH, 07H
    MOV CX, 0000H
    MOV DX, 184FH
    INT 10H

    ; Colocar cursor en fila 0, columna 0
    MOV AH, 02H
    MOV BH, 00H
    MOV DH, 00H        
    MOV DL, 00H       
    INT 10H

    MOV AH, 4CH
    INT 21H

MAIN ENDP
END MAIN