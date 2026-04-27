# printrange.s
# prints numbers from l to r inclusive, with set increment
# @version 04152026
# @author Boxuan Shan
.data
msg0: .asciiz "input l:\n"
msg1: .asciiz "input r:\n"
msg2: .asciiz "input inc:\n"
space: .asciiz " "
.text 0x00400000
.globl main
main:

# msg 0
li $v0, 4
la $a0, msg0
syscall

# read l 
li $v0, 5 
syscall
move $t0, $v0

# msg 1
li $v0, 4
la $a0, msg1
syscall

# read r 
li $v0, 5 
syscall
move $t1, $v0

# msg 2
li $v0, 4
la $a0, msg2
syscall

# read inc 
li $v0, 5 
syscall
move $t2, $v0

# loop
for:
bgt $t0, $t1, end 
# write $t0
li $v0, 1
move $a0, $t0
syscall
# write whitespace
li $v0, 4
la $a0, space
syscall
# increment
add $t0, $t0, $t2
j for
end:
li $v0, 10
syscall
