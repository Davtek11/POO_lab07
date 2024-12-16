package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Rook extends ChessPiece {

  protected Rook(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
  }

  public Rook(PlayerColor color, int x, int y) {
    this(PieceType.ROOK, color, x, y);
  }

  protected boolean move (int toX, int toY) {
    return piecesCheck(toX, toY) && (horizontal(toX, toY, true) || vertical(toX, toY, true));
  }
}
