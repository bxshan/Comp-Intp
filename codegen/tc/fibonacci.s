.data
__ignore: .space 1024
__varnext_fib: .space 1024
__varres: .space 1024
__vara: .space 1024
__vari: .space 1024
__varb: .space 1024
__varN: .space 1024
.text
j main

# begin stmt proc dec
procfibRec:
subu $sp $sp 4
sw $zero ($sp)
# begin stmt block
# begin stmt if
# begin to lbl binop
# begin expr var
lw $v0, 4($sp)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 0
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, else1
# end to lbl binop

# begin stmt assign
# begin expr num
li $v0, 0
# end expr num

sw $v0, 0($sp)
# end stmt assign

j endif1
else1:
# begin stmt if
# begin to lbl binop
# begin expr var
lw $v0, 4($sp)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, else2
# end to lbl binop

# begin stmt assign
# begin expr num
li $v0, 1
# end expr num

sw $v0, 0($sp)
# end stmt assign

j endif2
else2:
# begin stmt assign
# begin expr binop
# begin proc call
subu $sp $sp 4
sw $ra ($sp)
# begin expr binop
# begin expr var
lw $v0, 8($sp)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop

subu $sp $sp 4
sw $v0 ($sp)
jal procfibRec
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
# endproc call

subu $sp $sp 4
sw $v0 ($sp)
# begin proc call
subu $sp $sp 4
sw $ra ($sp)
# begin expr binop
# begin expr var
lw $v0, 12($sp)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 2
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop

subu $sp $sp 4
sw $v0 ($sp)
jal procfibRec
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
# endproc call

lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop

sw $v0, 0($sp)
# end stmt assign

endif2:
# end stmt if

endif1:
# end stmt if

# end stmt block

lw $v0 ($sp)
addu $sp $sp 4
jr $ra
# end stmt proc dev


.globl main
main:

# begin stmt block
# begin stmt writeln
# end stmt writeln

# begin stmt assign
# begin proc call
subu $sp $sp 4
sw $ra ($sp)
# begin expr num
li $v0, 10
# end expr num

subu $sp $sp 4
sw $v0 ($sp)
jal procfibRec
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
# endproc call

la $t0, __varres
sw $v0, ($t0)
# end stmt assign

# begin stmt writeln
# begin expr var
la $t0, __varres
lw $v0 ($t0)
# end expr var

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

# begin stmt writeln
# end stmt writeln

# begin stmt assign
# begin expr num
li $v0, 10
# end expr num

la $t0, __varN
sw $v0, ($t0)
# end stmt assign

# begin stmt assign
# begin expr num
li $v0, 0
# end expr num

la $t0, __vara
sw $v0, ($t0)
# end stmt assign

# begin stmt assign
# begin expr num
li $v0, 1
# end expr num

la $t0, __varb
sw $v0, ($t0)
# end stmt assign

# begin stmt writeln
# begin expr var
la $t0, __vara
lw $v0 ($t0)
# end expr var

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

# begin stmt writeln
# begin expr var
la $t0, __varb
lw $v0 ($t0)
# end expr var

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 2
# end expr num

la $t0, __vari
sw $v0, ($t0)
# end stmt assign

for3:
# begin to lbl binop
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varN
lw $v0 ($t0)
# end expr var

lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor3
# end to lbl binop

# begin stmt block
# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __vara
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varb
lw $v0 ($t0)
# end expr var

lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop

la $t0, __varnext_fib
sw $v0, ($t0)
# end stmt assign

# begin stmt writeln
# begin expr var
la $t0, __varnext_fib
lw $v0 ($t0)
# end expr var

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

# begin stmt assign
# begin expr var
la $t0, __varb
lw $v0 ($t0)
# end expr var

la $t0, __vara
sw $v0, ($t0)
# end stmt assign

# begin stmt assign
# begin expr var
la $t0, __varnext_fib
lw $v0 ($t0)
# end expr var

la $t0, __varb
sw $v0, ($t0)
# end stmt assign

# end stmt block

contfor3:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for3
endfor3:
# end stmt for

# end stmt block


# termination
li $v0 10
syscall