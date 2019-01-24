public class Position {

	public int x;
	public int y;
	
	public Position(int x, int y) {
		this.x = x; this.y = y;
    }
    
    public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public boolean equals(Object pos){
		Position p = (Position)pos;
		return this.x == p.x && this.y == p.y;
	}
}