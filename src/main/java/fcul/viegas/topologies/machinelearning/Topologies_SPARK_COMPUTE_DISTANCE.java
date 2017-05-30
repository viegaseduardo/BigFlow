/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.bigflow.definitions.Definitions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

/**
 *
 * @author viegas
 */
public class Topologies_SPARK_COMPUTE_DISTANCE {

    public static void runTopology(String pathCluster, String pathArff, String outputPath, String featureSet) throws Exception {

        Definitions.SPARK_FEATURE_SET = featureSet;

        double vecDouble[] = new double[(featureSet.equals("NIGEL")) ? Definitions.SPARK_NIGEL_NUMBER_OF_FEATURES
                : (featureSet.equals("ORUNADA")) ? Definitions.SPARK_ORUNADA_NUMBER_OF_FEATURES
                : (featureSet.equals("MOORE")) ? Definitions.SPARK_MOORE_NUMBER_OF_FEATURES
                : Definitions.SPARK_VIEGAS_NUMBER_OF_FEATURES];
        try (BufferedReader br = new BufferedReader(new FileReader(pathCluster))) {
            String line;
            while ((line = br.readLine()) != null) {
                String split[] = line.split(",");
                for (int i = 0; i < split.length - 1; i++) {
                    vecDouble[i] = Double.valueOf(split[i]);
                }
            }
        }

        Vector vecCluster = Vectors.dense(vecDouble);

        SparkConf sparkConf = new SparkConf().setAppName("Topologies_SPARK_COMPUTE_DISTANCE");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        JavaRDD<String> fileArff = jsc.textFile(pathArff);

        JavaRDD<LabeledPoint> inputData = fileArff.map(new Function<String, LabeledPoint>() {
            @Override
            public LabeledPoint call(String line) throws Exception {
                String[] split = line.split(",");
                double[] featVec = null;
                double instClass = 0.0d;
                if (split[split.length - 2].equals("anomalous")) {
                    instClass = 1.0d;
                } else if (split[split.length - 2].equals("suspicious")) {
                    instClass = 2.0d;
                } else {
                    instClass = 0.0d;
                }

                if (featureSet.contains("MOORE")) {
                    featVec = new double[Definitions.SPARK_MOORE_NUMBER_OF_FEATURES];
                    for (int i = Definitions.SPARK_MOORE_FIRST_FEATURE_INDEX; i < Definitions.SPARK_MOORE_LAST_FEATURE_INDEX; i++) {
                        featVec[i - Definitions.SPARK_MOORE_FIRST_FEATURE_INDEX] = Double.valueOf(split[i]);
                    }
                } else if (featureSet.contains("VIEGAS")) {
                    featVec = new double[Definitions.SPARK_VIEGAS_NUMBER_OF_FEATURES];
                    for (int i = Definitions.SPARK_VIEGAS_FIRST_FEATURE_INDEX; i < Definitions.SPARK_VIEGAS_LAST_FEATURE_INDEX; i++) {
                        featVec[i - Definitions.SPARK_VIEGAS_FIRST_FEATURE_INDEX] = Double.valueOf(split[i]);
                    }
                } else if (featureSet.contains("NIGEL")) {
                    featVec = new double[Definitions.SPARK_NIGEL_NUMBER_OF_FEATURES];
                    for (int i = Definitions.SPARK_NIGEL_FIRST_FEATURE_INDEX; i < Definitions.SPARK_NIGEL_LAST_FEATURE_INDEX; i++) {
                        featVec[i - Definitions.SPARK_NIGEL_FIRST_FEATURE_INDEX] = Double.valueOf(split[i]);
                    }
                } else if (featureSet.contains("ORUNADA")) {
                    featVec = new double[Definitions.SPARK_ORUNADA_NUMBER_OF_FEATURES];
                    for (int i = Definitions.SPARK_ORUNADA_FIRST_FEATURE_INDEX; i < Definitions.SPARK_ORUNADA_LAST_FEATURE_INDEX; i++) {
                        featVec[i - Definitions.SPARK_ORUNADA_FIRST_FEATURE_INDEX] = Double.valueOf(split[i]);
                    }
                }

                Vector vec = Vectors.dense(featVec);
                return new LabeledPoint(instClass, vec);
            }
        });

        JavaRDD<LabeledPoint> inputDataNormal = inputData.filter(new Function<LabeledPoint, Boolean>() {
            @Override
            public Boolean call(LabeledPoint t1) throws Exception {
                return t1.label() == 0.0d;
            }
        });

        JavaRDD<Vector> vecNormal = inputDataNormal.map(new Function<LabeledPoint, Vector>() {
            @Override
            public Vector call(LabeledPoint t1) throws Exception {
                return t1.features();
            }
        });

        JavaRDD<LabeledPoint> inputDataSuspicious = inputData.filter(new Function<LabeledPoint, Boolean>() {
            @Override
            public Boolean call(LabeledPoint t1) throws Exception {
                return t1.label() == 2.0d;
            }
        });

        JavaRDD<Vector> vecSuspicious = inputDataSuspicious.map(new Function<LabeledPoint, Vector>() {
            @Override
            public Vector call(LabeledPoint t1) throws Exception {
                return t1.features();
            }
        });

        JavaRDD<LabeledPoint> inputDataAnomalous = inputData.filter(new Function<LabeledPoint, Boolean>() {
            @Override
            public Boolean call(LabeledPoint t1) throws Exception {
                return t1.label() == 1.0d;
            }
        });

        JavaRDD<Vector> vecAnomalous = inputDataAnomalous.map(new Function<LabeledPoint, Vector>() {
            @Override
            public Vector call(LabeledPoint t1) throws Exception {
                return t1.features();
            }
        });

        JavaRDD<Double> vecDistanceNormal = vecNormal.map(new Function<Vector, Double>() {
            @Override
            public Double call(Vector t1) throws Exception {
                double[] vecClusterDouble = vecCluster.toArray();
                double[] vect1 = t1.toArray();
                double diff_square_sum = 0.0;
                for (int i = 0; i < vecClusterDouble.length; i++) {
                    diff_square_sum += (vecClusterDouble[i] - vect1[i]) * (vecClusterDouble[i] - vect1[i]);
                }
                return Math.sqrt(diff_square_sum);
            }
        });

        JavaDoubleRDD vecDoubleNormal = vecDistanceNormal.mapToDouble(new DoubleFunction<Double>() {
            @Override
            public double call(Double t) throws Exception {
                return t;
            }
        });

        JavaRDD<Double> vecDistanceSuspicious = vecSuspicious.map(new Function<Vector, Double>() {
            @Override
            public Double call(Vector t1) throws Exception {
                double[] vecClusterDouble = vecCluster.toArray();
                double[] vect1 = t1.toArray();
                double diff_square_sum = 0.0;
                for (int i = 0; i < vecClusterDouble.length; i++) {
                    diff_square_sum += (vecClusterDouble[i] - vect1[i]) * (vecClusterDouble[i] - vect1[i]);
                }
                return Math.sqrt(diff_square_sum);
            }
        });

        JavaDoubleRDD vecDoubleSuspicious = vecDistanceSuspicious.mapToDouble(new DoubleFunction<Double>() {
            @Override
            public double call(Double t) throws Exception {
                return t;
            }
        });

        JavaRDD<Double> vecDistanceAnomalous = vecAnomalous.map(new Function<Vector, Double>() {
            @Override
            public Double call(Vector t1) throws Exception {
                double[] vecClusterDouble = vecCluster.toArray();
                double[] vect1 = t1.toArray();
                double diff_square_sum = 0.0;
                for (int i = 0; i < vecClusterDouble.length; i++) {
                    diff_square_sum += (vecClusterDouble[i] - vect1[i]) * (vecClusterDouble[i] - vect1[i]);
                }
                return Math.sqrt(diff_square_sum);
            }
        });

        JavaDoubleRDD vecDoubleAnomalous = vecDistanceAnomalous.mapToDouble(new DoubleFunction<Double>() {
            @Override
            public double call(Double t) throws Exception {
                return t;
            }
        });

        double minNormal = vecDoubleNormal.min();
        double maxNormal = vecDoubleNormal.max();
        double minSuspicious = vecDoubleSuspicious.min();
        double maxSuspicious = vecDoubleSuspicious.max();
        double minAnomalous = vecDoubleAnomalous.min();
        double maxAnomalous = vecDoubleAnomalous.max();

        double[] buckets = new double[1000];

        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");

        double increment = minNormal;
        for (int i = 0; i < 1000; i++) {
            buckets[i] = increment;
            increment += (maxNormal - minNormal) / 1000.0f;
        }
        long[] normalHistogram = vecDoubleNormal.histogram(buckets);

        writer.println("Normal avg: " + vecDoubleNormal.mean() + " min: " + minNormal + " max: " + maxNormal);

        for (int i = 0; i < buckets.length - 1; i++) {
            writer.printf("[%f-%f];%d\n", buckets[i], buckets[i + 1], normalHistogram[i]);
        }

        increment = minSuspicious;
        for (int i = 0; i < 1000; i++) {
            buckets[i] = increment;
            increment += (maxSuspicious - minSuspicious) / 1000.0f;
        }
        long[] suspiciousHistogram = vecDoubleSuspicious.histogram(buckets);
        writer.println("Suspicious avg: " + vecDoubleSuspicious.mean() + " min: " + minSuspicious + " max: " + maxSuspicious);

        for (int i = 0; i < buckets.length - 1; i++) {
            writer.printf("[%f-%f];%d\n", buckets[i], buckets[i + 1], suspiciousHistogram[i]);
        }

        increment = minAnomalous;
        for (int i = 0; i < 1000; i++) {
            buckets[i] = increment;
            increment += (maxAnomalous - minAnomalous) / 1000.0f;
        }
        long[] anomalousHistogram = vecDoubleAnomalous.histogram(buckets);
        writer.println("Normal avg: " + vecDoubleAnomalous.mean() + " min: " + minAnomalous + " max: " + maxAnomalous);

        for (int i = 0; i < buckets.length - 1; i++) {
            writer.printf("[%f-%f];%d\n", buckets[i], buckets[i + 1], anomalousHistogram[i]);
        }

        writer.close();

    }

}
