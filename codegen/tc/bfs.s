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

# begin stmt block
# begin stmt assign
# begin expr num
li $v0, 4
# end expr num
la $t0, __varmax_nodes
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
la $t0, __vargraph
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
la $t0, __varvisited
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
la $t0, __varqueue
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
la $t0, __varhead
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
la $t0, __vartail
sw $v0, ($t0)
# end stmt assign
# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
la $t0, __vari
sw $v0, ($t0)
# end stmt assign
for1:
# begin to lbl binop
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor1
# end to lbl binop
# begin stmt arr assign
# begin expr bool
li $v0, 0
# end expr bool
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varvisited
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
contfor1:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for1
endfor1:
# end stmt for
# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
la $t0, __vari
sw $v0, ($t0)
# end stmt assign
for2:
# begin to lbl binop
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 16
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor2
# end to lbl binop
# begin stmt arr assign
# begin expr num
li $v0, 0
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
contfor2:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for2
endfor2:
# end stmt for
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 2
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr num
li $v0, 2
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 3
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr num
li $v0, 3
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr num
li $v0, 2
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr num
li $v0, 4
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 2
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr num
li $v0, 3
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr num
li $v0, 4
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 3
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vargraph
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt writeln
# begin expr string
la $v0, __strliteral0
# end expr string
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt arr assign
# begin expr num
li $v0, 1
# end expr num
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vartail
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varqueue
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __vartail
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
la $t0, __vartail
sw $v0, ($t0)
# end stmt assign
# begin stmt arr assign
# begin expr bool
li $v0, 1
# end expr bool
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varvisited
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt while
while3:
# begin to lbl binop
# begin expr var
la $t0, __varhead
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vartail
lw $v0 ($t0)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bge $t0, $v0, endwhile3
# end to lbl binop
# begin stmt block
# begin stmt assign
# begin expr array elem
# begin expr var
la $t0, __varhead
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __varqueue
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
la $t0, __varcurrent
sw $v0, ($t0)
# end stmt assign
# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __varhead
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
la $t0, __varhead
sw $v0, ($t0)
# end stmt assign
# begin stmt writeln
# begin expr string
la $v0, __strliteral1
# end expr string
move $a0, $v0
li $v0, 4
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt writeln
# begin expr var
la $t0, __varcurrent
lw $v0 ($t0)
# end expr var
move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall
# end stmt writeln
# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num
la $t0, __varneighbor
sw $v0, ($t0)
# end stmt assign
for4:
# begin to lbl binop
# begin expr var
la $t0, __varneighbor
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varmax_nodes
lw $v0 ($t0)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor4
# end to lbl binop
# begin stmt block
# begin stmt assign
# begin expr binop
# begin expr binop
# begin expr binop
# begin expr var
la $t0, __varcurrent
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varmax_nodes
lw $v0 ($t0)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
multu $t0, $v0
mflo $v0
# end expr binop
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varneighbor
lw $v0 ($t0)
# end expr var
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
la $t0, __varidx
sw $v0, ($t0)
# end stmt assign
# begin stmt if
# begin to lbl binop
# begin expr array elem
# begin expr var
la $t0, __varidx
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vargraph
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
bne $t0, $v0, endif5
# end to lbl binop
# begin stmt block
# begin stmt if
# begin to lbl binop
# begin expr array elem
# begin expr var
la $t0, __varneighbor
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __varvisited
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem
subu $sp $sp 4
sw $v0 ($sp)
# begin expr bool
li $v0, 1
# end expr bool
lw $t0 ($sp)
addu $sp $sp 4
beq $t0, $v0, endif6
# end to lbl binop
# begin stmt block
# begin stmt arr assign
# begin expr bool
li $v0, 1
# end expr bool
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varneighbor
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varvisited
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt arr assign
# begin expr var
la $t0, __varneighbor
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vartail
lw $v0 ($t0)
# end expr var
subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __varqueue
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign
# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __vartail
lw $v0 ($t0)
# end expr var
subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num
lw $t0 ($sp)
addu $sp $sp 4
addu $v0, $t0, $v0
# end expr binop
la $t0, __vartail
sw $v0, ($t0)
# end stmt assign
# end stmt block
endif6:
# end stmt if
# end stmt block
endif5:
# end stmt if
# end stmt block
contfor4:
lw $t0, __varneighbor
addi $t0, $t0, 1
sw $t0, __varneighbor
j for4
endfor4:
# end stmt for
# end stmt block
j while3
endwhile3:
# end stmt while
# end stmt block

# termination
li $v0 10
syscall