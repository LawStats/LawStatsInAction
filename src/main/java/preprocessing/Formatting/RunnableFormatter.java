package preprocessing.Formatting;

/**
 * @author Phillip
 *
 * Helper class to enables multithread formatting.
 */
public class RunnableFormatter implements Runnable {

    FormattingManager _formattingManager;
    int _endIndex;
    String _sourceBasePath;
    String _targetBasePath;


    public RunnableFormatter(FormattingManager formattingManager, int endIndex, String sourceBasePath, String targetBasePath){
        _formattingManager = formattingManager;
        _endIndex = endIndex;
        _sourceBasePath = sourceBasePath;
        _targetBasePath = targetBasePath;
    }



    public void run(){
        int counter = _formattingManager.getAndIncrementCounter();


        while(counter<=_endIndex){
            Formatter.formatText(_sourceBasePath+"\\verdict"+counter+".txt", _targetBasePath+"\\verdict"+counter+"_clean.txt");
            counter = _formattingManager.getAndIncrementCounter();
        }
    }
}
