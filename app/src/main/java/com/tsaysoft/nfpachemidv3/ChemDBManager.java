package com.tsaysoft.nfpachemidv3;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

/**
 * <h1>ChemDBManager</h1>
 * <h2>(Chemical Database Manager)</h2>
 * Manages multiple chemical databases.
 * <p>
 *     In essence, has the same functionality as a {@link ChemDB}.
 *     However, has the ability to compile data from numerous sources to provide the most accurate result.
 * </p>
 *
 * @see com.tsaysoft.nfpachemidv3.ChemDBInterface
 * @see com.tsaysoft.nfpachemidv3.ChemDB
 *
 * @author Clay Tsay
 */
public class ChemDBManager implements ChemDBInterface{

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    private List<ChemDB> databases = new ArrayList<>();
    private Context context_master;



    // --------------------
    // CONSTRUCTORS
    // --------------------

    /**
     * Constructs a basic <code>ChemDBManager</code> without any data.
     *
     * @see ChemDBManager#ChemDBManager(Context, String)
     * @see ChemDBManager#ChemDBManager(Context, String[])
     * @see ChemDBManager#ChemDBManager(Context, Collection)
     * @since 00.01.00
     */
    public ChemDBManager(Context context) {
        super();
        context_master = context;
    }

    /**
     * Constructs a <code>ChemDBManager</code> which takes data from a specified file.
     * <p>
     *     Uses the provided file name to construct a single <code>ChemDB</code> which is then
     *     added to the <code>ChemDBManager</code>'s "jurisdiction."
     * </p>
     *
     * @param fileName a file name to be turned into a <code>ChemDB</code>
     *
     * @see ChemDBManager#ChemDBManager(Context)
     * @see ChemDBManager#ChemDBManager(Context, String[])
     * @see ChemDBManager#ChemDBManager(Context, Collection)
     * @since 00.01.00
     */
    public ChemDBManager(Context context, String fileName) {
        super();
        context_master = context;
        databases.add(new ChemDB(context, fileName));
    }

    /**
     * Constructs a <code>ChemDBManager</code> which takes data from specified files.
     * <p>
     *     Uses the provided file names to construct separate <code>ChemDB</code>s which are then
     *     added to the <code>ChemDBManager</code>'s "jurisdiction."
     * </p>
     *
     * @param fileNames a <code>Collection</code> of file names to be turned into <code>ChemDB</code>s
     *
     * @see ChemDBManager#ChemDBManager(Context)
     * @see ChemDBManager#ChemDBManager(Context, String)
     * @see ChemDBManager#ChemDBManager(Context, Collection)
     * @since 00.01.00
     */
    public ChemDBManager(Context context, Collection<String> fileNames) {
        super();
        context_master = context;
        for(String s : fileNames) {
            databases.add(new ChemDB(context, s));
        }
    }

    /**
     * Constructs a <code>ChemDBManager</code> which takes data from specified files.
     * <p>
     *     Uses the provided file names to construct separate <code>ChemDB</code>s which are then
     *     added to the <code>ChemDBManager</code>'s "jurisdiction."
     * </p>
     *
     * @param fileNames a <code>String[]</code> of file names to be turned into <code>ChemDB</code>s
     *
     * @see ChemDBManager#ChemDBManager(Context)
     * @see ChemDBManager#ChemDBManager(Context, String)
     * @see ChemDBManager#ChemDBManager(Context, String[])
     * @since 00.01.00
     */
    public ChemDBManager(Context context, String[] fileNames) {
        super();
        for(String s : fileNames) {
            databases.add(new ChemDB(context, s));
        }
    }



    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    /**
     * Used to query for <code>Chemical</code>s that match each other in their NFPA 704 ratings.
     * <p>
     *     The <code>boolean</code> argument determines whether the special symbols
     *     (specified in {@link ChemSpecial}) should be considered in determining equality.
     * </p>
     * @param query the chemical with properties to be queried
     * @param special whether the special symbols should be taken into account in comparisons
     * @return a <code>Collection</code> of <code>Chemical</code>s matching the properties and/or specials
     *
     * @since 00.01.00
     */
    @Override
    public Collection<Chemical> queryChemNFPA(Chemical query, boolean special) {
        List<Chemical> results = new ArrayList<>();
        for(ChemDB chemDB : databases) {
            results.addAll(chemDB.queryChemNFPA(query, special));
        }

        return results;
        //return removeDuplicates(ResultsActivity, CID);
    }

    /**
     * Used to query for <code>Chemical</code>s that match the given <code>EnumMap</code>'s properties information.
     * <p>
     *     Does <b>not</b> take into account the special symbols.
     * </p>
     *
     * @param properties the <code>EnumMap</code> with properties to be queried
     * @return a <code>Collection</code> of <code>Chemical</code>s matching the properties
     *
     * @see ChemDBInterface#queryEnumMapNFPA(EnumMap, EnumMap)
     * @since 00.01.00
     */
    @Override
    public Collection<Chemical> queryEnumMapNFPA(EnumMap<ChemProp, Integer> properties) {
        Chemical query = new Chemical(null, properties);
        return queryChemNFPA(query, false);
    }

    /**
     * Used to query for <code>Chemical</code>s that match the given <code>EnumMap</code>'s properties and specials information.
     * <p>
     *     <b>Does</b> take into account special symbols.
     * </p>
     *
     * @param properties the <code>EnumMap</code> with properties to be queried
     * @param specials   the <code>EnumMap</code> with special symbols to be queried
     * @return a <code>Collection</code> of <code>Chemical</code>s matching the properties
     *
     * @see ChemDBInterface#queryEnumMapNFPA(EnumMap)
     * @since 00.01.00
     */
    @Override
    public Collection<Chemical> queryEnumMapNFPA(EnumMap<ChemProp, Integer> properties, EnumMap<ChemSpecial, Boolean> specials) {
        Chemical query = new Chemical(null, properties, specials);
        return queryChemNFPA(query, true);
    }


    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    /**
     * Removes duplicate <code>Chemical</code>s from a <code>List</code>.
     * <p>
     *     Duplication is determined by comparing chemical ID tokens.
     *     The type of <code>ChemID</code> compared is determined by a method argument.
     * </p>
     * <p>
     *     <b>WARNING:</b> This method WILL remove <code>Chemical</code>s with
     *     <code>null</code> IDs.
     * </p>
     *
     * @param chemList the <code>List</code> of <code>Chemical</code>s to be cleaned
     * @param id the <code>ChemID</code> type to be used for duplicate determination
     * @return the cleaned <code>Collection</code> of <code>Chemical</code>s, free from duplicates
     *
     * @since 00.02.00
     */
    private Collection<Chemical> removeDuplicates(List<Chemical> chemList, ChemID id) {
        // Declare variables
        Chemical tempOrigin;
        Chemical tempInsertion;

        // Generate IDs for each of the chemicals in the list
        for(Chemical c : chemList) {
            c.genChemID(id);
        }

        // Set the chemicals that match each other in ID to have null names
        // TODO: Sort of a convoluted solution; a more elegant one would be nice.
        for(int i = 0; i < chemList.size()-1; i++) {
            tempOrigin = chemList.get(i);
            if(tempOrigin.getName() != null) {
                for(int j = i+1; j < chemList.size(); j++) {
                    tempInsertion = chemList.get(j);
                    if(tempInsertion.getName() != null &&
                            tempInsertion.getID(id) != null &&
                            tempInsertion.getID(id).equals(tempOrigin.getID(id))) {
                        tempInsertion.setName(null);
                    }
                }
            }
        }

        // Remove all chemicals that have null names
        for(int i = chemList.size()-1; i >= 0; i--) {
            if(chemList.get(i).getName() == null) {
                chemList.remove(i);
            }
        }

        return chemList;
    }

}
