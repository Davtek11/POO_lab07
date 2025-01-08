package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Queen extends ChessPiece {

  protected Queen(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
  }

  public Queen(PlayerColor color, int x, int y) {
    this(PieceType.QUEEN, color, x, y);
  }

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
