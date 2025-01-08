package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Pawn extends ChessPiece {

  public Pawn(PlayerColor color, int x, int y) {
    super(PieceType.PAWN, color, x, y);
  }

  @Override
  protected  boolean move(int toX, int toY) {
    boolean canMove;
    int direction = this.color == PlayerColor.BLACK ? 1 : -1;
    boolean isCapturing = Math.abs(pos.x - toX) == 1 && pos.y - toY == direction && board[toX][toY] != null
            && board[toX][toY].color != this.color; // this checks weither I can capture if I wanted it

    canMove = this.pos.y - toY == direction;

    if (isFirstMove()){
      canMove = canMove || this.pos.y - toY == 2 * direction;
    }
    canMove = canMove && this.pos.x == toX && piecesCheck(toX, toY);

      return canMove && board[toX][toY] == null || isCapturing;
  }

  protected boolean canMove(){
    return move(this.pos.x, this.pos.y + (color == PlayerColor.BLACK? -1 : 1))
            || move(this.pos.x - 1, this.pos.y + (color == PlayerColor.BLACK? -1 : 1))
            || move(this.pos.x + 1, this.pos.y + (color == PlayerColor.BLACK? -1 : 1));
  }
}
