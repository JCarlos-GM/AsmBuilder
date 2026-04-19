.model small
.stack 40h
.data 
.code
	mov ax, 65
	inc ax 
	mov cx, 08
	
	et1:
		add ax, 2
	loop et1
	
	mov ah, 09h
	mov al, dl
	mov cx, 01
	mov bh, 0
	mov bl, 14
	int 10h
	
	mov ah, 5ch
	int 21h
end