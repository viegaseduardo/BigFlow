/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.windows.feature.extractor;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.dto.Features_Class_DTO;
import fcul.viegas.bigflow.dto.Features_DTO;
import fcul.viegas.bigflow.extractors.Features_CLASS_ASSIGNER;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

/**
 *
 * @author viegas
 */
public class FeatureClassAssigner extends RichMapFunction<Features_DTO, Features_DTO> {

    private ArrayList<Features_Class_DTO> featuresClasses;
    private String classDescriptionFile;
    private IntCounter toFile = new IntCounter();
    
    

    public FeatureClassAssigner(String classDescriptionFile) {
        this.classDescriptionFile = classDescriptionFile;
    }

    public void open(Configuration parameters) {
        
        getRuntimeContext().addAccumulator("toFile", this.toFile);

        this.featuresClasses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(this.classDescriptionFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");

                Features_Class_DTO featuresClass = new Features_Class_DTO();

                featuresClass.setAnomalyID(Integer.valueOf(split[0]));
                if (split[1].length() == 0) {
                    featuresClass.setSrcIP(Definitions.ANY_IP_ADDRESS);
                } else {
                    featuresClass.setSrcIP(split[1]);
                }
                if (split[2].length() == 0) {
                    featuresClass.setSrcPort(Definitions.ANY_PORT_ADDRESS);
                } else {
                    featuresClass.setSrcPort(Integer.valueOf(split[2]));
                }
                if (split[3].length() == 0) {
                    featuresClass.setDstIP(Definitions.ANY_IP_ADDRESS);
                } else {
                    featuresClass.setDstIP(split[3]);
                }
                if (split[4].length() == 0) {
                    featuresClass.setDstPort(Definitions.ANY_PORT_ADDRESS);
                } else {
                    featuresClass.setDstPort(Integer.valueOf(split[4]));
                }

                featuresClass.setTaxonomy(split[5]);
                //for some reason in mawilab it is stated that there is a field name heuristic however the field is missing in the csv files
                featuresClass.setLabel(split[split.length - 1]);
                featuresClass.setNbDetectors(Integer.valueOf(split[split.length - 2]));
                featuresClass.setDistance(Float.valueOf(split[split.length - 3]));

                this.featuresClasses.add(featuresClass);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public Features_DTO map(Features_DTO featDTO) throws Exception {
        
        this.toFile.add(1);
        
        Features_CLASS_ASSIGNER.extractFeatures(featuresClasses, featDTO);
        return featDTO;
    }

}
