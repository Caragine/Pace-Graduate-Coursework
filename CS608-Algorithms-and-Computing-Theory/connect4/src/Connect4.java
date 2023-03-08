import java.util.Scanner;

public class Connect4 {

	public static void initializeBoard(char[][] array) {
		for (int i=0; i < array.length; i++) {
			for (int j=0; j < array[0].length; j++) {
				array[i][j] = '-';
			}
		}
		}
	
	public static void printBoard(char[][] array) {
/* if we want column numbers above (not required by assignment)
 * 		for (int i = 0; i < array.length; i++) {
 
			System.out.print(i);
			System.out.print(" ");
		}
		System.out.println("");
*/		
		for (int i = array.length-1; i >= 0; i--) {
			for (int j = 0; j < array[0].length; j++) {
				System.out.print(array[i][j] + " ");
			}
		System.out.println();
		}
	System.out.println();
	}
	
	public static int insertChip(char[][] array, int col, char chipType) {
	for (int i = 0; i < array.length; i++) {
		if (array[i][col] == '-') {
			array[i][col] = chipType;
			return i;
		}
	}
	return -1;
	
	}

	public static boolean checkIfWinner(char[][] array, int col, int row, char chipType) {
		int chipsinrow = 0;
		
		for (int i = 0; i < array.length; i++) {
			if (array[i][col] == chipType) {
				chipsinrow++;
				if (chipsinrow == 4) {
					return true;
				}
			}
			else {
				chipsinrow = 0;
			}
		}
		chipsinrow = 0;
		for (int i=0; i<array[0].length; i++) {
			if (array[row][i] == chipType) {
				chipsinrow++;
				if (chipsinrow == 4) {
					return true;	
				}
			}		
		else {
			chipsinrow = 0;
		}
	}
	return false;
}

public static void main(String[] args) {

Scanner scan = new Scanner(System.in);

System.out.println("What would you like the height of the board to be?");
int height = scan.nextInt();
while (height < 4) {
	System.out.println("Please select a height that is 4 or greater.");
	height = scan.nextInt();
}

System.out.println("What would you like the length of the board to be?");
int length = scan.nextInt();
while (length < 4) { 
	System.out.println("Please select a length that is 4 or greater.");
	length = scan.nextInt();
}


char board[][] = new char[height][length];

initializeBoard(board);

printBoard(board);

System.out.println("Player 1: X");
System.out.println("Player 2: O");
System.out.println("");

// starts game with player 1 as active player
boolean player1 = true;

char chipType;
int choiceCol = 0;
int latestmove = 0;

//determines if game is finished, start as false 
boolean finished = false;

int turnnumber = 0;

//continuous while condition that continues switching turns until break

while(true){

if(player1){
chipType = 'X';
System.out.print("Player 1:");
} 
else {
chipType = 'O';
System.out.print("Player 2:");
}

System.out.print(" Which column would you like to choose? ");
System.out.print("");

choiceCol = scan.nextInt();

latestmove = insertChip(board,choiceCol,chipType);

printBoard(board);

finished = checkIfWinner(board,choiceCol,latestmove,chipType);

if(finished) {

if(player1) {
System.out.print("Player 1 won the game!");
} else
{
System.out.print("Player 2 won the game!");
}

break;
}

// switches players turn
player1 = !player1;

// counter to determine when board is full and game is draw
turnnumber++;



if(turnnumber==length*height){

System.out.println("Draw. Nobody wins.");

break;

}
}
}
}


