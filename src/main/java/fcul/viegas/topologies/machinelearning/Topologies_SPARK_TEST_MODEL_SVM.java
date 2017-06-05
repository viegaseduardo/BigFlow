/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.bigflow.definitions.Definitions;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;
import scala.Tuple2;

/**
 *
 * @author viegas
 */
public class Topologies_SPARK_TEST_MODEL_SVM {
    
    public static void runTopology(String pathModel, String pathTest, String outputPath, String featureSet) throws Exception {

        Definitions.SPARK_FEATURE_SET = featureSet;

        SparkConf sparkConf = new SparkConf().setAppName("Topologies_SPARK_TEST_MODEL");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        File directory = new File(pathTest);

        SVMModel model = SVMModel.load(jsc.sc(), pathModel);

        String[] directoryContents = directory.list();

        List<String> fileLocations = new ArrayList<String>();

        for (String fileName : directoryContents) {
            File temp = new File(String.valueOf(directory), fileName);
            fileLocations.add(String.valueOf(temp));
        }

        java.util.Collections.sort(fileLocations);

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

        MultivariateStatisticalSummary summary = Statistics.colStats(inputDataTrain.map(new Function<LabeledPoint, Vector>() {
            @Override
            public Vector call(LabeledPoint t1) throws Exception {
                return t1.features();
            }
        }).rdd());

        for (String fileName : fileLocations) {
            if (fileName.contains(".strat") || fileName.contains(".propstrat") || fileName.contains(".arff")) {

                JavaRDD<String> fileArff = jsc.textFile(fileName);

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

                inputData = inputData.map(new Function<LabeledPoint, LabeledPoint>() {
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
                
                

                JavaRDD<LabeledPoint> inputDataNormal = inputData.filter(new Function<LabeledPoint, Boolean>() {
                    @Override
                    public Boolean call(LabeledPoint t1) throws Exception {
                        return t1.label() == 0.0d;
                    }
                });

                JavaRDD<LabeledPoint> inputDataAnomalous = inputData.filter(new Function<LabeledPoint, Boolean>() {
                    @Override
                    public Boolean call(LabeledPoint t1) throws Exception {
                        return t1.label() == 1.0d;
                    }
                });

                JavaRDD<LabeledPoint> inputDataSuspicious = inputData.filter(new Function<LabeledPoint, Boolean>() {
                    @Override
                    public Boolean call(LabeledPoint t1) throws Exception {
                        return t1.label() == 2.0d;
                    }
                });

                JavaPairRDD<Double, Double> predictionAndLabelNormal
                        = inputDataNormal.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
                            @Override
                            public Tuple2<Double, Double> call(LabeledPoint p) {
                                return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
                            }
                        });

                JavaPairRDD<Double, Double> predictionAndLabelAnomalous
                        = inputDataAnomalous.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
                            @Override
                            public Tuple2<Double, Double> call(LabeledPoint p) {
                                return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
                            }
                        });

                JavaPairRDD<Double, Double> predictionAndLabelSuspicious
                        = inputDataSuspicious.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
                            @Override
                            public Tuple2<Double, Double> call(LabeledPoint p) {
                                return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
                            }
                        });

                double tNegative = predictionAndLabelNormal.filter(new Function<Tuple2<Double, Double>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<Double, Double> pl) {
                        return pl._1().equals(pl._2());
                    }
                }).count();

                double tPositiveAnomalous = predictionAndLabelAnomalous.filter(new Function<Tuple2<Double, Double>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<Double, Double> pl) {
                        return pl._1().equals(pl._2());
                    }
                }).count();

                double tPositiveSupicious = predictionAndLabelSuspicious.filter(new Function<Tuple2<Double, Double>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<Double, Double> pl) {
                        return pl._1().equals(pl._2());
                    }
                }).count();

                long nNormal = inputDataNormal.count();
                long nAttack = inputDataAnomalous.count();
                long nSuspicious = inputDataSuspicious.count();
                long total = inputData.count();

                double tNegativeRate = tNegative / nNormal;
                double tPositiveRateAnomalous = tPositiveAnomalous / nAttack;
                double tPositiveRateSuspicious = tPositiveSupicious / nSuspicious;

                double acc = (tNegative + tPositiveAnomalous + tPositiveSupicious) / (total);

                String result = pathModel + ";" + fileName + ";" + featureSet + ";"
                        + total + ";" + nNormal + ";" + nAttack + ";" + nSuspicious + ";"
                        + +acc + ";" + tNegativeRate + ";" + tPositiveRateAnomalous + ";" + tPositiveRateSuspicious + "\n";

                try {
                    Files.write(Paths.get(outputPath), result.getBytes(), StandardOpenOption.APPEND);
                } catch (Exception e) {
                    //exception handling left as an exercise for the reader
                }
            }
        }

        //System.out.println("Accuracy: " + accuracy);
        // Save and load model
        //System.out.println(model.toDebugString());
    }
    
}
