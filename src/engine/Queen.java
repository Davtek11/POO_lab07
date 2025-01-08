package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Queen extends ChessPiece {

  public Queen(PlayerColor color, int x, int y) {
    super(PieceType.QUEEN, color, x, y);
  }

  /*
   * Checks if the piece can reach the given destination
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @return a boolean indicating if the given movement is valid
  */
  @Override
  protected boolean move (int toX, int toY) {
    return (diagonal(toX, toY, true) || vertical(toX, toY, true)
            || horizontal(toX, toY, true)) && piecesCheck(toX, toY)  ;
  }

  @Override
  protected boolean canMove() {
    return true;
  }
}
