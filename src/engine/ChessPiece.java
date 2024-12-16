package engine;

import chess.Coord;
import chess.PieceType;
import chess.PlayerColor;
import java.util.ArrayList;

public abstract class ChessPiece {
  static public ChessPiece[][] board = new ChessPiece[8][8];
  protected final PieceType type;
  protected final PlayerColor color;
  protected Coord pos;
  private ArrayList<Coord> possibleMoves;
  
  protected ChessPiece(PieceType type, PlayerColor color, int x, int y) {
    this.type = type;
    this.color = color;
    pos = new Coord(x, y);
    if (board[x][y] == null) {
      board[x][y] = this;
    } else {
      System.out.println("ERROR : adding a piece somewhere another pieces exists already:" + type);
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


}
