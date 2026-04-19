;realiza una resta y puede modificar
;el registro de banderas
;sintax CMP variable, variable
;A - B
;ejemplo CMP Ax, 5      >>>>if<<<<

.model small
.stack 100h
.data  
    msg1 db 'es un uno$'
    msg2 db 'es un dos$'
    opc db 1

.code
    mov ax, @data 
    mov ds, ax
	
	cmp opc, 1
	mov ah, 09h
	jz t1
	jmp t2
	
	t1:
		mov dx, offset msg1
		jmp fin
		
	t2:
		mov dx, offset msg2
		
	fin:
		int 21h
		mov ah, 4ch
		int 21h

end