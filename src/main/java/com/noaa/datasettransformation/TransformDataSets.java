/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noaa.datasettransformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author djenkinsiii
 */
public class TransformDataSets {
    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {        
        ArrayList<NOAADataList> noaaList = new ArrayList<>();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("C:\\Personal\\JobSearch\\GeneralDynamics\\NOAADataList.csv"));
        } catch (FileNotFoundException e) {
        }
        StringBuilder sb = new StringBuilder();  
        String StationName = "";
        URL noaaUrl = new URL("https://ncei.noaa.gov/data/normals-daily/access/AQC00914000.csv");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(noaaUrl.openStream()))) {                        
            String inputLine;  
            int linenumber = 1;
            System.out.println("DATE,"+"ELEVATION,"+"dlysnwd25pctl,"+"dlysnwdpctallge001wi,"+"dlysnowpctallge001ti");
            while ((inputLine = in.readLine()) != null) {    
                if(linenumber != 1) {    
                    String parts = splitInTwo(inputLine.split(",",-1)[1]);                    
                    if (parts.endsWith("11") || parts.endsWith("12")) {  
                        NOAADataList dl = new NOAADataList();
                        dl.date = parts;  
                        dl.elevation = inputLine.split(",",-1)[4].concat(" ft");
                        dl.dlysnwd25pctl = inputLine.split(",",-1)[7];
                        dl.dlysnwdpctallge001wi = inputLine.split(",",-1)[30];
                        dl.dlysnowpctallge001ti = inputLine.split(",",-1)[20]; 
                        noaaList.add(dl);   
                    }   
                }
                if (linenumber == 2) {
                    StationName = inputLine.split(",",-1)[0];
                }
                linenumber++;
            }              
            String columnNamesList = "DATE,ELEVATION,dly-snwd-25pctl,dly-snwd-pctall-ge001wi,dly-snow-ge001ti";        
            sb.append(StationName).append("\n").append(columnNamesList).append("\n");
            System.out.println(StationName);
            noaaList.forEach((nl)->System.out.println(nl.date + "," + nl.elevation + "," + nl.dlysnwd25pctl +
                "," + nl.dlysnwdpctallge001wi + "," + nl.dlysnowpctallge001ti));
            noaaList.forEach((nl)->sb.append(nl.date).append(",").append(nl.elevation).append(",").append(nl.dlysnwd25pctl).append(",").append(nl.dlysnwdpctallge001wi).append(",").append(nl.dlysnowpctallge001ti).append('\n'));            
            pw.write(sb.toString());
            pw.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static class NOAADataList{
        String date; 
        String elevation;
        String dlysnwd25pctl;
        String dlysnwdpctallge001wi;
        String dlysnowpctallge001ti;        
    } 
    
    private static String splitInTwo(String str){
        String input = str.replaceAll("^\"|\"$", "");
        String[] parts = input.split("-",-1);
        return parts[1] + "-" + parts[0];
    }
}
