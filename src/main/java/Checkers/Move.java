package Checkers;

import java.util.HashSet;
import java.util.Set;


public class Move {
    private Cell cell;
    private Cell[][] board;

    private boolean player_one;
    private boolean player_two;

    public static CheckersPiece cellFromPiece;
    int toRow;
    int fromRow;
    int toCol;
    int fromCol;
    int deltaX;
    int deltaY;
    int midRow;
    int midCol;

    public Move(Cell cell, Cell[][] board){
        this.cell =cell;
        this.board = board;
        App.tempCell = new Cell(cell.getX(), cell.getY());

    }

    public void tick(){
        if (Math.abs(App.tempCell.dx-board[toRow][toCol].getX())<0.0001){
            cellFromPiece.isMoving=false;
            board[toRow][toCol].setPiece(cellFromPiece);
            App.tempCell = new Cell(0,0);
            App.tempCell.X1 = 0;
            App.tempCell.Y1 = 0;

            for (int i = 0; i < 8; i++) {
                if (board[0][i].getPiece() != null) {
                    if (board[0][i].getPiece().getColour() == 'b') {
                        board[0][i].getPiece().setColour('B');
                    }
                }
            }
            for (int i = 0; i < 8; i++) {
                if (board[7][i].getPiece() != null) {
                    if (board[7][i].getPiece().getColour() == 'w') {
                        board[7][i].getPiece().setColour('W');
                    }
                }
            }

        } else {
            App.tempCell.move();
        }
    }

    public CheckersPiece processMove(Cell cellFrom, Cell cellTo) {
        CheckersPiece midPiece = null;

        toRow = cellTo.getY();
        fromRow = cellFrom.getY();
        toCol = cellTo.getX();
        fromCol = cellFrom.getX();
        deltaX = toRow - fromRow;
        deltaY = toCol - fromCol;
        midRow = fromRow + deltaX / 2;
        midCol = fromCol + deltaY / 2;

        if (Math.abs(cellFrom.getX() - cellTo.getX()) > 1) {
            midPiece = board[midRow][midCol].getPiece();
        }
        boolean output = false;
        if (midPiece != null) {
            output = (Character.toLowerCase(midPiece.getColour()) != Character.toLowerCase(cellFrom.getPiece().getColour()));
        }

        cellFromPiece = cellFrom.getPiece();
        cellFrom.setPiece(null);

        cellFromPiece.isMoving = true;


        App.tempCell = new Cell(fromCol,fromRow);
        App.tempCell.setPiece(cellFromPiece);

        if (deltaX > 0 && deltaY > 0) {
            App.tempCell.X1 = 1.0F;
            App.tempCell.Y1 = 1.0F;
        } else if (deltaX > 0 && deltaY < 0) {
            App.tempCell.X1 = -1.0F;
            App.tempCell.Y1 = 1.0F;
        } else if (deltaX < 0 && deltaY > 0) {
            App.tempCell.X1 = 1.0F;
            App.tempCell.Y1 = -1*1.0F;
        } else if (deltaX < 0 && deltaY < 0) {
            App.tempCell.X1 = -1.0F;
            App.tempCell.Y1 = -1.0F;
        }

        if (output) {
            return midPiece;
        } else {
            return null;
        }
    }

    public Set<Cell> getMoves() {
        Set<Cell> availableMoves = new HashSet<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Cell cellFrom = this.cell;
                Cell cellTo = this.board[i][j];
                if (isValidMove(cellFrom,cellTo)) {
                    availableMoves.add(cellTo);
                }
            }
        }
        return availableMoves;
    }

    public boolean isValidMove(Cell cellFrom, Cell cellTo) {
        int toRow = cellTo.getY();
        int fromRow = cellFrom.getY();
        int toCol = cellTo.getX();
        int fromCol = cellFrom.getX();
        int deltaX = toRow - fromRow;
        int deltaY = toCol - fromCol;
        char b = 'b';
        char w = 'w';
        char B = 'B';
        char W = 'W';

        int midRow = fromRow + deltaX / 2;
        int midCol = fromCol + deltaY / 2;
        CheckersPiece midCheckersPiece = board[midRow][midCol].getPiece();

        char startPiece = cellFrom.getPiece().getColour();

        if (cellFrom.getPiece().getColour()=='b'|| cellFrom.getPiece().getColour()=='B') {
            player_one = true;
            player_two = false;
        } else if (cellFrom.getPiece().getColour()=='w'|| cellFrom.getPiece().getColour()=='W'){
            player_one = false;
            player_two = true;
        }

        if (cellTo.getPiece()!=null) {
            return false;
        }

        // Movement checks (either move one square diagonally or jump over two)
        if (!((Math.abs(deltaX) == 1 && Math.abs(deltaY) == 1) || (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 2))) {
            return false;
        }

        // Player one piece (b or B)
        if (player_one && (startPiece==b || startPiece==B)) {
            if (startPiece==b) {
                // b can only move forward (deltaX -1 for normal, -2 for capture)
                if (!(deltaX == -1 || deltaX == -2))
                    return false;
            }
            if (Math.abs(deltaX) == 2) {
                // Check for a piece to jump over
                if(midCheckersPiece!=null){
                    char midPiece = midCheckersPiece.getColour();
                    if (midPiece != b && midPiece != B && midPiece != w && midPiece != W) {
                        return false; // No piece to jump over
                    }
                    }   else {
                    return  false;
                }
            }
            return true; // Move is valid
        }
        // Player two piece (w or wk)
        if (player_two && (startPiece==w || startPiece==W)) {
            if (startPiece==w) {
                // w can only move forward (deltaX 1 for normal, 2 for capture)
                if (!(deltaX == 1 || deltaX == 2))
                    return false;
            }
            if (Math.abs(deltaX) == 2) {
                // Check for a piece to jump over
                if(midCheckersPiece!=null){
                    char midPiece = midCheckersPiece.getColour();
                    if (midPiece!=b && midPiece!=B && midPiece!=w && midPiece != W) {
                        return false; // No piece to jump over
                    }
                } else {
                    return false;
                }
            }
            return true; // Move is valid
        }
        return false; // If none of the conditions are met, the move is invalid
    }
}