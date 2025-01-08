package chess;

public class PieceChoice implements ChessView.UserChoice {

  private final String textValue;
  private final PieceType pieceType;
  private final Class pieceClass;

  public PieceChoice(String text, PieceType type, Class cl) {
    textValue = text;
    pieceType = type;
    pieceClass = cl;
  }

  /*
   * Returns the name of the piece that can be chosen, which will be displayed to help the player choose correctly
   * @return the name of the piece
  */
  @Override
  public String textValue() {
    return textValue;
  }

  /*
   * Returns the type of the piece that can be chosen
   * @return the type of the piece
  */
  public PieceType getType() {
    return pieceType;
  }

  /*
   * Return the class that needs to be called to construct the right piece
   * @returns the class of the piece
  */
  public Class getPieceClass() {
    return pieceClass;
  }
  
}
