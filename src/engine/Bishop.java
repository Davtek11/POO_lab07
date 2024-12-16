package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Bishop extends ChessPiece {

  protected Bishop(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
  }

  public Bishop(PlayerColor color, int x, int y) {
    this(PieceType.BISHOP, color, x, y);

    //Pour ajouter un mouvement possible: possibleMoves.add(new Coord(int, int));
    // vérifier si coordonnée valide (0-7)

    ///TODO: ajouter mouvements possibles
    ///diagonales
  }

  public boolean move (int toX, int toY) {
    return diagonal(toX, toY, true);
  }
  
}
