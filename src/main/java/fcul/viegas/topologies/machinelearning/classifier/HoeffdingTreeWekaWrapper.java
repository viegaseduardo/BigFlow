package fcul.viegas.topologies.machinelearning.classifier;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.SamoaToWekaInstanceConverter;
import moa.classifiers.AbstractClassifier;
import moa.core.Measurement;
import weka.classifiers.trees.HoeffdingTree;
import weka.core.Instances;

public class HoeffdingTreeWekaWrapper extends AbstractClassifier{

    private HoeffdingTree hoeff;
    private boolean isInitialized;
    private SamoaToWekaInstanceConverter converter;

    public HoeffdingTreeWekaWrapper() {
        super();
        this.converter = new SamoaToWekaInstanceConverter();
        this.hoeff = new HoeffdingTree();
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
