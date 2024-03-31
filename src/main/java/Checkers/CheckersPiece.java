package Checkers;

import java.util.Set;

public class CheckersPiece {

	private char colour;
	private Cell position;

	//Attribute to determine if this check piece is moving
	//Use to prevent mouse click
	public boolean isMoving = false;

	public CheckersPiece(char c) {
		this.colour = c;
	}

	public char getColour() {
		return this.colour;
	}

	public void setColour(char c) {
		this.colour = c;
	}

	public void setPosition(Cell p) {
		this.position = p;
	}

	public Cell getPosition() {
		return this.position;
	}

	public Set<Cell> getAvailableMoves(Cell[][] board) {
		//TODO: Get available moves for this piece depending on the board layout, and whether this piece is a king or not - done
		//How to record if the move is a capture or not? Maybe make a new class 'Move' that stores this information, along with the captured piece?
		Move movement = new Move(this.position, board);
		return movement.getMoves();
	}


	//draw the piece
	public void draw(App app) {

		if (!isMoving) {

			app.strokeWeight(5.0f);
			if (colour == 'w' || colour == 'W') {
				app.fill(255);
				app.stroke(0);
			} else if (colour == 'b' || colour == 'B') {
				app.fill(0);
				app.stroke(255);
			}
			app.ellipse(position.getX() * App.CELLSIZE + App.CELLSIZE / 2, position.getY() * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
			app.noStroke();

			if (colour == 'W' || colour == 'B') {
				app.strokeWeight(5.0f);
				if (colour == 'W') {
					app.fill(255);
					app.stroke(0);
				} else if (colour == 'B') {
					app.fill(0);
					app.stroke(255);
				}
				app.ellipse(position.getX() * App.CELLSIZE + App.CELLSIZE / 2, position.getY() * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.4f, App.CELLSIZE * 0.4f);
				app.noStroke();
			}
		}

		if (isMoving) {

			app.strokeWeight(5.0f);
			if (colour == 'w' || colour == 'W') {
				app.fill(255);
				app.stroke(0);
			} else if (colour == 'b' || colour == 'B') {
				app.fill(0);
				app.stroke(255);
			}
			app.ellipse(position.dx * App.CELLSIZE + App.CELLSIZE / 2, position.dy * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.8f, App.CELLSIZE * 0.8f);
			app.noStroke();

			if (colour == 'W' || colour == 'B') {
				app.strokeWeight(5.0f);
				if (colour == 'W') {
					app.fill(255);
					app.stroke(0);
				} else if (colour == 'B') {
					app.fill(0);
					app.stroke(255);
				}
				app.ellipse(position.dx * App.CELLSIZE + App.CELLSIZE / 2, position.dy * App.CELLSIZE + App.CELLSIZE / 2, App.CELLSIZE * 0.4f, App.CELLSIZE * 0.4f);
				app.noStroke();
			}
		}
	}
}