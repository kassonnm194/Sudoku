import java.util.*;
import java.io.*;

public class Sudoku{
	
	public static Unit [] nums;
	public static int solvedCount;
	
	public static void main(String[]args){
		solvedCount = 0;
		nums = new Unit[81];
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter suduko (0 for blank, seperated with space): ");
		for(int i=0; i<81; i++){
			nums[i] = new Unit(in.nextInt(), i%9, i/9);
		}
		for (int i=0; i<81; i++){
			if(nums[i].solved==true){
				solvedCount++;
				narrowPoss(nums[i].val, nums[i].x, nums[i].y, nums[i].group);
			}
		}
		String command = "";
		while (!command.equals("exit")){
			System.out.println("\nSolved: "+solvedCount);
			System.out.print("Enter Command ('h' for help): ");
			command = in.next();
			if (command.equals("print"))
				print();
			else if (command.equals("add"))
				add(in);
			else if (command.equals("possible"))
				showPoss(in);
			else if (command.equals("solveS"))
				solveSingle();
			else if (command.equals("solveG"))
				solveGroup();
			else if (command.equals("solveX"))
				solveX();
			else if (command.equals("solveY"))
				solveY();
			else if (command.equals("unsolved"))
				unsolved();
			else if (command.equals("h")){
				System.out.println("\nprint      (print current board)");
				System.out.println("add        (solve a square)");
				System.out.println("possible   (show possibilities for a square)");
				System.out.println("solveS     (solve all squares with only one possibility)");
				System.out.println("solveG     (solve within groups)");
				System.out.println("solveX     (solve within columns)");
				System.out.println("solveY     (solve within rows)");
				System.out.println("unsolved   (list all unsolved squares and their possibilities)");
			}
		}
	}
	
	public static void print(){
		for (int i=0; i<9; i++){		// i = row #
			if (i==3 || i==6)
				System.out.println("-------+-------+-------");
			System.out.print(" ");
			for (int j=0; j<9; j++){	// j = colomn #
				if (j==3 || j==6)
					System.out.print("| ");
				if (nums[(9*i+j)].val == 0)
					System.out.print(". ");
				else
					System.out.print(nums[(9*i+j)].val+" ");
			}
			System.out.println();
		}
	}
	
	public static void add(Scanner in){
		System.out.print("Enter the value, how many over (0-8), and how far down (0-8) seperated with spaces: ");
		int v = in.nextInt();
		int x = in.nextInt();
		int y = in.nextInt();
		nums[9*y+x].solve(v);
		narrowPoss(v, x, y, nums[9*y+x].group);
		solvedCount++;
	}
	
	public static void  narrowPoss(int val, int x, int y, int group){
		for (int i=0; i<81; i++){
			if(nums[i].solved == false && 
			  (nums[i].group==group || nums[i].x==x || nums[i].y==y)){
				nums[i].cantBe(val);
			}
		}
	}
	
	public static void showPoss(Scanner in){
		System.out.print("Enter x (0-8 over) and y (0-8 down) seperated with space: ");
		int x = in.nextInt();
		int y = in.nextInt();
		System.out.print("Possibilities: ");
		for (int i=1; i<=9; i++){
			if(nums[9*y+x].possible.contains(i))
				System.out.print(i+" ");
		}
		System.out.println();
	}
		
	public static void solveSingle(){
		int temp = solvedCount;
		for(int i=0; i<81; i++){
			if(nums[i].possible.size()==1 && !nums[i].solved){
				nums[i].val = nums[i].possible.get(0);
				nums[i].solved = true;
				narrowPoss(nums[i].val, nums[i].x, nums[i].y, nums[i].group);
				solvedCount++;
			}
		}
		if(temp!=solvedCount)
			solveSingle();
	}
	
	public static void solveGroup(){
		ArrayList<Unit> temp;
		int [] count;
		int preCount = solvedCount;			//solvedCount at start of method
		int tempCount;						//solvedCount at start of each group
		for (int g=1; g<=9; g++){
			tempCount = -1;
			while (tempCount!=solvedCount){
				tempCount = solvedCount;
				temp = new ArrayList<Unit>();
				count = new int[9];
				
				for(int i=0; i<81; i++)						//add unsolved units in group to 'temp'
					if(g==nums[i].group && !nums[i].solved)
						temp.add(nums[i]);
				
				for(int i=0; i<temp.size(); i++)						//go through Units in 'temp'
					for(int j=0; j<temp.get(i).possible.size(); j++)	//go through each 'possible' within temp
						count[temp.get(i).possible.get(j)-1]++;				//count how many of each possible number there are
			
				for(int i=0; i<9; i++){
					int x; 
					int y;
					if(count[i]==1){
						for(int j=0; j<temp.size(); j++){
							if(temp.get(j).possible.contains(i+1)){
								x = temp.get(j).x;
								y = temp.get(j).y;
								nums[9*y+x].solve(i+1);
								narrowPoss(i+1, x, y, nums[9*y+x].group);
								solvedCount++;
							}
						}
					}
				}
			}
		}
		if(preCount!=solvedCount)
			solveGroup();
	}
	
	//copy of solveGroup()
	public static void solveX(){
		ArrayList<Unit> temp;
		int [] count;
		int preCount = solvedCount;			//solvedCount at start of method
		int tempCount;						//solvedCount at start of each group
		for (int g=1; g<=9; g++){
			tempCount = -1;
			while (tempCount!=solvedCount){
				tempCount = solvedCount;
				temp = new ArrayList<Unit>();
				count = new int[9];
				
				for(int i=0; i<81; i++)						//add unsolved units in group to 'temp'
					if(g==nums[i].x && !nums[i].solved)
						temp.add(nums[i]);
				
				for(int i=0; i<temp.size(); i++)						//go through Units in 'temp'
					for(int j=0; j<temp.get(i).possible.size(); j++)	//go through each 'possible' within temp
						count[temp.get(i).possible.get(j)-1]++;				//count how many of each possible number there are
			
				for(int i=0; i<9; i++){
					int x; 
					int y;
					if(count[i]==1){
						for(int j=0; j<temp.size(); j++){
							if(temp.get(j).possible.contains(i+1)){
								x = temp.get(j).x;
								y = temp.get(j).y;
								nums[9*y+x].solve(i+1);
								narrowPoss(i+1, x, y, nums[9*y+x].group);
								solvedCount++;
							}
						}
					}
				}
			}
		}
		if(preCount!=solvedCount)
			solveGroup();
	}
	
	public static void solveY(){
		ArrayList<Unit> temp;
		int [] count;
		int preCount = solvedCount;			//solvedCount at start of method
		int tempCount;						//solvedCount at start of each group
		for (int g=1; g<=9; g++){
			tempCount = -1;
			while (tempCount!=solvedCount){
				tempCount = solvedCount;
				temp = new ArrayList<Unit>();
				count = new int[9];
				
				for(int i=0; i<81; i++)						//add unsolved units in group to 'temp'
					if(g==nums[i].y && !nums[i].solved)
						temp.add(nums[i]);
				
				for(int i=0; i<temp.size(); i++)						//go through Units in 'temp'
					for(int j=0; j<temp.get(i).possible.size(); j++)	//go through each 'possible' within temp
						count[temp.get(i).possible.get(j)-1]++;				//count how many of each possible number there are
			
				for(int i=0; i<9; i++){
					int x; 
					int y;
					if(count[i]==1){
						for(int j=0; j<temp.size(); j++){
							if(temp.get(j).possible.contains(i+1)){
								x = temp.get(j).x;
								y = temp.get(j).y;
								nums[9*y+x].solve(i+1);
								narrowPoss(i+1, x, y, nums[9*y+x].group);
								solvedCount++;
							}
						}
					}
				}
			}
		}
		if(preCount!=solvedCount)
			solveGroup();
	}
	
	public static void unsolved(){
		for (int i=0; i<81; i++){
			if(!nums[i].solved){
				System.out.println("Unit ("+nums[i].x+", "+nums[i].y+"): "+nums[i].possible.toString());	
			}
		}
	}
}
/*

Example 1:
0 0 0 0 0 0 0 0 7 0 0 3 9 0 0 5 8 0 5 0 0 2 0 1 0 9 0 0 0 7 0 0 8 1 0 0 9 6 8 1 0 0 0 0 0 3 1 0 0 5 0 8 2 0 6 0 0 0 4 7 0 5 0 0 0 0 6 1 0 0 0 0 0 0 1 5 9 0 7 0 0 

Example 2:
0 2 0 1 7 8 0 3 0 0 4 0 3 0 2 0 9 0 1 0 0 0 0 0 0 0 6 0 0 8 6 0 3 5 0 0 3 0 0 0 0 0 0 0 4 0 0 6 7 0 9 2 0 0 9 0 0 0 0 0 0 0 2 0 8 0 9 0 1 0 6 0 0 1 0 4 3 6 0 5 0
 
Hard:
0 0 0 0 3 7 6 0 0 0 0 0 6 0 0 0 9 0 0 0 8 0 0 0 0 0 4 0 9 0 0 0 0 0 0 1 6 0 0 0 0 0 0 0 9 3 0 0 0 0 0 0 4 0 7 0 0 0 0 0 8 0 0 0 1 0 0 0 9 0 0 0 0 0 2 5 4 0 0 0 0 

http://www.sudokuslam.com/hints.html 
0 0 0 2 0 0 0 0 0 0 6 5 0 0 0 9 0 0 0 7 0 0 0 6 0 4 0 0 0 0 0 0 1 0 0 5 7 1 0 0 0 0 0 0 9 0 0 9 0 2 0 0 1 0 0 0 1 0 0 0 7 0 0 0 8 7 3 0 4 0 2 0 0 0 0 0 6 0 0 9 4

*/
