package Game;

import javax.swing.*;
import java.awt.*;

class MyFrame extends JFrame {
    private MyFrame(String name) {
        super(name);
        setResizable(false);
        // Utworzenie panelu odpowiadającego za rozmiar okna (JFrame.setSize nie uwzględnia ramki)
        JPanel brd = new JPanel();
        brd.setPreferredSize(new Dimension(600, 600));
        add(brd);
        pack();

    }
    static void createAndShowGUI() {
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
        frame.getContentPane().add(new Board());
        //frame.add(p, BorderLayout.CENTER);
        //createBoard(frame);
    }

}
