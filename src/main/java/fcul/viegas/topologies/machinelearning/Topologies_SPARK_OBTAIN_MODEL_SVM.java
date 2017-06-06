/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.bigflow.definitions.Definitions;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.optimization.L1Updater;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;

/**
 *
 * @author viegas
 */
public class Topologies_SPARK_OBTAIN_MODEL_SVM {

    public static void runTopology(String path, String featureSet) throws Exception {

        Definitions.SPARK_FEATURE_SET = featureSet;

        SparkConf sparkConf = new SparkConf().setAppName("Topologies_SPARK_OBTAIN_MODEL");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        JavaRDD<String> fileArff = jsc.textFile(path);

        JavaRDD<LabeledPoint> inputData = fileArff.map(new Function<String, LabeledPoint>() {
            @Override
            public LabeledPoint call(String line) throws Exception {
                String[] split = line.split(",");
                double[] featVec = null;
                double instClass = 0.0d;
                if (split[split.length - 2].equals("anomalous")) {
                    instClass = 1.0d;
                } else if (split[split.length - 2].equals("suspicious")) {
                    instClass = 1.0d;
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

        JavaRDD<String> fileArffTrain = jsc.textFile("/home/projeto/disco/stratprop/1week");

        JavaRDD<LabeledPoint> inputDataTrain = fileArffTrain.map(new Function<String, LabeledPoint>() {
            @Override
            public LabeledPoint call(String line) throws Exception {
                String[] split = line.split(",");
                double[] featVec = null;
                double instClass = 0.0d;
                if (split[split.length - 2].equals("anomalous")) {
                    instClass = 1.0d;
                } else if (split[split.length - 2].equals("suspicious")) {
                    instClass = 1.0d;
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

        MultivariateStatisticalSummary summary = Statistics.colStats(inputDataTrain.map(new Function<LabeledPoint, Vector>() {
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
                    if (summary.max().toArray()[i] - summary.min().toArray()[i] > 0) {
                        feats[i] = (feats[i] - summary.min().toArray()[i]) / (summary.max().toArray()[i] - summary.min().toArray()[i])
                                * 2 - 1;
                    } else {
                        feats[i] = 0.0d;
                    }
                }

                Vector vec = Vectors.dense(feats);
                return new LabeledPoint(t1.label(), vec);
            }
        });

        int numIterations = 100;

//        SVMWithSGD svmAlg = new SVMWithSGD();
//        svmAlg.optimizer()
//                .setNumIterations(1000)
//                .setRegParam(0.1)
//                .setUpdater(new L1Updater());
//        final SVMModel model = svmAlg.run(inputDataNormalized.rdd());
        final SVMModel model = SVMWithSGD.train(inputDataNormalized.rdd(), numIterations);

        model.save(jsc.sc(), path + "_svm3" + featureSet);
    }

}
