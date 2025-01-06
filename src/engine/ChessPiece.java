package engine;

import chess.Coord;
import chess.PieceType;
import chess.PlayerColor;

public abstract class ChessPiece {
  static public ChessPiece[][] board = new ChessPiece[8][8];
  protected final PieceType type;
  protected final PlayerColor color;
  protected Coord pos;

  boolean isFirstMove;

  public boolean isFirstMove() {
    return isFirstMove;
  }

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

  protected abstract boolean move (int toX, int toY);

  protected boolean diagonal(int toX, int toY, boolean multipleMove) {
    return Math.abs(pos.x - toX) == Math.abs(pos.y - toY) && multipleMove || Math.abs(pos.x - toX) == 1 && Math.abs(pos.y - toY) == 1 ;
  }

  protected boolean horizontal(int toX, int toY, boolean multipleMove) {
    System.out.println(toX + " " + toY);
    return pos.y == toY && (multipleMove || Math.abs(pos.x - toX) == 1);
  }

  protected boolean vertical(int toX, int toY, boolean multipleMove) {
    return pos.x == toX && (multipleMove || Math.abs(pos.y - toY) == 1);
  }

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
