.data
__vararr: .space 1024
__varidxNext: .space 1024
__vartemp: .space 1024
__varsize: .space 1024
__varstopIdx: .space 1024
__vari: .space 1024
__varj: .space 1024
.text
.globl main
main:

# begin stmt assign
# begin expr num
li $v0, 5
# end expr num

la $t0, __varsize
sw $v0, ($t0)
# end stmt assign

# begin stmt assign

la $t0, __vararr
sw $v0, ($t0)
# end stmt assign

# begin stmt arr assign
# begin expr num
li $v0, 64
# end expr num

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 1
# end expr num

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign

# begin stmt arr assign
# begin expr num
li $v0, 34
# end expr num

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 2
# end expr num

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign

# begin stmt arr assign
# begin expr num
li $v0, 25
# end expr num

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 3
# end expr num

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign

# begin stmt arr assign
# begin expr num
li $v0, 12
# end expr num

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 4
# end expr num

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign

# begin stmt arr assign
# begin expr num
li $v0, 22
# end expr num

subu $sp $sp 4
sw $v0 ($sp)
# begin expr num
li $v0, 5
# end expr num

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign

# begin stmt writeln
# end stmt writeln

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
# begin expr var
la $t0, __varsize
lw $v0 ($t0)
# end expr var

lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor1
# end to lbl binop

# begin stmt writeln
# begin expr array elem
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

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
# begin expr binop
# begin expr var
la $t0, __varsize
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

lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor2
# end to lbl binop

# begin stmt block
# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __varsize
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var

lw $t0 ($sp)
addu $sp $sp 4
subu $v0, $t0, $v0
# end expr binop

la $t0, __varstopIdx
sw $v0, ($t0)
# end stmt assign

# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num

la $t0, __varj
sw $v0, ($t0)
# end stmt assign

for3:
# begin to lbl binop
# begin expr var
la $t0, __varj
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varstopIdx
lw $v0 ($t0)
# end expr var

lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor3
# end to lbl binop

# begin stmt block
# begin stmt assign
# begin expr binop
# begin expr var
la $t0, __varj
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

la $t0, __varidxNext
sw $v0, ($t0)
# end stmt assign

# begin stmt if
# begin to lbl binop
# begin expr array elem
# begin expr var
la $t0, __varj
lw $v0 ($t0)
# end expr var

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem

subu $sp $sp 4
sw $v0 ($sp)
# begin expr array elem
# begin expr var
la $t0, __varidxNext
lw $v0 ($t0)
# end expr var

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem

lw $t0 ($sp)
addu $sp $sp 4
ble $t0, $v0, endif4
# end to lbl binop

# begin stmt block
# begin stmt assign
# begin expr array elem
# begin expr var
la $t0, __varj
lw $v0 ($t0)
# end expr var

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem

la $t0, __vartemp
sw $v0, ($t0)
# end stmt assign

# begin stmt arr assign
# begin expr array elem
# begin expr var
la $t0, __varidxNext
lw $v0 ($t0)
# end expr var

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varj
lw $v0 ($t0)
# end expr var

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign

# begin stmt arr assign
# begin expr var
la $t0, __vartemp
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varidxNext
lw $v0 ($t0)
# end expr var

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t1, __vararr
addu $t1, $t1, $v0
lw $t0 ($sp)
addu $sp $sp 4
sw $t0, ($t1)
# end stmt arr assign

# end stmt block

endif4:
# end stmt if

# end stmt block

contfor3:
lw $t0, __varj
addi $t0, $t0, 1
sw $t0, __varj
j for3
endfor3:
# end stmt for

# end stmt block

contfor2:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for2
endfor2:
# end stmt for

# begin stmt writeln
# end stmt writeln

# begin stmt for
# begin stmt assign
# begin expr num
li $v0, 1
# end expr num

la $t0, __vari
sw $v0, ($t0)
# end stmt assign

for5:
# begin to lbl binop
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var

subu $sp $sp 4
sw $v0 ($sp)
# begin expr var
la $t0, __varsize
lw $v0 ($t0)
# end expr var

lw $t0 ($sp)
addu $sp $sp 4
bgt $t0, $v0, endfor5
# end to lbl binop

# begin stmt writeln
# begin expr array elem
# begin expr var
la $t0, __vari
lw $v0 ($t0)
# end expr var

subu $v0, $v0, 1
sll $v0, $v0, 2
la $t0, __vararr
addu $t0, $t0, $v0
lw $v0, ($t0)
# end expr array elem

move $a0, $v0
li $v0, 1
syscall
li $v0, 11
li $a0, 10
syscall

# end stmt writeln

contfor5:
lw $t0, __vari
addi $t0, $t0, 1
sw $t0, __vari
j for5
endfor5:
# end stmt for


# termination
li $v0 10
syscall