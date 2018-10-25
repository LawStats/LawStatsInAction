package utils;

import java.io.FileReader;
import java.util.Properties;

/**
 * @author Phillip
 */
public class PropertyManager {


    public static final String DOWNLOADTARGETDIRECTION = "downloadTargetDirection";
    public static final String RANDOMDOCUMENT_SOURCEBASEPATH = "randomDocumentSourceBasePath";
    public static final String RANDOMDOCUMENT_TARGETBASEPATH = "randomDocumentTargetBasePath";
    public static final String ABS_CONFIGURATION = "ABSentimentConfiguration";
    public static final String TRAININGSDATA_BASEFILEPATH = "trainingsDataBaseFilesFolderPath";
    public static final String TRAININGSDATA_FILE = "trainingsDataFile";
    public static final String TESTDATA_FILE = "testDataFile";
    public static final String TRAINGSDATA_ANNOTATIONS = "trainingsDataAnnotationsFolderPath";
    public static final String RELATION_TRAINING_TO_TESTDATA = "relationTrainingsToTestdata";
    public static final String WATSON_CLASSIFIER_USERNAME = "watson_classifier_username";
    public static final String WATSON_CLASSIFIER_PASSWORD = "watson_classifier_password";
    public static final String WATSON_CLASSIFIER_TRAININGSDATAFILE = "watson_classifier_trainingsdatafile";
    public static final String WATSON_CLASSIFIER_MODELID = "watson_classifier_modelid";
    public static final String WATSON_NLU_USERNAME = "watson_nlu_username";
    public static final String WATSON_NLU_PASSWORD = "watson_nlu_password";
    public static final String WATSON_NLU_MODELID = "watson_nlu_modelid";
    public static final String DEPLOYMODE = "deploy.mode";


    /**
     * Returns a property value for a given Key.
     * The Constants should be used for the key.
     * @param propertyKey The Parameter Key
     * @return the property value
     */
    public static String getLawProperty(String propertyKey) {

        String propertyString = null;

        try {
            Properties properties = new Properties();
            properties.load(new FileReader("resources/config/lawstats.properties"));

            propertyString = properties.getProperty(propertyKey);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return propertyString;
    }

}
