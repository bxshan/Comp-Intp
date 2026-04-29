# main.s
# contains all subroutines for subroutines lab
# @version 04192026
# @author Boxuan Shan
.data
list: .space 1024 # reserve 1024 bytes for listnodes
                  # 8 bytes per node, enough for 128 nodes
next: .word 8 # offset on list for addr of next free 8 byte loc
.text 0x00400000
.globl main 
main:
# init next
la $t0, list
sw $t0, next

# set up subroutine call
li $a0, 111
li $a1, 0 # points to null
# li $a1, 10000
# li $a2, 200
jal newlistnode
move $t0, $v0

# listnode only
li $a0, 222
move $a1, $t0
jal newlistnode
move $t0, $v0 # now $t0 sto address of 222, which points to 111
# now sum the list
move $a0, $t0
jal sumlist # should return 222 + 111 = 333
move $t0, $v0

# write output
li $v0, 1
move $a0, $t0
syscall

# termination 
li $v0, 10
syscall


# square
# sample provided
# calculates square of num
# @param $a0 register with num to square
# @return square of num in $a0
square: 
multu $a0, $a0
mflo $v0
jr $ra


# max2
# returns max of 2 nums
# @param $a0, $a1 registers with nums to find max 
# @return max of nums in $a0 and $a1
max2:
bge $a0, $a1, a0max 
a1max:
  move $v0, $a1
  j return
a0max:
  move $v0, $a0
return:
jr $ra


# max3
# returns max of 3 nums, using max2
# @param $a0, $a1, $a2 registers with nums to find max
# @return max of nums in $a0, $a1, $a2
max3:
subu $sp, $sp, 8
sw $ra, 4($sp) # push ra 
sw $a2, 0($sp) # push third num

jal max2 

lw $a0, 0($sp) # pop third num
move $a1, $v0

jal max2 
lw $ra, 4($sp) # pop ra
addu $sp, $sp, 8

jr $ra


# fact
# computes factorial
# @param $a0 containing num to find factorial of 
# @return factorial of num in $a0
fact:
bne $a0, $zero, factgoon
li $v0, 1
jr $ra

factgoon:
subu $sp, $sp, 8
sw $ra, 4($sp) # push ra
sw $a0, 0($sp) # push n
subu $a0, $a0, 1
jal fact # fact(n-1) in $v0
lw $a0, 0($sp) # pop n 
lw $ra, 4($sp) # pop ra
addu $sp, $sp, 8
mult $v0, $a0
mflo $v0
jr $ra


# fib
# finds nth fibonacci number
# fib(0) = 0, fib(1) = 1
# @param $a0 register containing n
# @return nth fibonacci number
fib:
bgt $a0, 1, fibgoon
move $v0, $a0
jr $ra

fibgoon:
subu $sp, $sp, 12
sw $ra, 8($sp) # push ra
sw $a0, 4($sp) # push n
subu $a0, $a0, 1
jal fib
sw $v0, 0($sp) # push fib(n-1)
lw $a0, 4($sp) # pop n
subu $a0, $a0, 2
jal fib
lw $a0, 0($sp) # pop fib(n-1)
add $v0, $a0, $v0
lw $ra, 8($sp) # pop ra
addu $sp, $sp, 12
jr $ra


# newlistnode
# constructs a new list node:
# allocates first 4 bytes for the value of this node,
#       and last 4 bytes for the address of the next node
# last node points to addr 0 (null)
# @param $a0 containing value of new node
# @param $a1 containing address of next node
# @return address of first byte of new node
newlistnode: # $a0 is val, $a1 is next
lw $t0, next # address of next free 8 bytes 
sw $a0, 0($t0) # put val in first 4
sw $a1, 4($t0) # next in next 4

move $v0, $t0
addu $t0, $t0, 8
sw $t0, next

jr $ra


# sumlist
# sums values over the entire list
# @param first byte of first listnode in list to sum
# @return sum of vals over entire list
sumlist:
bne $a0, $zero, sumlistgoon 
# if address is 0 (null)
move $v0, $zero
jr $ra

sumlistgoon:
subu $sp, $sp, 8
sw $ra, 4($sp) # push ra
sw $a0, 0($sp) # push listnode addr
# next is in 4($a0)
lw $a0, 4($a0)
jal sumlist
lw $t0, 0($sp) # pop listnode addr 
lw $t1, 0($t0)
addu $v0, $v0, $t1
lw $ra, 4($sp) # pop ra
addu $sp, $sp, 8
jr $ra

