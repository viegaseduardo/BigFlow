/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.timestamp;

import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 *
 * @author viegas
 */
public class NetworkPacketTimestampAssigner extends BoundedOutOfOrdernessTimestampExtractor<NetworkPacketDTO> {
    
    public NetworkPacketTimestampAssigner(Time BoundedOutOfOrdernessTimestampExtractor){
        super(BoundedOutOfOrdernessTimestampExtractor);
    }

    @Override
    public long extractTimestamp(NetworkPacketDTO t) {
        long timestamp = t.getTimestamp() / 1000;
        return timestamp;
    }

    
    /*
    private long maxTimestamp = 0l;
    private long lastEmittedWatermark = Long.MIN_VALUE;
    private final long maxLatenessSize = 500l;

    @Override
    public long extractTimestamp(NetworkPacketDTO t, long l) {
        long timestamp = t.getTimestamp()/1000;
        maxTimestamp = Math.max(timestamp, maxTimestamp);
        return timestamp;
    }

    @Override
    public Watermark getCurrentWatermark() {
        long watermarkTime = this.maxTimestamp - this.maxLatenessSize;
        Watermark watermark = new Watermark(maxTimestamp - maxLatenessSize);
        return watermark;
    }
    
     /*

    private long maxTimestamp = 0l;
    private final long maxLatenessSize = 500l;

    @Override
    public long extractTimestamp(NetworkPacketDTO t, long l) {
        maxTimestamp = Math.max(t.getTimestamp(), maxTimestamp);
        return t.getTimestamp() / 1000;
    }

    @Override
    public Watermark getCurrentWatermark() {
        Watermark watermark = new Watermark((long) ((maxTimestamp / 1000.0f) - maxLatenessSize));
        return watermark;
    }
*/

}
