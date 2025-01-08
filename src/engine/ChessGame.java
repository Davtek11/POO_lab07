package engine;

import chess.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static engine.ChessPiece.board;

public class ChessGame implements ChessController {

  private ChessView view;
  private PlayerColor colorTurn; // Current color turn
  private Coord pieceEnPassant; // Position of the pawn which can be the victim of the "en passant" move
  private Coord whiteKing; // Position of the white king
  private Coord blackKing; // Position of the blackKing
  private boolean end; // Indicates if the game is finished

  /*
   * Function used to start the game view
   * @param view the view to be started
  */
  @Override
  public void start(ChessView view) {
    this.view = view;
    view.startView();
  }

  /*
   * Checks if a castling (roque) is possible in the current conditions
   * @param fromX the current x coordinate of the moving piece
   * @param fromY the current y coordinate of the moving piece
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @return an int indicating the result: 0 = no castling, -1 = short castling (petit roque), 1 = long castling (grand roque)
  */
  public int checkRoque(int fromX, int fromY, int toX, int toY) {
    int ret;
    // If the moving piece is a king, is doing its first move, and is moving horizontaly
    if(board[fromX][fromY].isFirstMove() && board[fromX][fromY].type == PieceType.KING && fromY == toY) {
      int rookX;
      // Petit roque
      if(toX == fromX + 2) {
        rookX = toX + 1;
        ret = -1;
      } // Grand roque
      else if(toX == fromX - 2) {
        rookX = toX - 2;
        ret = 1;
      } // Neither
      else {
        return 0;
      }

      // 
      for(int i = 0; i < 2; i++) {
        if(stillEchec(fromX, fromY, fromX - ( 1 + i) * ret , toY)) {
          return 0;
        }
      }

      // If the other piece is a rook, and is doing its first move
      if(board[fromX][fromY].piecesCheck(rookX, toY) && board[rookX][toY] != null && board[rookX][toY].type == PieceType.ROOK && board[rookX][toY].isFirstMove()) {
        return ret;
      }
    }
    return 0;
  }

  /*
   * Moves the king and the rook following the rule of the castling (roque) move
   * @param x the x coordinate of the king
   * @param y the y coordinate of the king
   * @param roqueType the type of castling: -1 = short castling (petit roque), 1 = long castling (grand roque)
  */
  public void roque(int x, int y, int roqueType) {

    // Destination x of the king, starting x of the rook, destination x of the rook
    // the castling does not change the y coordinate
    int toX = 0, rookX = 0, rookToX = 0;

    // Petit roque
    if(roqueType == -1) {  
      toX = x + 2;
      rookX = toX + 1;
      rookToX = x + 1;
      
    } // Grand roque
    else if(roqueType == 1) {
      toX = x - 2;
      rookX = toX - 2;
      rookToX = x - 1;
    }

    view.displayMessage("Roque by " + board[x][y].color + " player");

    // Move the king
    movePiece(x, y, toX, y);

    // Move the rook
    movePiece(rookX, y, rookToX, y);
  }

  /*
   * Checks if a promotion is possible in the current conditions
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @return a boolean indicating if the promotion is possible
  */
  public boolean checkPromotion(int toX, int toY) {
    return board[toX][toY].type == PieceType.PAWN && // Moving piece is a pawn
    (board[toX][toY].color == PlayerColor.BLACK && toY == 0 || // Pawn has reached the other side of the board
    board[toX][toY].color == PlayerColor.WHITE && toY == Coord.BOARD_SIZE - 1);
  }

  /*
   * Asks the player which piece they would like their pawn to be promoted into, and promotes the piece
   * @param x the x coordinate of the piece being promoted
   * @param y the y coordinate of the piece being promoted
  */
  public void promotion(int x, int y) {

    try {
      PieceChoice[] choices = {new PieceChoice("reine", PieceType.QUEEN, Queen.class),
      new PieceChoice("cavalier", PieceType.KNIGHT, Knight.class),
      new PieceChoice("fou", PieceType.BISHOP, Bishop.class),
      new PieceChoice("tour", PieceType.ROOK, Rook.class)};

      ChessView.UserChoice c = view.askUser("promotion", "par quelle pièce voulez-vous remplacer votre pion", choices);
      
      view.removePiece(x, y);

      // Get the right choice
      int i;
      for(i = 0; i < choices.length; i++) {
        if(c.textValue().equalsIgnoreCase(choices[i].textValue())) {
          break;
        }
      }

      // Replace the pawn with the chosen piece
      if(i < choices.length) {
        view.putPiece(choices[i].getType(), colorTurn, x, y);
        board[x][y] = (ChessPiece) choices[i].getPieceClass().getDeclaredConstructor(PlayerColor.class, int.class, int.class).newInstance(board[x][y].color, x, y);
      }
        
    } catch (Exception e) {
      System.out.println("Promotion error : " + e);
    }
  }

  /*
   * Checks if an "en passant" is possible in the current conditions
   * @param fromX the current x coordinate of the moving piece
   * @param fromY the current y coordinate of the moving piece
   * @param toX the x coordinate of the destination
   * @param toY the y coordinate of the destination
   * @return a boolean indicating if the "en passant" is possible
  */
  public boolean checkEnPassant(int fromX, int fromY, int toX, int toY) {
    return board[fromX][fromY].type == PieceType.PAWN && // Moving piece is a pawn
    Math.abs(toX - fromX) == 1 && Math.abs(toY - fromY) == 1 && // Pawn is moving diagonaly
    board[toX][fromY] != null && board[toX][fromY].type == PieceType.PAWN // Other piece is a pawn
    && board[toX][fromY].color != board[fromX][fromY].color && // Other piece is of opponent's color
    toX == pieceEnPassant.x && fromY == pieceEnPassant.y; // En passant is possible on that piece
  }

  /*
   * Moves the pieces following the rules of the "en passant" move
   * @param fromX the current x coordinate of the piece
   * @param fromY the current y coordinate of the piece
   * @param toX the x coordinate of the destination of the piece
   * @param toY the y coordinate of the destination of the piece
  */
  public void enPassant(int fromX, int fromY, int toX, int toY) {
    view.displayMessage("en passant by " + board[fromX][fromY].color + " player");

    // Eat the other pawn
    view.removePiece(toX, fromY);
    board[toX][fromY] = null;

    // Move the moving pawn
    movePiece(fromX, fromY, toX, toY);
  }

  /*
   * Toggles the game turn from white to black, or from black to white
  */
  public void toggleTurn() {
    if(colorTurn == PlayerColor.WHITE)
      colorTurn = PlayerColor.BLACK;
    else if(colorTurn == PlayerColor.BLACK)
      colorTurn = PlayerColor.WHITE;
  }

  /*
   * Moves the piece in the board array and in the view
   * @param fromX the current x coordinate of the piece
   * @param fromY the current y coordinate of the piece
   * @param toX the x coordinate of the destination of the piece
   * @param toY the y coordinate of the destination of the piece
  */
  public void movePiece(int fromX, int fromY, int toX, int toY) {

    // Keep the current positions of the kings
    if(board[fromX][fromY].type == PieceType.KING) {
      if(board[fromX][fromY].color == PlayerColor.BLACK) {
        blackKing = new Coord(toX, toY);
      } else {
        whiteKing = new Coord(toX, toY);
      }
    }

    view.removePiece(fromX, fromY);
    view.putPiece(board[fromX][fromY].type, board[fromX][fromY].color, toX, toY);
    board[toX][toY] = board[fromX][fromY];
    board[toX][toY].pos.x = toX;
    board[toX][toY].pos.y = toY;
    board[fromX][fromY] = null;
  }

  /*
   * Checks if a knight is threatening the current position
   * @param base the coordinates of the current position
   * @return a boolean indicating if a king is threatening the position
  */
  private boolean knightCheck(Coord base) {
    int[][] moves = {
            {1, 2}, {-1, 2}, {2, -1}, {2, 1},
            {-1, -2}, {1, -2}, {-2, -1}, {-2, 1}
    };

    for(int[] move : moves) {
      int newX = base.x + move[0];
      int newY = base.y + move[1];

      if(isInsideBoard(newX, newY) &&
              board[newX][newY] != null &&
              board[newX][newY].type == PieceType.KNIGHT &&
              board[newX][newY].color != colorTurn) {
        return true;
      }
    }
    return false;
  }

  /*
   * Checks if the given coordinates do not go over the limits of the game board
   * @param x the x coordinate to check
   * @param y the y coordinate to check
   * @return a boolean indicating if the coordinates are valid
  */
  private boolean isInsideBoard(int x, int y) {
    return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
  }


  /*
   * Checks if the game contains a check, a checkmate, or a pat
   * Indicates if the game has to end
  */
  private void echecCheck() {
    Coord kingsCoord = colorTurn == PlayerColor.BLACK ? blackKing : whiteKing;
    boolean echec = false;
    boolean mat;
    boolean canNegate = false;
    boolean pat;

    if(isInEchec(kingsCoord)) {
      view.displayMessage("Player " + colorTurn + " is in echec");
      echec = true;
    }

    mat = isSurrounded(kingsCoord);

    if(howMuchThreat(kingsCoord)) {
      canNegate = canNegateThreat(kingsCoord);
    }

    if(echec && mat && !canNegate) {
      view.displayMessage("Player " + colorTurn + " is in echec et mat he lost");
      end = true;

    }

    pat = !canAnyPieceMove(colorTurn);


    if(!echec && pat) {
      view.displayMessage("Player " + colorTurn + " is in pat, it is a tie");
      end = true;
    }
  }

  /*
   * Checks if a king is currently attacked in the given position
   * @param kingsCoord the coordinates of the king
   * @return a boolean indicating if the king is attacked
  */
  private boolean isInEchec(Coord kingsCoord) {
    for(int i = -1; i <2; i++) {
      for(int j = -1; j <2; j++) {
        if(i != 0 || j != 0) {
          if(checkPotentialThreat(new Coord(i, j),kingsCoord,colorTurn)) {
            return true;
          }
        }
      }
    }
    return knightCheck(kingsCoord);
  }

  /*
   * Checks if a king still has legal moves available
   * @param kingsCoord the coordinates of the king
   * @return a boolean indicating if the king can still move
  */
  private boolean isSurrounded(Coord kingsCoord) {
    for(int i = -1; i <= 1; i++) {
      for(int j = -1; j <= 1; j++) {
        if(i != 0 || j != 0) {
          Coord tile = new Coord(kingsCoord.x + i, kingsCoord.y + j);
          if(isInsideBoard(tile.x, tile.y) &&
                  (board[tile.x][tile.y] == null || board[tile.x][tile.y].color != colorTurn)) {
            if(!isInEchec(tile) && !knightCheck(tile)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  /*
   * Checks if any piece in the board still has legal moves available
   * @param color the color of the piece
   * @return a boolean indicating if a piece can still move
  */
  private boolean canAnyPieceMove (PlayerColor color) {
    for(int x = 0; x < Coord.BOARD_SIZE; x++) {
      for(int y = 0; y < Coord.BOARD_SIZE; y++) {
        if(board[x][y] != null && board[x][y].color == color) {
          if(board[x][y].type == PieceType.KING) {
            if(!isSurrounded(new Coord(x, y))) {
              return true;
            }

          } else {
            if(board[x][y].canMove()) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /*
   * Checks if the tile the piece is trying to reach is threatened by any piece of the opponent
   * @param add the coordinates to add to the current coordinates to reach the destination
   * @param baseCoord the current coordinates of the moving piece
   * @param color the color of the moving piece
   * @return a boolean indicating if the tile is being threatened
  */
  private boolean checkPotentialThreat(Coord add, Coord baseCoord, PlayerColor color) {
    Coord tile = new Coord(baseCoord.x, baseCoord.y);
    for(int i = 0; i < Coord.BOARD_SIZE - Math.max(Math.abs(add.x), Math.abs(add.y)); i++ ) {
      tile.x += add.x;
      tile.y += add.y;

      if(!isInsideBoard(tile.x, tile.y)) {
        break;
      }

      if(board[tile.x][tile.y] != null) {
        if(board[tile.x][tile.y].color != color) {
          if(board[tile.x][tile.y].move(baseCoord.x, baseCoord.y)) {
            return true;
          }
        }
        break;
      }
    }
    return false;
  }

  /*
   * Checks if at least one piece is threatening the current position of a king
   * @param kingsCoord the coordinates of the king
   * @return a boolean indicating if at least one piece is threatening the king
  */
  private boolean howMuchThreat (Coord kingsCoord) {
    int count = 0;
    for(int x = 0; x < Coord.BOARD_SIZE; x++) {
      for(int y = 0; y < Coord.BOARD_SIZE; y++) {
        if(board[x][y] != null && board[x][y].color != colorTurn) {
          if(board[x][y].move(kingsCoord.x, kingsCoord.y)) {
            count++;
          }
        }
      }
    }
    return count <= 1;
  }

  /*
   * Gets the path of the piece which is attacking the king
   * @param kingsCoord the coordinates of the king
   * @param thread the coordinates of the piece threatening the king
   * @return a list of coordinates drawing the path of the piece
  */
  private List<Coord> getThreatPath(Coord kingsCoord, Coord threat) {
    List<Coord> path = new ArrayList<>();
    int dx = Integer.compare(threat.x, kingsCoord.x);
    int dy = Integer.compare(threat.y, kingsCoord.y);

    int steps = Math.max(Math.abs(threat.x - kingsCoord.x), Math.abs(threat.y - kingsCoord.y));
    for(int i = 1; i < steps; i++) {
      path.add(new Coord(kingsCoord.x + i * dx, kingsCoord.y + i * dy));
    }

    return path;
  }

  /*
   * Checks if the king that is checked can recover during his next turn otherwise he is checkmate
   * @param kingsCoord the current coordinates of the king that is checked
   * @return a boolean indicating if he can get out of the check situation
  */
  private boolean canNegateThreat(Coord kingsCoord) {
    ChessPiece threat = null;
    List<Coord> threatenedTiles = new ArrayList<>();

    // 
    for(int x = 0; x < Coord.BOARD_SIZE; x++) {
      for(int y = 0; y < Coord.BOARD_SIZE; y++) {
        ChessPiece piece = board[x][y];
        if(piece != null && piece.color != colorTurn) {
          if(piece.move(kingsCoord.x, kingsCoord.y)) {
            threat = piece;

            threatenedTiles.add(new Coord(x,y));
            if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN || piece.type == PieceType.ROOK) {
              threatenedTiles.addAll(getThreatPath(kingsCoord, threat.pos));
            }
          }
        }
      }
    }

    if(threat != null) {
      for(int x = 0; x < Coord.BOARD_SIZE; x++) {
        for(int y = 0; y < Coord.BOARD_SIZE; y++) {
          ChessPiece piece = board[x][y];
          if(piece != null && piece.color == colorTurn && piece.type != PieceType.KING) {
            for(Coord tile : threatenedTiles) {
              if(piece.move(tile.x, tile.y)) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /*
   * Checks if the movement would resolve the king's check
   * @param fromX the current x coordinate of the piece
   * @param fromY the current y coordinate of the piece
   * @param toX the x coordinate of the destination of the piece
   * @param toY the y coordinate of the destination of the piece
   * @return a boolean indicating is the king would still be attacked after the move
  */
  private boolean stillEchec(int fromX, int fromY, int toX, int toY) {

    ChessPiece[][] copyBoard = Arrays.copyOf(board, board.length);
    for(int i = 0; i < board.length; i++) {
      copyBoard[i] = Arrays.copyOf(board[i], board[i].length);
    }

    ChessPiece fromPiece = copyBoard[fromX][fromY];

    boolean still = false;
    // Try to move the piece
    fromPiece.pos.x = toX;
    fromPiece.pos.y = toY;
    copyBoard[toX][toY] = fromPiece;
    copyBoard[fromX][fromY] = null;

    // Check if the king is still attacked in the new configuration
    Coord kingsCoord = colorTurn == PlayerColor.BLACK ? blackKing : whiteKing;
    if(fromPiece.type == PieceType.KING) {
      kingsCoord = new Coord(toX, toY);
    }
    if(isInEchec(kingsCoord)) {
      view.displayMessage("you cannot do that");
      still =  true;
    }

    return still;
  }

  /*
   * Checks the move the current player wants to do, executes it if possible and returns whether the move was valid
   * @param fromX the current x coordinate of the piece
   * @param fromY the current y coordinate of the piece
   * @param toX the x coordinate of the destination of the piece
   * @param toY the y coordinate of the destination of the piece
  */
  @Override
  public boolean move(int fromX, int fromY, int toX, int toY) {
    if(end) {
      view.displayMessage("the game has ended, you cannot continue");
      return false;
    }

    // Check if 
    if(board[fromX][fromY] == null) {
      return false;
    } else if(board[fromX][fromY].color != colorTurn) {
      view.displayMessage("Not your turn");
      return false;
    }

    // Check all possible movements
    int roqueType = 0;
    if((roqueType = checkRoque(fromX, fromY, toX, toY)) != 0)
    {
      roque(fromX, fromY, roqueType);
    }
    else if(checkEnPassant(fromX, fromY, toX, toY))
    {
      enPassant(fromX, fromY, toX, toY);
    } 
    else if(board[fromX][fromY].move(toX, toY))
    {
      // If there is a piece on the destination tile
      if(board[toX][toY] != null) {
        if(board[toX][toY].color == board[fromX][fromY].color) {
          view.displayMessage("this position is not empty");
          return false;
        } else {
          view.displayMessage("a "+ board[toX][toY].color + " " + board[toX][toY].type + " has been eaten");
        }
      }

      if(stillEchec(fromX, fromY, toX, toY)) {
        return false;
      }

      // Do the movement
      movePiece(fromX, fromY, toX, toY);

      // Check if "en passant" is possible (= pawn made its first move, and moved two tiles)
      if(board[toX][toY].type == PieceType.PAWN && Math.abs(toY - fromY) == 2) {
        pieceEnPassant = new Coord(toX, toY);
      } else {
        pieceEnPassant = new Coord(-1, -1);
      }

      // Promotion
      if(checkPromotion(toX, toY)) {
        view.displayMessage("promotion");
        promotion(toX, toY);
      }

    } else {
      view.displayMessage("invalid movement");
      return false;
    }

    if(board[toX][toY].isFirstMove()) {
      board[toX][toY].makeFirstMove();
    }

    toggleTurn();
    echecCheck();
    return true;
  }

  /*
   * Sets up a new game
  */
  @Override
  public void newGame() {
    end = false;
    board = new ChessPiece[Coord.BOARD_SIZE][Coord.BOARD_SIZE];

    ChessPiece.board = new ChessPiece[Coord.BOARD_SIZE][Coord.BOARD_SIZE];

    colorTurn = PlayerColor.WHITE;

    pieceEnPassant = new Coord(-1, -1);
    
    // Starting placement
    view.putPiece(PieceType.ROOK, PlayerColor.WHITE, 0, 0);
    new Rook(PlayerColor.WHITE, 0, 0);
    view.putPiece(PieceType.KNIGHT, PlayerColor.WHITE, 1, 0);
    new Knight(PlayerColor.WHITE, 1, 0);
    view.putPiece(PieceType.BISHOP, PlayerColor.WHITE, 2, 0);
    new Bishop(PlayerColor.WHITE, 2, 0);
    view.putPiece(PieceType.QUEEN, PlayerColor.WHITE, 3, 0);
    new Queen(PlayerColor.WHITE, 3, 0);
    view.putPiece(PieceType.KING, PlayerColor.WHITE, 4, 0);
    whiteKing = new Coord(4, 0);
    new King(PlayerColor.WHITE, 4, 0);
    view.putPiece(PieceType.BISHOP, PlayerColor.WHITE, 5, 0);
    new Bishop(PlayerColor.WHITE, 5, 0);
    view.putPiece(PieceType.KNIGHT, PlayerColor.WHITE, 6, 0);
    new Knight(PlayerColor.WHITE, 6, 0);
    view.putPiece(PieceType.ROOK, PlayerColor.WHITE, 7, 0);
    new Rook(PlayerColor.WHITE, 7, 0);

    for(int i = 0; i < Coord.BOARD_SIZE; i++) {
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
    blackKing = new Coord(4, 7);
    new King(PlayerColor.BLACK, 4, 7);
    view.putPiece(PieceType.BISHOP, PlayerColor.BLACK, 5, 7);
    new Bishop(PlayerColor.BLACK, 5, 7);
    view.putPiece(PieceType.KNIGHT, PlayerColor.BLACK, 6, 7);
    new Knight(PlayerColor.BLACK, 6, 7);
    view.putPiece(PieceType.ROOK, PlayerColor.BLACK, 7, 7);
    new Rook(PlayerColor.BLACK, 7, 7);

    for(int i = 0; i < Coord.BOARD_SIZE; i++) {
      view.putPiece(PieceType.PAWN, PlayerColor.BLACK, i, 6);
      new Pawn(PlayerColor.BLACK, i, 6);
    }

  }
}
