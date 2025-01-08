package engine;

import chess.PieceType;
import chess.PlayerColor;

public class King extends ChessPiece {
  
  public King(PlayerColor color, int x, int y) {
    super(PieceType.KING, color, x, y);
  }

  /*
   * Checks if the piece can reach the given destination
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @return a boolean indicating if the given movement is valid
  */
  @Override
  protected boolean move (int toX, int toY) {
    return vertical(toX, toY, false) || horizontal(toX, toY, false) || diagonal(toX, toY, false);
  }

  @Override
  protected boolean canMove() {
    return false;
  }
}
