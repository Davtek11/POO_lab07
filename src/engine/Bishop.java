package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Bishop extends ChessPiece {

  protected Bishop(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
  }

  public Bishop(PlayerColor color, int x, int y) {
    this(PieceType.BISHOP, color, x, y);

  }

  public boolean move (int toX, int toY) {
    return diagonal(toX, toY, true) && piecesCheck(toX, toY);
  }

  @Override
  protected boolean canMove() {
    return true;
  }
}
