public class Javadoc
{
    public int aPublicField = 1;
    protected int aProtectedField = 2;
    int aPackageProtectedField = 3;
    private int aPrivateField = 4;

    /** A Javadoc comment for a field.*/
    private int _count;
@Overide
 /**
     * Getter of the property <tt>myAttribute</tt>
     * @return  Returns the myAttribute.
     * @uml.property  name="myAttribute"
     */
     public Javadoc(int param)
    {
    }

    public String aMethodfoo(int              param1, 
                             com.foo.MyObject param2)
        throws FooException, 
               SillyException
    {
        throw new IllegalArgumentException("");
    }

    private class AnInnerClass
    {
        public void aMethod()
        {
        }
    }
}

public interface AnInterface
{
}