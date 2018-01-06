package server.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import server.Constants;
import server.Game;

public class View  extends JFrame {

    private String teamNames[] = new String[2];
    private int scores[] = new int[2];
    private String turnInfo;
    private boolean updated = false;    
    private Font font = new Font("Tahoma", Font.BOLD, 18);
    private FontMetrics metrics = new FontMetrics(font) {}; 
    HashMap<Integer, Item> items;
    
    int rows = 0;
    int cols = 0;
    int world_w;
    int world_h;
    
    int wolrd_top_margin = 0;
    int wolrd_left_margin = 0;
    
    public void setTeamName(int team, String name) {
        teamNames[team] = name;
    }
    
    public void setScores(int team, int score) {
        scores[team] = score;
    }

    public void setTurnNum(String turn_info) {
        this.turnInfo = turn_info;
    }

    public void setItems(HashMap<Integer, Item> items) {
        this.items = items;
    }
    
    public View(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        world_w = cols * Item.cellSize;
        world_h = rows * Item.cellSize;
        wolrd_top_margin = (int) (48);
        wolrd_left_margin = (int) (16);
        Item.initialDx = Item.cellSize / 2 + wolrd_left_margin;
        Item.initialDy = wolrd_top_margin;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        WorldPanel panel = new WorldPanel();
        add(panel);
        panel.start();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setResizable(false);
        this.setTitle(Constants.Project.getInfo());
        this.setIconImage(Resource.Cheese[2]);
        this.pack();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }
    
    private class WorldPanel extends JPanel {
        private Thread th = null;
        public WorldPanel() {
            th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            repaint();
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.setPreferredSize(new Dimension(world_w + wolrd_left_margin * 2, world_h + wolrd_top_margin + wolrd_left_margin));
            setVisible(true);
        }
        
        public void start() {
            th.start();
        }
        
        public void drawInfo(Graphics g) {
            int margin_left_right = 10 + wolrd_left_margin;
            int name_text_top = 32;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(font);
            Rectangle2D bounds = metrics.getStringBounds(turnInfo, null);
            int textwidth = (int)bounds.getWidth();
            g2d.setColor(Color.BLACK);
            g2d.drawString(turnInfo, ((cols) * Item.cellSize + margin_left_right - textwidth) / 2, name_text_top);
            String a_info = scores[0] + "   " +  teamNames[0];
            String b_info = teamNames[1] + "   " + scores[1];
            bounds = metrics.getStringBounds(b_info + "", null);
            textwidth = (int)bounds.getWidth();
            g2d.setColor(Color.decode("#ce9255"));
            g2d.drawString(a_info, margin_left_right, name_text_top);
            g2d.setColor(Color.decode("#d7d3cf"));
            g2d.drawString(b_info, (cols) * Item.cellSize + wolrd_left_margin * 2 - margin_left_right - textwidth, name_text_top);
        }
        
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(new Color(110, 110, 110, 255));
            g.fillRect(0, 0, world_w * 2, world_h * 2);
            
            drawInfo(g);

            int w = Resource.BackImg.getWidth(null);
            int h = Resource.BackImg.getHeight(null);
            
            g.setClip(wolrd_left_margin, wolrd_top_margin, world_w + 1, world_h + 1);
            
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    int x = wolrd_left_margin + j * w;
                    int y = wolrd_top_margin + i * h;
                    g.drawImage(Resource.BackImg, x, y, this);
                }
            }
            
            g.setColor(new Color(10, 10, 10, 32));
            for(int i = 0; i < 40 ; i++) {
                g.drawLine(Item.cellSize * i + wolrd_left_margin, wolrd_top_margin, Item.cellSize * i + wolrd_left_margin, 2000);
                g.drawLine(0, Item.cellSize * i + wolrd_top_margin, 2000, Item.cellSize * i + wolrd_top_margin);
            }

            if(items != null) {
                synchronized(Game.lock) {
                    for(Map.Entry<Integer, Item> item: items.entrySet()) {
                        if(item.getValue().getType() == Item.Type.CHEESE || item.getValue().getType() == Item.Type.POISON) {
                            item.getValue().paint(g);
                        }
                    }
                    for(Map.Entry<Integer, Item> item: items.entrySet()) {
                        if(item.getValue().getType() == Item.Type.Wall) {
                            item.getValue().paint(g);
                        }
                    }
                    
                    for(Map.Entry<Integer, Item> item: items.entrySet()) {
                        if(item.getValue().getType() == Item.Type.Ladder) {
                            item.getValue().paint(g);
                        }
                    }
                    
                    for(Map.Entry<Integer, Item> item: items.entrySet()) {
                        if(item.getValue().getType() == Item.Type.BROWN_RAT || item.getValue().getType() == Item.Type.GRAY_RAT) {
                            item.getValue().paint(g);
                        }
                    }
                }
            }
        }
    }
}
