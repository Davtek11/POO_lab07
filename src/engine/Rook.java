package engine;

import chess.PieceType;
import chess.PlayerColor;

public class Rook extends ChessPiece {

  protected Rook(PieceType type, PlayerColor color, int x, int y) {
    super(type, color, x, y);
  }

  public Rook(PlayerColor color, int x, int y) {
    this(PieceType.ROOK, color, x, y);

    //Pour ajouter un mouvement possible: possibleMoves.add(new Coord(int, int));
    // vérifier si coordonnée valide (0-7)

    ///TODO: ajouter mouvements possibles
    ///ligne et colonne
  }

  protected boolean move (int toX, int toY) {
    return horizontal(toX, toY, true) || vertical(toX, toY, true);
  }
}
