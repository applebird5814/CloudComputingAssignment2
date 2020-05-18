package com.example.cloudass2.util;

/**
 * @author miaos
 */
public class COVIDnode {
    private String location;
    private long increased ;
    private long todayTotal;
    private long cure;
    private long dead;
    private long yesterdayTotal;
    private String outPrintString;

    public COVIDnode(String location, long todayTotal, long cure, long dead) {
        this.location = location;
        this.todayTotal = todayTotal;
        this.cure = cure;
        this.dead = dead;

    }

    public void setIncreased(long yesterdayTotal){
        increased=todayTotal-yesterdayTotal;
        outPrintString="Up to now, the number of newly diagnosed people in "+location+" is " +
                increased+", and there are currently " +todayTotal+
                " confirmed people, " +cure+
                " cured, and " +dead+
                " dead.";
    }

    public String getOutPrintString() {
        return outPrintString;
    }
}
