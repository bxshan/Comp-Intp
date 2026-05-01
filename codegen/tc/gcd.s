.data
__ignore: .space 1024
__varn1: .space 1024
__varn2: .space 1024
__varresRec: .space 1024
__varresIter: .space 1024
__strliteral0: .asciiz "Finding GCD of 48 and 18"
__strliteral1: .asciiz "Iterative:"
__strliteral2: .asciiz "Recursive:"
.text
j main

# begin stmt proc dec
procgcd:
subu $sp $sp 4
sw $zero ($sp)
subu $sp $sp 4
sw $zero ($sp)
# begin stmt block
# begin stmt while
while1:
# begin to lbl binop
# begin expr var
lw $v0, 8($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 0
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
beq $t0, $v0, endwhile1
# end to lbl binop
# begin stmt block
# begin stmt assign
# begin expr var
lw $v0, 8($sp)
# end expr var
sw $v0, 0($sp)
# end stmt assign
# begin stmt assign
# begin expr binop
# begin expr var
lw $v0, 12($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
lw $v0, 12($sp)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
divu $t0, $v0
mfhi $v0
# end expr binop
sw $v0, 8($sp)
# end stmt assign
# begin stmt assign
# begin expr var
lw $v0, 0($sp)
# end expr var
sw $v0, 12($sp)
# end stmt assign
# end stmt block
j while1
endwhile1:
# end stmt while
# begin stmt assign
# begin expr var
lw $v0, 12($sp)
# end expr var
sw $v0, 4($sp)
# end stmt assign
# end stmt block
lw $t0 ($sp)
addu $sp $sp 4
lw $v0 ($sp)
addu $sp $sp 4
jr $ra
# end stmt proc dev
# begin stmt proc dec
procgcdRec:
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
bne $t0, $v0, else2
# end to lbl binop
# begin stmt assign
# begin expr var
lw $v0, 8($sp)
# end expr var
sw $v0, 0($sp)
# end stmt assign
j endif2
else2:
# begin stmt assign
# begin proc call
subu $sp $sp 4
sw $ra ($sp)
# begin expr var
lw $v0, 8($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr var
lw $v0, 16($sp)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
lw $v0, 16($sp)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
divu $t0, $v0
mfhi $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
jal procgcdRec
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
# endproc call
sw $v0, 0($sp)
# end stmt assign
endif2:
# end stmt if
# end stmt block
lw $v0 ($sp)
addu $sp $sp 4
jr $ra
# end stmt proc dev

.globl main
main:

# begin stmt block
# begin stmt assign
# begin expr num
li $v0, 48
# end expr num
la $t0, __varn1
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
# begin expr num
li $v0, 18
# end expr num
la $t0, __varn2
sw $v0, ($t0)
# end stmt assign
# begin stmt writeln
# begin expr string
la $v0, __strliteral0
# end expr string
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt writeln
# begin expr string
la $v0, __strliteral1
# end expr string
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt assign
# begin proc call
subu $sp $sp 4
sw $ra ($sp)
# begin expr var
la $t0, __varn1
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varn2
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
jal procgcd
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
# endproc call
la $t0, __varresIter
sw $v0, ($t0)
# end stmt assign
# begin stmt writeln
# begin expr var
la $t0, __varresIter
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
# begin expr string
la $v0, __strliteral2
# end expr string
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt assign
# begin proc call
subu $sp $sp 4
sw $ra ($sp)
# begin expr var
la $t0, __varn1
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varn2
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
jal procgcdRec
lw $t0 ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
# endproc call
la $t0, __varresRec
sw $v0, ($t0)
# end stmt assign
# begin stmt writeln
# begin expr var
la $t0, __varresRec
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