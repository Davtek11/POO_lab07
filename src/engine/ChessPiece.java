package engine;

import chess.Coord;
import chess.PieceType;
import chess.PlayerColor;

public abstract class ChessPiece {
  static public ChessPiece[][] board = new ChessPiece[Coord.BOARD_SIZE][Coord.BOARD_SIZE];
  protected final PieceType type;
  protected final PlayerColor color;
  protected Coord pos;

  boolean isFirstMove;

  /*
   * Indicates if the piece is making its first move of the game or not
   * @return a boolean indicating if the piece is doing its first move
  */
  public boolean isFirstMove() {
    return isFirstMove;
  }
  
  /*
   * Indicates that the piece is no longer making its first move of the game
  */
  public void makeFirstMove() {
    isFirstMove = false;
  }
  
  protected ChessPiece(PieceType type, PlayerColor color, int x, int y) {
    this.type = type;
    this.color = color;
    pos = new Coord(x, y);
    isFirstMove = true;
    if (board[x][y] == null) {
      board[x][y] = this;
    } else {
      System.out.println("ERROR : there is already a piece in this position:" + type);
    }
  }

  /*
   * Checks if the piece can reach the given destination
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @return a boolean indicating if the given movement is valid
  */
  protected abstract boolean move (int toX, int toY);

  /*
   * 
  */
  protected abstract boolean canMove();

  /*
   * Checks if the movement needed to reach the destination from the current position is diagonal
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @param multipleMove a boolean indicating if the piece can go further than one tile in the direction
   * @return a boolean indicating if the movement is valid
  */
  protected boolean diagonal(int toX, int toY, boolean multipleMove) {
    return Math.abs(pos.x - toX) == Math.abs(pos.y - toY) && multipleMove || Math.abs(pos.x - toX) == 1 && Math.abs(pos.y - toY) == 1;
  }

    /*
   * Checks if the movement needed to reach the destination from the current position is horizontal
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @param multipleMove a boolean indicating if the piece can go further than one tile in the direction
   * @return a boolean indicating if the movement is valid
  */
  protected boolean horizontal(int toX, int toY, boolean multipleMove) {
    return pos.y == toY && (multipleMove || Math.abs(pos.x - toX) == 1);
  }

    /*
   * Checks if the movement needed to reach the destination from the current position is vertical
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @param multipleMove a boolean indicating if the piece can go further than one tile in the direction
   * @return a boolean indicating if the movement is valid
  */
  protected boolean vertical(int toX, int toY, boolean multipleMove) {
    return pos.x == toX && (multipleMove || Math.abs(pos.y - toY) == 1);
  }

    /*
   * Checks if there are any pieces blocking the path to the piece's destination
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @return a boolean indicating if the path is clear
  */
  protected boolean piecesCheck(int toX, int toY) {
    int dx = Integer.compare(toX, pos.x);
    int dy = Integer.compare(toY, pos.y);

    int steps = Math.max(Math.abs(toX - pos.x), Math.abs(toY - pos.y)); // number of tiles to check

    for (int i = 1; i < steps; i++) {
      if (board[pos.x + i * dx][pos.y + i * dy] != null) {
        System.out.println("ERROR: you can't move here there is a piece between you and the spot");
        return false; // no need to continue once we know there is an obstacle
      }
    }
    return true;
  }
}
