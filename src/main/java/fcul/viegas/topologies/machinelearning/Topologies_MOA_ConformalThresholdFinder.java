/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;


import com.yahoo.labs.samoa.instances.WekaToSamoaInstanceConverter;
import fcul.viegas.topologies.machinelearning.method.OperationPoints;
import fcul.viegas.topologies.machinelearning.method.Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal;
import fcul.viegas.topologies.machinelearning.method.WekaMoaClassifierWrapper;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;

import java.io.*;
import java.util.*;

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
        int indexToUse = 4;
        if (indexToUse < params.length && !params[indexToUse].equals("stream")) {
            for (; indexToUse < params.length;) {
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
        for (; indexToUse < params.length;) {
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
                    ? mlModelBuilder.trainClassifierOzaBoostingMOA(dataTrain,  Integer.valueOf(params[indexToUse++])) : classifierToBuild.equals("adaptiveforest")
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

        int j = 0;
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

                if(inst.classValue() == 0.0d){
                    nTotalNormal++;
                }else{
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

                for(int k = 0; k < wekaWrapper.getMoaClassifiers().size(); k++){
                    moa.classifiers.AbstractClassifier classifier = wekaWrapper.getMoaClassifiers().get(k);

                    double[] prob = classifier.getVotesForInstance(inst);
                    prob = Arrays.copyOf(prob, inst.numClasses()); //pequeno teste

                    boolean correctlyClassifies = classifier.correctlyClassifies(inst);
                    if (correctlyClassifies) {
                        acertou++;
                    }
                    boolean choosenAttack = false;
                    if(correctlyClassifies && inst.classValue() == 0.0d){
                        choosenAttack = false;
                    }else if(correctlyClassifies && inst.classValue() == 1.0d){
                        choosenAttack = true;
                    }else if(!correctlyClassifies && inst.classValue() == 0.0d){
                        choosenAttack = true;
                    }else{
                        choosenAttack = false;
                    }


                    if (!choosenAttack) {
                        values.predictClassClassifier.add(0.0d);
                        //values.credibility = conformal.getPValueForNormal(dataTest.get(counter));
                        //values.confidence = 1.0f - conformal.getPValueForAttack(dataTest.get(counter));
                        //values.alpha = values.credibility + values.confidene;

                        values.alpha = prob[0];
                        values.votesForNormal++;
                        values.alphaEachClassifier.add(prob);

                    } else {
                        values.predictClassClassifier.add(1.0d);
                        //values.credibility = conformal.getPValueForAttack(dataTest.get(counter));
                        //values.confidence = 1.0f - conformal.getPValueForNormal(dataTest.get(counter));
                        //values.alpha = values.credibility + values.confidence;

                        values.alpha = prob[1];
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

        for(int k = 0; k < wekaWrapper.getMoaClassifiers().size(); k++){
            double minProb = 1000000.0d;
            double maxProb = 0.0d;
            for(ValueForRejectEvaluation values: listValuesAll){
                double alpha = values.alphaEachClassifier.get(k)[0];
                if(!Double.isNaN(alpha)) {
                    if (alpha < minProb) {
                        minProb = alpha;
                    }
                    if (alpha > maxProb) {
                        maxProb = alpha;
                    }
                }
                alpha = values.alphaEachClassifier.get(k)[1];
                if(!Double.isNaN(alpha)) {
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

        for(ValueForRejectEvaluation values: listValuesAll){
            values.averageAttackProb = 0.0d;
            values.averageNormalProb = 0.0d;
            for(int k = 0; k < wekaWrapper.getMoaClassifiers().size(); k++){
                if(values.predictClassClassifier.get(k) == 0.0d){
                    double normalizedProb = values.alphaEachClassifier.get(k)[0] / maxProbClassifier.get(k);
                    if(normalizedProb > values.averageNormalProb || values.averageNormalProb == 0.0d){
                        values.averageNormalProb = normalizedProb;
                    }
                }else{
                    double normalizedProb = values.alphaEachClassifier.get(k)[1] / maxProbClassifier.get(k);
                    if(normalizedProb > values.averageAttackProb || values.averageAttackProb == 0.0d){
                        values.averageAttackProb = normalizedProb;
                    }
                }
            }

            if(values.votesForNormal >= wekaWrapper.getMoaClassifiers().size()){
                values.alpha = values.averageNormalProb;
                values.predictClass = 0.0d;
                listValuesPredictedNormal.add(values);
            }else{
                values.alpha = values.averageAttackProb;
                values.predictClass = 1.0d;
                listValuesPredictedAttack.add(values);
            }
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
        System.out.println("Acertou: " + acertou);

        int pingNormal = (int) (listValuesPredictedNormal.size() / 100.0f);
        int pingAttack = (int) (listValuesPredictedAttack.size() / 100.0f);
        for (int iNormal = 0; iNormal < 100; iNormal++) {
            System.out.println("Normal: [" + iNormal + "]: " + listValuesPredictedNormal.get(pingNormal * iNormal).alpha + " prob [" +
                    listValuesPredictedNormal.get(pingNormal * iNormal).alphaEachClassifier.get(0)[0] + "] - [" +
                    listValuesPredictedNormal.get(pingNormal * iNormal).alphaEachClassifier.get(0)[1] + "]"
            );
        }
        for (int iAttack = 0; iAttack < 100; iAttack++) {
            System.out.println("Attack: [" + iAttack + "]: " + listValuesPredictedAttack.get(pingAttack * iAttack).alpha+ " prob [" +
                    listValuesPredictedAttack.get(pingAttack * iAttack).alphaEachClassifier.get(0)[0] + "] - [" +
                    listValuesPredictedAttack.get(pingAttack * iAttack).alphaEachClassifier.get(0)[1] + "]"
            );
        }

        List<String> outputList = Collections.synchronizedList(new ArrayList<String>());


        class ParameterClass implements Runnable {
            int iNormal;
            int iNormalUpper;
            int nTotalAttack;
            int nTotalNormal;
            ParameterClass(int iNorm, int iNormUpper, int nTotalAtk, int nTotalNorm) {
                iNormal = iNorm;
                iNormalUpper = iNormUpper;
                nTotalAttack = nTotalAtk;
                nTotalNormal = nTotalNorm;
            }
            public void run() {
                System.out.println("iNormal: " + iNormal + " iNormalUpper: " + iNormalUpper);
                for (; iNormal < iNormalUpper; iNormal++) {
                    System.out.println("Classifier: percentage done: " + iNormal + "/" + iNormalUpper);
                    for (int iAttack = 0; iAttack < 100; iAttack++) {

                        int n = 0;
                        int nAcc = 0;
                        int nAtk = 0;
                        int nAtkAcc = 0;
                        int nNormal = 0;
                        int nNormalAcc = 0;

                        int nToUseNormal = pingNormal * iNormal;
                        int nToUseAttack = pingAttack * iAttack;

                        for (int j = 0; j < nToUseNormal; j++) {
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

                        for (int j = 0; j < nToUseAttack; j++) {
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
                                + (nAtk/(float)nTotalAttack) + ";"
                                + (nNormal/(float)nTotalNormal) + ";"
                                + listValuesPredictedNormal.get(pingNormal * iNormal).alpha + ";"
                                + listValuesPredictedAttack.get(pingAttack * iAttack).alpha);
                    }
                }

            }
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for(int nThreads = 0; nThreads < 15; nThreads++){
            Thread t = new Thread(new ParameterClass((int)((100.0f/15.0f)*nThreads), (int)(((100.0f/15.0f)*(nThreads+1))), nTotalAttack, nTotalNormal));
            t.start();
            threads.add(t);
        }

        for(Thread t: threads) {
            t.join();
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

                float error = Float.valueOf(split[3]) + Float.valueOf(split[4]);
                error = error / 2.0f;
                float rejection = ((Float.valueOf(split[10]) + Float.valueOf(split[11]))/2.0f);
                rejection = 1.0f - rejection;

                //float error = Float.valueOf(split[4]);
                //error = error / 1.0f;
                //float rejection = ((Float.valueOf(split[10]) + Float.valueOf(split[11]))/2.0f);
                //rejection = 1.0f - rejection;
                //float rejection = ((Float.valueOf(split[11]))/1.0f);
                //rejection = 1.0f - rejection;

                //float error = Float.valueOf(split[3]);
                //error = error / 1.0f;
                //float rejection = ((Float.valueOf(split[10]))/1.0f);
                //rejection = 1.0f - rejection;

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
