package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Knight extends ChessPiece {

  protected Knight(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
  }

  public Knight(PlayerColor color, int x, int y) {
    this(PieceType.KNIGHT, color, x, y);

    //Pour ajouter un mouvement possible: possibleMoves.add(new Coord(int, int));
    // vérifier si coordonnée valide (0-7)

    ///TODO: ajouter mouvements possibles
    ///en L
  }
  
}