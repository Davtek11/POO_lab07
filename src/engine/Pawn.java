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
    //Pour ajouter un mouvement possible: possibleMoves.add(new Coord(int, int));
    // vérifier si coordonnée valide (0-7)

    ///TODO: ajouter mouvements possibles
    ///une case devant, deux cases devant si premier mouvement, diagonale pour manger
    ///ATTENTION sens de mouvement dépend si blanc ou noir
  }
}