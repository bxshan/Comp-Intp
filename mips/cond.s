# cond.s
# takes input from user and determines and prints parity
# @version 04152026
# @author Boxuan Shan
.data
msg: .asciiz "input int:\t"
odd: .asciiz "odd\n"
even: .asciiz "even\n"

.text 0x00400000
.globl main
main:
# msg 1
li $v0, 4
la $a0, msg
syscall

# take input into $t0
li $v0, 5
syscall 
move $t0, $v0

# compute $t2 = $t1 & 1
andi $t1, $t0, 1

# parity
beq $t1, 0, ev
# odd
li $v0, 4
la $a0, odd
syscall
j after

# even
ev:
li $v0, 4
la $a0, even
syscall

after:
li $v0, 10
syscall
