package preprocessing.Formatting;

/**
 * @author Phillip
 *
 * Enables multithread formatting.
 * Coordinates the different formatted threads.
 */
public class FormattingManager {

    int _counter;


    /**
     * Starts multiple formatting threads.
     * @param numberOfThreads Specifies the number of threads.
     * @param startIndex Specifies with which decision document shall be started. Works only if the decision naming convention is uphold.
     * @param endIndex Specifies which decision document shall be the last to format. Works only if the decision naming convention is uphold.
     * @param sourceBasePath Specifies where the files to format are found.
     * @param targetBasePath Specifies where the formatted files shall be stored.
     */
    public void formatMultithread(int numberOfThreads, int startIndex, int endIndex, String sourceBasePath, String targetBasePath){
        _counter = startIndex;

        for(int i = 1; i <= numberOfThreads; i++){
            new RunnableFormatter(this, endIndex, sourceBasePath, targetBasePath).run();
        }


    }



    public synchronized  int getAndIncrementCounter(){
        _counter++;

        return _counter;
    }

}
