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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author viegas
 */
public class ParseRawOutputFlinkNoUpdate {

    public static int MonthRange = 6;
    public static int YearRange = 4;

    public static void generateSummaryFileWithoutRejection(String rawFile, String outputFile, int range) throws Exception {
        HashMap<String, ValuesDTO> hashMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rawFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String split[] = line.split(";");
                if (split.length > 4 && !line.contains("NaN")) {
                    String month = split[0].split("/")[split[0].split("/").length - 1];
                    month = month.substring(0, range);

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

    public static int generateSummaryFileWithRejection(String rawFile, String outputFile, float normalThreshold, float attackThreshold, int range) throws Exception {
        HashMap<String, ValuesDTO> hashMap = new HashMap<>();
        int n = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(rawFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String split[] = line.split(";");
                if (split.length > 4
                        && !line.contains("NaN")
                        && Float.compare(normalThreshold, Float.valueOf(line.split(";")[1])) == 0
                        && Float.compare(attackThreshold, Float.valueOf(line.split(";")[2])) == 0) {
                    String month = split[0].split("/")[split[0].split("/").length - 1];
                    month = month.substring(0, range);

                    //System.out.println(month);
                    if (!hashMap.containsKey(month)) {
                        hashMap.put(month, new ValuesDTO());
                    }

                    hashMap.get(month).nNormal += Integer.valueOf(split[4]);
                    hashMap.get(month).nSusp += Integer.valueOf(split[6]);
                    hashMap.get(month).nAnomalous += Integer.valueOf(split[5]);
                    hashMap.get(month).floatAVGAvgAccuracy += Float.valueOf(split[14]);
                    hashMap.get(month).floatAccAccept += Float.valueOf(split[9]);
                    hashMap.get(month).floatAccAcceptAttack += Float.valueOf(split[8]);
                    hashMap.get(month).floatAccAcceptNormal += Float.valueOf(split[7]);
                    hashMap.get(month).floatClassificationQuality += Float.valueOf(split[15]);
                    hashMap.get(month).floatRejection += Float.valueOf(split[13]);
                    hashMap.get(month).floatRejectionAttack += Float.valueOf(split[16]);
                    hashMap.get(month).floatRejectionNormal += Float.valueOf(split[17]);
                    hashMap.get(month).nMeasures += 1.0f;

                    n++;
                    //System.out.println("Accepted - " + split[0]);
                } else {
                    if (split.length > 4) {
                        //System.out.println("IGNORED - " + split[0]);
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

        writer.println("month;nNormal;nAnomalous;nSusp;accAccept;accAcceptNormal;"
                + "accAcceptAttack;rejectionPCT;rejectionAttackPCT;rejectionNormalPCT;"
                + "avgAVGAccuracy;classificationQuality");

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ValuesDTO values = (ValuesDTO) pair.getValue();
            String s = (String) pair.getKey();

            writer.println(s + ";"
                    + values.nNormal + ";"
                    + values.nAnomalous + ";"
                    + values.nSusp + ";"
                    + (values.floatAccAccept / values.nMeasures) + ";"
                    + (values.floatAccAcceptNormal / values.nMeasures) + ";"
                    + (values.floatAccAcceptAttack / values.nMeasures) + ";"
                    + (values.floatRejection / values.nMeasures) + ";"
                    + (values.floatRejectionAttack / values.nMeasures) + ";"
                    + (values.floatRejectionNormal / values.nMeasures) + ";"
                    + (values.floatAVGAvgAccuracy / values.nMeasures) + ";"
                    + (values.floatClassificationQuality / values.nMeasures));
        }
        writer.close();

        return n;
    }
    
    
    public static int generateSummaryFileWithRejection(String rawFile, String outputFile) throws Exception {
        HashMap<String, ValuesDTO> hashMap = new HashMap<>();
        int n = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(rawFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String split[] = line.split(";");
                if (split.length > 4
                        && !line.contains("NaN")) {
                    String rej = line.split(";")[1] + "_" + line.split(";")[2];

                    //System.out.println(month);
                    if (!hashMap.containsKey(rej)) {
                        hashMap.put(rej, new ValuesDTO());
                    }

                    hashMap.get(rej).nNormal += Integer.valueOf(split[4]);
                    hashMap.get(rej).nSusp += Integer.valueOf(split[6]);
                    hashMap.get(rej).nAnomalous += Integer.valueOf(split[5]);
                    hashMap.get(rej).floatAVGAvgAccuracy += Float.valueOf(split[14]);
                    hashMap.get(rej).floatAccAccept += Float.valueOf(split[9]);
                    hashMap.get(rej).floatAccAcceptAttack += Float.valueOf(split[8]);
                    hashMap.get(rej).floatAccAcceptNormal += Float.valueOf(split[7]);
                    hashMap.get(rej).floatClassificationQuality += Float.valueOf(split[15]);
                    hashMap.get(rej).floatRejection += Float.valueOf(split[13]);
                    hashMap.get(rej).floatRejectionAttack += Float.valueOf(split[16]);
                    hashMap.get(rej).floatRejectionNormal += Float.valueOf(split[17]);
                    hashMap.get(rej).nMeasures += 1.0f;

                    n++;
                    //System.out.println("Accepted - " + split[0]);
                } else {
                    if (split.length > 4) {
                        //System.out.println("IGNORED - " + split[0]);
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

        writer.println("month;nNormal;nAnomalous;nSusp;accAccept;accAcceptNormal;"
                + "accAcceptAttack;rejectionPCT;rejectionAttackPCT;rejectionNormalPCT;"
                + "avgAVGAccuracy;classificationQuality");

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ValuesDTO values = (ValuesDTO) pair.getValue();
            String s = (String) pair.getKey();

            writer.println(s + ";"
                    + values.nNormal + ";"
                    + values.nAnomalous + ";"
                    + values.nSusp + ";"
                    + (values.floatAccAccept / values.nMeasures) + ";"
                    + (values.floatAccAcceptNormal / values.nMeasures) + ";"
                    + (values.floatAccAcceptAttack / values.nMeasures) + ";"
                    + (values.floatRejection / values.nMeasures) + ";"
                    + (values.floatRejectionAttack / values.nMeasures) + ";"
                    + (values.floatRejectionNormal / values.nMeasures) + ";"
                    + (values.floatAVGAvgAccuracy / values.nMeasures) + ";"
                    + (values.floatClassificationQuality / values.nMeasures));
        }
        writer.close();

        return n;
    }

}
