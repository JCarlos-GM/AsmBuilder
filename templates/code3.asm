.model small

.stack 50h

.data

.code
	move ax, 0940h
	mov cx, 01
	mov bx, 000Eh
	int 10h
	
	mov ah, 4ch
	int 21h
end