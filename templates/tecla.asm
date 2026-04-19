.model small
.stack 10h
.data
    msg db 'Nico y la China$'
    opc db 2

.code
    inicio:
        mov ax, @data
        mov ds, ax

    ciclo:
        mov ah, 09h
        mov dx, offset msg
        int 21h       

        mov ah, 0bh
        int 21h

    cmp al, 00
    jz ciclo

    fin:
        mov ah, 4ch
        int 21h
end          