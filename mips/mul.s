# mul.s
# takes two nums from user and returns their product
# @version 04152026
# @author Boxuan Shan
.data
msg: .asciiz "input int:\t"
.text 0x00400000
.globl main 
main:
# msg 1
li $v0, 4
la $a0, msg
syscall

# read 1
li $v0, 5  
syscall   
move $t0, $v0 

# msg 2
li $v0, 4
la $a0, msg
syscall

# read 2
li $v0, 5      
syscall          
move $t1, $v0     

# multiply
mult $t0, $t1
mflo $t2

# print to cli
li $v0, 1
move $a0, $t2
syscall

li $v0, 10
syscall

