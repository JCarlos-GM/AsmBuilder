.MODEL SMALL
.STACK 100H
.DATA 
	cad db 'hola$'
	
.CODE
	mov ax, @data
	mov ds, ax
	
	mov si, offset cad
	inc si
	inc si
	
	;servicio para imprimir un caracter
	mov ah, 0ah
	mov al, [si] ;a lo que apunta
	mov bh, 00h
	mov cx, 01h
	int 10h
	
	mov ah, 4ch
    int 21h
	
MOV AH, 4CH
INT 21H
END

;EJERCICIO: RECUPERAR LOS CARACTERES DE UNA CADEBA
;Y LOS RETORNE UNO POR UNO EN MAYUSCULA