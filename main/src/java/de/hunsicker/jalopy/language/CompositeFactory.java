package de.hunsicker.jalopy.language;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import de.hunsicker.jalopy.language.antlr.ExtendedToken;
import de.hunsicker.jalopy.language.antlr.JavaNodeFactory;
import de.hunsicker.jalopy.language.antlr.Node;

/**
 * This class creates instances of all the Factories used to generate
 * JavaNodes, Nodes, & Extended Tokens. It is also responsible for maintaining
 * and clearing the cache for these factories
 */
public class CompositeFactory {
    private JavaNodeFactory javaNodeFactory = null;
    private NodeFactory nodeFactory  = null;
    private ExtendedTokenFactory extendedTokenFactory = null;
    
    public class ExtendedTokenFactory {
        private final CompositeFactory compositeFactory;
        private class ExtendedTokenImpl extends ExtendedToken{
            // Empty implementation
        }

        private ExtendedTokenFactory(CompositeFactory compositeFactory) {
            this.compositeFactory = compositeFactory;
        }
        public ExtendedToken create() {
            ExtendedToken cached = (ExtendedToken) compositeFactory.getCached(this.getClass());
            if (cached == null) {
                cached = new ExtendedTokenImpl();
                compositeFactory.addCached(ExtendedTokenFactory.class,cached);
            }
            
            return cached;
        }
        public ExtendedToken create(int type, String text) {
            ExtendedToken token = create();
            token.setType(type);
            token.setText(text);
            return token;
        }
    }
    private final Map cacheMap;
    private final Map reuseMap;
    private Recognizer recognizer;
    public CompositeFactory() {
        this.cacheMap = new HashMap();
        this.reuseMap = new HashMap();
        this.extendedTokenFactory = new ExtendedTokenFactory(this);
        this.javaNodeFactory = new JavaNodeFactory(this);
        this.nodeFactory = new NodeFactory(this);
    }
    /**
     * A factory calls this method to return an object that was cached
     * 
     * @param class1 The class used to index
     * @return
     */
    public Object getCached(Class class1) {
        return null;
// Commented out, just allow the objects to be created by the factories and let
// the GC take care of removing them
//        List cache = (List) reuseMap.get(class1);
//        Object result= null;
//        if (cache!=null && !cache.isEmpty()) {
//            result = cache.remove(0);
//            addCached(class1, result);
//        }
//        
//        return result;
    }
    public void addCached(Class class1, Object cached) {
        List cache = (List) cacheMap.get(class1);
        
        if (cache == null) {
            cache = new Vector();
            cacheMap.put(class1,cache);
            reuseMap.put(class1,new Vector());
        }
        cache.add(cached);        
    }
    
    public ExtendedTokenFactory getExtendedTokenFactory() {
    
        return extendedTokenFactory;
    }
    
    
    public JavaNodeFactory getJavaNodeFactory() {
    
        return javaNodeFactory;
    }
    
    public NodeFactory getNodeFactory() {
    
        return nodeFactory;
    }
    /**
     * Clears all objects that were created in the factories
     * 
     */
    public void clear() {
        List cache = null;
//        List reuseList = null;
        Object item = null;
        // For each of the types clear the contents
        for(Iterator i =cacheMap.entrySet().iterator();i.hasNext();) {
            Map.Entry entry = (Entry) i.next();
//            reuseList = (List) reuseMap.get(entry.getKey());
            cache = (List) entry.getValue();

            for(Iterator l =cache.iterator();l.hasNext();) {
                item = l.next();
                if (item instanceof Node) {
                    ((Node)item).clear();
                }
                else if (item instanceof ExtendedToken) {
                    ((ExtendedToken)item).clear();
                }
//                reuseList.add(item);
                l.remove();
            }
        }
        
    }
    public void setJavadocRecognizer(Recognizer recognizer) {
        this.recognizer = recognizer;
        
    }
    
    public Recognizer getRecognizer() {
        return recognizer;
    }
    
}

