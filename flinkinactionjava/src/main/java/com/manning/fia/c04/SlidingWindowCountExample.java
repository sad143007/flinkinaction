package com.manning.fia.c04;

import com.manning.fia.transformations.media.NewsFeedMapper;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.shaded.com.google.common.base.Throwables;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

/**
 * Created by hari on 5/30/16.
 */
public class SlidingWindowCountExample {

    public void executeJob() {
        try {
            final StreamExecutionEnvironment execEnv = StreamExecutionEnvironment.createLocalEnvironment(1);
            final DataStream<String> socketStream = execEnv.socketTextStream("localhost", 9000);
            final DataStream<Tuple5<Long, String, String, String, Long>> selectDS = socketStream.map(new NewsFeedMapper());
            final KeyedStream<Tuple5<Long, String, String, String, Long>, Tuple> keyedDS = selectDS.keyBy(1, 2);
            final WindowedStream<Tuple5<Long, String, String, String, Long>, Tuple, GlobalWindow> windowedStream = keyedDS
                    .countWindow(4, 1);
            final DataStream<Tuple5<Long, String, String, String, Long>> result = windowedStream.
                    sum(4);
            final DataStream<Tuple3<String, String, Long>> projectedResult = result.project(1, 2, 4);
            projectedResult.print();
            execEnv.execute("Sliding Count Window");

        } catch (Exception ex) {
            Throwables.propagate(ex);
        }
    }

    public static void main(String[] args) throws Exception {
        new NewsFeedSocket().start();
        final SlidingWindowCountExample window = new SlidingWindowCountExample();
        window.executeJob();

    }
}
