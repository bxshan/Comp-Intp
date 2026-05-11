# prints nums from 1 to N where N is read from user
.data
in: .asciiz "enter upper bound\n"
.text
.globl main
j main
inp:
li $v0, 4
la $a0, in
syscall
li $v0, 5
syscall
jr $ra

main:
jal inp 
move $t1, $v0

li $t0, 1
loop:
li $v0, 1
move $a0, $t0
syscall
li $v0, 11
li $a0, 10
syscall
addu $t0, $t0, 1
ble $t0, $t1, loop

li $v0, 10
syscall
