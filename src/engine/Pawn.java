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
  }

  protected  boolean move(int toX, int toY) {
    boolean canMove;
    int direction = this.color == PlayerColor.BLACK ? 1 : -1;
    boolean isCapturing = Math.abs(pos.x - toX) == 1 && pos.y - toY == direction && board[toX][toY] != null
            && board[toX][toY].color != this.color; // this checks weither I can capture if I wanted it

    canMove = this.pos.y - toY == direction;

    if (isFirstMove){
      canMove = canMove || this.pos.y - toY == 2 * direction;
    }
    canMove = canMove && this.pos.x == toX && piecesCheck(toX, toY);

    if (canMove && board[toX][toY] == null || isCapturing) {
      isFirstMove = false;
      return true;
    }
    return false;
  }
}
