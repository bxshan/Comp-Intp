# rand0.s
# chooses a random number in 0 to 100 inclusive, 
# then takes guesses from user until correct, giving
# high / low feedback
# @version 04152026
# @author Boxuan Shan
.data
msg0: .asciiz "input guess [0, 100]:\t\n"
hi: .asciiz "too high\n"
lo: .asciiz "too low\n"
fin: .asciiz "done!\n"
.text 0x00400000
.globl main
main: 
# syscall 42 for random
li $a1, 100
li $v0, 42
syscall

# move to $t1
move $t1, $a0

# prompt initial guess
li $v0, 4
la $a0, msg0
syscall
# read input
li $v0, 5
syscall
move $t0, $v0

# while
while:
  # check equal
  beq $t0, $t1, done
  # if 
  blt $t0, $t1, less 
  # guess too high
  li $v0, 4
  la $a0, hi
  syscall
  j after
  less:
  # guess too low
    li $v0, 4
    la $a0, lo
    syscall
  after:
  # prompt guess
  li $v0, 4
  la $a0, msg0
  syscall
  # read input
  li $v0, 5
  syscall
  move $t0, $v0
j while
done: 
li $v0, 4
la $a0, fin 
syscall

li $v0, 10
syscall
