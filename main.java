
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.applet.Applet;
import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.border.TitledBorder;
import javax.swing.ImageIcon;

import java.io.*;
import javax.sound.*;

import java.io.File;

public class main extends JFrame{
    
    private static JPanel pnlLeft;
    
    public main() {
        super("Reversi - Developed by Ritik Rawal(AIT)");
        setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width/2, height/2);

        // center the jframe on screen
        setLocationRelativeTo(null);
        
        pnlLeft = new ReversiGui();
        add(pnlLeft, BorderLayout.CENTER);
        
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new main();
       
    }
    
    
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Cell 
{
    
    private int corY;
    private char corX;
    private char ch;
    public Cell(char x, int y, char c)
    {
        corY = y;
        corX = x;
        ch = c;  
    }

    Cell() {}
    char getCorX()
    {
        return corX; 
    }
    int getCorY() 
    { 
        return corY; 
    }
    char getCh() 
    {
        return ch; 
    }
    void setPosition(char x, char c, int y)
    {
        corX = x;
        corY = y;
        ch = c;
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Reversi {
    
    private  int rows = 8;
    private  int cols = 8;
    private  int userCont = 0;
    private  int computerCont = 0;
    
    public Cell gameCells[][];
    
   
    public Reversi()
    {
        int mid;
        mid = rows / 2;
        gameCells = new Cell[8][8];
        for(int i = 0; i < rows; ++i)
            gameCells[i] = new Cell[8];
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < cols; ++j)
            {   
                gameCells[i][j] = new Cell();
                char c = (char) (97 + j);
                if((i == mid-1) && (j == mid-1)){
                    gameCells[i][j].setPosition(c, 'X', i+1);
                }
                else if((i == mid-1) && (j == mid))
                {
                    gameCells[i][j].setPosition(c, 'O', i+1);
                }
                else if((i == mid) && (j == mid-1))
                {
                    gameCells[i][j].setPosition(c, 'O', i+1);
                }
                else if((i == mid) && (j == mid))
                {
                    gameCells[i][j].setPosition(c, 'X', i+1);
                }
                else        
                {
                    gameCells[i][j].setPosition(c, '.', i+1);                    
                }
            }   
        }  
    }
    public Reversi(Reversi obje)
    {
        int y;
        char c, x;
        gameCells = new Cell[8][8];
        for(int i = 0; i < rows; ++i)
            gameCells[i] = new Cell[8];
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < cols; ++j)
            {   
                gameCells[i][j] = new Cell();
                
                c = obje.gameCells[i][j].getCh();
                y = obje.gameCells[i][j].getCorY();
                x = obje.gameCells[i][j].getCorX();
                gameCells[i][j].setPosition(x, c, y);
            }
        }
    }
    public void findLegalMove(ArrayList <Integer> arr)
    {
        int status;
        int change,max = 0; 
        change = 0;
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                if(gameCells[i][j].getCh() == '.')
                {
                    int numberOfMoves[] = new int[1];
                    move(i,j,change,'X','O',numberOfMoves);
                    if(numberOfMoves[0] != 0)
                    {
                        arr.add(i);
                        arr.add(j);
                    }    
                } 
            }
        }
    }
    public int play() // play function for computer
    {  
        int change,max = 0,mX = 0,mY = 0,sum;;    
        change = 0;
        int numberOfMoves[] = new int[1];

        for (int i = 0; i < rows; ++i) // En buyuk hamleyi bulur
        {
            for (int j = 0; j < cols; ++j)
            {
                if(gameCells[i][j].getCh() == '.')
                {
                    move(i,j,change,'O','X',numberOfMoves);
                    if(max < numberOfMoves[0])
                    { 
                        max = numberOfMoves[0];
                        mX = i; mY = j;
                    }
                }
            }
        }   
        computerCont = max;
        if (computerCont == 0)
        {
            //cout << endl;
            //cout << "Computer Passes(no move possible)\n";
            computerCont = -1;
            return -1;
        }
        if(computerCont != 0)
        {
            change = 1;
            //cout << "numberOfMoves : " << sum << endl;
            move(mX,mY,change,'O','X',numberOfMoves);       
        }
        return 0;
    }
    public int play(int xCor,int yCor) // play function for user
    {  
        int status;
        int change,max = 0; 
        int numberOfMoves[] = new int[1];
        change = 0;
        for (int i = 0; i < rows; ++i)
        {
            for (int j = 0; j < cols; ++j)
            {
                if(gameCells[i][j].getCh() == '.')
                {
                    move(i,j,change,'X','O',numberOfMoves);
                    if(max < numberOfMoves[0])
                        max = numberOfMoves[0];
                }
            }
        }
        userCont = max;
        if(userCont == 0) // Hamle kalmazsa computera gecer
        { 
            userCont = -1;
            return -1;
        }
        if(userCont != 0)
        {   
            change = 1;
            if(!(gameCells[xCor][yCor].getCh() == '.'))         
                return 1; // dolu yere basti, gecersiz hamle
                
            status = move(xCor,yCor,change,'X','O',numberOfMoves);
            if(status == -1)
                return 1; // gecersiz hamle
        }
        for (int i = 0; i < 8; i++) 
        {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%c",gameCells[i][j].getCh());
            }
            System.out.println("");
        }
        return 0;
    }
    public int endOfGame() 
    {
        int[] arr = new int[3];
        int cross, circular, point ;
        controlElements(arr);
        cross = arr[0];
        circular = arr[1];
        point = arr[2];
        
        if( (userCont == -1 && computerCont == -1) || point == 0)
        {
            if(userCont == -1 && computerCont == -1) //No legal move
                return 0;
            if(circular > cross)
                return 1;
            else if(cross > circular)
                return 2;
            else if(cross == 0)
                return 3;
            else if(circular == 0)
                return 4;
            else // scoreless
              return 5;
        }
        return -1;
    }
    public void controlElements(int arr[] )
    {
        int cross = 0, point = 0, circular = 0;

        int max = 0,numberOfMoves=0;
        for (int i = 0; i < rows; ++i)
        {
            for (int j = 0; j < cols; ++j)
            {
                if(gameCells[i][j].getCh() == 'X')
                    cross++;
                else if (gameCells[i][j].getCh() == 'O')
                    circular++;
                else if(gameCells[i][j].getCh() == '.')
                    point++;
            }
        } 
        arr[0] = cross; arr[1] = circular; arr[2] = point;
    }
    public void reset()
    {
        int mid = rows / 2;
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < cols; ++j)
            {   
                char c = (char) (97 + j);
                if((i == mid-1) && (j == mid-1))
                {
                    gameCells[i][j].setPosition(c, 'X', i+1);
                }
                else if((i == mid-1) && (j == mid))
                {
                    gameCells[i][j].setPosition(c, 'O', i+1);
                }
                else if((i == mid) && (j == mid-1))
                {
                    gameCells[i][j].setPosition(c, 'O', i+1);
                }
                else if((i == mid) && (j == mid))
                {
                    gameCells[i][j].setPosition(c, 'X', i+1);
                }
                else        
                {
                    gameCells[i][j].setPosition(c, '.', i+1);                    
                }
                System.out.printf("i : %d, j : %d, c : %c\n",i,j,gameCells[i][j].getCh());
            }
        }
    }
    int move(int xCor, int yCor,int change,char char1,char char2,int []numberOfMoves)
    {
    int cont,st2=0,st=0;
    int status = -1,corX,corY,temp;
        char str;
        int ix,y,x;
        
        x = xCor; y = yCor;
    numberOfMoves[0] = 0;
    //cout << "x : " << x << ", y: " << y << endl;
    if((x+1 < rows) && ( gameCells[x+1][y].getCh() == char2)) //asagi
    {   
            //cout << "deneme\n";
            cont = x;
            while((cont < rows) && (st2 != -1) && (st != 2))
            {
                cont++;
                if(cont < rows){
                    if(gameCells[cont][y].getCh() == char2)
                        st = 1;
                    else if(gameCells[cont][y].getCh() == char1)
                        st = 2;
                    else
                        st2 = -1;   
                }
            }
            if (st == 2)
            {
                temp = cont - x - 1;
                numberOfMoves[0] += temp;
            }   
            if(st == 2 && change == 1)
            {
                for (int i = x; i < cont; ++i)
                {
                    str = gameCells[i][y].getCorX();
                    ix = gameCells[i][y].getCorY();
                    gameCells[i][y].setPosition(str,char1,ix);
                }
                status = 0;
            }
            st=0;st2=0;
    }
    if((x-1 >= 0) && (gameCells[x-1][y].getCh() == char2)) //yukari
    {
            //cout << "deneme2\n";
            cont = x;
            while((cont >= 0) && (st2 != -1) && (st != 2))
            {
                cont--;
                if(cont >= 0){
                    if(gameCells[cont][y].getCh() == char2)
                        st = 1;
                    else if(gameCells[cont][y].getCh() == char1)
                        st = 2;
                    else 
                        st2 = -1;
                }           
            }   
            if (st == 2)
            {
                temp = x - cont - 1;
                numberOfMoves[0] += temp;             
            }   
            if(st == 2 && change == 1)
            {
                for (int i = cont; i <= x; ++i)
                {
                    str = gameCells[i][y].getCorX();
                    ix = gameCells[i][y].getCorY();
                    gameCells[i][y].setPosition(str,char1,ix);
                }
                status = 0;
            }       
            st=0;st2=0;
    }
    if((y+1 < cols) && (gameCells[x][y+1].getCh() == char2)) //sag
    {
            //cout << "deneme3\n";
            cont = y;
            while((cont < cols) && (st2 != -1) && (st != 2))
            {
                cont++;
                if(cont < cols){
                    if(gameCells[x][cont].getCh() == char2)
                        st = 1;
                    else if(gameCells[x][cont].getCh() == char1)
                        st = 2;
                    else 
                        st2 = -1;   
                }   
            }   
            if (st == 2)
            {
                    temp = cont - y - 1;
                    numberOfMoves[0] += temp;             
            }   
            if(st == 2 && change == 1)
            {
                for (int i = y; i < cont; ++i)
                {
                    str = gameCells[x][i].getCorX();
                    ix = gameCells[x][i].getCorY();
                    gameCells[x][i].setPosition(str,char1,ix);
                }
                status = 0;
            }
            st=0;st2=0;
    }
    if((y-1 >= 0) && (gameCells[x][y-1].getCh() == char2)) //sol
    {
            //cout << "deneme4\n";
            cont = y;
            while((cont >= 0) && (st2 != -1) && (st != 2))
            {
                cont--;
                if(cont >= 0){
                    if(gameCells[x][cont].getCh() == char2)
                        st = 1;
                    else if(gameCells[x][cont].getCh() == char1)
                        st = 2;
                    else 
                        st2 = -1;   
                }       
            }   
            if (st == 2)
            {
                    temp = y - cont - 1;
                    numberOfMoves[0] += temp;             
            }   
            if(st == 2 && change == 1)
            {
                for (int i = cont; i <= y; ++i)
                {
                    str = gameCells[x][i].getCorX();
                    ix = gameCells[x][i].getCorY();
                    gameCells[x][i].setPosition(str,char1,ix);
                }
                status = 0;
            }
            st=0;st2=0; 
    }
    if((x-1 >= 0) && (y+1 < cols) && (gameCells[x-1][y+1].getCh() == char2)) //sag yukari
    {
            //cout << "deneme5\n";
            corY = y;
            corX = x;
            while((corX >= 0) && (corY < cols) && (st2 != -1) && (st != 2))
            {
                corX--;
                corY++;
                if((corX >= 0) && (corY < cols)){
                    if(gameCells[corX][corY].getCh() == char2)
                        st = 1;
                    else if(gameCells[corX][corY].getCh() == char1)
                        st = 2;
                    else 
                        st2 = -1;
                }           
            }   
            if (st == 2)
            {
                    temp = x - corX - 1;
                    numberOfMoves[0] += temp;             
            }   
            if(st == 2 && change == 1)
            {
                while((corX <= x) && (y < corY))
                {
                    corX++;
                    corY--;
                    //cout << "corX : " << corX << ", corY : " << corY << endl << endl;
                    if((corX <= x) && (y <= corY)){
                        str = gameCells[corX][corY].getCorX();
                        ix = gameCells[corX][corY].getCorY();
                        gameCells[corX][corY].setPosition(str,char1,ix);
                    }
                }
                status = 0;
            }
            st=0;st2=0;     
    }
    if((x-1 >= 0) && (y-1 >= 0) && (gameCells[x-1][y-1].getCh() == char2)) //sol yukari
    {
            //cout << "deneme6\n";
            corY = y;
            corX = x;
            while((corX >= 0) && (corY >= 0) && (st2 != -1) && (st != 2))
            {
                corX--;
                corY--;
                if((corX >= 0) && (corY >= 0)){
                    if(gameCells[corX][corY].getCh() == char2)
                        st = 1;
                    else if(gameCells[corX][corY].getCh() == char1)
                        st = 2;
                    else 
                        st2 = -1;   
                }       
            }   
            if (st == 2)
            {
                    temp = x - corX - 1;
                    numberOfMoves[0] += temp;             
            }   
            if(st == 2 && change == 1)
            {
                //cout << "corX : " << corX << ", corY : " << corY << endl << endl;
                while((corX <= x) && (corY <= y))
                {
                    corX++;
                    corY++;
                    if((corX <= x) && (corY <= y)){
                        str = gameCells[corX][corY].getCorX();
                        ix = gameCells[corX][corY].getCorY();
                        gameCells[corX][corY].setPosition(str,char1,ix);
                    }
                }
                status = 0;
            }
            st=0;st2=0; 
    }
    if((x+1 < rows) && (y+1 < cols) && (gameCells[x+1][y+1].getCh() == char2)) //sag asagi
    {
            //cout << "deneme7\n";
            corY = y;
            corX = x;
            while((corX < rows) && (corY < cols) && (st2 != -1) && (st != 2))
            {
                corX++;
                corY++;
                if((corX < rows) && (corY < cols)){
                    if(gameCells[corX][corY].getCh() == char2)
                            st = 1;
                    else if(gameCells[corX][corY].getCh() == char1)
                            st = 2;
                    else 
                            st2 = -1;       
                }   
            }   
            if (st == 2)
            {
                temp = corX - x - 1;
                numberOfMoves[0] += temp;             
            }   
            if(st == 2 && change == 1)
            {
                while((corX >= x) && (corY >= y))
                {
                    corX--;
                    corY--;
                    //cout << "corX : " << corX << ", corY : " << corY << endl << endl;
                    if((corX >= x) && (corY >= y)){
                        str = gameCells[corX][corY].getCorX();
                        ix = gameCells[corX][corY].getCorY();
                        gameCells[corX][corY].setPosition(str,char1,ix);
                    }
                }
                status = 0;
            }
            st=0;st2=0;
    }
    if((x+1 < rows) && (y-1 >= 0) && (gameCells[x+1][y-1].getCh() == char2)) //sol asagi
    {
            //cout << "deneme8\n";
            corY = y;
            corX = x;
            while((corX < rows) && (corY >= 0) && (st2 != -1) && (st != 2))
            {
                    corX++;
                    corY--;
                    if((corX < rows) && (corY >= 0)){
                        if(gameCells[corX][corY].getCh() == char2)
                            st = 1;
                        else if(gameCells[corX][corY].getCh() == char1)
                            st = 2;
                        else 
                            st2 = -1;   
                   }        
            }   
            if (st == 2)
            {
                temp = corX - x - 1;
                numberOfMoves[0] += temp;             
            }   
            if(st == 2 && change == 1)
            {
                while((corX >= x) && (corY <= y))
                {
                    corX--;
                    corY++;
                    //cout << "corX : " << corX << ", corY : " << corY << endl << endl;
                    if((corX >= x) && (corY <= y)){
                        str = gameCells[corX][corY].getCorX();
                        ix = gameCells[corX][corY].getCorY();
                        gameCells[corX][corY].setPosition(str,char1,ix);
                    }
                }
                status = 0;
            }
            st=0;st2=0;     
    }
    if(status == 0)
            return 0;
    else
            return -1;          
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class ReversiGui extends JPanel{

    JPanel panel;
    JPanel boardPanel;
    static JLabel score1;
    static JLabel score2;
    static JButton newGame;
    static JButton undo;
    static JButton [] cell;
    static Reversi board;
    static ArrayList<Reversi>  arrReversi= new ArrayList<Reversi>();
    static int countUndo = 0;
    
    static public int playerScore = 2; 
    static public int pcScore = 2;
    private static Reversi start;
    private static int rows = 8;
    private static int cols = 8;
    private static Color col = Color.green;
    
  
    public ReversiGui()
    {
        super(new BorderLayout());    
        setPreferredSize(new Dimension(800,700));
        setLocation(0, 0);
        
        board = new Reversi();
        start = board;
        arrReversi.add(new Reversi(board));

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(800,60));
        
        newGame = new JButton();
        newGame.setPreferredSize(new Dimension(80,50));
        try 
        {
            Image img = ImageIO.read(getClass().getResource("images/start.png"));
            newGame.setIcon(new ImageIcon(img));
        } catch (IOException ex) {}
        newGame.addActionListener(new Action());
        
        undo = new JButton();
        undo.setPreferredSize(new Dimension(80,50));
        try 
        {
            Image img2 = ImageIO.read(getClass().getResource("images/undo.png"));
            undo.setIcon(new ImageIcon(img2));
        } catch (IOException ex) {}
        undo.addActionListener(new Action());
        JLabel name = new JLabel();
        name.setText("Developed by Onur Sezer");
        name.setLocation(750, 680);
        panel.add(newGame);
        panel.add(undo);
        
        add(panel, BorderLayout.SOUTH);
        
        // Board Panel
        boardPanel = new JPanel(new GridLayout(8,8));
        cell = new JButton[64];
        int k=0;
        for(int row = 0; row < rows; row++) 
        {
            for(int colum=0; colum < cols; colum++) 
            {
                cell[k] = new JButton();
                cell[k].setSize(50, 50);
                cell[k].setBackground(Color.GREEN);
                cell[k].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if(board.gameCells[row][colum].getCh() == 'X')
                {
                    try 
                    {
                        Image img = ImageIO.read(getClass().getResource("images/dark.png"));
                        cell[k].setIcon(new ImageIcon(img));
                    } catch (IOException ex) {}
                }
                else if(board.gameCells[row][colum].getCh() == 'O')
                {
                    try 
                    {
                        Image img = ImageIO.read(getClass().getResource("images/light.png"));
                        cell[k].setIcon(new ImageIcon(img));
                    } catch (IOException ex) {}                    
                }
                else if(row == 2 && colum == 4 || row == 3 && colum == 5 || 
                        row == 4 && colum == 2 || row == 5 && colum == 3 )
                {
                    try 
                    {
                        Image img = ImageIO.read(getClass().getResource("images/legalMoveIcon.png"));
                        cell[k].setIcon(new ImageIcon(img));
                    } catch (IOException ex) {} 
                }
                cell[k].addActionListener(new Action());
                boardPanel.add(cell[k]);
                k++;
            }
        }
        add(boardPanel, BorderLayout.CENTER);
        
        
        JPanel scorePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        scorePanel.setPreferredSize(new Dimension(210,800));
        
        JLabel dark = new JLabel();
        try 
        {
            Image img = ImageIO.read(getClass().getResource("images/dark.png"));
            dark.setIcon(new ImageIcon(img));
        } catch (IOException ex) {}
        JLabel light = new JLabel();
        try 
        {
            Image img = ImageIO.read(getClass().getResource("images/light.png"));
            light.setIcon(new ImageIcon(img));
        } catch (IOException ex) {}
        score1 = new JLabel();
        score1.setText(" Player : " + playerScore + "  ");
        score1.setFont(new Font("Serif", Font.BOLD, 22));
        
        score2 = new JLabel();   
        score2.setText(" Computer : " + pcScore + "  ");
        score2.setFont(new Font("Serif", Font.BOLD, 22));        
               
        c.gridx = 0;
        c.gridy = 1;
        scorePanel.add(dark, c);  
        c.gridx = 1;
        c.gridy = 1;
        scorePanel.add(score1,c);
        
        
        c.gridx = 0;
        c.gridy = 2;
        scorePanel.add(light, c);  
        c.gridx = 1;
        c.gridy = 2;
        scorePanel.add(score2,c);
              
        add(scorePanel, BorderLayout.EAST);
        
        //scorePanel.add(light);
        //scorePanel.add(score2);
        
    }  
    static class Action implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() == newGame)
            {
                board.reset();
                arrReversi.clear();
                arrReversi.add(new Reversi(start));
                int k = 0;
                for (int row = 0; row < rows; row++) 
                {
                    for (int colum = 0; colum < cols; colum++) 
                    {
                        cell[k].setIcon(null);
                        if(board.gameCells[row][colum].getCh() == 'X')
                        {
                            try 
                            {
                                Image img = ImageIO.read(getClass().getResource("images/dark.png"));
                                cell[k].setIcon(new ImageIcon(img));
                            } catch (IOException ex) {}
                        }
                        else if(board.gameCells[row][colum].getCh() == 'O')
                        {
                            try 
                            {
                                Image img = ImageIO.read(getClass().getResource("images/light.png"));
                                cell[k].setIcon(new ImageIcon(img));
                            } catch (IOException ex) {}                    
                        }
                        if(row == 2 && colum == 4 || row == 3 && colum == 5 || 
                        row == 4 && colum == 2 || row == 5 && colum == 3 )
                        {
                            try 
                            {
                                Image img = ImageIO.read(getClass().getResource("images/legalMoveIcon.png"));
                                cell[k].setIcon(new ImageIcon(img));
                            } catch (IOException ex) {} 
                        }
                        ++k;
                    }
                }
                playerScore = 2; pcScore = 2;
                score1.setText(" Player : " + playerScore + "  ");
                score2.setText(" Computer : " + pcScore + "  ");
            }
            else if(e.getSource() == undo)
            {
                countUndo++;
                int y,point;
                char c,x;
                int arr[] = new int[3];
                ArrayList <Integer> arrList = new ArrayList <Integer>();
                
                if(arrReversi.size() - countUndo - 1 >= 0)
                {
                    for (int i = 0; i < rows; i++) 
                    {
                        for (int j = 0; j < cols; j++) 
                        {
                            c = arrReversi.get(arrReversi.size() - countUndo - 1).gameCells[i][j].getCh();
                            x = arrReversi.get(arrReversi.size() - countUndo - 1).gameCells[i][j].getCorX();
                            y = arrReversi.get(arrReversi.size() - countUndo - 1).gameCells[i][j].getCorY();
                            board.gameCells[i][j].setPosition(x, c, y); 
                        }
                    }
                    int k = 0;
                    for (int row = 0; row < rows; row++) 
                    {
                        for (int colum = 0; colum < cols; colum++) 
                        {
                            cell[k].setIcon(null);
                            if(board.gameCells[row][colum].getCh() == 'X')
                            {
                                try 
                                {
                                    Image img = ImageIO.read(getClass().getResource("images/dark.png"));
                                    cell[k].setIcon(new ImageIcon(img));
                                } catch (IOException ex) {}
                            }
                            else if(board.gameCells[row][colum].getCh() == 'O')
                            {
                                try 
                                {
                                    Image img = ImageIO.read(getClass().getResource("images/light.png"));
                                    cell[k].setIcon(new ImageIcon(img));
                                } catch (IOException ex) {}                    
                            }
                            ++k;
                        }
                    }
                    board.findLegalMove(arrList);
                    for (int j = 0; j < arrList.size(); j += 2) 
                    {
                        try 
                        {
                            Image img = ImageIO.read(getClass().getResource("images/legalMoveIcon.png"));
                            cell[arrList.get(j)*rows + arrList.get(j + 1)].setIcon(new ImageIcon(img));
                        } catch (IOException ex) {}  
                    }
                    board.controlElements(arr);
                    playerScore = arr[0]; pcScore = arr[1]; point = arr[2];
                    score1.setText("Player : " + playerScore + "  ");
                    score2.setText("Computer : " + pcScore + "  "); 
                }
                
            }
            else
            {
                for (int i = 0; i < 64; i++) 
                {
                    if(e.getSource() == cell[i])  
                    {
                        int xCor, yCor;
                        int ct = -100, point;
                        int arr[] = new int[3];
                        System.out.println("button : "+ i);
                        if(i==0)
                        {
                            xCor=0;
                            yCor=0;
                        }
                        else
                        {
                            yCor =i%8;
                            xCor=i/8;
                        }
                        ct = board.play(xCor, yCor);
                        if(ct == 0)
                        {
                            arrReversi.add(new Reversi(board));
                            int k=0;
                            for(int row = 0; row < rows; row++) 
                            {
                                for(int colum=0; colum < cols; colum++) 
                                {
                                    if(board.gameCells[row][colum].getCh() == 'X')
                                    {
                                        try 
                                        {
                                            Image img = ImageIO.read(getClass().getResource("images/dark.png"));
                                            cell[k].setIcon(new ImageIcon(img));
                                        } catch (IOException ex) {}
                                    }
                                    else if(board.gameCells[row][colum].getCh() == 'O')
                                    {
                                        try 
                                        {
                                            Image img = ImageIO.read(getClass().getResource("images/light.png"));
                                            cell[k].setIcon(new ImageIcon(img));
                                        } catch (IOException ex) {}                    
                                    }
                                    k++;
                                }
                            }
                            board.controlElements(arr);
                            playerScore = arr[0]; pcScore = arr[1]; point = arr[2];
                            score1.setText("Player : " + playerScore + "  ");
                            score2.setText("Computer : " + pcScore + "  "); 
                        }
                        if(ct == 0 || ct == -1)
                        {    
                            board.play();
                            arrReversi.add(new Reversi(board));
                            ArrayList <Integer> arrList = new ArrayList <Integer>();
                            int k=0;
                            for(int row = 0; row < rows; row++) 
                            {
                                for(int colum=0; colum < cols; colum++) 
                                {
                                    if(board.gameCells[row][colum].getCh() == 'X')
                                    {
                                        try 
                                        {
                                            Image img = ImageIO.read(getClass().getResource("images/dark.png"));
                                            cell[k].setIcon(new ImageIcon(img));
                                        } catch (IOException ex) {}
                                    }
                                    else if(board.gameCells[row][colum].getCh() == 'O')
                                    {
                                        try 
                                        {
                                            Image img = ImageIO.read(getClass().getResource("images/light.png"));
                                            cell[k].setIcon(new ImageIcon(img));
                                        } catch (IOException ex) {}                    
                                    }
                                    else if(board.gameCells[row][colum].getCh() == '.')
                                    {
                                        cell[k].setIcon(null);
                                    }
                                    k++;
                                }
                            }
                            board.findLegalMove(arrList);
                            for (int j = 0; j < arrList.size(); j += 2) 
                            {
                                try 
                                {
                                    Image img = ImageIO.read(getClass().getResource("images/legalMoveIcon.png"));
                                    cell[arrList.get(j)*rows + arrList.get(j + 1)].setIcon(new ImageIcon(img));
                                } catch (IOException ex) {}  
                            }
                            board.controlElements(arr);
                            playerScore = arr[0]; pcScore = arr[1]; point = arr[2];
                            score1.setText("Player : " + playerScore + "  ");
                            score2.setText("Computer : " + pcScore + "  ");  
                        }
                    }

                }
                int st = board.endOfGame();
                if(st == 0)
                {
                    if(playerScore > pcScore)
                        JOptionPane.showMessageDialog(null,"No legal move!\nPlayer Win!","Game Over",JOptionPane.PLAIN_MESSAGE);   
                    else
                        JOptionPane.showMessageDialog(null,"No legal move!\nComputer Win!","Game Over",JOptionPane.PLAIN_MESSAGE);
                }
                else if(st == 1 || st == 3)
                {
                    JOptionPane.showMessageDialog(null,"Computer Win!","Game Over",JOptionPane.PLAIN_MESSAGE);
                }
                else if(st == 2 || st == 4)
                {
                    JOptionPane.showMessageDialog(null,"Player Win!","Game Over",JOptionPane.PLAIN_MESSAGE); 
                }
                else if(st == 3)
                {
                    JOptionPane.showMessageDialog(null,"Scoreless!","Game Over",JOptionPane.PLAIN_MESSAGE); 
                }
            }
        }
        
    }
    
}

