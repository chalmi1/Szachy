import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyFrame extends JFrame {
    public MyFrame(String name) {
        super(name);
        setResizable(false);
        // Utworzenie panelu odpowiadającego za rozmiar okna (JFrame.setSize nie uwzględnia ramki)
        JPanel brd = new JPanel();
        brd.setPreferredSize(new Dimension(600, 600));
        add(brd);
        pack();

    }
    public static void createAndShowGUI() {
        MyFrame frame = new MyFrame("Szachy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        //frame.setLayout(BorderLayout());
        /*try {
            String workingDirectory = System.getProperty("user.dir");
            String ImgFilePath = workingDirectory + File.separator + "src" + File.separator + "img" + File.separator;
            System.out.println(ImgFilePath);
            BufferedImage myPicture = ImageIO.read(new File(ImgFilePath, "szachy.jpg"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            add(picLabel, BorderLayout.CENTER);
            paint(null);

        } catch (IOException e) {
            e.printStackTrace();*/
        //JPanel p = new JPanel();
        //frame.setLayout(new GridLayout(8, 8));
        //p.setBackground(Color.gray);
        //Tile t = new Tile("a8", Tile.ColorEnum.black);
        frame.getContentPane().add(new Board());
        //frame.add(p, BorderLayout.CENTER);
        //Board.createAndShowBoard(frame);
        //createBoard(frame);
    }

}
