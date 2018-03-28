package fcul.viegas.topologies.machinelearning.classifier;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.SamoaToWekaInstanceConverter;
import moa.classifiers.AbstractClassifier;
import moa.core.Measurement;
import weka.classifiers.trees.HoeffdingTree;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Tag;

public class HoeffdingTreeWekaWrapper extends AbstractClassifier{

    private HoeffdingTree hoeff;
    private boolean isInitialized;
    private SamoaToWekaInstanceConverter converter;

    public HoeffdingTreeWekaWrapper() {
        super();
        this.converter = new SamoaToWekaInstanceConverter();
        this.hoeff = new HoeffdingTree();

        int LEAF_MAJ_CLASS = 0;
        int LEAF_NB = 1;
        int LEAF_NB_ADAPTIVE = 2;
        Tag[] TAGS_SELECTION2 = {
                new Tag(LEAF_MAJ_CLASS, "Majority class"),
                new Tag(LEAF_NB, "Naive Bayes"),
                new Tag(LEAF_NB_ADAPTIVE, "Naive Bayes adaptive") };

        this.hoeff.setLeafPredictionStrategy(new SelectedTag(2,
                TAGS_SELECTION2));

        this.isInitialized = false;
    }

    @Override
    public double[] getVotesForInstance(Instance instance) {
        weka.core.Instance instWeka = this.converter.wekaInstance(instance);
        try {
            return this.hoeff.distributionForInstance(instWeka);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void resetLearningImpl() {
        this.hoeff = new HoeffdingTree();

        int LEAF_MAJ_CLASS = 0;
        int LEAF_NB = 1;
        int LEAF_NB_ADAPTIVE = 2;
        Tag[] TAGS_SELECTION2 = {
                new Tag(LEAF_MAJ_CLASS, "Majority class"),
                new Tag(LEAF_NB, "Naive Bayes"),
                new Tag(LEAF_NB_ADAPTIVE, "Naive Bayes adaptive") };

        this.hoeff.setLeafPredictionStrategy(new SelectedTag(2,
                TAGS_SELECTION2));

        this.converter = new SamoaToWekaInstanceConverter();
        this.isInitialized = false;
    }

    @Override
    public void trainOnInstanceImpl(Instance instance) {
        weka.core.Instance instWeka = this.converter.wekaInstance(instance);
        try {
            if(!this.isInitialized){
                this.hoeff.buildClassifier(this.converter.wekaInstancesInformation(instance.dataset()));
                this.isInitialized = true;
            }
            this.hoeff.updateClassifier(instWeka);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected Measurement[] getModelMeasurementsImpl() {
        return new Measurement[0];
    }

    @Override
    public void getModelDescription(StringBuilder stringBuilder, int i) {

    }

    @Override
    public boolean isRandomizable() {
        return false;
    }
}
