.data
msg0: .asciiz "guess a number, in [0, 100]:\t\n"
gs0: .asciiz "guess:\t"
gs1: .asciiz "\n 0 if low; 1 if high; 2 if correct:\t\n"
fin: .asciiz "done:\t\n"
.text 0x00400000
.globl main
main: 
# initial msg
li $v0, 4
la $a0, msg0
syscall

# binary search on guess
# set up l, r, m
li $t0, 0
li $t1, 100

while:
  # calculate m into $t3
  move $a0, $t0
  move $a1, $t1
  jal avg
  move $t2, $v0 
  # done if $t0 (l) >= $t1 (r)
  bge $t0, $t1, done
  # guess $t2
  li $v0, 4
  la $a0, gs0
  syscall
  li $v0, 1
  move $a0, $t2
  syscall
  li $v0, 4
  la $a0, gs1
  syscall
  # read feedback
  li $v0, 5
  syscall
  # move feedback to temp $t3
  move $t3, $v0
  beq $t3, 2, done
  sltu $t3, $zero, $t3
  # branch
  if:
    beq $t3, $zero, low 
    # too high
    addi $t1, $t2, -1
    j after
  low: 
    # too low 
    addi $t0, $t2, 1
  after:
  j while
done:
li $v0, 4
la $a0, fin
syscall
li $v0, 1
move $a0, $t2
syscall

li $v0, 10
syscall

# finds avg of $a0 and $a1, sto into $v0
avg:
add $t4, $a0, $a1
srl $v0, $t4, 1
jr $ra
