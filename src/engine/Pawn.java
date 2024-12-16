package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Pawn extends ChessPiece {

  private boolean isFirstMove; //repetition avec roi, trouver solution
  
  protected Pawn(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
    isFirstMove = true;
  }

  public Pawn(PlayerColor color, int x, int y) {
    this(PieceType.PAWN, color, x, y);

    ///une case devant, deux cases devant si premier mouvement, diagonale pour manger
  }

  protected  boolean move(int toX, int toY) {
    boolean canMove;
    if (this.color == PlayerColor.BLACK){
      canMove = this.pos.y - toY == 1;
    } else {
      canMove = this.pos.y - toY == -1;
    }
    if (isFirstMove){
      if (this.color == PlayerColor.BLACK){
        canMove = canMove || this.pos.y - toY == 2;
      } else {
        canMove = canMove || this.pos.y - toY == -2;
      }
      isFirstMove = false;
    }
    return canMove && this.pos.x == toX;
  }
}
