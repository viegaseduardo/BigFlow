/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies;

import fcul.viegas.bigflow.definitions.Definitions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author viegas
 */
public class Topologies_ARFF_SPLIT_FEATURE_SET {

    public static PrintWriter[] createFiles(String path, String name) throws Exception {
        PrintWriter writerVIEGAS = new PrintWriter(path + "_VIEGAS_" + name, "UTF-8");
        PrintWriter writerORUNADA = new PrintWriter(path + "_ORUNADA_" + name, "UTF-8");
        PrintWriter writerNIGEL = new PrintWriter(path + "_NIGEL_" + name, "UTF-8");
        PrintWriter writerMOORE = new PrintWriter(path + "_MOORE_" + name, "UTF-8");

        PrintWriter[] print = new PrintWriter[4];

        print[0] = writerVIEGAS;
        print[1] = writerORUNADA;
        print[2] = writerNIGEL;
        print[3] = writerMOORE;

        return print;
    }

    public static void runTopology(String featureSetPath, String newFeatureSetPath) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(featureSetPath));

        HashMap<String, PrintWriter[]> map = new HashMap<String, PrintWriter[]>();

        PrintWriter writerVIEGAS;
        PrintWriter writerORUNADA;
        PrintWriter writerNIGEL;
        PrintWriter writerMOORE;

        String line;
        
        while ((line = br.readLine()) != null) {
            int i = 0;
            String[] split = line.split(Definitions.FIELD_DELIM_WEKA);
            //String type = split[split.length - 5];
            String type = "unique";

            PrintWriter[] prints = map.get(type);
            if (prints == null) {
                prints = Topologies_ARFF_SPLIT_FEATURE_SET.createFiles(newFeatureSetPath, "unique");
                map.put("unique", prints);
            }
            writerVIEGAS = prints[0];
            writerORUNADA = prints[1];
            writerNIGEL = prints[2];
            writerMOORE = prints[3];

            String viegas = "";
            String orunada = "";
            String nigel = "";
            String moore = "";

            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[i++] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[split.length - 5] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[split.length - 4] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[split.length - 3] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[split.length - 2] + Definitions.FIELD_DELIM_WEKA;
            orunada = orunada + split[split.length - 1];

            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[i++] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[split.length - 5] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[split.length - 4] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[split.length - 3] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[split.length - 2] + Definitions.FIELD_DELIM_WEKA;
            nigel = nigel + split[split.length - 1];

            for (int j = 0; j < 58; j++) {
                moore = moore + split[i++] + Definitions.FIELD_DELIM_WEKA;
            }
            moore = moore + split[split.length - 5] + Definitions.FIELD_DELIM_WEKA;
            moore = moore + split[split.length - 4] + Definitions.FIELD_DELIM_WEKA;
            moore = moore + split[split.length - 3] + Definitions.FIELD_DELIM_WEKA;
            moore = moore + split[split.length - 2] + Definitions.FIELD_DELIM_WEKA;
            moore = moore + split[split.length - 1];

            for (int j = 0; j < 48; j++) {
                viegas = viegas + split[i++] + Definitions.FIELD_DELIM_WEKA;
            }
            viegas = viegas + split[split.length - 5] + Definitions.FIELD_DELIM_WEKA;
            viegas = viegas + split[split.length - 4] + Definitions.FIELD_DELIM_WEKA;
            viegas = viegas + split[split.length - 3] + Definitions.FIELD_DELIM_WEKA;
            viegas = viegas + split[split.length - 2] + Definitions.FIELD_DELIM_WEKA;
            viegas = viegas + split[split.length - 1];

            writerVIEGAS.println(viegas);
            writerORUNADA.println(orunada);
            writerNIGEL.println(nigel);
            writerMOORE.println(moore);
        }

        br.close();

        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            PrintWriter[] prints = (PrintWriter[]) mentry.getValue();
            prints[0].close();
            prints[1].close();
            prints[2].close();
            prints[3].close();
        }
    }

}
