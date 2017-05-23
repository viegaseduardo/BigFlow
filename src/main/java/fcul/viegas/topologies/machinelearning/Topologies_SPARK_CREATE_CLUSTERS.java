/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.bigflow.definitions.Definitions;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import scala.Tuple2;

/**
 *
 * @author viegas
 */
public class Topologies_SPARK_CREATE_CLUSTERS {

    public static void runTopology(String pathArff, int numberOfClusters, String outputPath, String featureSet) throws Exception {

        Definitions.SPARK_FEATURE_SET = featureSet;

        SparkConf sparkConf = new SparkConf().setAppName("JavaDecisionTreeClassificationExample");
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

        int numClusters = numberOfClusters;
        int numIterations = 20;
        KMeansModel clusters = KMeans.train(vecNormal.rdd(), numClusters, numIterations);

        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");

        for (Vector center : clusters.clusterCenters()) {
            double vec[] = center.toArray();
            for (double d : vec) {
                writer.print(d + ",");
            }
            writer.println("normal");
        }
        
        writer.close();

    }

}
