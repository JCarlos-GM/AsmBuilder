; --- CODIGO BASE + CURSOR DIAGONAL

.MODEL SMALL
.STACK 100h
.DATA
.CODE

MAIN PROC
    etiqueta1:
        ; --- Posicion inicial ---
        MOV AH, 02H
        MOV BH, 00H

        MOV DH, 0       
        MOV DL, 0       
        INT 10H

        MOV CX, 20      

    etiqueta2:
        ; --- Mover cursor en diagonal  ---
        INC DH          
        INC DL          

        PUSH CX         
        PUSH DX         

        ; --- Mover cursor ---
        MOV AH, 02H
        MOV BH, 00H
        INT 10H

        ; --- Esperar 1 segundo ---
        MOV AH, 86H
        MOV CX, 000FH   
        MOV DX, 4240H   
        INT 15H

        POP DX          ; Recuperamos la posicion
        POP CX          ; Recuperamos el contador

        ; --- Repetir ---
        DEC CX
        JNZ etiqueta2

        ; --- FIN ---
        MOV AH, 4CH
        INT 21H
        

MAIN ENDP
END MAIN