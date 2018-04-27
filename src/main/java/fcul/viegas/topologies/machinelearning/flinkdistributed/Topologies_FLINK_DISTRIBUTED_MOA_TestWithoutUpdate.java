package fcul.viegas.topologies.machinelearning.flinkdistributed;

import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.core.fs.FileSystem;
import moa.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Random;

public class Topologies_FLINK_DISTRIBUTED_MOA_TestWithoutUpdate {

    public String folderPath;
    public String featureSET;
    //public static String PathToModel = "/home/viegas/model";

    public void run(String pathArffs, String featureSet, String outputPath, String classifierToBuild, int daysToUseForTraining, int numEnsemble) throws Exception {
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
        dataTrain.randomize(new Random(1));
        for (int i = 1; i < daysToUseForTraining; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            dataTrainInc.randomize(new Random(1));
            for (Instance inst : dataTrainInc) {
                dataTrain.add(inst);
            }
        }

        if (featureSet.equals("VIEGAS")) {
            dataTrain = mlModelBuilder.removeParticularAttributesViegas(dataTrain);
        } else if (featureSet.equals("ORUNADA")) {
            dataTrain = mlModelBuilder.removeParticularAttributesOrunada(dataTrain);
        }


        final Classifier classifier = classifierToBuild.equals("hoeffding")
                ? mlModelBuilder.trainClassifierHoeffdingTreeMOA(dataTrain) : classifierToBuild.equals("hoeffdingadaptivetree")
                ? mlModelBuilder.trainClassifierHoeffingAdaptiveTreeMOA(dataTrain) : classifierToBuild.equals("ozabagging")
                ? mlModelBuilder.trainClassifierOzaBaggingMOA(dataTrain, numEnsemble) : classifierToBuild.equals("ozaboosting")
                ? mlModelBuilder.trainClassifierOzaBoostingMOA(dataTrain,  numEnsemble) : classifierToBuild.equals("adaptiveforest")
                ? mlModelBuilder.trainClassifierAdaptiveRandomForestMOA(dataTrain) : classifierToBuild.equals("adahoeffdingoptiontree")
                ? mlModelBuilder.trainClassifierAdaHoeffdingOptionTreeMOA(dataTrain) : classifierToBuild.equals("ocboost")
                ? mlModelBuilder.trainClassifierOCBoostMOA(dataTrain, numEnsemble) : classifierToBuild.equals("leveragingbag")
                ? mlModelBuilder.trainClassifierLeveragingBagMOA(dataTrain, numEnsemble) : null;
/*
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate.PathToModel));
        oos.writeObject(classifier);
        oos.flush();
        oos.close();
*/
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();


        //Collections.shuffle(testFiles);
        DataSet<String> testFilesDataset = env.fromCollection(testFiles.subList(0, 1000));
        System.out.println("CLASSIFIER BUILT");

        testFilesDataset.map(new EvaluateClassiferMapFunction(mlModelBuilder, null, classifier))
                .setParallelism(env.getParallelism())
                .sortPartition(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String in) throws Exception {
                        return in;
                    }
                }, Order.ASCENDING).setParallelism(1).
                writeAsText(outputPath + "_raw_output.csv", FileSystem.WriteMode.OVERWRITE).
                setParallelism(1);

        env.execute(pathArffs + "_DISTRIBUTED_NO_UPDATE");

        ParseRawOutputFlinkNoUpdate.generateSummaryFileWithoutRejection(outputPath + "_raw_output.csv", outputPath + "_summarized_monthly.csv",
                ParseRawOutputFlinkNoUpdate.MonthRange);

        ParseRawOutputFlinkNoUpdate.generateSummaryFileWithoutRejection(outputPath + "_raw_output.csv", outputPath + "_summarized_yearly.csv",
                ParseRawOutputFlinkNoUpdate.YearRange);

    }
}
