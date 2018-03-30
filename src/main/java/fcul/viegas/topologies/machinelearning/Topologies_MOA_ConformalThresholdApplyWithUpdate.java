package fcul.viegas.topologies.machinelearning;

import com.yahoo.labs.samoa.instances.WekaToSamoaInstanceConverter;
import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import fcul.viegas.topologies.machinelearning.method.OperationPoints;
import fcul.viegas.topologies.machinelearning.method.Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal;
import fcul.viegas.topologies.machinelearning.method.WekaMoaClassifierWrapper;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Topologies_MOA_ConformalThresholdApplyWithUpdate {


    public class ValueForRejectEvaluation {

        public double instClass;
        public double predictClass;
        public ArrayList<Double> predictClassClassifier;
        public ArrayList<Double> credibility;
        public ArrayList<Double> confidence;
        public ArrayList<double[]> alphaEachClassifier;
        public double alpha;
        public int votesForAttack;
        public int votesForNormal;
        public double choosenClass;
        public double averageAttackProb;
        public double averageNormalProb;
    }

    public String folderPath;
    public String outputPath;
    public int daysToUseForTraining;
    public ArrayList<String> testFilesVIEGAS = new ArrayList();
    public ArrayList<String> testFilesMOORE = new ArrayList();
    public ArrayList<String> testFilesNIGEL = new ArrayList();
    public ArrayList<String> testFilesORUNADA = new ArrayList();

    public static String PathToModel = "/home/viegas/Bases2/model/model";

    public void generateThresholdEvaluationFile(
            String[] params) throws Exception {

        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();
        double thresholdAttack = Double.valueOf(params[5]);
        double thresholdNormal = Double.valueOf(params[4]);

        this.folderPath = params[1];
        this.outputPath = params[2] + "_threshold_file.csv";
        this.daysToUseForTraining = Integer.valueOf(params[3]);

        System.out.println("Path to test directory: " + this.folderPath);
        mlModelBuilder.findFilesForTest(this.folderPath, "VIEGAS", testFilesVIEGAS);
        mlModelBuilder.findFilesForTest(this.folderPath, "MOORE", testFilesMOORE);
        mlModelBuilder.findFilesForTest(this.folderPath, "NIGEL", testFilesNIGEL);
        mlModelBuilder.findFilesForTest(this.folderPath, "ORUNADA", testFilesORUNADA);

        java.util.Collections.sort(testFilesVIEGAS);
        java.util.Collections.sort(testFilesMOORE);
        java.util.Collections.sort(testFilesNIGEL);
        java.util.Collections.sort(testFilesORUNADA);

        for (int i = 0; i < testFilesVIEGAS.size(); i++) {
            System.out.println("\t" + testFilesVIEGAS.get(i) + " "
                    + testFilesMOORE.get(i) + " "
                    + testFilesNIGEL.get(i) + " "
                    + testFilesORUNADA.get(i) + " ");
        }

        System.out.println("Opening training file....");
        Instances dataTrainVIEGAS = mlModelBuilder.openFile(testFilesVIEGAS.get(0));
        dataTrainVIEGAS.randomize(new Random(1));
        for (int i = 1; i < daysToUseForTraining; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFilesVIEGAS.get(i));
            dataTrainInc.randomize(new Random(1));
            for (Instance inst : dataTrainInc) {
                dataTrainVIEGAS.add(inst);
            }
        }
        dataTrainVIEGAS = mlModelBuilder.getAsNormalizeFeatures(dataTrainVIEGAS);
        dataTrainVIEGAS = mlModelBuilder.removeParticularAttributesViegas(dataTrainVIEGAS);

//        ArffSaver saver = new ArffSaver();
//        saver.setInstances(dataTrainVIEGAS);
//        saver.setFile(new File("/home/viegas/Downloads/2007Years/VIEGAS.arff"));
//        //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
//        saver.writeBatch();
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/viegas/Downloads/2007Years/VIEGAS.arff"));
//        writer.write(dataTrainVIEGAS.toString());
//        writer.flush();
//        writer.close();
//
//        System.exit(1);


        System.out.println("building conformal");
        Transcend_ConformalPredictor conformalVIEGAS = new Transcend_ConformalPredictor();
        //conformalVIEGAS.setDataset(dataTrainVIEGAS);


        System.out.println("building classifiers now, this will take some time.........");

        //aqui ainda nao usamos o moa mas who cares?, agora usamos
        WekaMoaClassifierWrapper wekaWrapper = new WekaMoaClassifierWrapper();
        //wekaWrapper.setConformalEvaluatorVIEGAS(conformalVIEGAS);
        //wekaWrapper.setConformalEvaluatorNIGEL(conformalNIGEL);
        //wekaWrapper.setConformalEvaluatorORUNADA(conformalORUNADA);
        //wekaWrapper.setConformalEvaluatorMOORE(conformalMOORE);

        wekaWrapper.setConformalEvaluatorVIEGAS(null);
        wekaWrapper.setConformalEvaluatorNIGEL(null);
        wekaWrapper.setConformalEvaluatorORUNADA(null);
        wekaWrapper.setConformalEvaluatorMOORE(null);

        //weka classifiers
        int indexToUse = 6;
        if (indexToUse < params.length && !params[indexToUse].equals("stream")) {
            for (; indexToUse < params.length; ) {
                String featureSet = params[indexToUse++];
                String classifierToBuild = params[indexToUse++];
                float normalThreshold = Float.valueOf(params[indexToUse++]);
                float attackThreshold = Float.valueOf(params[indexToUse++]);

                Classifier classifier = null;
                Instances dataTrain = null;
                //se nao for nem A nem B, da pau...
                if (featureSet.equals("VIEGAS")) {
                    dataTrain = dataTrainVIEGAS;
                }

                System.out.println("STATIC - Building " + classifierToBuild + " classifier...");
                //se nao for nem A nem B, da pau...
                classifier = classifierToBuild.equals("naive")
                        ? mlModelBuilder.trainClassifierNaive(dataTrain) : classifierToBuild.equals("tree")
                        ? mlModelBuilder.trainClassifierTree(dataTrain) : classifierToBuild.equals("forest")
                        ? mlModelBuilder.trainClassifierForest(dataTrain) : classifierToBuild.equals("bagging")
                        ? mlModelBuilder.trainClassifierBagging(dataTrain) : classifierToBuild.equals("extratrees")
                        ? mlModelBuilder.trainClassifierExtraTrees(dataTrain) : classifierToBuild.equals("adaboost")
                        ? mlModelBuilder.trainClassifierAdaboostTree(dataTrain) : classifierToBuild.equals("hoeffding")
                        ? mlModelBuilder.trainClassifierHoeffing(dataTrain) : null;

                wekaWrapper.getFeatureSetToLookWeka().add(featureSet);
                wekaWrapper.getWekaClassifiers().add(classifier);
                wekaWrapper.getWekaOperationPoints().add(new OperationPoints(normalThreshold, attackThreshold));

                if (indexToUse == params.length || params[indexToUse].equals("stream")) {
                    break;
                }
            }
        }

        //moa classifiers
        indexToUse++;
        for (; indexToUse < params.length; ) {
            String featureSet = params[indexToUse++];
            String classifierToBuild = params[indexToUse++];
            float normalThreshold = Float.valueOf(params[indexToUse++]);
            float attackThreshold = Float.valueOf(params[indexToUse++]);

            moa.classifiers.AbstractClassifier classifier = null;
            Instances dataTrain = null;
            //se nao for nem A nem B, da pau...
            if (featureSet.equals("VIEGAS")) {
                dataTrain = dataTrainVIEGAS;
            }

            System.out.println("STREAM - Building " + classifierToBuild + " classifier...");
            //se nao for nem A nem B, da pau...
            classifier = classifierToBuild.equals("hoeffding")
                    ? mlModelBuilder.trainClassifierHoeffdingTreeMOA(dataTrain) : classifierToBuild.equals("hoeffdingadaptivetree")
                    ? mlModelBuilder.trainClassifierHoeffingAdaptiveTreeMOA(dataTrain) : classifierToBuild.equals("ozabagging")
                    ? mlModelBuilder.trainClassifierOzaBaggingMOA(dataTrain, Integer.valueOf(params[indexToUse++])) : classifierToBuild.equals("ozaboosting")
                    ? mlModelBuilder.trainClassifierOzaBoostingMOA(dataTrain, Integer.valueOf(params[indexToUse++])) : classifierToBuild.equals("adaptiveforest")
                    ? mlModelBuilder.trainClassifierAdaptiveRandomForestMOA(dataTrain) : classifierToBuild.equals("adahoeffdingoptiontree")
                    ? mlModelBuilder.trainClassifierAdaHoeffdingOptionTreeMOA(dataTrain) : classifierToBuild.equals("ocboost")
                    ? mlModelBuilder.trainClassifierOCBoostMOA(dataTrain, Integer.valueOf(params[indexToUse++])) : classifierToBuild.equals("leveragingbag")
                    ? mlModelBuilder.trainClassifierLeveragingBagMOA(dataTrain, Integer.valueOf(params[indexToUse++])) : null;

            wekaWrapper.getFeatureSetToLookMoa().add(featureSet);
            wekaWrapper.getMoaClassifiers().add(classifier);
            wekaWrapper.getMoaOperationPoints().add(new OperationPoints(normalThreshold, attackThreshold));

            Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal.PathToModel = Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal.PathToModel + "_" +
                    classifierToBuild + "_" + featureSet;
        }

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal.PathToModel));
        oos.writeObject(wekaWrapper);
        oos.flush();
        oos.close();

        ArrayList<String[]> testFiles = new ArrayList<>();
        for (int i = 0; i < testFilesVIEGAS.size(); i++) {
            //for (int i = 0; i < 600; i++) {

            String[] array = new String[4];
            array[0] = testFilesVIEGAS.get(i);
            array[1] = testFilesNIGEL.get(i);
            array[2] = testFilesMOORE.get(i);
            array[3] = testFilesORUNADA.get(i);

            testFiles.add(array);
        }

        int acertou = 0;
        int nTotalAttack = 0;
        int nTotalNormal = 0;
        int daysToUseForTest = 30;
        String featureSet = "VIEGAS";
        ArrayList<ValueForRejectEvaluation> listValuesPredictedNormal = new ArrayList<>();
        ArrayList<ValueForRejectEvaluation> listValuesPredictedAttack = new ArrayList<>();
        ArrayList<ValueForRejectEvaluation> listValuesAll = new ArrayList<>();


        for (String[] s1 : testFiles.subList(0, daysToUseForTest)) {
            String s = s1[0];

            Instances dataTest = mlModelBuilder.openFile(s);
            dataTest = mlModelBuilder.getAsNormalizeFeatures(dataTest);

            if (featureSet.equals("VIEGAS")) {
                dataTest = mlModelBuilder.removeParticularAttributesViegas(dataTest);
            }

            WekaToSamoaInstanceConverter converterViegas = new WekaToSamoaInstanceConverter();
            com.yahoo.labs.samoa.instances.Instances insts = converterViegas.samoaInstances(dataTest);

            for (int counter = 0; counter < insts.size(); counter++) {
                com.yahoo.labs.samoa.instances.Instance inst = insts.get(counter);

                if (inst.classValue() == 0.0d) {
                    nTotalNormal++;
                } else {
                    nTotalAttack++;
                }

                ValueForRejectEvaluation values = new ValueForRejectEvaluation();

                values.predictClassClassifier = new ArrayList<>();
                values.credibility = new ArrayList<>();
                values.confidence = new ArrayList<>();
                values.alphaEachClassifier = new ArrayList<>();
                values.votesForAttack = 0;
                values.votesForNormal = 0;
                values.instClass = inst.classValue();

                for (int k = 0; k < wekaWrapper.getMoaClassifiers().size(); k++) {
                    moa.classifiers.AbstractClassifier classifier = wekaWrapper.getMoaClassifiers().get(k);

                    double[] prob = classifier.getVotesForInstance(inst);
                    prob = Arrays.copyOf(prob, inst.numClasses()); //pequeno teste

                    boolean correctlyClassifies = classifier.correctlyClassifies(inst);
                    if (correctlyClassifies) {
                        acertou++;
                    }
                    boolean choosenAttack = false;
                    if (correctlyClassifies && inst.classValue() == 0.0d) {
                        choosenAttack = false;
                    } else if (correctlyClassifies && inst.classValue() == 1.0d) {
                        choosenAttack = true;
                    } else if (!correctlyClassifies && inst.classValue() == 0.0d) {
                        choosenAttack = true;
                    } else {
                        choosenAttack = false;
                    }


                    if (!choosenAttack) {
                        values.predictClassClassifier.add(0.0d);
                        //values.credibility = conformal.getPValueForNormal(dataTest.get(counter));
                        //values.confidence = 1.0f - conformal.getPValueForAttack(dataTest.get(counter));
                        //values.alpha = values.credibility + values.confidene;

                        values.votesForNormal++;
                        values.alphaEachClassifier.add(prob);

                    } else {
                        values.predictClassClassifier.add(1.0d);
                        //values.credibility = conformal.getPValueForAttack(dataTest.get(counter));
                        //values.confidence = 1.0f - conformal.getPValueForNormal(dataTest.get(counter));
                        //values.alpha = values.credibility + values.confidence;

                        values.votesForAttack++;
                        values.alphaEachClassifier.add(prob);
                    }
                }

                listValuesAll.add(values);
            }

            System.out.println(s);
        }

        ArrayList<Double> minProbClassifier = new ArrayList<>();
        ArrayList<Double> maxProbClassifier = new ArrayList<>();

        for (int k = 0; k < wekaWrapper.getMoaClassifiers().size(); k++) {
            double minProb = 1000000.0d;
            double maxProb = 0.0d;
            for (ValueForRejectEvaluation values : listValuesAll) {
                double alpha = values.alphaEachClassifier.get(k)[0];
                if (!Double.isNaN(alpha)) {
                    if (alpha < minProb) {
                        minProb = alpha;
                    }
                    if (alpha > maxProb) {
                        maxProb = alpha;
                    }
                }
                alpha = values.alphaEachClassifier.get(k)[1];
                if (!Double.isNaN(alpha)) {
                    if (alpha < minProb) {
                        minProb = alpha;
                    }
                    if (alpha > maxProb) {
                        maxProb = alpha;
                    }
                }
            }
            minProbClassifier.add(minProb);
            maxProbClassifier.add(maxProb);
        }

        System.out.println("Computing daily now");
        List<String> outputList = Collections.synchronizedList(new ArrayList<String>());

        ArrayList<ArrayList<com.yahoo.labs.samoa.instances.Instance>> instancesToUseUpdate = new ArrayList<>();

        for (int j = 0; j < testFiles.size(); j++) {
            String[] s1 = testFiles.get(j);
            String s = s1[0];

            Instances dataTest = mlModelBuilder.openFile(s);
            dataTest = mlModelBuilder.getAsNormalizeFeatures(dataTest);

            if (featureSet.equals("VIEGAS")) {
                dataTest = mlModelBuilder.removeParticularAttributesViegas(dataTest);
            }

            WekaToSamoaInstanceConverter converterViegas = new WekaToSamoaInstanceConverter();
            com.yahoo.labs.samoa.instances.Instances insts = converterViegas.samoaInstances(dataTest);

            ArrayList<com.yahoo.labs.samoa.instances.Instance> instancesRejected = new ArrayList<>();

            listValuesAll = new ArrayList<>();
            for (int counter = 0; counter < insts.size(); counter++) {
                com.yahoo.labs.samoa.instances.Instance inst = insts.get(counter);

                ValueForRejectEvaluation values = new ValueForRejectEvaluation();

                values.predictClassClassifier = new ArrayList<>();
                values.credibility = new ArrayList<>();
                values.confidence = new ArrayList<>();
                values.alphaEachClassifier = new ArrayList<>();
                values.votesForAttack = 0;
                values.votesForNormal = 0;
                values.instClass = inst.classValue();

                for (int k = 0; k < wekaWrapper.getMoaClassifiers().size(); k++) {
                    moa.classifiers.AbstractClassifier classifier = wekaWrapper.getMoaClassifiers().get(k);

                    double[] prob = classifier.getVotesForInstance(inst);
                    prob = Arrays.copyOf(prob, inst.numClasses()); //pequeno teste

                    boolean correctlyClassifies = classifier.correctlyClassifies(inst);

                    boolean choosenAttack = false;
                    if (correctlyClassifies && inst.classValue() == 0.0d) {
                        choosenAttack = false;
                    } else if (correctlyClassifies && inst.classValue() == 1.0d) {
                        choosenAttack = true;
                    } else if (!correctlyClassifies && inst.classValue() == 0.0d) {
                        choosenAttack = true;
                    } else {
                        choosenAttack = false;
                    }


                    if (!choosenAttack) {
                        values.predictClassClassifier.add(0.0d);
                        //values.credibility = conformal.getPValueForNormal(dataTest.get(counter));
                        //values.confidence = 1.0f - conformal.getPValueForAttack(dataTest.get(counter));
                        //values.alpha = values.credibility + values.confidene;

                        values.votesForNormal++;
                        values.alphaEachClassifier.add(prob);

                    } else {
                        values.predictClassClassifier.add(1.0d);
                        //values.credibility = conformal.getPValueForAttack(dataTest.get(counter));
                        //values.confidence = 1.0f - conformal.getPValueForNormal(dataTest.get(counter));
                        //values.alpha = values.credibility + values.confidence;

                        values.votesForAttack++;
                        values.alphaEachClassifier.add(prob);
                    }


                }

                listValuesAll.add(values);
            }


            int n = 0;
            int nNormal = 0; //ok
            int nAttack = 0; //ok
            int nRejectedNormal = 0; //ok
            int nRejectedAttack = 0; //ok
            int nAcceptedNormal = 0; //ok
            int nAcceptedAttack = 0; //ok
            int nCorrectlyAcceptedNormal = 0; //ok
            int nCorrectlyAcceptedAttack = 0;


            for (ValueForRejectEvaluation values : listValuesAll) {
                n++;

                if (values.instClass == 0.0d) {
                    nNormal++;
                } else {
                    nAttack++;
                }

                values.averageAttackProb = 0.0d;
                values.averageNormalProb = 0.0d;
                for (int k = 0; k < wekaWrapper.getMoaClassifiers().size(); k++) {
                    if (values.predictClassClassifier.get(k) == 0.0d) {
                        double normalizedProb = values.alphaEachClassifier.get(k)[0] / maxProbClassifier.get(k);
                        if (normalizedProb > values.averageNormalProb || values.averageNormalProb == 0.0d) {
                            values.averageNormalProb = normalizedProb;
                        }
                    } else {
                        double normalizedProb = values.alphaEachClassifier.get(k)[1] / maxProbClassifier.get(k);
                        if (normalizedProb > values.averageAttackProb || values.averageAttackProb == 0.0d) {
                            values.averageAttackProb = normalizedProb;
                        }
                    }
                }

                if (values.votesForNormal >= wekaWrapper.getMoaClassifiers().size()) {
                    values.alpha = values.averageNormalProb;
                    values.predictClass = 0.0d;
                } else {
                    values.alpha = values.averageAttackProb;
                    values.predictClass = 1.0d;
                }

                if (values.predictClass == 0.0d) {
                    if (values.alpha >= thresholdNormal) {
                        if (values.instClass == 0.0d) {
                            nAcceptedNormal++;
                            nCorrectlyAcceptedNormal++;
                        } else {
                            nAcceptedAttack++;
                        }
                    } else {
                        if (values.instClass == 0.0d) {
                            nRejectedNormal++;
                        } else {
                            nRejectedAttack++;
                        }
                        instancesRejected.add(insts.get(n-1));
                    }
                } else {
                    if (values.alpha >= thresholdAttack) {
                        if (values.instClass == 0.0d) {
                            nAcceptedNormal++;
                        } else {
                            nAcceptedAttack++;
                            nCorrectlyAcceptedAttack++;
                        }
                    } else {
                        if (values.instClass == 0.0d) {
                            nRejectedNormal++;
                        } else {
                            nRejectedAttack++;
                        }
                        instancesRejected.add(insts.get(n-1));
                    }
                }

            }
            if (nNormal == 0) {
                nNormal = 1;
            }
            if (nAttack == 0) {
                nAttack = 1;
            }
            if (n == 0) {
                n = 1;
            }

            instancesToUseUpdate.add(instancesRejected);
            if(j - 3 > 0){
                for(com.yahoo.labs.samoa.instances.Instance inst: instancesToUseUpdate.get(j - 3)){
                    for (int k = 0; k < wekaWrapper.getMoaClassifiers().size(); k++) {
                        wekaWrapper.getMoaClassifiers().get(k).trainOnInstance(inst);
                    }
                }
            }


            float accAceito = ((nCorrectlyAcceptedNormal + nCorrectlyAcceptedAttack) / (float) (nAcceptedNormal + nAcceptedAttack));


            String print = s1[0] + ";";
            print = print + 0 + ";";
            print = print + 0 + ";";
            print = print + (n) + ";";
            print = print + nNormal + ";";
            print = print + nAttack + ";";
            print = print + nAttack + ";";
            print = print + (nCorrectlyAcceptedNormal / (float) nAcceptedNormal) + ";";
            print = print + (nCorrectlyAcceptedAttack / (float) nAcceptedAttack) + ";";
            print = print + accAceito + ";";
            print = print + 0 + ";";
            print = print + 0 + ";";
            print = print + 0 + ";";
            print = print + ((nRejectedNormal + nRejectedAttack) / (float) (nNormal + nAttack)) + ";";
            print = print + ((((nCorrectlyAcceptedNormal / (float) nAcceptedNormal)) + ((nCorrectlyAcceptedAttack / (float) nAcceptedAttack))) / 2.0f) + ";";
            print = print + 0 + ";";
            print = print + ((nRejectedAttack) / (float) nAttack) + ";";
            print = print + ((nRejectedNormal) / (float) nNormal);

            outputList.add(print);

            System.out.println(s);
        }


        PrintWriter writer = new PrintWriter(outputPath + "_raw_output.csv", "UTF-8");

        for (String s : outputList) {
            writer.println(s);
        }
        writer.close();

        ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejection(
                outputPath + "_raw_output.csv",
                outputPath + "_summarized_monthly.csv",
                ParseRawOutputFlinkNoUpdate.MonthRange
        );

        ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejection(
                outputPath + "_raw_output.csv",
                outputPath + "_summarized_yearly.csv",
                ParseRawOutputFlinkNoUpdate.YearRange
        );

    }

}
