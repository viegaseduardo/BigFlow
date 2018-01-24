/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.method;

import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.core.fs.FileSystem;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author viegas
 */
public class Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal {

    public String folderPath;
    public String outputPath;
    public int daysToUseForTraining;
    public ArrayList<String> testFilesVIEGAS = new ArrayList();
    public ArrayList<String> testFilesMOORE = new ArrayList();
    public ArrayList<String> testFilesNIGEL = new ArrayList();
    public ArrayList<String> testFilesORUNADA = new ArrayList();

    public static String PathToModel = "/home/viegas/Bases2/model/model";
    //public static String PathToModel = "/home/viegas/Downloads/model/model";

    public void run(
            String[] params) throws Exception {

        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();

        this.folderPath = params[1];
        this.outputPath = params[2];
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

        System.out.println("building conformal");
        Transcend_ConformalPredictor conformalVIEGAS = new Transcend_ConformalPredictor();
        conformalVIEGAS.setDataset(dataTrainVIEGAS);

        Transcend_ConformalPredictor conformalNIGEL = new Transcend_ConformalPredictor();
        conformalNIGEL.setDataset(dataTrainNIGEL);

        Transcend_ConformalPredictor conformalMOORE = new Transcend_ConformalPredictor();
        conformalMOORE.setDataset(dataTrainMOORE);

        Transcend_ConformalPredictor conformalORUNADA = new Transcend_ConformalPredictor();
        conformalORUNADA.setDataset(dataTrainORUNADA);

        System.out.println("building classifiers now, this will take some time...");

        //aqui ainda nao usamos o moa mas who cares?, agora usamos
        WekaMoaClassifierWrapper wekaWrapper = new WekaMoaClassifierWrapper();
        wekaWrapper.setConformalEvaluatorVIEGAS(conformalVIEGAS);
        wekaWrapper.setConformalEvaluatorNIGEL(conformalNIGEL);
        wekaWrapper.setConformalEvaluatorORUNADA(conformalORUNADA);
        wekaWrapper.setConformalEvaluatorMOORE(conformalMOORE);

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
                } else if (featureSet.equals("MOORE")) {
                    dataTrain = dataTrainMOORE;
                } else if (featureSet.equals("NIGEL")) {
                    dataTrain = dataTrainNIGEL;
                } else if (featureSet.equals("ORUNADA")) {
                    dataTrain = dataTrainORUNADA;
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
            } else if (featureSet.equals("MOORE")) {
                dataTrain = dataTrainMOORE;
            } else if (featureSet.equals("NIGEL")) {
                dataTrain = dataTrainNIGEL;
            } else if (featureSet.equals("ORUNADA")) {
                dataTrain = dataTrainORUNADA;
            }

            System.out.println("STREAM - Building " + classifierToBuild + " classifier...");
            //se nao for nem A nem B, da pau...
            classifier = classifierToBuild.equals("hoeffding")
                    ? mlModelBuilder.trainClassifierHoeffingTreeMOA(dataTrain) : classifierToBuild.equals("hoeffdingadaptivetree")
                    ? mlModelBuilder.trainClassifierHoeffingAdaptiveTreeMOA(dataTrain) : classifierToBuild.equals("ozabagging")
                    ? mlModelBuilder.trainClassifierOzaBaggingMOA(dataTrain) : classifierToBuild.equals("ozaboosting")
                    ? mlModelBuilder.trainClassifierOzaBoostingMOA(dataTrain) : classifierToBuild.equals("adaptiveforest")
                    ? mlModelBuilder.trainClassifierAdaptiveRandomForestMOA(dataTrain) : classifierToBuild.equals("adahoeffdingoptiontree")
                    ? mlModelBuilder.trainClassifierAdaHoeffdingOptionTreeMOA(dataTrain) : classifierToBuild.equals("ocboost")
                    ? mlModelBuilder.trainClassifierOCBoostMOA(dataTrain) : classifierToBuild.equals("leveragingbag")
                    ? mlModelBuilder.trainClassifierLeveragingBagMOA(dataTrain) : null;

            wekaWrapper.getFeatureSetToLookMoa().add(featureSet);
            wekaWrapper.getMoaClassifiers().add(classifier);
            wekaWrapper.getMoaOperationPoints().add(new OperationPoints(normalThreshold, attackThreshold));
        }

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Topologies_EVALUATION_DISTRIBUTED_HYBRID_CASCADE_WithConformal.PathToModel));
        oos.writeObject(wekaWrapper);
        oos.flush();
        oos.close();

        ArrayList<String[]> testFiles = new ArrayList<>();
        //for (int i = 0; i < testFilesVIEGAS.size(); i++) {
        for (int i = 0; i < 1000; i++) {

            String[] array = new String[4];
            array[0] = testFilesVIEGAS.get(i);
            array[1] = testFilesNIGEL.get(i);
            array[2] = testFilesMOORE.get(i);
            array[3] = testFilesORUNADA.get(i);

            testFiles.add(array);
        }

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //Collections.shuffle(testFiles);
        DataSet<String[]> testFilesDataset = env.fromCollection(testFiles);

        testFilesDataset.map(new EvaluateClassifierMapFunctionWithConformalCascadeHybrid(mlModelBuilder))
                .setParallelism(env.getParallelism())
                .sortPartition(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String in) throws Exception {
                        return in;
                    }
                }, Order.ASCENDING).setParallelism(1).
                writeAsText(outputPath + "_raw_output.csv", FileSystem.WriteMode.OVERWRITE).
                setParallelism(1);

        env.execute(this.folderPath + "_CASCADE_HYBRID");

        ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejectionCascade(
                outputPath + "_raw_output.csv",
                outputPath + "_summarized_monthly.csv",
                ParseRawOutputFlinkNoUpdate.MonthRange
        );

        ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejectionCascade(
                outputPath + "_raw_output.csv",
                outputPath + "_summarized_yearly.csv",
                ParseRawOutputFlinkNoUpdate.YearRange
        );

    }

}
