# take N ints into arr and then prints them out 
.data
arr: .space 1024
idx: .word 4 # current lenght of arr
in0: .asciiz "enter input:\t"
in1: .asciiz "enter arr len:\t"
.text
.globl main
j main
inp:
li $v0, 4
# la $a0, in
syscall
li $v0, 5
syscall
jr $ra

main:
la $a0, in1
jal inp
move $t0, $v0 # len of arr

li $t1, 0

la $t2, arr # base of arr
inploop:
la $a0, in0
jal inp

sw $v0, 0($t2)

addu $t2, $t2, 4
addu $t1, $t1, 1
blt $t1, $t0, inploop


li $t1, 0
la $t2, arr
outploop:
lw $a0, 0($t2)
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
addu $t1, $t1, 1
addu $t2, $t2, 4
blt $t1, $t0, outploop

