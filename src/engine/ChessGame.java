package engine;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;

import static engine.ChessPiece.board;

public class ChessGame implements ChessController {

  private ChessView view;
  PlayerColor colorTurn;

  @Override
  public void start(ChessView view) {
    this.view = view;
    view.startView();
  }

  public void toggleTurn() {
    if(colorTurn == PlayerColor.WHITE)
      colorTurn = PlayerColor.BLACK;
    else if(colorTurn == PlayerColor.BLACK)
      colorTurn = PlayerColor.WHITE;
  }

  @Override
  public boolean move(int fromX, int fromY, int toX, int toY) {

    if(board[fromX][fromY] == null) {
      return false;
    } else if(board[fromX][fromY].color != colorTurn) {
      System.out.println("Not your turn !!!!");
      return false;
    }

    System.out.printf("TO REMOVE : from (%d, %d) to (%d, %d)%n", fromX, fromY, toX, toY); // TODO remove

    boolean canMove = board[fromX][fromY].move(toX, toY);
    if (canMove) {
      if (board[toX][toY] != null){
        view.displayMessage("a "+ board[toX][toY].color + " " + board[toX][toY].type + " has been eaten");
      }
      view.removePiece(fromX, fromY);
      view.putPiece(board[fromX][fromY].type, board[fromX][fromY].color, toX, toY);
      board[toX][toY] = board[fromX][fromY];
      board[toX][toY].pos.x = toX;
      board[toX][toY].pos.y = toY;
      board[fromX][fromY] = null;
    } else {
      System.out.println("this piece can't move like this, learn how to play!!!!!");
      return false;
    }

    toggleTurn();
    return true;
  }

  @Override
  public void newGame() {
    view.displayMessage("new game (TO REMOVE)"); // TODO
    ///TODO: optimiser placement pieces

    colorTurn = PlayerColor.WHITE;
    
    // Placement de départ
    view.putPiece(PieceType.ROOK, PlayerColor.WHITE, 0, 0);
    new Rook(PlayerColor.WHITE, 0, 0);
    view.putPiece(PieceType.KNIGHT, PlayerColor.WHITE, 1, 0);
    new Knight(PlayerColor.WHITE, 1, 0);
    view.putPiece(PieceType.BISHOP, PlayerColor.WHITE, 2, 0);
    new Bishop(PlayerColor.WHITE, 2, 0);
    view.putPiece(PieceType.QUEEN, PlayerColor.WHITE, 3, 0);
    new Queen(PlayerColor.WHITE, 3, 0);
    view.putPiece(PieceType.KING, PlayerColor.WHITE, 4, 0);
    new King(PlayerColor.WHITE, 4, 0);
    view.putPiece(PieceType.BISHOP, PlayerColor.WHITE, 5, 0);
    new Bishop(PlayerColor.WHITE, 5, 0);
    view.putPiece(PieceType.KNIGHT, PlayerColor.WHITE, 6, 0);
    new Knight(PlayerColor.WHITE, 6, 0);
    view.putPiece(PieceType.ROOK, PlayerColor.WHITE, 7, 0);
    new Rook(PlayerColor.WHITE, 7, 0);

    for(int i = 0; i < 8; i++) {
      view.putPiece(PieceType.PAWN, PlayerColor.WHITE, i, 1);
      new Pawn(PlayerColor.WHITE, i, 1);
    }

    view.putPiece(PieceType.ROOK, PlayerColor.BLACK, 0, 7);
    new Rook(PlayerColor.BLACK, 0, 7);
    view.putPiece(PieceType.KNIGHT, PlayerColor.BLACK, 1, 7);
    new Knight(PlayerColor.BLACK, 1, 7);
    view.putPiece(PieceType.BISHOP, PlayerColor.BLACK, 2, 7);
    new Bishop(PlayerColor.BLACK, 2, 7);
    view.putPiece(PieceType.QUEEN, PlayerColor.BLACK, 3, 7);
    new Queen(PlayerColor.BLACK, 3, 7);
    view.putPiece(PieceType.KING, PlayerColor.BLACK, 4, 7);
    new King(PlayerColor.BLACK, 4, 7);
    view.putPiece(PieceType.BISHOP, PlayerColor.BLACK, 5, 7);
    new Bishop(PlayerColor.BLACK, 5, 7);
    view.putPiece(PieceType.KNIGHT, PlayerColor.BLACK, 6, 7);
    new Knight(PlayerColor.BLACK, 6, 7);
    view.putPiece(PieceType.ROOK, PlayerColor.BLACK, 7, 7);
    new Rook(PlayerColor.BLACK, 7, 7);

    for(int i = 0; i < 8; i++) {
      view.putPiece(PieceType.PAWN, PlayerColor.BLACK, i, 6);
      new Pawn(PlayerColor.BLACK, i, 6);
    }

  }
}
