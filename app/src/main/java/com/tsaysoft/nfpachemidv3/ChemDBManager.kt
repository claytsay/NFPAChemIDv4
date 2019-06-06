package com.tsaysoft.nfpachemidv3

import android.content.Context

import java.util.ArrayList
import java.util.EnumMap

/**
 * <h1>ChemDBManager</h1>
 * <h2>(Chemical Database Manager)</h2>
 * Manages multiple chemical databases.
 *
 * In essence, has the same functionality as a [ChemDB].
 * However, has the ability to compile data from numerous sources to provide the most accurate result.
 *
 * @see com.tsaysoft.nfpachemidv3.ChemDBInterface
 * @see com.tsaysoft.nfpachemidv3.ChemDB
 * @author Clay Tsay
 */
class ChemDBManager : ChemDBInterface {

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    private val databases = ArrayList<ChemDB>()
    private var context_master: Context? = null


    // --------------------
    // CONSTRUCTORS
    // --------------------

    // TODO: Clean up these constructors.

    /**
     * Constructs a basic [ChemDBManager] without any data.
     *
     * @constructor Creates an empty [ChemDBManager]
     * @see ChemDBManager.ChemDBManager
     * @see ChemDBManager.ChemDBManager
     * @see ChemDBManager.ChemDBManager
     * @since 00.01.00
     */
    constructor(context: Context) : super() {
        context_master = context
    }

    /**
     * Constructs a `ChemDBManager` which takes data from a specified file.
     *
     * Uses the provided file name to construct a single `ChemDB` which is then
     * added to the `ChemDBManager`'s "jurisdiction."
     *
     * @param fileName a file name to be turned into a `ChemDB`
     * @see ChemDBManager.ChemDBManager
     * @see ChemDBManager.ChemDBManager
     * @see ChemDBManager.ChemDBManager
     * @since 00.01.00
     */
    constructor(context: Context, fileName: String) : super() {
        context_master = context
        databases.add(ChemDB(context, fileName))
    }

    /**
     * Constructs a `ChemDBManager` which takes data from specified files.
     *
     * Uses the provided file names to construct separate `ChemDB`s which are then
     * added to the `ChemDBManager`'s "jurisdiction."
     *
     * @param fileNames a `Collection` of file names to be turned into `ChemDB`s
     * @see ChemDBManager.ChemDBManager
     * @see ChemDBManager.ChemDBManager
     * @see ChemDBManager.ChemDBManager
     * @since 00.01.00
     */
    constructor(context: Context, fileNames: Collection<String>) : super() {
        context_master = context
        for (s in fileNames) {
            databases.add(ChemDB(context, s))
        }
    }

    /**
     * Constructs a `ChemDBManager` which takes data from specified files.
     *
     * Uses the provided file names to construct separate `ChemDB`s which are then
     * added to the `ChemDBManager`'s "jurisdiction."
     *
     * @param fileNames a `String[]` of file names to be turned into `ChemDB`s
     *
     * @see ChemDBManager.ChemDBManager
     * @see ChemDBManager.ChemDBManager
     * @see ChemDBManager.ChemDBManager
     * @since 00.01.00
     */
    constructor(context: Context, fileNames: Array<String>) : super() {
        for (s in fileNames) {
            databases.add(ChemDB(context, s))
        }
    }


    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    /**
     * Used to query for `Chemical`s that match each other in their NFPA 704 ratings.
     *
     *
     * The `boolean` argument determines whether the special symbols
     * (specified in [ChemSpecial]) should be considered in determining equality.
     *
     * @param query the chemical with properties to be queried
     * @param special whether the special symbols should be taken into account in comparisons
     * @return a `Collection` of `Chemical`s matching the properties and/or specials
     *
     * @since 00.01.00
     */
    override fun queryChemNFPA(query: Chemical, special: Boolean): Collection<Chemical> {
        val results = ArrayList<Chemical>()
        for (chemDB in databases) {
            results.addAll(chemDB.queryChemNFPA(query, special))
        }

        return results
        //return removeDuplicates(ResultsActivity, CID);
    }

    /**
     * Used to query for `Chemical`s that match the given `EnumMap`'s properties information.
     *
     *
     * Does **not** take into account the special symbols.
     *
     *
     * @param properties the `EnumMap` with properties to be queried
     * @return a `Collection` of `Chemical`s matching the properties
     *
     * @see ChemDBInterface.queryEnumMapNFPA
     * @since 00.01.00
     */
    override fun queryEnumMapNFPA(properties: EnumMap<ChemProp, Int>): Collection<Chemical> {
        val query = Chemical(null, properties)
        return queryChemNFPA(query, false)
    }

    /**
     * Used to query for `Chemical`s that match the given `EnumMap`'s properties and specials information.
     *
     *
     * **Does** take into account special symbols.
     *
     *
     * @param properties the `EnumMap` with properties to be queried
     * @param specials   the `EnumMap` with special symbols to be queried
     * @return a `Collection` of `Chemical`s matching the properties
     *
     * @see ChemDBInterface.queryEnumMapNFPA
     * @since 00.01.00
     */
    override fun queryEnumMapNFPA(properties: EnumMap<ChemProp, Int>, specials: EnumMap<ChemSpecial, Boolean>): Collection<Chemical> {
        val query = Chemical(null, properties, specials)
        return queryChemNFPA(query, true)
    }


    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    /**
     * Removes duplicate `Chemical`s from a `List`.
     *
     *
     * Duplication is determined by comparing chemical ID tokens.
     * The type of `ChemID` compared is determined by a method argument.
     *
     *
     *
     * **WARNING:** This method WILL remove `Chemical`s with
     * `null` IDs.
     *
     *
     * @param chemList the `List` of `Chemical`s to be cleaned
     * @param id the `ChemID` type to be used for duplicate determination
     * @return the cleaned `Collection` of `Chemical`s, free from duplicates
     *
     * @since 00.02.00
     */
    private fun removeDuplicates(chemList: MutableList<Chemical>, id: ChemID): Collection<Chemical> {
        // Declare variables
        var tempOrigin: Chemical
        var tempInsertion: Chemical

        // Generate IDs for each of the chemicals in the list
        for (c in chemList) {
            c.genChemID(id)
        }

        // Set the chemicals that match each other in ID to have null names
        // TODO: Sort of a convoluted solution; a more elegant one would be nice.
        for (i in 0 until chemList.size - 1) {
            tempOrigin = chemList[i]
            if (tempOrigin.name != null) {
                for (j in i + 1 until chemList.size) {
                    tempInsertion = chemList[j]
                    if (tempInsertion.name != null &&
                            tempInsertion.getID(id) != null &&
                            tempInsertion.getID(id) == tempOrigin.getID(id)) {
                        tempInsertion.name = null
                    }
                }
            }
        }

        // Remove all chemicals that have null names
        for (i in chemList.indices.reversed()) {
            if (chemList[i].name == null) {
                chemList.removeAt(i)
            }
        }

        return chemList
    }

}
