import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


class Fire {
    public boolean isabetEtti=false;
    private int x;

    public Fire(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int y;

}

public class Game extends JPanel implements KeyListener, ActionListener {
    private final BufferedImage heart;
    Timer timer = new Timer(6, this);
    private int sure = 0;
    private int kullanilanAtes = 0;
    private BufferedImage ucak;
    private BufferedImage space;
    private BufferedImage enemy;
    private BufferedImage enemyYaniyor;

    private ArrayList<Fire> shoot = new ArrayList<Fire>();
    private int fire_Oy = 1;
    private int enemy_OX = 405;
    private int enemy_move_OX = 2;

    private int uzayGemisi_OX = 405;

    private int uzayGemisi_move_OX = 20;
    private long enemyVuruldu = 0;


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // g.setColor(Color.RED);
        //g.fillOval(enemy_OX , 0 , 20 , 20);

        sure += 5;

        g.drawImage(space, 0, 0, space.getWidth(), space.getHeight(), this);
        g.drawImage(ucak, uzayGemisi_OX, 590, ucak.getWidth() / 5, ucak.getHeight() / 5, this);

        g.drawImage(enemy, enemy_OX, 0, enemy.getWidth() / 6, enemy.getHeight() / 5, this);

        for (int i = 0; i < this.can; i++) {
            g.drawImage(heart, i*40,0 , 40, 40, this);
        }

        if (System.currentTimeMillis() - this.enemyVuruldu < 500) {
            g.drawImage(enemyYaniyor, enemy_OX, 0, enemy.getWidth() / 6, enemy.getHeight() / 5, this);
        }

        for (Fire fire : shoot) {
            if (fire.getY()< 0){
                shoot.remove(shoot);
            }
        }
        g.setColor(Color.red);
        for (Fire fire : shoot){
            g.fillOval(fire.getX(), fire.getY(), 4, 20);
        }

        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(mermi-kullanilanAtes),200,50);

        if (checkGame()){
            timer.stop();
            String message = "DUSMAN YOK EDILDI !!!\n"+
                    "Harcana Ates : " + kullanilanAtes +
                    "\nSure : " + sure / 1000;
            JOptionPane.showMessageDialog(this, message);

            System.exit(0);
        }
    }

    @Override
    public void repaint() {
        super.repaint();
    }


    int can= 3;
    int mermi= 300;


    private static Logger log = Logger.getLogger(Game.class.getName());

    public boolean checkGame (){
        for (Fire fire : shoot){
            if (new Rectangle(fire.getX(), fire.getY(),4, 20 ).intersects(new Rectangle(enemy_OX + 40, 0 ,40,2))){
                if (fire.isabetEtti) {
                    continue;
                }
                enemyVuruldu = System.currentTimeMillis();
                fire.isabetEtti = true;
                this.can --;
                log.info("Can gitti, kalan can:"+can);
                if (this.can <= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    public Game() {
        try {
            ucak = ImageIO.read(new FileImageInputStream(new File("uzayGemisi.png")));
            space = ImageIO.read(new FileImageInputStream(new File("space.png")));
            enemy = ImageIO.read(new FileImageInputStream(new File("enemy.png")));
            heart = ImageIO.read(new FileImageInputStream(new File("heart.png")));
            enemyYaniyor = ImageIO.read(new FileImageInputStream(new File("enemyYaniyor.png")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //setBackground(Color.BLACK);
        timer.start();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_LEFT) {
            if (uzayGemisi_OX <= 0) {
                uzayGemisi_OX = 0;
            } else {
                uzayGemisi_OX -= uzayGemisi_move_OX;
            }
        }
        if (c == KeyEvent.VK_RIGHT){
            if (uzayGemisi_OX >= 810){
                uzayGemisi_OX = 820;
            }
            else {
                uzayGemisi_OX += uzayGemisi_move_OX;
            }
        }
        if (c == KeyEvent.VK_CONTROL){
            if (kullanilanAtes >= mermi) {
                return;
            }
            shoot.add(new Fire(uzayGemisi_OX+37, 575));
            kullanilanAtes ++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (Fire fire : shoot){
            fire.setY(fire.getY() - fire_Oy);
        }

        enemy_OX += enemy_move_OX;
        if (enemy_OX >= 810) {
            enemy_move_OX = -enemy_move_OX;
        }
        if (enemy_OX <= 0) {
            enemy_move_OX = -enemy_move_OX;
        }
        repaint();
    }
}
