package Checkers;
public class Cell {
	private int x;
	private int y;

	//X1 & Y1 use to track direction of cell
	public float X1;
	public  float Y1;

	// dx & dy use to track amount moved by the cell
	public float dx;
	public  float dy;
	private CheckersPiece piece;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		dx = x;
		dy = y;
	}

	public int getX() {
		return this.x;
	}


	public int getY() {
		return this.y;
	}

	
	public void setPiece(CheckersPiece p) {

		this.piece = p;
		if (p != null) {
			p.setPosition(this);
		}
	}
	
	public CheckersPiece getPiece() {
		return this.piece;
	}

	//Method use to move the cell
	public void move(){
		this.dx = dx + (X1/30.0F);
		this.dy = dy + (Y1/30.0F);

	}
	
	public void draw(App app) {
		if (this.piece != null) this.piece.draw(app);
	}
}