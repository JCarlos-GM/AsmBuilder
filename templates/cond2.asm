;cmp compare
;realiza una resta y puede modificar
;el registro de banderas
;sintax CMP variable, variable
;A - B
;ejemplo CMP Ax, 5      >>>>if<<<<

.model small
.stack 100h
.data  
    msg db 'es un uno$'
    msg2 db 'es un dos$'
    opc db 02

.code
    mov ax, @data 
    mov ds, ax

    inicio:
        CMP opc, 01
        JE mostrar
        JMP fin
    
    mostrar:
        mov ah, 09h
        mov dx, offset msg
        int 21h

    fin:
        mov ah, 4ch
        int 21h

end