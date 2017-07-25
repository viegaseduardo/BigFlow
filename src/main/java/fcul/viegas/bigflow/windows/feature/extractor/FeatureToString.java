/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.windows.feature.extractor;

import fcul.viegas.bigflow.dto.Features_DTO;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;

/**
 *
 * @author viegas
 */
public class FeatureToString extends RichMapFunction<Features_DTO, String> {

    @Override
    public String map(Features_DTO in) throws Exception {
        return in.toString();
    }
    
}
