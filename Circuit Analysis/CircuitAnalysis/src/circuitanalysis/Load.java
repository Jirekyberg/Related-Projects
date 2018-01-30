/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package circuitanalysis;

/**
 *
 * @author Michael Li
 */
public class Load {
    double v;
    double r;
    double i;
    String l;
    
    public Load (double v){
        this.v = v;
        this.r = -1;
        this.i = -1;
        this.l = "Source";
    }
    
    public Load (double r, String location){
        this.r = r;
        this.l = location;
        this.v = -1;
        this.i = -1;
    }
}
