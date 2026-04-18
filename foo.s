.data
__varx: .word 10
__vary: .word 0
.text
.globl main
main:

# begin stmt block
# begin stmt if
# begin to lbl binop
# begin expr var
la $t0, __varx
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 5
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
ble $t0, $v0, endif1
# end to lbl binop

# begin stmt writeln
# begin expr num
li $v0, 1
# end expr num

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

endif1:
# end stmt if

# begin stmt writeln
# begin expr binop
# begin expr var
la $t0, __varx
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

# begin stmt while
while2:
# begin to lbl binop
# begin expr var
la $t0, __vary
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 5
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
bge $t0, $v0, endwhile2
# end to lbl binop

# begin stmt block
# begin stmt writeln
# begin expr var
la $t0, __vary
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
# begin expr binop
# begin expr var
la $t0, __vary
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop

la $t0, __vary
sw $v0, ($t0)
# end stmt assign

# end stmt block

j while2
endwhile2:
# end stmt while

# end stmt block


# termination
li $v0 10
syscall