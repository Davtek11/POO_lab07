package engine;

import chess.Coord;
import chess.PieceType;
import chess.PlayerColor;
import java.util.ArrayList;

public abstract class ChessPiece {
  static public ChessPiece[][] board = new ChessPiece[8][8];
  private final PieceType type;
  private final PlayerColor color;
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
}
