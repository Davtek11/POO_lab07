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

  @Override
  public String textValue() {
    return textValue;
  }

  public PieceType getType() {
    return pieceType;
  }

  public Class getPieceClass() {
    return pieceClass;
  }
  
}
