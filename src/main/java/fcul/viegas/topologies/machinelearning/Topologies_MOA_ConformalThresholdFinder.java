/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;


import com.yahoo.labs.samoa.instances.WekaToSamoaInstanceConverter;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author viegas
 */
public class Topologies_MOA_ConformalThresholdFinder {

    public class ValueForRejectEvaluation {

        public double instClass;
        public double predictClass;
        public double credibility;
        public double confidence;
        public double alpha;
    }

    public String folderPath;
    public String featureSET;
    //public static String PathToModel = "/home/viegas/Bases2/model/model";
    public static String PathToModel = "/home/viegas/Downloads/model/model";

    public void generateThresholdEvaluationFile(
            String pathArffs,
            String featureSet,
            String outputPath,
            String classifierToBuild,
            int daysToUseForTraining,
            int daysToUseForTest,
            int numberEnsemble) throws Exception {

        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();
        ArrayList<String> testFiles = new ArrayList();

        this.folderPath = pathArffs;
        this.featureSET = featureSet;

        System.out.println("Path to test directory: " + this.folderPath + " searching for feature set: " + this.featureSET);
        mlModelBuilder.findFilesForTest(this.folderPath, featureSET, testFiles);
        java.util.Collections.sort(testFiles);

        for (String s : testFiles) {
            System.out.println("\t" + s);
        }

        System.out.println("Opening training file....");
        Instances dataTrain = mlModelBuilder.openFile(testFiles.get(0));
        for (int i = 1; i < daysToUseForTraining; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            for (Instance inst : dataTrainInc) {
                dataTrain.add(inst);
            }
        }
        dataTrain = mlModelBuilder.getAsNormalizeFeatures(dataTrain);

        if (featureSet.equals("VIEGAS")) {
            dataTrain = mlModelBuilder.removeParticularAttributesViegas(dataTrain);
        } else if (featureSet.equals("ORUNADA")) {
            dataTrain = mlModelBuilder.removeParticularAttributesOrunada(dataTrain);
        }

        System.out.println("building conformal");
        Transcend_ConformalPredictor conformal = new Transcend_ConformalPredictor();
        conformal.setDataset(dataTrain);

        System.out.println("building classifier");


        moa.classifiers.AbstractClassifier classifier = null;

        classifier = classifierToBuild.equals("hoeffding")
                ? mlModelBuilder.trainClassifierHoeffdingTreeMOA(dataTrain) : classifierToBuild.equals("hoeffdingadaptivetree")
                ? mlModelBuilder.trainClassifierHoeffingAdaptiveTreeMOA(dataTrain) : classifierToBuild.equals("ozabagging")
                ? mlModelBuilder.trainClassifierOzaBaggingMOA(dataTrain) : classifierToBuild.equals("ozaboosting")
                ? mlModelBuilder.trainClassifierOzaBoostingMOA(dataTrain, numberEnsemble) : classifierToBuild.equals("adaptiveforest")
                ? mlModelBuilder.trainClassifierAdaptiveRandomForestMOA(dataTrain) : classifierToBuild.equals("adahoeffdingoptiontree")
                ? mlModelBuilder.trainClassifierAdaHoeffdingOptionTreeMOA(dataTrain) : classifierToBuild.equals("ocboost")
                ? mlModelBuilder.trainClassifierOCBoostMOA(dataTrain, numberEnsemble) : classifierToBuild.equals("leveragingbag")
                ? mlModelBuilder.trainClassifierLeveragingBagMOA(dataTrain) : null;

        ArrayList<ValueForRejectEvaluation> listValuesPredictedNormal = new ArrayList<>();
        ArrayList<ValueForRejectEvaluation> listValuesPredictedAttack = new ArrayList<>();

        int j = 0;
        for (String s : testFiles.subList(0, daysToUseForTest)) {

            Instances dataTest = mlModelBuilder.openFile(s);
            dataTest = mlModelBuilder.getAsNormalizeFeatures(dataTest);

            if (featureSet.equals("VIEGAS")) {
                dataTest = mlModelBuilder.removeParticularAttributesViegas(dataTest);
            } else if (featureSet.equals("ORUNADA")) {
                dataTest = mlModelBuilder.removeParticularAttributesOrunada(dataTest);
            }

            WekaToSamoaInstanceConverter converterViegas = new WekaToSamoaInstanceConverter();
            com.yahoo.labs.samoa.instances.Instances insts = converterViegas.samoaInstances(dataTest);

            for (int counter = 0; counter < insts.size(); counter++) {
                com.yahoo.labs.samoa.instances.Instance inst = insts.get(counter);

                ValueForRejectEvaluation values = new ValueForRejectEvaluation();

                values.instClass = inst.classValue();

                double[] prob = classifier.getVotesForInstance(inst);
                prob = Arrays.copyOf(prob, inst.numClasses());

                if (prob[0] > prob[1]) {
                    values.predictClass = 0.0d;
                    values.credibility = conformal.getPValueForNormal(dataTest.get(counter));
                    values.confidence = 1.0f - conformal.getPValueForAttack(dataTest.get(counter));
                    values.alpha = values.credibility + values.confidence;
                    //values.alpha = prob[0];

                    listValuesPredictedNormal.add(values);
                } else {
                    values.predictClass = 1.0d;
                    values.credibility = conformal.getPValueForAttack(dataTest.get(counter));
                    values.confidence = 1.0f - conformal.getPValueForNormal(dataTest.get(counter));
                    values.alpha = values.credibility + values.confidence;

                    //values.alpha = prob[1];

                    listValuesPredictedAttack.add(values);
                }
            }
            System.out.println(s);
        }

        Collections.sort(listValuesPredictedNormal, new Comparator<ValueForRejectEvaluation>() {
            @Override
            public int compare(ValueForRejectEvaluation o1, ValueForRejectEvaluation o2) {
                return Double.compare(o2.alpha, o1.alpha);
            }
        });

        Collections.sort(listValuesPredictedAttack, new Comparator<ValueForRejectEvaluation>() {
            @Override
            public int compare(ValueForRejectEvaluation o1, ValueForRejectEvaluation o2) {
                return Double.compare(o2.alpha, o1.alpha);
            }
        });

        System.out.println("Primeiro alpha: " + listValuesPredictedNormal.get(0).alpha);
        System.out.println("Ultimo alpha: " + listValuesPredictedNormal.get(listValuesPredictedNormal.size() - 1).alpha);

        System.out.println("listValuesPredictedNormal.size(): " + listValuesPredictedNormal.size());
        System.out.println("listValuesPredictedAttack.size(): " + listValuesPredictedAttack.size());

        int pingNormal = (int) (listValuesPredictedNormal.size() / 100.0f);
        int pingAttack = (int) (listValuesPredictedAttack.size() / 100.0f);
        for (int iNormal = 0; iNormal < 100; iNormal++) {
            System.out.println("Normal: [" + iNormal + "]: " + listValuesPredictedNormal.get(pingNormal * iNormal).alpha);
        }
        for (int iAttack = 0; iAttack < 100; iAttack++) {
            System.out.println("Attack: [" + iAttack + "]: " + listValuesPredictedAttack.get(pingAttack * iAttack).alpha);
        }

        ArrayList<String> outputList = new ArrayList<>();

        for (int iNormal = 0; iNormal < 100; iNormal++) {
            System.out.println("Classifier: " + classifierToBuild + " " + this.featureSET +" percentage done: " + iNormal);
            for (int iAttack = 0; iAttack < 100; iAttack++) {

                int n = 0;
                int nAcc = 0;
                int nAtk = 0;
                int nAtkAcc = 0;
                int nNormal = 0;
                int nNormalAcc = 0;

                int nToUseNormal = pingNormal * iNormal;
                int nToUseAttack = pingAttack * iAttack;

                for (j = 0; j < nToUseNormal; j++) {
                    n++;

                    if (listValuesPredictedNormal.get(j).instClass == 0.0d) {
                        nNormal++;
                    } else {
                        nAtk++;
                    }

                    if (Double.compare(listValuesPredictedNormal.get(j).instClass, listValuesPredictedNormal.get(j).predictClass) == 0) {
                        nAcc++;
                        if (listValuesPredictedNormal.get(j).instClass == 0.0d) {
                            nNormalAcc++;
                        } else {
                            nAtkAcc++;
                        }
                    }

                }

                for (j = 0; j < nToUseAttack; j++) {
                    n++;

                    if (listValuesPredictedAttack.get(j).instClass == 0.0d) {
                        nNormal++;
                    } else {
                        nAtk++;
                    }

                    if (Double.compare(listValuesPredictedAttack.get(j).instClass, listValuesPredictedAttack.get(j).predictClass) == 0) {
                        nAcc++;
                        if (listValuesPredictedAttack.get(j).instClass == 0.0d) {
                            nNormalAcc++;
                        } else {
                            nAtkAcc++;
                        }
                    }

                }

                if (nNormal == 0) {
                    nNormal = 1;
                }
                if (nAtk == 0) {
                    nAtk = 1;
                }
                if (n == 0) {
                    n = 1;
                }


                outputList.add(iAttack + ";"
                        + iNormal + ";"
                        + (1 - (nAcc / (float) n)) + ";"
                        + (1 - (nNormalAcc / (float) nNormal)) + ";"
                        + (1 - (nAtkAcc / (float) nAtk)) + ";"
                        + nNormalAcc + ";"
                        + nNormal + ";"
                        + nAtkAcc + ";"
                        + nAtk + ";"
                        + ((nToUseNormal + nToUseAttack) / (float) (listValuesPredictedAttack.size() + listValuesPredictedNormal.size())) + ";"
                        + listValuesPredictedNormal.get(pingNormal * iNormal).alpha + ";"
                        + listValuesPredictedAttack.get(pingAttack * iAttack).alpha);
            }
        }

        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");

        for (String s : outputList) {
            writer.println(s);
        }
        writer.close();

    }

    public class NonDominated {

        public float error;
        public float rejection;
        public String line;
        public boolean toberemoved;

    }

    public void getNonDominatedSolutions(
            String fileInputPath,
            String fileOutputPath) {

        ArrayList<NonDominated> nonDominatedList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileInputPath))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] split = line.split(";");

                float error = Float.valueOf(split[2]);
                float rejection = 1.0f - Float.valueOf(split[9]);

                NonDominated nonDominated = new NonDominated();
                nonDominated.error = error;
                nonDominated.rejection = rejection;
                nonDominated.line = line;
                nonDominated.toberemoved = false;

                nonDominatedList.add(nonDominated);
            }

            for (NonDominated nonDominated1 : nonDominatedList) {
                for (NonDominated nonDominated2 : nonDominatedList) {
                    if (!nonDominated1.toberemoved && !nonDominated2.toberemoved) {
                        if (nonDominated2.error < nonDominated1.error && nonDominated2.rejection < nonDominated1.rejection) {
                            nonDominated1.toberemoved = true;
                        }
                    }
                }
            }
            Collections.sort(nonDominatedList, new Comparator<NonDominated>() {
                @Override
                public int compare(NonDominated o1, NonDominated o2) {
                    return Double.compare(o1.rejection, o2.rejection);
                }
            });

            PrintWriter writer = new PrintWriter(fileOutputPath, "UTF-8");

            for (NonDominated nonDominated1 : nonDominatedList) {
                if (!nonDominated1.toberemoved) {
                    writer.println(nonDominated1.line);
                }
            }

            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class OperationPoint {

        public float difference;
        public float error;
        public float rejection;
        public String line;
    }

    public void findOperationPoints(
            String fileInputPath,
            String fileOutputPath) {
        ArrayList<OperationPoint> operationPointList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileInputPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(";");
                OperationPoint op = new OperationPoint();

                op.error = Float.valueOf(split[2]);
                op.rejection = 1 - Float.valueOf(split[9]);
                op.difference = Math.abs(op.error - op.rejection);
                op.line = line;

                operationPointList.add(op);
            }

            Collections.sort(operationPointList, new Comparator<OperationPoint>() {
                @Override
                public int compare(OperationPoint o1, OperationPoint o2) {
                    return Double.compare(o1.difference, o2.difference);
                }
            });

            PrintWriter writer = new PrintWriter(fileOutputPath, "UTF-8");

            writer.println("\tAverage Operation Point:");
            writer.println("Difference between objectives: " + operationPointList.get(0).difference);
            writer.println("Error rate: " + operationPointList.get(0).error);
            writer.println("Rejection rate: " + operationPointList.get(0).rejection);
            writer.println("Normal threshold AVG: " + operationPointList.get(0).line.split(";")[10]);
            writer.println("Attack threshold AVG: " + operationPointList.get(0).line.split(";")[11]);
            writer.println("Line: " + operationPointList.get(0).line + "\n");

            Collections.sort(operationPointList, new Comparator<OperationPoint>() {
                @Override
                public int compare(OperationPoint o1, OperationPoint o2) {
                    return Double.compare(o2.rejection, o1.rejection);
                }
            });

            OperationPoint bestOP = operationPointList.get(0);

            for (OperationPoint op : operationPointList) {
                if (Math.abs(op.rejection - 0.4f) < Math.abs(bestOP.rejection - 0.4f)) {
                    bestOP = op;
                }
            }

            writer.println("\tReject rate 40% Operation Point:");
            writer.println("Difference between objectives: " + bestOP.difference);
            writer.println("Error rate: " + bestOP.error);
            writer.println("Rejection rate: " + bestOP.rejection);
            writer.println("Normal threshold Error5%: " + bestOP.line.split(";")[10]);
            writer.println("Attack threshold Error5%: " + bestOP.line.split(";")[11]);
            writer.println("Line: " + bestOP.line + "\n");

            writer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
