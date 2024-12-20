package engine;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;

public class ChessGame implements ChessController {

  private ChessView view;
  ///TODO: variable pour stocker à qui le tour de jouer

  @Override
  public void start(ChessView view) {
    this.view = view;
    view.startView();
  }

  @Override
  public boolean move(int fromX, int fromY, int toX, int toY) {
    System.out.println(String.format("TO REMOVE : from (%d, %d) to (%d, %d)", fromX, fromY, toX, toY)); // TODO remove
    //récupérer type et couleur de la piece en fromX et fromY
    //si toX, toY est une position valide ET si tour de la bonne couleur
    //view.removePiece(fromX, fromY);
    //view.putPiece(type, color, toX, toY);
    //return true
    //else return false
    return false; // TODO
  }

  @Override
  public void newGame() {
    view.displayMessage("new game (TO REMOVE)"); // TODO
    ///TODO: optimiser placement pieces
    
    // Placement de départ
    view.putPiece(PieceType.ROOK, PlayerColor.WHITE, 0, 0);
    view.putPiece(PieceType.KNIGHT, PlayerColor.WHITE, 1, 0);
    view.putPiece(PieceType.BISHOP, PlayerColor.WHITE, 2, 0);
    view.putPiece(PieceType.QUEEN, PlayerColor.WHITE, 3, 0);
    view.putPiece(PieceType.KING, PlayerColor.WHITE, 4, 0);
    view.putPiece(PieceType.BISHOP, PlayerColor.WHITE, 5, 0);
    view.putPiece(PieceType.KNIGHT, PlayerColor.WHITE, 6, 0);
    view.putPiece(PieceType.ROOK, PlayerColor.WHITE, 7, 0);

    for(int i = 0; i < 8; i++) {
      view.putPiece(PieceType.PAWN, PlayerColor.WHITE, i, 1);
    }

    view.putPiece(PieceType.ROOK, PlayerColor.BLACK, 0, 7);
    view.putPiece(PieceType.KNIGHT, PlayerColor.BLACK, 1, 7);
    view.putPiece(PieceType.BISHOP, PlayerColor.BLACK, 2, 7);
    view.putPiece(PieceType.QUEEN, PlayerColor.BLACK, 3, 7);
    view.putPiece(PieceType.KING, PlayerColor.BLACK, 4, 7);
    view.putPiece(PieceType.BISHOP, PlayerColor.BLACK, 5, 7);
    view.putPiece(PieceType.KNIGHT, PlayerColor.BLACK, 6, 7);
    view.putPiece(PieceType.ROOK, PlayerColor.BLACK, 7, 7);

    for(int i = 0; i < 8; i++) {
      view.putPiece(PieceType.PAWN, PlayerColor.BLACK, i, 6);
    }


  }
}
