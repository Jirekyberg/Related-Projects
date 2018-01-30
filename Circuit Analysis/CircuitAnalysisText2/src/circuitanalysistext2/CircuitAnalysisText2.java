/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitanalysistext2;

/**
 *
 * @author Michael Li
 */
public class CircuitAnalysisText2 {
    public static void main(String[] args) {
        
        //Input 20 volts
        Source s = new Source(20);
       
        
        //Depth 3 Case
        
        s.addSeriesLoad(2, "1");
        
        s.addSeriesLoad(10, "2");
        s.addParallelLoad(5,"2");
        
        s.addSeriesLoad(6.4, "3");
        s.addParallelLoad(2.3, "3");
        s.addParallelLoad(5.1, "3");
        s.addSeriesLoad(9.7, "3-2");
        s.addSeriesLoad(0.5, "3-3");
        s.addSeriesLoad(4.4, "3-3");
        
        s.addSeriesLoad(6, "4");
        s.addParallelLoad(7, "4");
        s.addSeriesLoad(8, "4-1");
        s.addSeriesLoad(9, "4-2");
        
        
        //Depth 4 Case
        /*
        s.addSeriesLoad(3, "1");
        
        s.addSeriesLoad(12, "2");
        
        s.addSeriesLoad(6, "3");
        s.addParallelLoad(8, "3");
        s.addSeriesLoad(9, "3-2");
        s.addParallelLoad(4,"3-2");
        
        */
        s.calculate();
        s.describe();
    }
}
