.data
__ignore: .space 1024
__varnext_fib: .space 1024
__varres: .space 1024
__vara: .space 1024
__vari: .space 1024
__varb: .space 1024
__varN: .space 1024
__strliteral0: .asciiz "Fibonacci Recursive 10th:"
__strliteral1: .asciiz "Fibonacci Iterative Sequence Up To 10:"
.text
j main

procfibRec:
subu $sp $sp 4
sw $zero ($sp)
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 0
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, else1
li $v0, 0
sw $v0, 0($sp)
j endif1
else1:
lw $v0, 4($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, else2
li $v0, 1
sw $v0, 0($sp)
j endif2
else2:
subu $sp $sp 4
sw $ra ($sp)
lw $v0, 8($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
jal procfibRec
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
subu $sp $sp 4
sw $v0 ($sp)
subu $sp $sp 4
sw $ra ($sp)
lw $v0, 12($sp)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
jal procfibRec
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
sw $v0, 0($sp)
endif2:
endif1:
lw $v0 ($sp)
addu $sp $sp 4
jr $ra

.globl main
main:

la $v0, __strliteral0
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
subu $sp $sp 4
sw $ra ($sp)
li $v0, 10
subu $sp $sp 4
sw $v0 ($sp)
jal procfibRec
lw $t0 ($sp)
addu $sp $sp 4
lw $ra ($sp)
addu $sp $sp 4
la $t0, __varres
sw $v0, ($t0)
la $t0, __varres
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
la $v0, __strliteral1
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 10
la $t0, __varN
sw $v0, ($t0)
li $v0, 0
la $t0, __vara
sw $v0, ($t0)
li $v0, 1
la $t0, __varb
sw $v0, ($t0)
la $t0, __vara
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
la $t0, __varb
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 2
la $t0, __vari
sw $v0, ($t0)
for3:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varN
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor3
la $t0, __vara
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varb
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
la $t0, __varnext_fib
sw $v0, ($t0)
la $t0, __varnext_fib
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
la $t0, __varb
lw $v0 ($t0)
la $t0, __vara
sw $v0, ($t0)
la $t0, __varnext_fib
lw $v0 ($t0)
la $t0, __varb
sw $v0, ($t0)
contfor3:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for3
endfor3:

# termination
li $v0 10
syscall