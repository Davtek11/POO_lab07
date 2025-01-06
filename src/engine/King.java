package engine;

import chess.PieceType;
import chess.PlayerColor;

public class King extends ChessPiece {

  private boolean isFirstMove; //répétition avec pion, trouver solution
  
  protected King(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
    isFirstMove = true;
  }

  public King(PlayerColor color, int x, int y) {
    this(PieceType.KING, color, x, y);
  }

  @Override
  protected boolean move (int toX, int toY) {
    return vertical(toX, toY, false) || horizontal(toX, toY, false) || diagonal(toX, toY, false);
  }

}
