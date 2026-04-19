.model small

.stack 50h

.data

.code
	mov ah, 09h
	mov al, 64
	mov cx, 01		;repetición
	mov bh, 00
	mov bl, 14 		;color (amarrillo)
	int 10h
	
	mov ah, 4ch
	int 21h
end