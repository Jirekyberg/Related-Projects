package circuitanalysistext2;

import java.util.ArrayList;

public class Source extends Load{
    double p;
    ArrayList<Load> seriesLoad = new ArrayList();
    ArrayList<Load> parallelLoad = new ArrayList();
    
    //Constructor
    public Source(double v){
        super(v);
        p = -1;
        i = -1;
        r = -1;
    }
    
    //Adding Resistors to series
    public void addSeriesLoad(double r, String l){
        seriesLoad.add(new Load (r,l));
    }
    
    //Adding Resistors to parallel
    public void addParallelLoad(double r, String l){
        parallelLoad.add(new Load (r,l));
    }
    
    //Gets how deep the circuit is for parallel
    private int getParallelGroups(){
        int depth = 0;
        if (parallelLoad.size()>0){
            depth ++;
        }
        
        for(int j=0; j<parallelLoad.size(); j++){
            if (parallelLoad.get(j).l.contains("-")){
                depth++;
                return depth;
            }
        }
        
        return depth;
    }
    
    //Gets how deep the circuit is for series
    private int getSeriesGroups(){
        int depth = 1;
        for(int j=0; j<seriesLoad.size(); j++){
            if (seriesLoad.get(j).l.contains("-")){
                depth ++;
                return depth;
                
            }
        }
        return depth;
    }
    
    //Creates ArrayList of all the resistors
    private ArrayList<Load> gather(){
        ArrayList<Load> a = new ArrayList();
        for(int k=0; k<seriesLoad.size(); k++){
            a.add(seriesLoad.get(k));
            for(int j=0; j<parallelLoad.size(); j++){
                if(seriesLoad.get(k).l.equals(parallelLoad.get(j).l)){
                    a.add(parallelLoad.get(j));
                }
            }
        }
        return a;
    }
    
    //Creates location values for all the parallel resistors
    private ArrayList<String> getParallelPaths(){
        ArrayList<String> a = new ArrayList();
        int j,k;
        for (j=0; j<seriesLoad.size(); j++){
            for (k=0; k<parallelLoad.size(); k++){
                if (parallelLoad.get(k).l.equals(seriesLoad.get(j).l)){
                    boolean contains = false;
                    for (int u = 0; u<a.size(); u++){
                        if (parallelLoad.get(k).l.equals(a.get(u))){
                            contains = true;
                        }
                    }
                    
                    if (contains == false){
                        a.add(parallelLoad.get(k).l);
                    }
                }
            }
        }
        return a;
    }
    
    //Gets the total Resistance
    private double getTotalResistance(){
        double rt = 0;
        int j,k;
        
        //**********************************************************************
        //Start of depth 1
        if (getSeriesGroups() == 1 && getParallelGroups() == 0){
            for (j=0; j<seriesLoad.size(); j++){
                rt += seriesLoad.get(j).r;
            }
        } //End of depth 1
        
        //**********************************************************************
        //Start of depth 2
        else if (getSeriesGroups() == 1 && getParallelGroups() == 1){
            
            ArrayList<String> parallelPaths = getParallelPaths();
            ArrayList<Load> allResistors = gather();
            ArrayList<Load> nonParallelResistors = new ArrayList();
            
            for(k=0; k<parallelPaths.size();k++){
                double temporaryParallel = 0;
                for(j=0; j<allResistors.size(); j++){
                    if (parallelPaths.get(k).equals(allResistors.get(j).l)){
                        temporaryParallel += 1/allResistors.get(j).r;
                    }
                }
                rt += 1/temporaryParallel;
            }
            
            for(j=0; j<allResistors.size(); j++){
                boolean parallel = false;
                for (k=0; k<parallelPaths.size(); k++){
                    if (parallelPaths.get(k).equals(allResistors.get(j).l)){
                        parallel = true;
                    }
                }
                if (parallel == false){
                    nonParallelResistors.add(allResistors.get(j));
                }
            }

            for(k=0; k<nonParallelResistors.size(); k++){
                rt += nonParallelResistors.get(k).r;
            }
        } //End of depth 2
        
        //**********************************************************************
        //Start of depth 3
        else if(getSeriesGroups() == 2 && getParallelGroups() == 1){
            //Gather all the possible routes from the source in series
            ArrayList<String> greaterSeriesNum = new ArrayList();
            
            String check;
            for(j=0; j<seriesLoad.size();j++){
                if (seriesLoad.get(j).l.contains("-")){
                    int checkIndex = seriesLoad.get(j).l.indexOf("-");
                    check = seriesLoad.get(j).l.substring(0, checkIndex);
                } else{
                    check = seriesLoad.get(j).l;
                }
                boolean contains = false;
                for(int u=0; u<greaterSeriesNum.size(); u++){
                    if (check.equals(greaterSeriesNum.get(u))){
                        contains = true;
                    }
                }
                if (contains == false){
                    greaterSeriesNum.add(check);
                }
            }
            
            ArrayList<Load> all = gather();
            ArrayList<Load> smallerCircuit = new ArrayList();
            
            //For each series in relationship to the source
            for(j=0; j<greaterSeriesNum.size(); j++){
                for(k=0;k<all.size(); k++){
                    if (all.get(k).l.contains("-")){
                        int checkIndex = all.get(k).l.indexOf("-");
                        if (greaterSeriesNum.get(j).equals(all.get(k).l.substring(0, checkIndex))){
                            smallerCircuit.add(all.get(k));
                        }
                        
                    } else{
                        if (greaterSeriesNum.get(j).equals(all.get(k).l)){
                            smallerCircuit.add(all.get(k));
                        }
                    }
                }
                
                ArrayList<String> smallerSeriesNum = new ArrayList();
                for(k=0; k<smallerCircuit.size(); k++){
                    if(smallerCircuit.get(k).l.contains("-")){
                        int checkIndex = smallerCircuit.get(k).l.indexOf("-");
                        check = smallerCircuit.get(k).l.substring(checkIndex+1);
                        boolean contains = false;
                        for (int u=0; u<smallerSeriesNum.size(); u++){
                            if (check.equals(smallerSeriesNum.get(u))){
                                contains = true;
                            }
                        }
                        if (contains == false){
                            smallerSeriesNum.add(check);
                        }
                    }
                }
                
                double parallelTrack = 0;
                for(k=0; k<smallerSeriesNum.size();k++){
                    double track = 0;
                    int counter = 0;
                    for( int u=0; u< smallerCircuit.size(); u++){
                        boolean containsR = false;
                        if(smallerCircuit.get(u).l.contains("-")){
                            containsR = true;
                            int checkIndex = smallerCircuit.get(u).l.indexOf("-");
                            check = smallerCircuit.get(u).l.substring(checkIndex+1);
                            if (check.equals(smallerSeriesNum.get(k))){
                                track += smallerCircuit.get(u).r;
                            }
                            
                        } else{
                            containsR = true;
                            counter++;
                            if (Integer.parseInt(smallerSeriesNum.get(k)) == counter){
                                track += smallerCircuit.get(u).r;
                            }
                        }
                    }
                    parallelTrack += 1/track;
                }
                
                int counter = 0;
                
                for(k=0; k<smallerCircuit.size(); k++){
                    if (!smallerCircuit.get(k).l.contains("-")){
                        counter++;
                        boolean contains = false;
                        for(int u=0; u<smallerSeriesNum.size(); u++){
                            if (Integer.parseInt(smallerSeriesNum.get(u)) == counter){
                                contains = true;
                            }
                        }
                        if (contains == false){
                            parallelTrack += 1/smallerCircuit.get(k).r;
                        }
                    }
                }
                
                rt += 1/parallelTrack;
                smallerCircuit.clear();
            }
        }//End of depth 
        return rt;
    }
    
    //Calculates the variables for everything
    public void calculate(){
        this.r = getTotalResistance();
        this.i = this.v/this.r;
        this.p = this.i*this.v;

        
        //*********************************************************************
        //Start of depth 1
        if (getSeriesGroups() == 1 && getParallelGroups() == 0){
            for(int k=0; k<seriesLoad.size(); k++){
                seriesLoad.get(k).i = this.i;
                seriesLoad.get(k).v = seriesLoad.get(k).i*seriesLoad.get(k).r;
            }
        }//End of depth 1
        
        //*********************************************************************
        //Start of depth 2
        else if(getSeriesGroups() == 1 && getParallelGroups() == 1){
            ArrayList<String> parallelPaths = getParallelPaths();
            double voltageLeft = this.v;
                    
            for(int k=0; k<seriesLoad.size(); k++){
                boolean contain = false;
                for(int j=0; j<parallelPaths.size(); j++){
                    if (seriesLoad.get(k).l.equals(parallelPaths.get(j))){
                        contain = true;
                    }
                }
                //Calculates all resistors not in parallel 
                if (contain == false){
                    seriesLoad.get(k).i = this.i;
                    seriesLoad.get(k).v = seriesLoad.get(k).i*seriesLoad.get(k).r;
                    voltageLeft -= seriesLoad.get(k).v;
                    
                //Calculates all resistors in parallel    
                } else{
                    double rp = 1/seriesLoad.get(k).r;
                    
                    for(int u=0; u<parallelLoad.size(); u++){
                        if (seriesLoad.get(k).l.equals(parallelLoad.get(u).l)){
                            rp += 1/parallelLoad.get(u).r;
                        }
                    }
                    rp = 1/rp;

                    seriesLoad.get(k).v = this.i*rp;
                    seriesLoad.get(k).i = seriesLoad.get(k).v/seriesLoad.get(k).r;
                    
                    for (int u=0; u<parallelLoad.size(); u++){
                        if (seriesLoad.get(k).l.equals(parallelLoad.get(u).l)){
                            parallelLoad.get(u).v = seriesLoad.get(k).v;
                            parallelLoad.get(u).i = parallelLoad.get(u).v/parallelLoad.get(u).r;
                        }
                    }
                }
            }
        }
        
        //**********************************************************************
        //Start of depth 3, Another series deeper in the parallel (Recursion)
        else if (getSeriesGroups() == 2 && getParallelGroups() == 1){
            ArrayList<String> greaterSeriesNum = new ArrayList();
            
            String check;
            
            //Go through everything to get greater series groups
            for(int j=0; j<seriesLoad.size();j++){
                if (seriesLoad.get(j).l.contains("-")){
                    int checkIndex = seriesLoad.get(j).l.indexOf("-");
                    check = seriesLoad.get(j).l.substring(0, checkIndex);
                } else{
                    check = seriesLoad.get(j).l;
                }
                
                boolean contains = false;
                for(int u=0; u<greaterSeriesNum.size(); u++){
                    if (check.equals(greaterSeriesNum.get(u))){
                        contains = true;
                    }
                }
                if (contains == false){
                    greaterSeriesNum.add(check);
                }
            }//End of gathering greater series
            
            //Gather all the possible routes from the source in series
            greaterSeriesNum = new ArrayList();
            
            for(int j=0; j<seriesLoad.size();j++){
                if (seriesLoad.get(j).l.contains("-")){
                    int checkIndex = seriesLoad.get(j).l.indexOf("-");
                    check = seriesLoad.get(j).l.substring(0, checkIndex);
                } else{
                    check = seriesLoad.get(j).l;
                }
                boolean contains = false;
                for(int u=0; u<greaterSeriesNum.size(); u++){
                    if (check.equals(greaterSeriesNum.get(u))){
                        contains = true;
                    }
                }
                if (contains == false){
                    greaterSeriesNum.add(check);
                }
            }
            
            
            ArrayList<Load> all = gather();
            ArrayList<Load> smallerCircuit = new ArrayList();
            
            //For each series in relationship to the source
            for(int j=0; j<greaterSeriesNum.size(); j++){
                for(int k=0;k<all.size(); k++){
                    if (all.get(k).l.contains("-")){
                        int checkIndex = all.get(k).l.indexOf("-");
                        if (greaterSeriesNum.get(j).equals(all.get(k).l.substring(0, checkIndex))){
                            smallerCircuit.add(all.get(k));
                        }
                        
                    } else{
                        if (greaterSeriesNum.get(j).equals(all.get(k).l)){
                            smallerCircuit.add(all.get(k));
                        }
                    }
                } //End of gathering smaller circuit
                ArrayList<String> smallerSeriesNum = new ArrayList();
                for(int k=0; k<smallerCircuit.size(); k++){
                    if(smallerCircuit.get(k).l.contains("-")){
                        int checkIndex = smallerCircuit.get(k).l.indexOf("-");
                        check = smallerCircuit.get(k).l.substring(checkIndex+1);
                        boolean contains = false;
                        for (int u=0; u<smallerSeriesNum.size(); u++){
                            if (check.equals(smallerSeriesNum.get(u))){
                                contains = true;
                            }
                        }
                        if (contains == false){
                            smallerSeriesNum.add(check);
                        }
                    }
                }//End of finding deeper series
                
                //Start of getting complex seires within parallels
                double parallelTrack = 0;
                for(int k=0; k<smallerSeriesNum.size();k++){
                    double track = 0;
                    int counter = 0;
                    for( int u=0; u< smallerCircuit.size(); u++){
                        boolean containsR = false;
                        if(smallerCircuit.get(u).l.contains("-")){
                            containsR = true;
                            int checkIndex = smallerCircuit.get(u).l.indexOf("-");
                            check = smallerCircuit.get(u).l.substring(checkIndex+1);
                            if (check.equals(smallerSeriesNum.get(k))){
                                track += smallerCircuit.get(u).r;
                            }
                            
                        } else{
                            containsR = true;
                            counter++;
                            if (Integer.parseInt(smallerSeriesNum.get(k)) == counter){
                                track += smallerCircuit.get(u).r;
                            }
                        }
                    }//End of getting individual complex series within parallels
                     parallelTrack += 1/track;
                }
                
                //If they're simple series within parallels
                int counter = 0;
                for(int k=0; k<smallerCircuit.size(); k++){
                    if (!smallerCircuit.get(k).l.contains("-")){
                        counter++;
                        boolean contains = false;
                        for(int u=0; u<smallerSeriesNum.size(); u++){
                            if (Integer.parseInt(smallerSeriesNum.get(u)) == counter){
                                contains = true;
                            }
                        }
                        if (contains == false){
                            parallelTrack += 1/smallerCircuit.get(k).r;
                        }
                    }
                }
                parallelTrack = 1/parallelTrack;
                double voltsParallel = this.i*parallelTrack;
                ArrayList<Load> setVariables = new ArrayList();
                
                //Please Work --- Recursion (depth 3)
                /**/
                /*
                Source d = new Source(voltsParallel);
                
                for(int k=0; k<smallerSeriesNum.size(); k++){
                    if (smallerSeriesNum.contains("-")){
                        d.seriesLoad.add(smallerCircuit.get(k));
                    }
                }
                d.calculate();
                */
                /**/
                
                //Begin calculations for resistors with complex series
                for(int k=0; k<smallerSeriesNum.size();k++){
                    double track = 0;
                    counter = 0;
                    for( int u=0; u< smallerCircuit.size(); u++){
                        boolean containsR = false;
                        if(smallerCircuit.get(u).l.contains("-")){
                            containsR = true;
                            int checkIndex = smallerCircuit.get(u).l.indexOf("-");
                            check = smallerCircuit.get(u).l.substring(checkIndex+1);
                            if (check.equals(smallerSeriesNum.get(k))){
                                track += smallerCircuit.get(u).r;
                                setVariables.add(smallerCircuit.get(u));
                            }
                            
                        } else{
                            containsR = true;
                            counter++;
                            if (Integer.parseInt(smallerSeriesNum.get(k)) == counter){
                                track += smallerCircuit.get(u).r;
                                setVariables.add(smallerCircuit.get(u));
                            }
                        }
                    }//End of getting individual series within parallels
                    for(int u=0; u<setVariables.size(); u++){
                        setVariables.get(u).i = voltsParallel/track;
                    }
                    
                    for(int u=0; u<setVariables.size(); u++){
                        setVariables.get(u).v = setVariables.get(u).i * setVariables.get(u).r;
                    }
                    setVariables.clear();
                }//End of calculating all resistor values with complex series
                
                //Begin calculations for resistors without complex Series
                counter = 0;
                for(int k=0; k<smallerCircuit.size(); k++){
                    if (!smallerCircuit.get(k).l.contains("-")){
                        counter++;
                        boolean contains = false;
                        for(int u=0; u<smallerSeriesNum.size(); u++){
                            if (Integer.parseInt(smallerSeriesNum.get(u)) == counter){
                                contains = true;
                            }
                        }
                        if (contains == false){
                            smallerCircuit.get(k).i = voltsParallel/smallerCircuit.get(k).r;
                            smallerCircuit.get(k).v = smallerCircuit.get(k).i * smallerCircuit.get(k).r;
                        }
                    }
                }//End of recursion 
                smallerCircuit.clear();
            }//End of each series branch off
        }//End of calculate depth 3
        //**********************************************************************
        
        //describe();
    }
    
    //Rounds everything
    private void round(){
        //(double) Math.round( * 1000) / 1000;
        this.v = (double) Math.round(this.v* 1000) / 1000;
        this.r = (double) Math.round(this.r* 1000) / 1000;
        this.i = (double) Math.round(this.i* 1000) / 1000;
        this.p = (double) Math.round(this.p* 1000) / 1000;
        
        for(int j=0; j<seriesLoad.size();j++){
            seriesLoad.get(j).v = (double) Math.round(seriesLoad.get(j).v* 1000) / 1000;
            seriesLoad.get(j).r = (double) Math.round(seriesLoad.get(j).r* 1000) / 1000;
            seriesLoad.get(j).i = (double) Math.round(seriesLoad.get(j).i* 1000) / 1000;
        }
        
        for(int j=0; j<parallelLoad.size();j++){
            parallelLoad.get(j).v = (double) Math.round(parallelLoad.get(j).v* 1000) / 1000;
            parallelLoad.get(j).r = (double) Math.round(parallelLoad.get(j).r* 1000) / 1000;
            parallelLoad.get(j).i = (double) Math.round(parallelLoad.get(j).i* 1000) / 1000;
        }
    }
    
    //Describes all the resistors
    public void describe(){
        int counter = 1;
        round();
        ArrayList<Load> a = gather();
        
        System.out.println("name\tV\tR\tI\tW");
        System.out.println("S\t"+this.v+"\t"+this.r+"\t"+this.i+"\t"+this.p);
        System.out.println();
        
        for(int i=0; i<a.size(); i++){
            System.out.println("R"+counter+"\t"+a.get(i).v+"\t"+a.get(i).r+"\t"+a.get(i).i+"\t*");
            counter++;
        }
    }
    
    //Case Testing
    public void test(){
        ArrayList<Load> a = new ArrayList();
        for(int j=0; j<seriesLoad.size();j++){
            a.add(seriesLoad.get(j));
        }
        for(int j=0; j<a.size(); j++){
            a.get(j).r += 1;
        }
    }
}
