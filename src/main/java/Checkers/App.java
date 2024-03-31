package Checkers;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 60;
    public static final int SIDEBAR = 0;
    public static final int BOARD_WIDTH = 8;
    public static final int[] BLACK_RGB = {181, 136, 99};
    public static final int[] WHITE_RGB = {240, 217, 181};
    public static final float[][][] coloursRGB = new float[][][] {
            //default - white & black
            {
                    {WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]},
                    {BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]}
            },
            //green
            {
                    {105, 138, 76}, //when on white cell
                    {105, 138, 76} //when on black cell
            },
            //blue
            {
                    {196,224,232},
                    {170,210,221}
            }
    };

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    public static final int FPS = 60;

    /* --------------------------------------- */
    // DATA STORAGE
    /* --------------------------------------- */
    private Cell[][] board;
    private CheckersPiece currentSelected;
    private HashSet<Cell> selectedCells;
    private Set<Cell> availableMoves;
    private HashMap<Character, HashSet<CheckersPiece>> piecesInPlay = new HashMap<>();
    public char currentPlayer = 'w';

    /* --------------------------------------- */
    /* --------------------------------------- */

    public App() {

    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(FPS);

        //Set up the data structures used for storing data in the game
        this.board = new Cell[BOARD_WIDTH][BOARD_WIDTH];
        HashSet<CheckersPiece> w = new HashSet<>();
        HashSet<CheckersPiece> b = new HashSet<>();
        piecesInPlay.put('w', w);
        piecesInPlay.put('b', b);

        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                board[i][i2] = new Cell(i2,i);

                if ((i2+i) % 2 == 1) {
                    if (i < 3) {
                        //white piece
                        board[i][i2].setPiece(new CheckersPiece('w'));
                        w.add(board[i][i2].getPiece());
                    } else if (i >= 5) {
                        //black piece
                        board[i][i2].setPiece(new CheckersPiece('b'));
                        b.add(board[i][i2].getPiece());
                    }
                }
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed(){

    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased(){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Check if the user clicked on a piece which is theirs - make sure only whoever current turn it is, can click on pieces
        int x = e.getX();
        int y = e.getY();
        if (x < 0 || x >= App.WIDTH || y < 0 || y >= App.HEIGHT) return;

        Cell clicked = board[y/App.CELLSIZE][x/App.CELLSIZE];

        if (clicked.getPiece() != null && Character.toLowerCase(clicked.getPiece().getColour()) == currentPlayer) {
            //valid piece to click
            if (clicked.getPiece() == currentSelected) {
                currentSelected = null;
            } else {
                currentSelected = clicked.getPiece();
                availableMoves = currentSelected.getAvailableMoves(this.board);
            }
            //TODO: highlight available moves for current piece - done
        } else if (clicked.getPiece()==null && currentSelected!=null){
            if (availableMoves.contains(clicked)){

                Move movementOfCheckersPiece = new Move(currentSelected.getPosition(), this.board);
                CheckersPiece capturedPiece = movementOfCheckersPiece.processMove(currentSelected.getPosition(),clicked);

                if (capturedPiece !=null && currentPlayer=='w'){
                    piecesInPlay.get('b').remove(capturedPiece);
                    capturedPiece.getPosition().setPiece(null);
                } else if (capturedPiece !=null && currentPlayer=='b') {
                    piecesInPlay.get('w').remove(capturedPiece);
                    capturedPiece.getPosition().setPiece(null);
                }


                availableMoves.clear();
                currentSelected = null;
                if(currentPlayer=='b') {
                    currentPlayer='w';
                } else if (currentPlayer=='w'){
                    currentPlayer ='b';
                }
            }
            }

        //TODO: Check if user clicked on an available move - move the selected piece there - done
        //TODO: Remove captured pieces from the board - done
        //TODO: Check if piece should be promoted and promote it - done
        //TODO: Then it's the other player's turn. - done
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        this.noStroke();
        background(WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]);
        //draw the board
        for (int i = 0; i < board.length; i++) {
            for (int i2 = 0; i2 < board[i].length; i2++) {
                //if cell is selected, highlight in green
                //TODO: draw highlighted cells

                if (currentSelected != null && board[i][i2].getPiece() == currentSelected) {
                    this.setFill(1, (i2 + i) % 2);
                    this.rect(i2 * App.CELLSIZE, i * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                } else if ((i2 + i) % 2 == 1) {
                    //black cell
                    this.fill(BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]);
                    this.rect(i2 * App.CELLSIZE, i * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                }

                if (currentSelected != null && availableMoves!=null){
                    if(availableMoves.contains(board[i][i2])){
                        this.setFill(2, (i2 + i) % 2);
                        this.rect(i2 * App.CELLSIZE, i * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                    }

                }
                board[i][i2].draw(this); //draw the piec3

            }
        }

//        text("Black wins!", App.WIDTH*0.2f, App.HEIGHT*0.4f);
//        check if the any player has no more pieces. The winner is the player who still has pieces remaining

        if (piecesInPlay.get('w').isEmpty() || piecesInPlay.get('b').isEmpty()) {
            fill(255);
            stroke(0);
            strokeWeight(4.0f);
            rect(App.WIDTH*0.2f-5, App.HEIGHT*0.4f-25, 150, 40);
            fill(200,0,200);
            textSize(24.0f);
            if (piecesInPlay.get('w').isEmpty()) {
                text("Black wins!", App.WIDTH*0.2f, App.HEIGHT*0.4f);
            } else if (piecesInPlay.get('b').isEmpty()) {
                text("White wins!", App.WIDTH*0.2f, App.HEIGHT*0.4f);
            }
        }
    }

    /**
     * Set fill colour for cell background
     * @param colourCode The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may have different shades
     */
    public void setFill(int colourCode, int blackOrWhite) {
        this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
    }

    public static void main(String[] args) {
        PApplet.main("Checkers.App");
    }

}
