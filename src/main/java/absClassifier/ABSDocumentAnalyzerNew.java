package absClassifier;


import model.Decision;
import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.type.Result;
import utils.DecisionUtil;
import utils.PropertyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tjorben and Phillip
 */
public class ABSDocumentAnalyzerNew {

    AbSentiment _abSentiment;
    double relevanceScore;

    public Decision analyzeABSResultsAndUpdateVerdict(Result result, Decision decision) {
        decision.setRevisionOutcome(determineRevisionOutcome(result));

        decision.setRelevanceScore(result.getRelevanceScore());

        return decision;
    }



    private int determineRevisionOutcome(Result result) {

        int revisionOutcome = -99;

        if (result.getRelevanceScore() > 0.3) {
            switch (result.getRelevance()) {
                case "revisionsErfolg":
                    revisionOutcome = 1;
                    break;
                case "revisionsMisserfolg":
                    revisionOutcome = -1;
                    break;
                case "revisionsTeilerfolg":
                    revisionOutcome = 0;
                    break;
                default:
                    revisionOutcome = -99;
                    break;
            }
        }
        else{
            revisionOutcome = -100;
        }

        return revisionOutcome;
    }


    /**
     * Classifies a document by analyzing the first 10 sentences
     *
     * @param decisionSentence - The full Document Text as String
     * @return List<Result> - A List of ABS results, where one result is the analyzes of one sentence
     */
    public Result retrieveABSResultsForDocumentText(String decisionSentence) {

        List<Result> resultList = new ArrayList<>();
        Result result = null;

        if (_abSentiment == null) {
            setAbSentiment(new AbSentiment(PropertyManager.getLawProperty(PropertyManager.ABS_CONFIGURATION)));
        }

        decisionSentence = DecisionUtil.getStringAfterColon(decisionSentence);

        result = _abSentiment.analyzeText(decisionSentence);


        return result;
    }


    public void setAbSentiment(AbSentiment abSentiment) {
        _abSentiment = abSentiment;
    }
}
