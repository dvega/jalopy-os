package de.hunsicker.jalopy.plugin.jdeveloper;


import java.util.ListResourceBundle;


public class JDevExtensionBundle extends ListResourceBundle
{

    public JDevExtensionBundle()
    {
    }

    public Object[][] getContents()
    {
        return contents;
    }

    static final Object contents[][] = 
    {
        { "EXTENSION_DESCRIPTION", "See http://jalopy.sourceforge.net/ for more information" }, 
        { "EXTENSION_NAME", "Jalopy Java Sourfce Code Formatter" },
        { "EXTENSION_OWNER", "Rob Clevenger" }
    };

}


