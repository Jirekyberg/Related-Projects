/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitanalysis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;

/**
 *
 * @author Michael Li
 */
public class CircuitAnalysis extends javax.swing.JFrame {

    /**
     * Creates new form CircuitAnalysis
     */
    Source s = new Source(0);
    int resistorIndex;
    int allResistorCounter=1;
    ArrayList<Load> allResistors = new ArrayList();
    Graphics2D g;
    Color back = new Color(98,135,196);
    
    
    public CircuitAnalysis(){
        
        allResistors.add(s);
        initComponents();
        setTitle("oDias");
        getContentPane().setBackground(new Color(240, 240, 240));
        resistorSelect.addItem("Source");
        g = (Graphics2D) panel.getGraphics();
        panel.setBackground(new Color(98, 135, 196));
    }
    
    public void updateDisplay(){
        for(int k=0; k<allResistors.size(); k++){
            allResistors.get(k).r = (double) Math.round(allResistors.get(k).r* 1000) / 1000;
            allResistors.get(k).v = (double) Math.round(allResistors.get(k).v* 1000) / 1000;
            allResistors.get(k).i = (double) Math.round(allResistors.get(k).i* 1000) / 1000;
        }
        iLabel.setText(Double.toString(allResistors.get(resistorIndex).i));
        rLabel.setText(Double.toString(allResistors.get(resistorIndex).r));
        vLabel.setText(Double.toString(allResistors.get(resistorIndex).v));
    }

    public void addedResistor() {
        //Start of calculation
        double r = Double.parseDouble(resistorValue.getText());
        String loc = resistorLocation.getText();
        boolean contains = false;
        if (s.seriesLoad.size() == 0){
            s.addSeriesLoad(r, loc);
            
        } else if(loc.contains("-")){
            s.addSeriesLoad(r, loc);
            
        } else{
            for(int k=0; k<s.seriesLoad.size(); k++){
                if (loc.equals(s.seriesLoad.get(k).l)){
                    contains = true;
                }
            }

            if (contains == true){
                s.addParallelLoad(r, loc);
            } else{
                s.addSeriesLoad(r, loc);
            }
        }
        
        allResistors.clear();
        
        allResistors.add(s);
        
        for(int k=0; k<s.seriesLoad.size(); k++){
            allResistors.add(s.seriesLoad.get(k));
            for(int j=0; j<s.parallelLoad.size(); j++){
                if(s.seriesLoad.get(k).l.equals(s.parallelLoad.get(j).l)){
                    allResistors.add(s.parallelLoad.get(j));
                }
            }
        }
        
        resistorSelect.addItem("R"+allResistorCounter);
        allResistorCounter++;
        s.calculate();
        updateDisplay();
        //End of calculation part
        
        //Start of drawing
        paint();
    }
    
    public void drawResistor(int x, int y, int c){
        g.setColor(back);
        if(c == 1){
            g.fillRect(x, y, 30, 3);

            g.setColor(Color.black);
            g.drawLine(x, y, x+5, y-5);
            g.drawLine(x+5, y-5, x+12, y+3);
            g.drawLine(x+12, y+3, x+19, y-5);
            g.drawLine(x+19, y-5, x+26, y+3);
            g.drawLine(x+26, y+3, x+29, y+1);
            
        } else if(c == 2){
            g.fillRect(x, y, 3, 30);
            g.setColor(Color.black);
            
            g.drawLine(x, y, x-5, y+5);
            g.drawLine(x-5, y+5, x+3, y+12);
            g.drawLine(x+3, y+12, x-5, y+19);
            g.drawLine(x-5, y+19, x+3, y+26);
            g.drawLine(x+3, y+26, x+1, y+29);
            
        } else if(c == 3){
            drawResistor(x,y+25,1);
            g.drawLine(x, y, x, y+25);
            g.drawLine(x+29, y, x+29, y+25);
            
        } else if(c == 4){
            drawResistor(x-25,y,2);
            g.drawLine(x, y, x-25, y);
            g.drawLine(x, y+29, x-25, y+29);
            
        } else if (c == 5){
            drawResistor(x, y-25,1);
            g.drawLine(x, y, x, y-25);
            g.drawLine(x+29, y, x+29, y-25);
        }
    }
    private void paint(){
        int h = panel.getHeight();
        int w = panel.getWidth();
        g.setColor(back);
        g.fillRect(0, 0, w, h);
        
        g.setColor(Color.BLACK);
        int border = 20;
        g.fillRect(13, h/2-5, 19, 3);
        g.fillRect(17, h/2+5, 11,3);
        g.fillRect(21, h/2+5, 3, h/2-5-border);
        g.fillRect(border, border, 3, h/2-22);
        g.fillRect(border, border, w-2*border, 3);
        g.fillRect(w-border, border, 3, h-2*border);
        g.fillRect(border, h-border, w-2*border+3, 3);
        g.setStroke(new BasicStroke(3));        
        
        int numLine = (int) (s.seriesLoad.size())/3 + 1;
        if (s.seriesLoad.size() % 3 == 0 && numLine>=1 || s.seriesLoad.size() == 0){
            numLine--;
        } 
        
        int totalResistors =s.seriesLoad.size();

        ArrayList<Integer> a = new ArrayList();
        int sc = 1;
        
        for(int k=0; k<s.parallelLoad.size(); k++){
            boolean contains = false;
            for(int j=0; j<a.size(); j++){
                if (Integer.parseInt(s.parallelLoad.get(k).l) == a.get(j)){
                    contains = true;
                }
            }
            if (contains == false){
                a.add(Integer.parseInt(s.parallelLoad.get(k).l));
            }
        }
        
        //top line
        int drawLocation=(w-2*border)/(numLine+1);
        for(int k=0; k<numLine; k++){
            drawResistor(drawLocation,border,1);
            sc++;
            for(int j=0; j<a.size(); j++){
                if (a.get(j) == (s.seriesLoad.size() - totalResistors + 1)){
                    //Go through the entire list to det how many times to draw
                    int times = 0;
                    for(int e=0; e<s.parallelLoad.size(); e++){
                        if(Integer.parseInt(s.parallelLoad.get(e).l) == (s.seriesLoad.size() - totalResistors + 1)){
                            times++;
                        }
                    }
                    int tx = drawLocation;
                    int ty = border;
                    for(int e=0; e<times; e++){
                        drawResistor(tx,ty,3);
                        ty += 25;
                        sc++;
                    }
                }
            }
            totalResistors--;
            drawLocation += (w-2*border)/(numLine+1);
        }
        
        //Side line set up
        if(s.seriesLoad.size()%3==1 && numLine>1){
            numLine--;
        }
        
        //Side Line
        boolean done = false;
        while(totalResistors > 0 && done == false){
            drawLocation = (h-2*border)/(numLine+1);
            for(int k=0; k<numLine; k++){
                if (sc ==resistorIndex){
                    System.out.println("hello");
                }
                drawResistor(w-border,drawLocation,2);
                sc++;
                for(int j=0; j<a.size(); j++){
                    if (a.get(j) == (s.seriesLoad.size() - totalResistors + 1)){
                        //Go through the entire list to det how many times to draw
                        int times = 0;
                        for(int e=0; e<s.parallelLoad.size(); e++){
                            if(Integer.parseInt(s.parallelLoad.get(e).l) == (s.seriesLoad.size() - totalResistors + 1)){
                                times++;
                            }
                        }
                        int tx = w-border;
                        int ty = drawLocation;
                        for(int e=0; e<times; e++){
                            drawResistor(tx,ty,4);
                            tx -= 25;
                            sc++;
                        }
                    }
                }
                totalResistors--;
                drawLocation += (h-2*border)/(numLine+1);
            }
            done = true;
        }
        
        
        //Bottom Line
        int ss = totalResistors;
        drawLocation=(w-2*border)/(ss+1);
        while(totalResistors > 0){
            drawResistor(drawLocation, h-border, 1);
            sc++;
            for(int k=0; k<a.size(); k++){
                if (a.get(k) == (int) (s.seriesLoad.size() - totalResistors + 1)){
                    //Go through the entire list to det how many times to draw
                    int times = 0;
                    for(int e=0; e<s.parallelLoad.size(); e++){
                        if(Integer.parseInt(s.parallelLoad.get(e).l) == (s.seriesLoad.size() - totalResistors + 1)){
                            times++;
                        }
                    }
                    int tx = drawLocation;
                    int ty = h-border;
                    for(int e=0; e<times; e++){
                        drawResistor(tx,ty,5);
                        ty -= 25;
                        sc++;
                    }
                }
            }
            totalResistors--;
            drawLocation += (w-2*border)/(ss+1);
        }
            
            
        
        
        
        
        
    }
    public void k(){
        paint();
        System.out.println("hello");
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        iLabel = new javax.swing.JLabel();
        vLabel = new javax.swing.JLabel();
        addResistorButton = new javax.swing.JButton();
        resistorValue = new javax.swing.JTextField();
        resistorLocation = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        resistorSelect = new javax.swing.JComboBox();
        panel = new javax.swing.JPanel();
        voltInput = new javax.swing.JTextField();
        voltageUpdateButton = new javax.swing.JButton();
        calculateButton = new javax.swing.JButton();

        jLabel7.setText("jLabel7");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 339, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 233, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Resistance(Ω)");

        jLabel3.setText("Amperes(I)");

        jLabel2.setText("Voltage(V)");

        addResistorButton.setText("Add Resistor");
        addResistorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addResistorButtonActionPerformed(evt);
            }
        });

        resistorValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resistorValueActionPerformed(evt);
            }
        });

        resistorLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resistorLocationActionPerformed(evt);
            }
        });

        jLabel8.setText("Input Voltage");

        resistorSelect.setFont(new java.awt.Font("Myriad Pro", 0, 14)); // NOI18N
        resistorSelect.setForeground(new java.awt.Color(46, 46, 46));
        resistorSelect.setAlignmentX(0.0F);
        resistorSelect.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        resistorSelect.setFocusable(false);
        resistorSelect.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                resistorSelectItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        voltInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voltInputActionPerformed(evt);
            }
        });

        voltageUpdateButton.setText("Update All");
        voltageUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voltageUpdateButtonActionPerformed(evt);
            }
        });

        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(calculateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resistorSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(rLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(vLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(iLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(resistorValue, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(resistorLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addResistorButton))
                    .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(voltInput, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(voltageUpdateButton)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(resistorSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(voltInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(voltageUpdateButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(iLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13))
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addResistorButton)
                    .addComponent(resistorValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resistorLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(calculateButton))
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addResistorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addResistorButtonActionPerformed
        //Start of calculation
        addedResistor();
        
    }//GEN-LAST:event_addResistorButtonActionPerformed

    private void resistorSelectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_resistorSelectItemStateChanged
        resistorIndex = resistorSelect.getSelectedIndex();
        updateDisplay();
    }//GEN-LAST:event_resistorSelectItemStateChanged

    private void voltageUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voltageUpdateButtonActionPerformed
        s.v = Double.parseDouble(voltInput.getText());
        if (resistorIndex == 0){
            vLabel.setText(Double.toString(s.v));
        }
        allResistors.get(0).v = s.v;
        paint();
    }//GEN-LAST:event_voltageUpdateButtonActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        s.calculate();
        s.describe();
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void voltInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voltInputActionPerformed
        s.v = Double.parseDouble(voltInput.getText());
        if (resistorIndex == 0){
            vLabel.setText(Double.toString(s.v));
        }
        allResistors.get(0).v = s.v;
        paint();
    }//GEN-LAST:event_voltInputActionPerformed

    private void resistorLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resistorLocationActionPerformed
        addedResistor();
    }//GEN-LAST:event_resistorLocationActionPerformed

    private void resistorValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resistorValueActionPerformed
        addedResistor();
    }//GEN-LAST:event_resistorValueActionPerformed
    
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CircuitAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CircuitAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CircuitAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CircuitAnalysis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CircuitAnalysis().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addResistorButton;
    private javax.swing.JButton calculateButton;
    private javax.swing.JLabel iLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel rLabel;
    private javax.swing.JTextField resistorLocation;
    public javax.swing.JComboBox resistorSelect;
    private javax.swing.JTextField resistorValue;
    private javax.swing.JLabel vLabel;
    private javax.swing.JTextField voltInput;
    private javax.swing.JButton voltageUpdateButton;
    // End of variables declaration//GEN-END:variables
}
