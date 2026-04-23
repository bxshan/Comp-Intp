.data
__varlimit: .word 0
__vari: .word 0
__vartemp: .word 0
__varstep: .word 2
__varcounter: .word 0
__varmyBool: .space 1024
.text
.globl main
main:

# begin stmt block
# begin stmt readln
li $v0, 5
syscall
la $t0, __varlimit
sw $v0, ($t0)
# end stmt readln

# begin stmt assign
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr var
la $t0, __varlimit
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 2
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 10
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varstep
lw $v0 ($t0)
# end expr var

lw $t0 ($sp)
addu $sp $sp 4
divu $t0, $v0
mflo $v0
# end expr binop

la $t0, __vartemp
sw $v0, ($t0)
# end stmt assign

# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __vartemp
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 5
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
divu $t0, $v0
mfhi $v0
# end expr binop

la $t0, __vartemp
sw $v0, ($t0)
# end stmt assign

# begin stmt writeln
# begin expr var
la $t0, __vartemp
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
# begin expr bool
li $v0, 1
# end expr bool

la $t0, __varmyBool
sw $v0, ($t0)
# end stmt assign

# begin stmt if
# begin to lbl var
# begin expr var
la $t0, __varmyBool
lw $v0 ($t0)
# end expr var

beq $v0, $zero, else1
# end to lbl var

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

j endif1
else1:
# begin stmt writeln
# begin expr num
li $v0, 0
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

# begin stmt assign
# begin expr var
la $t0, __varlimit
lw $v0 ($t0)
# end expr var

la $t0, __varcounter
sw $v0, ($t0)
# end stmt assign

# begin stmt while
while2:
# begin to lbl binop
# begin expr var
la $t0, __varcounter
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 0
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
ble $t0, $v0, endwhile2
# end to lbl binop

# begin stmt block
# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __varcounter
lw $v0 ($t0)
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

la $t0, __varcounter
sw $v0, ($t0)
# end stmt assign

# end stmt block

j while2
endwhile2:
# end stmt while

# begin stmt writeln
# begin expr var
la $t0, __varcounter
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
# begin expr num
li $v0, 0
# end expr num

la $t0, __vartemp
sw $v0, ($t0)
# end stmt assign

# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 1
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
# begin expr num
li $v0, 5
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor3
# end to lbl binop

# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __vartemp
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var

lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop

la $t0, __vartemp
sw $v0, ($t0)
# end stmt assign

contfor3:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for3
endfor3:
# end stmt for

# begin stmt writeln
# begin expr var
la $t0, __vartemp
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
# begin expr num
li $v0, 5
# end expr num

la $t0, __varcounter
sw $v0, ($t0)
# end stmt assign

# begin stmt rep until
repeat4:
# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __varcounter
lw $v0 ($t0)
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

la $t0, __varcounter
sw $v0, ($t0)
# end stmt assign

# begin to lbl binop
# begin expr var
la $t0, __varcounter
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 0
# end expr num

lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, repeat4
# end to lbl binop

endrpt4:
# end stmt rep until

# begin stmt writeln
# begin expr var
la $t0, __varcounter
lw $v0 ($t0)
# end expr var

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

# end stmt block


# termination
li $v0 10
syscall