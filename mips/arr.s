# arr.s
# accepts 10 nums from user and stores them in arraay, then 
# returns sum, min, max, mean of nums. works with float
# @version 04152026
# @author Boxuan Shan
.data
arr: .space 40 # reserve 40 bytes for 10 int32
msg: .asciiz "input into arr:\t"
msgsum: .asciiz "\nsum\n"
msgmin: .asciiz "\nmin\n"
msgmax: .asciiz "\nmax\n"
msgmean: .asciiz "\nmean\n"
.text 0x00400000
.globl main
main:
la $t0, arr # lower: addr of arr
li $t1, 10 # upper
li $t2, 0 # cnt

for_read:
# write prompt
li $v0, 4
la $a0, msg
syscall
li $v0, 5
syscall
# store into
sw $v0, 0($t0)
addi $t0, $t0, 4 # inc 32 bits 
addi $t2, $t2, 1 # $t2++
blt $t2, $t1, for_read

# must do another pass across the array
la $t0, arr # reset $t0 + 4 to skip first elem
li $t2, 1 # reset $t2 to start at 1; $t1 is still 10

lw $t3, 0($t0) # sum
lw $t4, 0($t0) # min
lw $t5, 0($t0) # max
addi $t0, $t0, 4 # inc the addr of arr to start at

for_calc:
lw $t6, 0($t0)
add $t3, $t3, $t6 # add to sum
# check less than
slt $t7, $t6, $t4
beq $t7, $zero, after1
# $t7 == 1, so $t6 < $t4
move $t4, $t6
after1:

# check greater than
slt $t7, $t5, $t6
beq $t7, $zero, after2
# $t7 == 1, so $t5 < $t6
move $t5, $t6
after2:
addi $t0, $t0, 4 # inc 32 bits
addi $t2, $t2, 1 # $t2++
blt $t2, $t1, for_calc

# compute mean into $f2
# want ans as a float
# move sum and len of arr to floating pt registers
mtc1 $t3, $f0
mtc1 $t1, $f1
# convert to float
cvt.s.w $f0 $f0
cvt.s.w $f1 $f1
# divide and sto into $f12 so no need to move later
div.s $f12, $f0, $f1

# output
# sum
li $v0, 4
la $a0, msgsum
syscall
li $v0, 1
move $a0, $t3
syscall
# min
li $v0, 4
la $a0, msgmin
syscall
li $v0, 1
move $a0, $t4
syscall
# max
li $v0, 4
la $a0, msgmax
syscall
li $v0, 1
move $a0, $t5
syscall
# mean
li $v0, 4
la $a0, msgmean
syscall
li $v0, 2
# mean already in f12
syscall

li $v0, 10
syscall
