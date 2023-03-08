@R0
D=M
@R1
D=D-M
@1LT0
D; JGT

@R0
D=M
@R2
D=D-M
@2LT0
D; JGT

@R1
D=M
@R2
D=D-M
@2LT1
D; JGT

@R0
D=M
@R3
D=D-M
@3LT0
D; JGT

@R1
D=M
@R3
D=D-M
@3LT1
D; JGT

@R2
D=M
@R3
D=D-M
@3LT2
D; JGT

@END
D; JMP

(1LT0)
@R0
D=M
@temp
M=D
@R1
D=M
@R0
M=D
@temp
D=M
@R1
M=D
@6
D; JMP

(2LT0)
@R0
D=M
@temp
M=D
@R2
D=M
@R0
M=D
@R1
D=M
@temp1
M=D
@temp
D=M
@R1
M=D
@temp1
D=M
@R2
M=D
@18
D; JMP

(2LT1)
@R1
D=M
@temp
M=D
@R2
D=M
@R1
M=D
@temp
D=M
@R2
M=D
@18
D; JMP

(3LT0)
@R0
D=M
@temp
M=D
@R3
D=M
@R0
M=D
@R1
D=M
@temp1
M=D
@R2
D=M
@temp2
M=D
@temp
D=M
@R1
M=D
@temp1
D=M
@R2
M=D
@temp2
D=M
@R3
M=D
@END
D; JMP

(3LT1)
@R1
D=M
@temp
M=D
@R3
D=M
@R1
M=D
@R2
D=M
@temp1
M=D
@temp
D=M
@R2
M=D
@temp1
D=M
@R3
M=D
@END
D; JMP

(3LT2)
@R2
D=M
@temp
M=D
@R3
D=M
@R2
M=D
@temp
D=M
@R3
M=D
@END
D; JMP

(END)
0; JMP
