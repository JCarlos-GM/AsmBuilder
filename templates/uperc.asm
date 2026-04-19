.MODEL SMALL
.STACK 100H

.DATA 
    cad db 'nico y la china$'

.CODE
    mov ax, @data
    mov ds, ax
    
    mov si, offset cad   ; apuntador a la cadena

siguiente:
    mov al, [si]         ; obtener carácter
    cmp al, '$'          ; fin de cadena
    je fin
    
    ; convertir a mayúscula si es minúscula
    cmp al, 'a'
    jb imprimir
    cmp al, 'z'
    ja imprimir
    sub al, 20h          ; convertir a mayúscula

imprimir:
    mov dl, al
    mov ah, 02h
    int 21h
    
    inc si               ; siguiente carácter
    jmp siguiente

fin:
    mov ah, 4Ch
    int 21h

END