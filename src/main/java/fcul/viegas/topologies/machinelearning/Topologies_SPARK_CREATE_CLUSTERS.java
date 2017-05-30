/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.bigflow.definitions.Definitions;
import java.io.PrintWriter;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.feature.MaxAbsScaler;
import org.apache.spark.ml.feature.MaxAbsScalerModel;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

/**
 *
 * @author viegas
 */
public class Topologies_SPARK_CREATE_CLUSTERS {

    public static void runTopology(String pathArff, int numberOfClusters, String outputPath, String featureSet) throws Exception {

        Definitions.SPARK_FEATURE_SET = featureSet;

        SparkConf sparkConf = new SparkConf().setAppName("Topologies_SPARK_CREATE_CLUSTERS");
        SparkContext jsc = new SparkContext(sparkConf);

        SparkSession spark = new SparkSession(jsc);

        JavaRDD<String> fileArff = jsc.textFile(pathArff, 1).toJavaRDD();

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

                if (featureSet.equals("MOORE")) {
                    featVec = new double[Definitions.SPARK_MOORE_NUMBER_OF_FEATURES];
                    for (int i = Definitions.SPARK_MOORE_FIRST_FEATURE_INDEX; i < Definitions.SPARK_MOORE_LAST_FEATURE_INDEX; i++) {
                        featVec[i - Definitions.SPARK_MOORE_FIRST_FEATURE_INDEX] = Double.valueOf(split[i]);
                    }
                } else if (featureSet.equals("VIEGAS")) {
                    featVec = new double[Definitions.SPARK_VIEGAS_NUMBER_OF_FEATURES];
                    for (int i = Definitions.SPARK_VIEGAS_FIRST_FEATURE_INDEX; i < Definitions.SPARK_VIEGAS_LAST_FEATURE_INDEX; i++) {
                        featVec[i - Definitions.SPARK_VIEGAS_FIRST_FEATURE_INDEX] = Double.valueOf(split[i]);
                    }
                } else if (featureSet.equals("NIGEL")) {
                    featVec = new double[Definitions.SPARK_NIGEL_NUMBER_OF_FEATURES];
                    for (int i = Definitions.SPARK_NIGEL_FIRST_FEATURE_INDEX; i < Definitions.SPARK_NIGEL_LAST_FEATURE_INDEX; i++) {
                        featVec[i - Definitions.SPARK_NIGEL_FIRST_FEATURE_INDEX] = Double.valueOf(split[i]);
                    }
                } else if (featureSet.equals("ORUNADA")) {
                    featVec = new double[Definitions.SPARK_ORUNADA_NUMBER_OF_FEATURES];
                    for (int i = Definitions.SPARK_ORUNADA_FIRST_FEATURE_INDEX; i < Definitions.SPARK_ORUNADA_LAST_FEATURE_INDEX; i++) {
                        featVec[i - Definitions.SPARK_ORUNADA_FIRST_FEATURE_INDEX] = Double.valueOf(split[i]);
                    }
                }

                Vector vec = Vectors.dense(featVec);
                return new LabeledPoint(instClass, vec);
            }
        });

        MultivariateStatisticalSummary summary = Statistics.colStats(inputData.map(new Function<LabeledPoint, Vector>() {
            @Override
            public Vector call(LabeledPoint t1) throws Exception {
                return t1.features();
            }
        }).rdd());

        JavaRDD<LabeledPoint> inputDataNormalized = inputData.map(new Function<LabeledPoint, LabeledPoint>() {
            @Override
            public LabeledPoint call(LabeledPoint t1) throws Exception {

                double[] feats = t1.features().toArray();

                for (int i = 0; i < feats.length; i++) {
                    feats[i] = (feats[i] - summary.min().toArray()[i]) / (summary.max().toArray()[i] - summary.min().toArray()[i])
                            * 2 - 1;
                }

                Vector vec = Vectors.dense(feats);
                return new LabeledPoint(t1.label(), vec);
            }
        });

        JavaRDD<LabeledPoint> inputDataNormal = inputDataNormalized.filter(new Function<LabeledPoint, Boolean>() {
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

        JavaRDD<LabeledPoint> inputDataSuspicious = inputDataNormalized.filter(new Function<LabeledPoint, Boolean>() {
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

        JavaRDD<LabeledPoint> inputDataAnomalous = inputDataNormalized.filter(new Function<LabeledPoint, Boolean>() {
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

        int numClusters = numberOfClusters;
        int numIterations = 20;
        KMeansModel clusters = KMeans.train(vecNormal.rdd(), numClusters, numIterations);

        Vector center = clusters.clusterCenters()[0];

        JavaRDD<Double> vecDistanceNormal = vecNormal.map(new Function<Vector, Double>() {
            @Override
            public Double call(Vector t1) throws Exception {
                double[] vecClusterDouble = center.toArray();
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
                double[] vecClusterDouble = center.toArray();
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
                double[] vecClusterDouble = center.toArray();
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
        
        double maxall = maxNormal;
        if(maxall < maxSuspicious){
            maxall = maxSuspicious;
        }
        if(maxall < maxAnomalous){
            maxall = maxAnomalous;
        }

        double[] bucketsNormal = new double[1000];
        double[] bucketsAnomalous = new double[1000];
        double[] bucketsSuspicious = new double[1000];

        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");

        double increment = 0;
        for (int i = 0; i < 1000; i++) {
            bucketsNormal[i] = increment;
            increment += (maxall - 0) / 1000.0f;
        }
        long[] normalHistogram = vecDoubleNormal.histogram(bucketsNormal);

        increment = 0;
        for (int i = 0; i < 1000; i++) {
            bucketsSuspicious[i] = increment;
            increment += (maxall - 0) / 1000.0f;
        }
        long[] suspiciousHistogram = vecDoubleSuspicious.histogram(bucketsSuspicious);

        increment = 0;
        for (int i = 0; i < 1000; i++) {
            bucketsAnomalous[i] = increment;
            increment += (maxall - 0) / 1000.0f;
        }
        long[] anomalousHistogram = vecDoubleAnomalous.histogram(bucketsAnomalous);
        
        writer.println("Normal avg: " + vecDoubleNormal.mean() + " min: " + minNormal + " max: " + maxNormal);
        writer.println("Suspicious avg: " + vecDoubleSuspicious.mean() + " min: " + minSuspicious + " max: " + maxSuspicious);
        writer.println("Anomalous avg: " + vecDoubleAnomalous.mean() + " min: " + minAnomalous + " max: " + maxAnomalous);

        for (int i = 0; i < bucketsAnomalous.length - 1; i++) {
            writer.printf("%f;%f;%d;%f;%f;%d;%f;%f;%d\n", bucketsNormal[i], bucketsNormal[i + 1], normalHistogram[i],
                    bucketsSuspicious[i], bucketsSuspicious[i + 1], suspiciousHistogram[i],
                    bucketsAnomalous[i], bucketsAnomalous[i + 1], anomalousHistogram[i]);
        }

        writer.close();

    }

}
