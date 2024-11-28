package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Queen extends ChessPiece {

  protected Queen(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
  }

  public Queen(PlayerColor color, int x, int y) {
    this(PieceType.QUEEN, color, x, y);

    //Pour ajouter un mouvement possible: possibleMoves.add(new Coord(int, int));
    // vérifier si coordonnée valide (0-7)

    ///TODO: ajouter mouvements possibles
    ///ligne, colonne, diagonales
  }
  
}
