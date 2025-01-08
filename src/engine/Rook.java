package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Rook extends ChessPiece {

  public Rook(PlayerColor color, int x, int y) {
    super(PieceType.ROOK, color, x, y);
  }

  /*
   * Checks if the piece can reach the given destination
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @return a boolean indicating if the given movement is valid
  */
  @Override
  protected boolean move (int toX, int toY) {
    return (horizontal(toX, toY, true) || vertical(toX, toY, true)) && piecesCheck(toX, toY);
  }

  @Override
  protected boolean canMove() {
    return true;
  }
}
