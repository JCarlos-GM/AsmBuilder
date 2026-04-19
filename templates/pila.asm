.model small

.stack 100h

.data

.code
main proc
    ; Meter valores en la pila
    mov bx, 4C00h
    push bx

    mov bx, 23o
    push bx
    
    mov bx, 10h
    push bx

    mov bx, 200o
    push bx

    ; En este caso, si trato de meter directamente el valor a la pila,
    ; obtenemos un error.
    ; Una solucion temporal a esto es iniciar un registro con el valor
    ; que deseamos meter en la pila, y el valor de ese registro lo 
    ; ingresamos a la pila

    pop ax      ; Sacamos el 0x80
    pop ax      ; Sacamos el 0x10
    pop ax      ; Sacamos el 0x13

    ; Terminar el programa
    pop ax      ; Finalmente, sacamos el valor 0x4c00 para finalizar el programa
    int 21h

end main