package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import com.yahoo.labs.samoa.instances.WekaToSamoaInstanceConverter;
import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import fcul.viegas.topologies.machinelearning.Topologies_MOA_ConformalThresholdFinder;
import fcul.viegas.topologies.machinelearning.method.OperationPoints;
import fcul.viegas.topologies.machinelearning.method.Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal;
import fcul.viegas.topologies.machinelearning.method.WekaMoaClassifierWrapper;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;
import java.util.*;

public class ConformalEvaluator_Batch_ThresholdFinder {

    public class ValueForRejectEvaluation {
        public double instClass;
        public double predictClass;
        public double alpha;
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
        String featureSet = params[4];


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

        Instances dataTrain = null;
        if(featureSet.equals("VIEGAS")) {
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

            dataTrain = dataTrainVIEGAS;
        }else if(featureSet.equals("NIGEL")) {

            Instances dataTrainMOORE = mlModelBuilder.openFile(testFilesMOORE.get(0));
            dataTrainMOORE.randomize(new Random(1));
            for (int i = 1; i < daysToUseForTraining; i++) {
                Instances dataTrainInc = mlModelBuilder.openFile(testFilesMOORE.get(i));
                dataTrainInc.randomize(new Random(1));
                for (Instance inst : dataTrainInc) {
                    dataTrainMOORE.add(inst);
                }
            }
            dataTrainMOORE = mlModelBuilder.getAsNormalizeFeatures(dataTrainMOORE);

            dataTrain = dataTrainMOORE;
        }else if(featureSet.equals("MOORE")) {

            Instances dataTrainNIGEL = mlModelBuilder.openFile(testFilesNIGEL.get(0));
            dataTrainNIGEL.randomize(new Random(1));
            for (int i = 1; i < daysToUseForTraining; i++) {
                Instances dataTrainInc = mlModelBuilder.openFile(testFilesNIGEL.get(i));
                dataTrainInc.randomize(new Random(1));
                for (Instance inst : dataTrainInc) {
                    dataTrainNIGEL.add(inst);
                }
            }
            dataTrainNIGEL = mlModelBuilder.getAsNormalizeFeatures(dataTrainNIGEL);

            dataTrain = dataTrainNIGEL;
        }else if(featureSet.equals("ORUNADA")) {

            Instances dataTrainORUNADA = mlModelBuilder.openFile(testFilesORUNADA.get(0));
            dataTrainORUNADA.randomize(new Random(1));
            for (int i = 1; i < daysToUseForTraining; i++) {
                Instances dataTrainInc = mlModelBuilder.openFile(testFilesORUNADA.get(i));
                dataTrainInc.randomize(new Random(1));
                for (Instance inst : dataTrainInc) {
                    dataTrainORUNADA.add(inst);
                }
            }
            dataTrainORUNADA = mlModelBuilder.getAsNormalizeFeatures(dataTrainORUNADA);
            dataTrainORUNADA = mlModelBuilder.removeParticularAttributesOrunada(dataTrainORUNADA);

            dataTrain = dataTrainORUNADA;
        }

        System.out.println("building conformal");
        ConformalEvaluator_Batch conformalEvaluator = new ConformalEvaluator_Batch();
        conformalEvaluator.buildConformal(dataTrain);


        System.out.println("building classifiers now, this will take some time.........");
        //aqui ainda nao usamos o moa mas who cares?, agora usamos


        //weka classifiers
        int indexToUse = 5;

        String classifierToBuild = params[indexToUse++];

        Classifier classifier = null;
        //se nao for nem A nem B, da pau...

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


        ArrayList<String[]> testFiles = new ArrayList<>();
        for (int i = 0; i < testFilesVIEGAS.size(); i++) {

            String[] array = new String[4];
            array[0] = testFilesVIEGAS.get(i);
            array[1] = testFilesNIGEL.get(i);
            array[2] = testFilesMOORE.get(i);
            array[3] = testFilesORUNADA.get(i);

            testFiles.add(array);
        }

        List<ValueForRejectEvaluation> listValuesPredictedNormal = Collections.synchronizedList(new ArrayList<ValueForRejectEvaluation>());
        List<ValueForRejectEvaluation> listValuesPredictedAttack = Collections.synchronizedList(new ArrayList<ValueForRejectEvaluation>());

        int j = 0;
        class Stats{
            public int acertou = 0;
            public int nTotalAttack = 0;
            public int nTotalNormal = 0;
        }
        Stats stats = new Stats();


        class TestClass implements Runnable {
            int i;
            int iUpper;
            Classifier classifier;

            TestClass(int i, int iUpper, Classifier classifier) {
                this.i = i;
                this.iUpper = iUpper;
                this.classifier = classifier;
            }

            public void run() {
                try {
                    for (int k = i; k < iUpper; k++) {
                        String s = null;
                        String[] s1 = testFiles.get(k);
                        if (featureSet.equals("VIEGAS")) {
                            s = s1[0];
                        } else if (featureSet.equals("MOORE")) {
                            s = s1[1];
                        } else if (featureSet.equals("NIGEL")) {
                            s = s1[2];
                        } else if (featureSet.equals("ORUNADA")) {
                            s = s1[3];
                        }

                        Instances dataTest = mlModelBuilder.openFile(s);
                        dataTest = mlModelBuilder.getAsNormalizeFeatures(dataTest);

                        if (featureSet.equals("VIEGAS")) {
                            dataTest = mlModelBuilder.removeParticularAttributesViegas(dataTest);
                        } else if (featureSet.equals("ORUNADA")) {
                            dataTest = mlModelBuilder.removeParticularAttributesOrunada(dataTest);
                        }


                        for (int counter = 0; counter < dataTest.size(); counter++) {
                            Instance inst = dataTest.get(counter);
                            double predict = classifier.classifyInstance(inst);

                            synchronized (stats) {
                                if (inst.classValue() == 0.0d) {
                                    stats.nTotalNormal++;
                                } else {
                                    stats.nTotalAttack++;
                                }
                                if (inst.classValue() == predict) {
                                    stats.acertou++;
                                }
                            }

                            ValueForRejectEvaluation values = new ValueForRejectEvaluation();

                            values.instClass = inst.classValue();
                            values.predictClass = predict;
                            if (values.predictClass == 0.0d) {
                                values.alpha = conformalEvaluator.getPValueForNormal(inst);
                                //values.alpha = classifier.distributionForInstance(inst)[0];
                                listValuesPredictedNormal.add(values);
                            } else {
                                values.alpha = conformalEvaluator.getPValueForAttack(inst);
                                //values.alpha = classifier.distributionForInstance(inst)[1];
                                listValuesPredictedAttack.add(values);
                            }

                        }

                        System.out.println(s);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }


        ArrayList<Thread> threads = new ArrayList<>();
        int jump = testFiles.size() / 15;
        int start = 0;
        for(int nThreads = 0; nThreads < 15; nThreads++){
            Thread t = new Thread(new TestClass(start, start+jump, classifier));
            t.start();
            threads.add(t);
            start += jump;
        }

        for(Thread t: threads) {
            t.join();
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

        threads = new ArrayList<>();
        for(int nThreads = 0; nThreads < 15; nThreads++){
            Thread t = new Thread(new ParameterClass((int)((100.0f/15.0f)*nThreads), (int)(((100.0f/15.0f)*(nThreads+1))), stats.nTotalAttack, stats.nTotalNormal));
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
