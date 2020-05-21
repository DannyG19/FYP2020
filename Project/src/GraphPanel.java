
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Used to generate graphs
public class GraphPanel extends JPanel {

    private int width = 800;
    private int heigth = 600;
    private int padding = 70;
    private int pointSize = 7;
    private int numberYDivisions = 10;
    private int labelPadding = 25;
    
    private Color lineColor1 = new Color(200, 0, 0, 180);
    private Color lineColor2 = new Color(45, 100, 230, 180);
    
    /*
    private Color lineColor3 = new Color(255, 0, 125, 180);
    private Color lineColor4 = new Color(0, 255, 0, 180);
    private Color lineColor5 = new Color(255, 125, 0, 180);
    */
    
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    
    private List<Double> parallelTimes, sequentialTimes;
    private List<String> xValues;
    private String xLabel;

    //Read in given times and labels
    public GraphPanel(List<Double> parallelTimes, List<Double> sequentialTimes, List<String> xValues, String xLabel) {
        this.parallelTimes = parallelTimes;
        this.sequentialTimes = sequentialTimes;
        this.xValues = xValues;
        this.xLabel = xLabel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xAxisScale = ((double) width - (padding*2) - labelPadding) / (parallelTimes.size() - 1);
        double yAxisScale = ((double) heigth - (padding*2) - labelPadding) / getMaxTime();
        
        //Determines coordinates of points to be drawn
        List<Point> graphPointsS = new ArrayList<>();
        for (int i=0;i<sequentialTimes.size();i++) {
            int x1 = (int) (i*xAxisScale+padding+labelPadding);
            int y1 = (int) ((getMaxTime()-sequentialTimes.get(i))*yAxisScale+padding);
            graphPointsS.add(new Point(x1, y1));
        }

        List<Point> graphPointsP = new ArrayList<>();
        for (int i=0;i<parallelTimes.size();i++) {
            int x1 = (int) (i*xAxisScale+padding+labelPadding);
            int y1 = (int) ((getMaxTime()-parallelTimes.get(i))*yAxisScale+padding);
            graphPointsP.add(new Point(x1, y1));
        }

       
        
        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, width-(padding*2)-labelPadding, heigth-(padding*2)-labelPadding);
        g2.setColor(Color.BLACK);

        // draw marks and grid lines for y axis.
        for (int i= 0;i<numberYDivisions+1;i++) {
            int x0 = padding+labelPadding;
            int x1 = pointSize+padding+labelPadding;
            int y0 = getHeight()-((i*(heigth - (padding*2) - labelPadding))/numberYDivisions+padding+labelPadding);
            int y1 = y0;
            if (parallelTimes.size()>0) {
                g2.setColor(gridColor);
                g2.drawLine(padding+labelPadding+1+pointSize, y0, width-padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = (Math.ceil((int) ((getMaxTime()*((i*1.0)/numberYDivisions))*100))/100.0) + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0-labelWidth-5, y0+(metrics.getHeight()/2)-3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // draw marks and grid lines for x axis 
        for (int i=0;i<xValues.size();i++) {
            if (parallelTimes.size()>1) {
                int x0 = i * (width-(padding*2)-labelPadding)/(parallelTimes.size()-1)+padding+labelPadding;
                int x1 = x0;
                int y0 = heigth-padding-labelPadding;
                int y1 = y0-pointSize;
                if ((i % ((int) ((parallelTimes.size()/20.0))+1))==0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, heigth-padding-labelPadding-1-pointSize, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = xValues.get(i) + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0-labelWidth/2, y0+metrics.getHeight()+3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        //create x and y axis 
        g2.drawLine(padding+labelPadding, heigth-padding-labelPadding, padding+labelPadding, padding);
        g2.drawLine(padding+labelPadding, heigth-padding-labelPadding, width-padding, heigth-padding-labelPadding);
        
        //Label both x and y axis
        g2.drawString(xLabel, 380, 550);
        g2.drawString("Time (s)", 15, 310);
        
        int stringY = 70;
        
        //Displays the times along the right side of the graph
        g2.drawString("Sequential Time (Blue) : ", 810, stringY);
        stringY = stringY+15;
        
        for(int size=0;size<sequentialTimes.size();size++){
        	g2.drawString("Time "+(size+1)+":  "+sequentialTimes.get(size)+" s", 800, stringY);
        	stringY=stringY+15;
        }
        
        stringY = stringY+15;
        
        
        g2.drawString("Parallel Times (Red) :", 810, stringY);
        stringY = stringY+15;        
        
        
        for(int size=0;size<parallelTimes.size();size++){
        	g2.drawString("Time "+(size+1)+":  "+parallelTimes.get(size)+" s", 800, stringY);
        	stringY=stringY+15;
        }
        
        
        stringY = stringY+60;
        g2.drawString("Size of Input Log : 8,100 Events", 780, stringY);


        
        //Draw line for parallel times
        Stroke stroke1 = g2.getStroke();
        g2.setColor(lineColor1);
        g2.setStroke(GRAPH_STROKE);
        for (int i=0;i<graphPointsP.size()-1;i++){
            int x1 = graphPointsP.get(i).x;
            int y1 = graphPointsP.get(i).y;
            int x2 = graphPointsP.get(i + 1).x;
            int y2 = graphPointsP.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(stroke1);
        g2.setColor(pointColor);
        for (int i=0;i<graphPointsP.size();i++){
            int x = graphPointsP.get(i).x - pointSize/2;
            int y = graphPointsP.get(i).y - pointSize/2;
            int ovalW = pointSize;
            int ovalH = pointSize;
            g2.fillOval(x, y, ovalW, ovalH);
        }
          
       
        //Draw line for sequential times
        Stroke stroke2 = g2.getStroke();
        g2.setColor(lineColor2);
        g2.setStroke(GRAPH_STROKE);
        for (int i=0;i<graphPointsS.size()-1;i++){
            int x1 = graphPointsS.get(i).x;
            int y1 = graphPointsS.get(i).y;
            int x2 = graphPointsS.get(i + 1).x;
            int y2 = graphPointsS.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2); 
        }

        g2.setStroke(stroke2);
        g2.setColor(pointColor);
        for (int i=0;i<graphPointsS.size();i++){
            int x = graphPointsS.get(i).x - pointSize/2;
            int y = graphPointsS.get(i).y - pointSize/2;
            int ovalW = pointSize;
            int ovalH = pointSize;
            g2.fillOval(x, y, ovalW, ovalH);
        }
        
        
    }


    private double getMaxTime() {
        double maxScore = Collections.max(sequentialTimes);
        return Math.ceil(maxScore)+30;
    }

    public List<Double> getParallelTimes() {
        return parallelTimes;
    }
    
    public List<Double> getSequentialTimes() {
        return sequentialTimes;
    }
    

    public void drawGraph(){
    	GraphPanel mainPanel = new GraphPanel(parallelTimes, sequentialTimes, xValues, xLabel);
        mainPanel.setPreferredSize(new Dimension(1000, 600));
        JFrame frame = new JFrame("Time Comparison Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}