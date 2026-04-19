.model small

.stack 100h

.data

    msg db 'hola$'

.code
    ;jmp es un salto incondicional
    ;la sintaxis dice que salta a la 
    ;etiqueta especificada
    inicio:

        ;JMP fin

        mov ax, @data 
        mov ds, ax

        mov ah, 09h
        mov dx, offset msg 
        int 21h

    fin:
        mov ah, 4ch
        int 21h
end
