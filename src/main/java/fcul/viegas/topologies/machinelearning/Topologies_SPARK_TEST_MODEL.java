/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.bigflow.definitions.Definitions;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import scala.Tuple2;

/**
 *
 * @author viegas
 */
public class Topologies_SPARK_TEST_MODEL {

    public static void runTopology(String pathModel, String pathTest, String outputPath, String featureSet) throws Exception {

        Definitions.SPARK_FEATURE_SET = featureSet;

        SparkConf sparkConf = new SparkConf().setAppName("Topologies_SPARK_TEST_MODEL");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        JavaRDD<String> fileArff = jsc.textFile(pathTest);

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

        RandomForestModel model = RandomForestModel.load(jsc.sc(), pathModel);

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

        String result = pathModel + ";" + pathTest + ";" + featureSet + ";"
                + total + ";" + nNormal + ";" + nAttack + ";" + nSuspicious + ";"
                + +acc + ";" + tNegativeRate + ";" + tPositiveRateAnomalous + ";" + tPositiveRateSuspicious + "\n";

        try {
            Files.write(Paths.get(outputPath), result.getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            //exception handling left as an exercise for the reader
        }

        //System.out.println("Accuracy: " + accuracy);
        // Save and load model
        //System.out.println(model.toDebugString());
    }

}
