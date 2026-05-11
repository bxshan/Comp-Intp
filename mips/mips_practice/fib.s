# writes Nth fib number; fib(0) = fib(1) = 1
.data
in: .asciiz "which fib number:\t"
.text
.globl main
j main

fib:

bgt $a0, 1, goon 
li $v0, 1
jr $ra

goon:
subu $sp, $sp, 8
sw $ra, 4($sp) # push ra
sw $a0, 0($sp) # push N
subu $a0, $a0, 1
jal fib
lw $a0, 0($sp) # pop N
sw $v0, 0($sp) # push fib(N-1)
subu $a0, $a0, 2
jal fib
lw $t0, 0($sp) # pop fib(N-1)
lw $ra, 4($sp) # pop ra
addu $sp, $sp, 8
addu $v0, $v0, $t0
jr $ra


main:
li $v0, 4
la $a0, in
syscall
li $v0, 5
syscall
move $a0, $v0

jal fib
move $a0, $v0
li $v0, 1
syscall

li $v0, 10
syscall
