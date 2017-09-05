/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.output;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author viegas
 */
public class ParseRawOutputFlinkNoUpdate {

    public static void generateSummaryFile(String rawFile, String outputFile) throws Exception {
        HashMap<String, ValuesDTO> hashMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rawFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String split[] = line.split(";");
                if (split.length > 4 && !line.contains("NaN")) {
                    String month = split[0].split("/")[split[0].split("/").length - 1];
                    month = month.substring(0, 6);
                    //System.out.println(month);
                    if (!hashMap.containsKey(month)) {
                        hashMap.put(month, new ValuesDTO());
                    }
                    hashMap.get(month).nNormal += Integer.valueOf(split[2]);
                    hashMap.get(month).nSusp += Integer.valueOf(split[4]);
                    hashMap.get(month).nAnomalous += Integer.valueOf(split[3]);
                    hashMap.get(month).accNormal += (Float.valueOf(split[6]) * Integer.valueOf(split[2]));
                    hashMap.get(month).accAnomalous += (Float.valueOf(split[7]) * Integer.valueOf(split[3]));
                    hashMap.get(month).accSusp += (Float.valueOf(split[8]) * Integer.valueOf(split[4]));
                    System.out.println("Accepted - " + split[0]);
                } else {
                    if (split.length > 4) {
                        System.out.println("IGNORED - " + split[0]);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        TreeMap<String, ValuesDTO> sorted = new TreeMap<>(hashMap);
        Set<Entry<String, ValuesDTO>> mappings = sorted.entrySet();
        Iterator it = mappings.iterator();
        
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
        
        writer.println("month;nNormal;nAnomalous;nSusp;ACC;accnormal;accallatk;accanomalous;accsusp");

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ValuesDTO values = (ValuesDTO) pair.getValue();
            String s = (String) pair.getKey();
            float acc = (values.accAnomalous + values.accSusp + values.accNormal) / (float) (values.nAnomalous + values.nSusp + values.nNormal);
            float accNormal = values.accNormal / (float) values.nNormal;
            float accAnomalous = values.accAnomalous / (float) values.nAnomalous;
            float accSuspicious = values.accSusp / (float) values.nSusp;
            float accAllATK = (values.accAnomalous + values.accSusp) / (float) (values.nAnomalous + values.nSusp);
            writer.println(s + ";" + values.nNormal + ";" + values.nAnomalous + ";" + values.nSusp + ";"
                    + acc + ";" + accNormal + ";" + accAllATK + ";" + accAnomalous + ";" + accSuspicious);
        }
        writer.close();
        
        

    }

}
