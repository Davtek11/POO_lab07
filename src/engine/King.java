package engine;

import chess.PieceType;
import chess.PlayerColor;

public class King extends ChessPiece {
  
  public King(PlayerColor color, int x, int y) {
    super(PieceType.KING, color, x, y);
  }

  @Override
  protected boolean move (int toX, int toY) {
    return vertical(toX, toY, false) || horizontal(toX, toY, false) || diagonal(toX, toY, false);
  }

  @Override
  protected boolean canMove() {
    return false;
  }
}
