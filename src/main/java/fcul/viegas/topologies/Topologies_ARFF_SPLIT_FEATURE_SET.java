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

/**
 *
 * @author viegas
 */
public class Topologies_ARFF_SPLIT_FEATURE_SET {

    public static void runTopology(String featureSetPath) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(featureSetPath));

        PrintWriter writerVIEGAS = new PrintWriter(featureSetPath + "_VIEGAS", "UTF-8");
        PrintWriter writerORUNADA = new PrintWriter(featureSetPath + "_ORUNADA", "UTF-8");
        PrintWriter writerNIGEL = new PrintWriter(featureSetPath + "_NIGEL", "UTF-8");
        PrintWriter writerMOORE = new PrintWriter(featureSetPath + "_MOORE", "UTF-8");

        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(Definitions.FIELD_DELIM_WEKA);

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
        writerVIEGAS.close();
        writerORUNADA.close();
        writerNIGEL.close();
        writerMOORE.close();

    }

}
