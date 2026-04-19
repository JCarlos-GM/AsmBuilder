;realiza una resta y puede modificar
;el registro de banderas
;sintax CMP variable, variable
;A - B
;ejemplo CMP Ax, 5      >>>>if<<

;MODIFICAR DE TAL FORMA QUE AHORA SEAN 3
;OPCIONES, QUE MANDE UN MENSAJE DE QUE NO EXISTE

.model small
.stack 100h
.data  
    msg1 db 'Es un uno$'
    msg2 db 'Es un dos$'
    msg3 db 'Esta opcion no existe$'
    opc  db 3
.code
    mov ax, @data 
    mov ds, ax

    cmp opc, 1      
    jz  t1         
	
    cmp opc, 2     
    jz  t2          

    jmp t3          

    t1:
        mov dx, offset msg1
        jmp fin
        
    t2:
        mov dx, offset msg2
        jmp fin
    
    t3:
        mov dx, offset msg3
    
    fin:
        mov ah, 09h    
        int 21h
        mov ah, 4ch
        int 21h
end