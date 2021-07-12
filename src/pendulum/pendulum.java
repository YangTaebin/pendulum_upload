package pendulum;

import javax.swing.*;
import java.awt.*;

public class pendulum extends JFrame {
    public float m_ela = (float) 1;
    public float gravity = (float) 0.01;
    Pendulum pen = new Pendulum(1, 550, 500, 500, 500, 10);

    pendulum() {
        setTitle("Double Pendulum Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new MyPanel());
        setSize(1000, 1000);
        setVisible(true);
    }

    public static void main(String[] args) {
        pendulum d_pendulum = new pendulum();
    }

    class MyPanel extends JPanel {
        MyPanel() {
            new Thread() {
                public void run(){
                    while(true){
                        repaint();
                        pen.Next_Stat();

                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.fillOval((int) pen.x-pen.radius, (int) pen.y-pen.radius, 2*pen.radius, 2*pen.radius);
            g.setColor(Color.BLACK);
            g.fillOval((int) pen.cent_x-5, (int) pen.cent_y-5, 2*5, 2*5);
            g.setColor(Color.BLACK);
            g.drawLine((int) pen.cent_x, (int) pen.cent_y, (int) pen.cent_x, (int) pen.cent_y);
        }
    }

    private class Pendulum {
        public float x, y, cent_x, cent_y, vel_x, vel_y, ac_x, ac_y, mass;
        public int radius;

        Pendulum(float mass, float initial_x, float initial_y, float center_x, float center_y, int radius) {
            this.mass = mass;
            this.x = initial_x;
            this.y = initial_y;
            this.cent_x = center_x;
            this.cent_y = center_y;
            this.radius = radius;
            this.vel_x = 0;
            this.vel_y = 0;
            this.ac_x = 0;
            this.ac_y = 0;
        }

        void Next_Stat() {
            float scope = (float) 0.001;
            for(int i=0; i<1000; i++) {
                Next_acc();
                this.x = (float) (this.x + this.vel_x*(scope) + ((this.ac_x / 2))*Math.pow(scope,2));
                this.y = (float) (this.y + this.vel_y*(scope) + ((this.ac_y / 2))*Math.pow(scope,2));
                this.vel_x = (float) (this.vel_x + (this.ac_x*scope));
                this.vel_y = (float) (this.vel_y + (this.ac_y*scope));
                this.ac_x = 0;
                this.ac_y = 0;

                if (this.x > 1000 || this.x < 0) {
                    this.vel_x *= -m_ela;
                }
                if (this.y > 1000 || this.y < 0) {
                    this.vel_y *= -m_ela;
                }
            }
        }
        void Next_acc() {
            ac_x = 0;
            ac_y = gravity;
            float sin_seta = (float) Math.round(((x-cent_x)/((float) Math.sqrt(Math.pow(x-cent_x, 2)+Math.pow(y-cent_y, 2))))*100)/(float) 100.0;
            float cos_seta = (float) Math.round(((y-cent_y)/((float) Math.sqrt(Math.pow(x-cent_x, 2)+Math.pow(y-cent_y, 2))))*100)/(float) 100.0;
            ac_y -= gravity * cos_seta * cos_seta;
            ac_x -= gravity * cos_seta * sin_seta;
        }
    }
}
