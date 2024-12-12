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

    //Pour ajouter un mouvement possible: possibleMoves.add(new Coord(int, int));
    // vérifier si coordonnée valide (0-7)

    ///TODO: ajouter mouvements possibles
    ///une case autour
  }

  protected void move

}
