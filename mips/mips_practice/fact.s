# writes N!, 0! = 1
.data
in: .asciiz "enter N:\t"
.text
.globl main
j main

fact:
bgt $a0, $zero, goon
li $v0, 1
jr $ra
goon:

subu $sp, $sp, 8
sw $ra, 4($sp) # push ra
sw $a0, 0($sp) # push $a0

subu $a0, $a0, 1
jal fact # fact(n-1) -> $v0

lw $t0, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8

mul $v0, $t0, $v0
jr $ra


main:
li $v0, 4
la $a0, in
syscall
li $v0, 5
syscall
move $a0, $v0
jal fact
move $a0, $v0
li $v0, 1
syscall

li $v0, 10
syscall
