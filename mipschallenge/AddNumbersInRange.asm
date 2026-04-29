# This program prompts the user to enter two numbers in the range
# [1000 - 5000] inclusive. Once the user enters the two values successfully
# it prints the sum of the two numbers
#
# @author	Boxuan Shan, Kendra Zhao
# @version 04272026
.data
msg: 	.asciiz		"Enter a number between 1000 and 5000 "

.text

.globl main

main:

read:
li $v0, 4
la $a0, msg
syscall

li $v0, 5
syscall
move $t0, $v0

blt $t0, 1000, read
bgt $t0, 5000, read

read1:
li $v0, 4
la $a0, msg
syscall

li $v0, 5
syscall
move $t1, $v0

blt $t1, 1000, read1
bgt $t1, 5000, read1

addu $a0, $t0, $t1
li $v0, 1
syscall

li $v0, 10
syscall
