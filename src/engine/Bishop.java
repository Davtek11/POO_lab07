package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Bishop extends ChessPiece {

  public Bishop(PlayerColor color, int x, int y) {
    super(PieceType.BISHOP, color, x, y);
  }

  @Override
  public boolean move (int toX, int toY) {
    return diagonal(toX, toY, true) && piecesCheck(toX, toY);
  }
  
  @Override
  protected boolean canMove() {
    return true;
  }
}
