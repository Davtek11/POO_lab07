package engine;

import chess.Coord;
import chess.PieceType;
import chess.PlayerColor;
import java.util.ArrayList;

public abstract class ChessPiece {
  private final PieceType type;
  private final PlayerColor color;
  private Coord pos;
  private ArrayList<Coord> possibleMoves;
  
  protected ChessPiece(PieceType type, PlayerColor color, int x, int y) {
    this.type = type;
    this.color = color;
    pos = new Coord(x, y);
  }
}
