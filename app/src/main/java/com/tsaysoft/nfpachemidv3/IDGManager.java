package com.tsaysoft.nfpachemidv3;

import java.util.*;

/**
 * <code>IDGManager</code>s manage multiple {@link IDGAbstract}s.
 * <p>
 *     Past experience has shown that it is difficult to query chemical IDs
 *     from chemical names as many names have added terms (e.g. "inhibited")
 *     that interfere with name-to-ID conversion services. Therefore, <code>IDGManager</code>s
 *     take in information from numerous sources to arrive at an accurate chemical ID.
 * </p>
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
public class IDGManager implements IDGInterface{

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    private List<IDGAbstract> IDGs = new ArrayList<>();



    // --------------------
    // CONSTRUCTORS
    // --------------------

    /**
     * Constructs an empty <code>IDGManager</code>.
     *
     * @since 00.01.00
     */
    public IDGManager() {
        super();
    }

    /**
     * Constructs an <code>IDGManager</code> with one <code>IDGetter</code>.
     * <p>
     *     Does not possess the ability to determine between <code>null</code> and non-<code>null</code> inputs.
     * </p>
     *
     * @param IDG the <code>IDGetter</code> to be put into the <code>IDGManager</code>
     *
     * @since 00.01.00
     */
    public IDGManager(IDGAbstract IDG) {
        super();
        IDGs.add(IDG);
    }

    /**
     * Constructs an <code>IDGManager</code> with multiple <code>IDGetter</code>s.
     * <p>
     *     Does not possess the ability to determine between <code>null</code> and non-<code>null</code> inputs.
     * </p>
     *
     * @param IDGCollection a <code>Collection</code> of <code>IDGetter</code>s to be put into the <code>IDGManager</code>
     *
     * @since 00.01.00
     */
    public IDGManager(Collection<IDGAbstract> IDGCollection) {
        super();
        IDGs.addAll(IDGCollection);
    }

    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    @Override
    public String requestID(String chemName, ChemID id) {
        // Initialize variables
        List<String> results = new ArrayList<>();
        Map<String, Integer> idFreqs = new HashMap<>();
        Integer tempInt;
        String maxToken = null;
        int maxFreq = 0;

        // Ask all of the IDGetters for their IDs
        for(IDGAbstract i : IDGs) {
            results.add(i.requestID(chemName, id));
        }

        // Find the numerical frequencies of the IDs
        for(String token : results) {
            if(!idFreqs.containsKey(token)) {
                idFreqs.put(token, 1);
            } else {
                tempInt = idFreqs.get(token);
                idFreqs.put(token, tempInt+1);
            }
        }

        // Find the ID with the greatest frequency
        for(String key : idFreqs.keySet()) {
            if(idFreqs.get(key) > maxFreq) {
                maxFreq = idFreqs.get(key);
                maxToken = key;
            }
        }

        // If there is an ID with greatest frequency, return. If not, randomly choose.

        // TODO: Get this method working
        return maxToken;



    }

    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    private static String determineMax(List<String> strings) {
        //Initialise variables
        Map<String, Integer> map = new HashMap<String, Integer>();

        // Initialise frequency table
        for(String s : strings) {
            Integer freq = map.get(s);
            map.put(s, (freq == null) ? 1 : freq + 1);
        }
        return null;

    }


}
