/**
* Test
*/
public class Braces
{
/**
 * Annotation type to indicate a task still needs to be
 *   completed.
 */
    @Target( { ANNOTATION_TYPE, METHOD } )
    @Retention( RUNTIME )
 public @interface TODO {
  String value() default "TODO";
}

/**
 * The card enumeration with built in constructor
 */
enum CardValue {
    ACE(1, 1, 11), DEUCE(2, 2), THREE(3, 3), FOUR(4, 4), 
    FIVE(5, 5), SIX(6, 6), SEVEN(7, 7), EIGHT(8, 8), 
    NINE(9, 9), TEN(10, 10), JACK(11, 10), QUEEN(12, 10), KING(13, 10);
    
    //extra private information
    private int order; // A simple assignment !
    private int value;
    private int otherValue;
    
    //constructor
    CardValue(int order, int value){
        this.order = order;
        this.value = value;
        this.otherValue = -1;
    }}

    public enum NavigateTo
    {
        currentPage,
        previousPage,
        previousAction,
        page
    }
@TODO(value="Figure out the amount of interest per month")
public void calculateInterest(float amount, float rate) {
  // Need to finish this method later
}    
/**
* Test 2
*/
    public void foo() throws IOException, FileNotFoundException, InterruptedIOException, UnsupportedEncodingException
    {
        do
        {
            for (int i = 0, n = getMax(); i < n; i++)
            {
                doFor();
            } // end for

            try
            {
                if (x > 0)
                {
                    doIf();
                } // end if
                else if (x < 0)
                {
                    doElseIf();
                } // end else if
                else
                {
                    doElse();
                } // end else

                switch (a)
                {
                    case 0 : {
                        doCase0();
                        break;
                    } // end case
                    case 1:
                        doCase1();
                        break;

                    default : {
                        doDefault();
                        break;
                    } // end default
                } // end switch
            } // end try
            catch (Exception e)
            {
                processException(e.getMessage(), x + y, z, a);
            } // end catch
            finally
            {
                processFinally();
            } // end finally
        }
        while (true);
    } // end method
} // end class