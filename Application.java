import javax.swing.*;

public class Application  {
    
    public static void main (String args[]) throws Exception
    {
    int boardWidth =360 ;        //window size in pixels
    int boardHeight=640;

    JFrame frame=new JFrame("Flappy Bird");
    frame.setSize(boardWidth,boardHeight);
    //frame.setVisible(true);
    frame.setLocationRelativeTo(null);         //so they place window at center of screen
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    FloppyBird floppyBird=new FloppyBird();

    frame.add(floppyBird);
    frame.pack();   //avoid color on tittle bar
    floppyBird.requestFocus();
    frame.setVisible(true);
}

}
