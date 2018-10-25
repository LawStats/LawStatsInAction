package study.AttributeAnalyzer;

import model.Decision;
import utils.DateUtil;
import utils.RegexUtil;
import utils.TextUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateAnalyzer {

    private static DateAnalyzer dateAnalyzer;

    public static DateAnalyzer getInstance() {
        if (dateAnalyzer == null) {
            dateAnalyzer = new DateAnalyzer();
        }

        return dateAnalyzer;
    }


    private DateAnalyzer() {
    }

    /**
     * Finds the dates of the decision itself and the preliminary decisions.
     * The dates are set in the decision object.
     * If no preliminary decisions exist the dates are set to null.
     *
     * @param decisionSentence The decisionSentence of the document
     * @param lastSentences The last sentences in the document
     * @param decision  The Decision object corresponding to the document being analyzed
     * @return A Decision object with the decision date and preliminary decision dates (and all previously extracted information) set
     */
    public Decision analyzeDocumentForDate(String decisionSentence, String lastSentences, Decision decision) {

        String fragementToAnalyze;
        Set<String> datesFoundUnique = new HashSet<>();
        Long decisionDateLong = null;
        List<Long> datesLong;
        List<String> decisionDateList = new ArrayList<>();
        CourtAnalyzer courtAnalyzer = CourtAnalyzer.getInstance();

        String dateRegex = "(0?[1-9]|[0-2]\\d|3[01])[-\\s]+((Janu|Febru)ar|März|April|Mai|Ju(ni|li)|August|(Septem|Okto|Novem|Dezem)ber|\\d|\\d\\d)-?\\s?\\d{2,4}";
        List<Integer> courtPositionsDecisionSentence = courtAnalyzer.getCourtPositionsDecisionSentence();
        List<Integer> courtPositionsEnd = courtAnalyzer.getCourtPositionsEnd();


        //finds and sets the decisionDate
        if (decisionSentence != null) {
            decisionDateList = RegexUtil.findAllMatches(dateRegex, decisionSentence);

            if (!decisionDateList.isEmpty()) {
                decisionDateLong = DateUtil.formatStringToLong(decisionDateList.get(0));
                decision.setDateDecision(decisionDateLong);
            }
        }

        //determines the text fragement of the decisionSentence that shall be analyzed
        //finds all dates in that fragement
        if (!courtPositionsDecisionSentence.isEmpty()) {
            for (Integer position : courtPositionsDecisionSentence) {
                fragementToAnalyze = TextUtil.getFragementFromText(position, 3, 4, decisionSentence);
                datesFoundUnique.addAll(RegexUtil.findAllMatches(dateRegex, fragementToAnalyze));
            }
        }


        //determines the text fragement of the lastSentences that shall be analyzed
        //finds all dates in that fragement
        if (!courtPositionsEnd.isEmpty()) {
            for (Integer position : courtPositionsEnd) {
                fragementToAnalyze = TextUtil.getFragementFromText(position, 3, 4, lastSentences);
                datesFoundUnique.addAll(RegexUtil.findAllMatches(dateRegex, fragementToAnalyze));
            }
        }


        //Transforms the String dates into Long dates
        datesLong = DateUtil.formatStringDateToLongList(datesFoundUnique);

        //removes all dates with a higher or equal value compared to the decisionDate
        //assignes the dates to the preliminary decisions
        if (!datesLong.isEmpty() && decision.getDateDecision() != null) {
            removeIrrelevantDates(decision.getDateDecision(), datesLong);
            sortAndSetDatesInDecision(decision, datesLong);
        }

        //For debugging purposes
        //printOutDatesAsString(decision);


        return decision;
    }


    /**
     * Assigns the dates found in the document to the corresponding preliminary decision.
     * Assignment is done based on the value of the date. The date with the highest value is assigned to the first preliminary decision.
     * @param decision The decision object corresponding to the current document being analyzed
     * @param datesLong The list of dates found in the document
     */
    private void sortAndSetDatesInDecision(Decision decision, List<Long> datesLong) {

        //removes all duplicates
        Set<Long> datesLongWithoutDuplicates = new HashSet<>(datesLong);
        List<Long> datesLongList = new ArrayList<>(datesLongWithoutDuplicates);

        datesLongList.sort(Comparator.reverseOrder());
        int datesLongLength = datesLongList.size();


        if (datesLongLength > 0) {

            if (decision.getDecisionOLG() != null) {
                decision.setDateOLG(datesLongList.get(0));
            } else if (decision.getDecisionLG() != null) {
                decision.setDateLG(datesLongList.get(0));
            } else if (decision.getDecisionAG() != null) {
                decision.setDateAG(datesLongList.get(0));
            }


            if (datesLongLength > 1) {
                if (decision.getDecisionLG() != null && decision.getDateLG() == null) {
                    decision.setDateLG(datesLongList.get(1));
                } else if (decision.getDecisionAG() != null && decision.getDateAG() == null) {
                    decision.setDateAG(datesLongList.get(1));
                }
            }
        }
    }


    /**
     * Removes all dates with a higher or equal value compared to the decisionDate
     * @param decisionDate The date of the decision itself
     * @param datesList The list of dates found in the text fragements
     */
    private void removeIrrelevantDates(Long decisionDate, List<Long> datesList) {
        List<Long> removeList = new ArrayList<>();

        for (Long date : datesList) {
            if (date >= decisionDate) {
                removeList.add(date);
            }
        }

        datesList.removeAll(removeList);
    }


    /**
     * Method for debugging purposes.
     * Prints the dates (given as longs) to the console as formatted STrings.
     *
     * @param decision The decision for which the dates shall be printed
     */
    private void printOutDatesAsString(Decision decision) {
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String decisionDateString = null;
        String olgDateString = null;
        String lgDateString = null;
        String agDateString = null;


        if (decision.getDateDecision() != null) {
            Date dateDecision = new Date(decision.getDateDecision());
            decisionDateString = df2.format(dateDecision);
        }
        if (decision.getDateOLG() != null) {

            Date dateOLG = new Date(decision.getDateOLG());
            olgDateString = df2.format(dateOLG);
        }
        if (decision.getDateLG() != null) {
            Date dateLG = new Date(decision.getDateLG());
            lgDateString = df2.format(dateLG);
        }

        if (decision.getDateAG() != null) {
            Date dateAG = new Date(decision.getDateAG());
            agDateString = df2.format(dateAG);
        }

        System.out.println("Entscheidungsdatum: " + decisionDateString);
        System.out.println("OLG: " + olgDateString);
        System.out.println("LG: " + lgDateString);
        System.out.println("AG: " + agDateString);
    }

}
