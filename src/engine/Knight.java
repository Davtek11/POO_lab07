package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Knight extends ChessPiece {

  public Knight(PlayerColor color, int x, int y) {
    super(PieceType.KNIGHT, color, x, y);
  }

  @Override
  protected boolean move (int toX, int toY) {
    return (Math.abs(pos.x - toX) == 2 && Math.abs(pos.y - toY) == 1) || (Math.abs(pos.x - toX) == 1 && Math.abs(pos.y - toY) == 2);
  }

  @Override
  protected boolean canMove() {
    return true;
  }
}
