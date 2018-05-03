package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import fcul.viegas.topologies.machinelearning.method.Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class ConformalEvaluator_Batch_ThresholdFinder {

    public class ValueForRejectEvaluation {
        public double instClass;
        public double predictClass;
        public double alpha;
        public Instance inst;
    }

    public String folderPath;
    public String outputPath;
    public int daysToUseForTraining;
    public List<String> testFiles = new ArrayList();

    public static String PathToModel = "/home/viegas/Bases2/model/model";

    public void generateThresholdEvaluationFile(
            String[] params) throws Exception {

        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();

        this.folderPath = params[1];
        this.outputPath = params[2] + "_threshold_file.csv";
        this.daysToUseForTraining = Integer.valueOf(params[3]);
        String featureSet = params[4];


        System.out.println("Path to test directory: " + this.folderPath);
        ArrayList<String> testFilesAux = new ArrayList();
        mlModelBuilder.findFilesForTest(this.folderPath, featureSet, testFilesAux);
        this.testFiles = testFilesAux;

        java.util.Collections.sort(testFiles);

        for (int i = 0; i < testFiles.size(); i++) {
            System.out.println("\t" + testFiles.get(i));
        }

        System.out.println("Opening training file....");

        Instances dataTrain = null;
        if (featureSet.equals("VIEGAS")) {
            Instances dataTrainVIEGAS = mlModelBuilder.openFile(testFiles.get(0));
            dataTrainVIEGAS.randomize(new Random(1));
            for (int i = 1; i < daysToUseForTraining; i++) {
                Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
                dataTrainInc.randomize(new Random(1));
                for (Instance inst : dataTrainInc) {
                    dataTrainVIEGAS.add(inst);
                }
            }
            dataTrainVIEGAS = mlModelBuilder.getAsNormalizeFeatures(dataTrainVIEGAS);
            dataTrainVIEGAS = mlModelBuilder.removeParticularAttributesViegas(dataTrainVIEGAS);

            dataTrain = dataTrainVIEGAS;
        } else if (featureSet.equals("MOORE")) {

            Instances dataTrainMOORE = mlModelBuilder.openFile(testFiles.get(0));
            dataTrainMOORE.randomize(new Random(1));
            for (int i = 1; i < daysToUseForTraining; i++) {
                Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
                dataTrainInc.randomize(new Random(1));
                for (Instance inst : dataTrainInc) {
                    dataTrainMOORE.add(inst);
                }
            }
            dataTrainMOORE = mlModelBuilder.getAsNormalizeFeatures(dataTrainMOORE);

            dataTrain = dataTrainMOORE;
        } else if (featureSet.equals("NIGEL")) {

            Instances dataTrainNIGEL = mlModelBuilder.openFile(testFiles.get(0));
            dataTrainNIGEL.randomize(new Random(1));
            for (int i = 1; i < daysToUseForTraining; i++) {
                Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
                dataTrainInc.randomize(new Random(1));
                for (Instance inst : dataTrainInc) {
                    dataTrainNIGEL.add(inst);
                }
            }
            dataTrainNIGEL = mlModelBuilder.getAsNormalizeFeatures(dataTrainNIGEL);

            dataTrain = dataTrainNIGEL;
        } else if (featureSet.equals("ORUNADA")) {

            Instances dataTrainORUNADA = mlModelBuilder.openFile(testFiles.get(0));
            dataTrainORUNADA.randomize(new Random(1));
            for (int i = 1; i < daysToUseForTraining; i++) {
                Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
                dataTrainInc.randomize(new Random(1));
                for (Instance inst : dataTrainInc) {
                    dataTrainORUNADA.add(inst);
                }
            }
            dataTrainORUNADA = mlModelBuilder.getAsNormalizeFeatures(dataTrainORUNADA);
            dataTrainORUNADA = mlModelBuilder.removeParticularAttributesOrunada(dataTrainORUNADA);

            dataTrain = dataTrainORUNADA;
        } else if (featureSet.equals("ALL")) {

            Instances dataTrainALL = mlModelBuilder.openFile(testFiles.get(0));
            dataTrainALL.randomize(new Random(1));
            for (int i = 1; i < daysToUseForTraining; i++) {
                Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
                dataTrainInc.randomize(new Random(1));
                for (Instance inst : dataTrainInc) {
                    dataTrainALL.add(inst);
                }
            }


            dataTrain = dataTrainALL;
        }


        //ConformalEvaluator_Batch_Classifier conformalEvaluatorBatch = new ConformalEvaluator_Batch_Classifier();
        //double[] classGivenByClassifier = new double[dataTrain.size()];
        //double[] probabilities = new double[dataTrain.size()];

        System.out.println("building classifiers now, this will take some time.........");
        //aqui ainda nao usamos o moa mas who cares?, agora usamos


        //weka classifiers
        int indexToUse = 5;

        String classifierToBuild = params[indexToUse++];

        weka.classifiers.Classifier classifier = null;
        //se nao for nem A nem B, da pau...

        System.out.println("STATIC - Building " + classifierToBuild + " classifier...");
        //se nao for nem A nem B, da pau...
        /*
        classifier = classifierToBuild.equals("naive")
                ? mlModelBuilder.trainClassifierNaive(dataTrain) : classifierToBuild.equals("tree")
                ? mlModelBuilder.trainClassifierTree(dataTrain) : classifierToBuild.equals("forest")
                ? mlModelBuilder.trainClassifierForest(dataTrain) : classifierToBuild.equals("bagging")
                ? mlModelBuilder.trainClassifierBagging(dataTrain) : classifierToBuild.equals("extratrees")
                ? mlModelBuilder.trainClassifierExtraTrees(dataTrain) : classifierToBuild.equals("adaboost")
                ? mlModelBuilder.trainClassifierAdaboostTree(dataTrain) : classifierToBuild.equals("hoeffding")
                ? mlModelBuilder.trainClassifierHoeffing(dataTrain) : null;
*/
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("/home/viegas/model"));
        classifier = (Classifier) ois.readObject();
        ois.close();


        ConformalEvaluator_Batch_Transcend conformalEvaluator = new ConformalEvaluator_Batch_Transcend(new ConformalEvaluator_BatchClassifier_RandomForest(100, 100));

        System.out.println("Opening training file...");
        Instances dataTrainConformal = mlModelBuilder.openFile(testFiles.get(0));
        dataTrainConformal.randomize(new Random(1));
        dataTrainConformal.clear();
        for (int i = 0; i < 30; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            dataTrainInc.randomize(new Random(1));
            for(int j = 0; j < 2000; j++){
                dataTrainConformal.add(dataTrainInc.get(j));
            }
        }
        System.out.println("Opening testing file...");
        Instances dataTestConformal = mlModelBuilder.openFile(testFiles.get(150));
        dataTestConformal.randomize(new Random(1));
        dataTestConformal.clear();
        for (int i = 150; i < 180; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            dataTrainInc.randomize(new Random(1));
            for(int j = 0; j < 2000; j++){
                dataTestConformal.add(dataTrainInc.get(j));
            }
        }


        conformalEvaluator.buildConformal(dataTrain, dataTestConformal);


        Instances dataTest = mlModelBuilder.openFile(testFiles.get(240));
        dataTest.randomize(new Random(1));
        for (int i = 241; i < 300; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            dataTrainInc.randomize(new Random(1));
            for (Instance inst : dataTrainInc) {
                dataTest.add(inst);
            }
        }

        List<ValueForRejectEvaluation> listValueslThreadedNormal = Collections.synchronizedList(new ArrayList<ValueForRejectEvaluation>());
        List<ValueForRejectEvaluation> listValueslThreadedAttack = Collections.synchronizedList(new ArrayList<ValueForRejectEvaluation>());


        class Stats {
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
                    int pct = 0;
                    for (int k = i; k < iUpper; k++) {

                        if (k >= i) {
                            if (k % ((dataTest.size()) / 100) == 0) {
                                pct++;
                                System.out.println("\tTestClass " + pct + "% ...[" + k + "/" + iUpper + "]");
                            }
                        }

                        Instance inst = dataTest.get(k);
                        double prob[] = null;
                        synchronized (classifier) {
                            prob = classifier.distributionForInstance(inst);
                        }



                        synchronized (stats) {
                            if (inst.classValue() == 0.0d) {
                                stats.nTotalNormal++;
                            } else {
                                stats.nTotalAttack++;
                            }
                            if (prob[0] >= prob[1]) {
                                if (inst.classValue() == 0.0d) {
                                    stats.acertou++;
                                }
                            } else {
                                if (inst.classValue() == 1.0d) {
                                    stats.acertou++;
                                }
                            }
                        }

                        ValueForRejectEvaluation values = new ValueForRejectEvaluation();
                        values.inst = inst;

                        if (prob[0] >= prob[1]) {
                            values.predictClass = 0.0d;
                            values.instClass = inst.classValue();
                            values.alpha = conformalEvaluator.getPValueForNormal(inst);// * (1 - conformalEvaluator.getPValueForAttack(inst));
                            listValueslThreadedNormal.add(values);
                        } else {
                            values.predictClass = 1.0d;
                            values.instClass = inst.classValue();
                            values.alpha = conformalEvaluator.getPValueForAttack(inst);// * (1 - conformalEvaluator.getPValueForNormal(inst));
                            listValueslThreadedAttack.add(values);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


        int index = 0;
        ArrayList<Thread> threads = new ArrayList<>();
        int jump = dataTest.size() / 20;
        int start = 0;
        for (int nThreads = 0; nThreads < 20; nThreads++) {
            if (nThreads + 1 == 20) {
                Thread t = new Thread(new TestClass(start, dataTest.size(), classifier));
                t.start();
                threads.add(t);
                start += jump;
            } else {
                Thread t = new Thread(new TestClass(start, start + jump, classifier));
                t.start();
                threads.add(t);
                start += jump;
            }
        }

        for (Thread t : threads) {
            t.join();
        }

        //System.exit(1);
        //conformalEvaluatorBatch.setStartWriting(false);


        ArrayList<ValueForRejectEvaluation> listValuesPredictedNormal = new ArrayList<ValueForRejectEvaluation>();
        ArrayList<ValueForRejectEvaluation> listValuesPredictedAttack = new ArrayList<ValueForRejectEvaluation>();

        for (ValueForRejectEvaluation obj : listValueslThreadedNormal) {
            listValuesPredictedNormal.add(obj);
        }
        for (ValueForRejectEvaluation obj : listValueslThreadedAttack) {
            listValuesPredictedAttack.add(obj);
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
                                + (nAtk / (float) nTotalAttack) + ";"
                                + (nNormal / (float) nTotalNormal) + ";"
                                + listValuesPredictedNormal.get(pingNormal * iNormal).alpha + ";"
                                + listValuesPredictedAttack.get(pingAttack * iAttack).alpha);
                    }
                }

            }
        }

        threads = new ArrayList<>();
        for (int nThreads = 0; nThreads < 15; nThreads++) {
            Thread t = new Thread(new ParameterClass((int) ((100.0f / 15.0f) * nThreads), (int) (((100.0f / 15.0f) * (nThreads + 1))), stats.nTotalAttack, stats.nTotalNormal));
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
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
                float rejection = ((Float.valueOf(split[10]) + Float.valueOf(split[11])) / 2.0f);
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
