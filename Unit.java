import java.util.*;

public class Unit{
	
	public boolean solved;
	public int val;
	public ArrayList<Integer> possible;
	public int x;	// (0-8) how far over
	public int y;	// (0-8) how far down
	public int group;	//which block the unit is in:
						// 1 | 2 | 3
						//---+---+---
						// 4 | 5 | 6
						//---+---+---
						// 7 | 8 | 9
	
	public Unit(int value, int xCoord, int yCoord){
		x = xCoord;
		y = yCoord;
		if (y<3){
			if (x<3){
				group = 1;
			}else if (x>5){
				group = 3;
			}else{
				group = 2;
			}
		}else if (y>5){
			if (x<3){
				group = 7;
			}else if (x>5){
				group = 9;
			}else{
				group = 8;
			}
		}else{
			if (x<3){
				group = 4;
			}else if (x>5){
				group = 6;
			}else{
				group = 5;
			}
		} 
		val = value;
		possible = new ArrayList<Integer>();
		if (val==0){
			solved = false;
			for (int i=1; i<=9; i++){
				possible.add((Integer)i);
			}
		}else{
			possible.add((Integer)val);
			solved = true;
		}
	}
	
	public void cantBe(int x){
		possible.remove((Integer)x);
	}
	
	public void solve(int x){
		val = x;
		for (int i=1; i<=9; i++){
			possible.remove((Integer)i);
		}
		possible.add((Integer)val);
		solved = true;
	}
		
}
