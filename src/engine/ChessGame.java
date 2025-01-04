package engine;

import chess.ChessController;
import chess.ChessView;
import chess.Coord;
import chess.PieceChoice;
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

  // 0 = nothing, -1 = petit roque, 1 = grand roque
  public int checkRoque(int fromX, int fromY, int toX, int toY) {
    int ret = 0;
    if(board[fromX][fromY].isFirstMove() && board[fromX][fromY].type == PieceType.KING && fromY == toY) {
      int rookX = -1;
      if(toX == fromX + 2) {
        // Petit roque
        rookX = toX + 1;
        ret = -1;
      } else if(toX == fromX - 2) {
        // Grand roque
        rookX = toX - 2;
        ret = 1;
      } else {
        return 0;
      }

      if(board[fromX][fromY].piecesCheck(rookX, toY) && board[rookX][toY] != null && board[rookX][toY].type == PieceType.ROOK && board[rookX][toY].isFirstMove()) {
        return ret;
      }
    }
    return 0;
  }

  public void roque(int x, int y, int roqueType) {

    int toX = 0, rookX = 0, rookToX = 0;

    if(roqueType == -1) {
      // Petit roque
      toX = x + 2;
      rookX = toX + 1;
      rookToX = x + 1;
      
    } else if(roqueType == 1) {
      // Grand roque
      toX = x - 2;
      rookX = toX - 2;
      rookToX = x - 1;
    }

    view.displayMessage("Roque by " + board[x][y].color + " player");

    // Move king
    view.removePiece(x, y);
    view.putPiece(PieceType.KING, board[x][y].color, toX, y);
    board[toX][y] = board[x][y];
    board[toX][y].pos.x = toX;
    board[toX][y].pos.y = y;
    board[x][y] = null;

    // Move rook
    view.removePiece(rookX, y);
    view.putPiece(PieceType.ROOK, board[rookX][y].color, rookToX, y);
    board[rookToX][y] = board[rookX][y];
    board[rookToX][y].pos.x = rookToX;
    board[rookToX][y].pos.y = y;
    board[rookX][y] = null;
  }

  public boolean checkPromotion(int toX, int toY) {
    return board[toX][toY].type == PieceType.PAWN &&
    (board[toX][toY].color == PlayerColor.BLACK && toY == 0 ||
    board[toX][toY].color == PlayerColor.WHITE && toY == Coord.BOARD_SIZE - 1);
  }

  public void promotion(int x, int y) {

    try {
      PieceChoice[] choices = {new PieceChoice("reine", PieceType.QUEEN, Queen.class),
      new PieceChoice("cavalier", PieceType.KNIGHT, Knight.class),
      new PieceChoice("fou", PieceType.BISHOP, Bishop.class),
      new PieceChoice("tour", PieceType.ROOK, Rook.class)};

      ChessView.UserChoice c = view.askUser("promotion", "par quelle pièce voulez-vous remplacer votre pion", choices);
      view.removePiece(x, y);

      int i;
      for(i = 0; i < choices.length; i++) {
        if(c.textValue().equalsIgnoreCase(choices[i].textValue())) {
          break;
        }
      }

      if(i < choices.length) {
        view.putPiece(choices[i].getType(), colorTurn, x, y);
        board[x][y] = (ChessPiece) choices[i].getPieceClass().getDeclaredConstructor(PlayerColor.class, int.class, int.class).newInstance(board[x][y].color, x, y);
      }
        
    } catch (Exception e) {
      System.out.println("Promotion error : " + e);
    }
    
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

    // Roque if possible, else check other movements
    int roqueType = 0;
    if((roqueType = checkRoque(fromX, fromY, toX, toY)) != 0)
    {
      roque(fromX, fromY, roqueType);
    } 
    else if (board[fromX][fromY].move(toX, toY))
    {
      if (board[toX][toY] != null) {
        if(board[toX][toY].color == board[fromX][fromY].color) {
          System.out.println("this position is not empty.");
          return false;
        } else {
          view.displayMessage("a "+ board[toX][toY].color + " " + board[toX][toY].type + " has been eaten");
        }
      }
      view.removePiece(fromX, fromY);
      view.putPiece(board[fromX][fromY].type, board[fromX][fromY].color, toX, toY);
      board[toX][toY] = board[fromX][fromY];
      board[toX][toY].pos.x = toX;
      board[toX][toY].pos.y = toY;
      board[fromX][fromY] = null;

      // Promotion
      if(checkPromotion(toX, toY)) {

        System.out.println("promotion");
        promotion(toX, toY);

      }

    } else {
      System.out.println("this piece can't move like this, learn how to play!!!!!");
      return false;
    }

    if(board[toX][toY].isFirstMove()) {
      board[toX][toY].makeFirstMove();
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
