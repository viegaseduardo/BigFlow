/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.timestamp;

import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

/**
 *
 * @author viegas
 */
public class NetworkPacketTimestampAssigner implements AssignerWithPeriodicWatermarks<NetworkPacketDTO>{
    
    private long maxTimestamp = 0l;
    private final long maxLatenessSize = 10l;

    @Override
    public long extractTimestamp(NetworkPacketDTO t, long l) {
        maxTimestamp = Math.max(t.getTimestamp(), maxTimestamp);
        return t.getTimestamp()/1000;
    }

    @Override
    public Watermark getCurrentWatermark() {
        Watermark watermark = new Watermark((long) ((maxTimestamp/1000.0f) - maxLatenessSize));
        return watermark;
    }
    
}