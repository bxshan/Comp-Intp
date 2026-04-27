# int64.s
# performs multiplication on two 32 bit ints in hex, that will overflow. 
# takes the hex results from the lo and hi register and parses them by 
# sequentially taking he 4 msb from each register, converting to an ascii 
# char, then printing out the char corresponding to the hex. this is 
# necessary because just printing out the hi and lo registers prints the 
# 0x prefix twice
# @version 04162026
# @author Boxuan Shan
.data
hexpfx: .asciiz "\n0x"
.text 0x00400000
.globl main
main:
# mult 0x00010000, 0x00020000 = 0x0000000200000000
#      65536     x 131072     = 8589934592
li $t0, 0x10000
li $t1, 0x20000
multu $t0, $t1 
mflo $t2 # lower 32 bits
mfhi $t3 # upper 32 bits

# print 0x prefix
li $v0, 4
la $a0, hexpfx
syscall

# get each 4 bits at a time from high and low
move $a0, $t3
jal writehex
move $a0, $t2
jal writehex

# newline
li $v0, 11      
li $a0, 10     
syscall       

# dec
# move $a0, $t2 # lo 32
# move $a1, $t3 # hi 32
# jal writedec

li $v0, 10
syscall

# writedec:
# move $s0, $a0
# move $s1, $a1
# li $s2, 10 # divisor
# #TODO



# takes in a 32 bit register representing a hex number, 
# and prints the number out without the 0x prefix by 
# sequentially converting the 4 msb to ascii chars.
# @input $a0 32 bits representing a hex number
writehex:
move $s0, $a0
li $s1, 8 # 8*4=32

while:
beq $s1, 0, end

rol $s0, $s0, 4 # get the top 4 msb
andi $s2, $s0, 0xF # bitmask
# want to convert 4 bits to ascii char of hex
# but there is a gap of 7
# between ascii 9 and ascii A
ble $s2, 9, toascii 
addi $s2, $s2, 7 # remove offset
toascii:
addi $s2, $s2, 48 # convert to ascii code

li $v0, 11 # syscall 11 is print ascii char
move $a0, $s2
syscall

addi $s1, $s1, -1
j while
end:
jr $ra
