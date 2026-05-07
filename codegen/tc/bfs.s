.data
__ignore: .space 1024
__varhead: .space 1024
__varcurrent: .space 1024
__vartail: .space 1024
__varvisited: .space 1024
__varmax_nodes: .space 1024
__vari: .space 1024
__varidx: .space 1024
__varneighbor: .space 1024
__vargraph: .space 1024
__varqueue: .space 1024
__strliteral0: .asciiz "Starting BFS:"
__strliteral1: .asciiz "Visiting Node "
.text
j main


.globl main
main:

li $v0, 4
la $t0, __varmax_nodes
sw $v0, ($t0)
la $t0, __vargraph
sw $v0, ($t0)
la $t0, __varvisited
sw $v0, ($t0)
la $t0, __varqueue
sw $v0, ($t0)
li $v0, 1
la $t0, __varhead
sw $v0, ($t0)
li $v0, 1
la $t0, __vartail
sw $v0, ($t0)
li $v0, 1
la $t0, __vari
sw $v0, ($t0)
for1:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor1
li $v0, 0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varvisited
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
contfor1:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for1
endfor1:
li $v0, 1
la $t0, __vari
sw $v0, ($t0)
for2:
la $t0, __vari
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 16
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor2
li $v0, 0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vari
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
contfor2:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for2
endfor2:
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 2
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 4
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 3
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
la $v0, __strliteral0
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vartail
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varqueue
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
la $t0, __vartail
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
la $t0, __vartail
sw $v0, ($t0)
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varvisited
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
while3:
la $t0, __varhead
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vartail
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bge $t0, $v0, endwhile3
la $t0, __varhead
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __varqueue
addu $t0, $t0, $v0
lw $v0, ($t0)
la $t0, __varcurrent
sw $v0, ($t0)
la $t0, __varhead
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
la $t0, __varhead
sw $v0, ($t0)
la $v0, __strliteral1
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
la $t0, __varcurrent
lw $v0 ($t0)
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
li $v0, 1
la $t0, __varneighbor
sw $v0, ($t0)
for4:
la $t0, __varneighbor
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varmax_nodes
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor4
la $t0, __varcurrent
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varmax_nodes
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varneighbor
lw $v0 ($t0)
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
la $t0, __varidx
sw $v0, ($t0)
la $t0, __varidx
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vargraph
addu $t0, $t0, $v0
lw $v0, ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, endif5
la $t0, __varneighbor
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __varvisited
addu $t0, $t0, $v0
lw $v0, ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
beq $t0, $v0, endif6
li $v0, 1
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __varneighbor
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varvisited
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
la $t0, __varneighbor
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
la $t0, __vartail
lw $v0 ($t0)
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varqueue
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
la $t0, __vartail
lw $v0 ($t0)
subu $sp $sp 4
sw $v0 ($sp)
li $v0, 1
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
la $t0, __vartail
sw $v0, ($t0)
endif6:
endif5:
contfor4:
lw $t0, __varneighbor
addi $t0, $t0, 1
sw $t0, __varneighbor
j for4
endfor4:
j while3
endwhile3:

# termination
li $v0 10
syscall