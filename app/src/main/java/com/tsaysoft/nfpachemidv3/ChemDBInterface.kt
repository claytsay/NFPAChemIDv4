package com.tsaysoft.nfpachemidv3

import java.util.EnumMap

/**
 * <h1>ChemDBInterface</h1>
 * <h2>(Chemical Database Interface)</h2>
 * An interface specifying the functions of [Chemical] databases.
 *
 * Applies to chemical databases and super-databases (collections of databases).
 *
 * @see com.tsaysoft.nfpachemidv3.ChemDB
 * @see com.tsaysoft.nfpachemidv3.ChemDBManager
 * @author Clay Tsay
 * @version 00.01.01
 */
interface ChemDBInterface {


    // --------------------
    // METHODS
    // --------------------

    /**
     * Used to query for `Chemical`s that match each other in their NFPA 704 ratings.
     *
     * The `boolean` argument determines whether the special symbols
     * (specified in [ChemSpecial]) should be considered in determining equality.
     *
     * @param query the chemical with properties to be queried
     * @param special whether the special symbols should be taken into account in comparisons
     * @return a `Collection` of `Chemical`s matching the properties and/or specials
     * @since 00.01.00
     */
    fun queryChemNFPA(query: Chemical, special: Boolean): Collection<Chemical>

    /**
     * Used to query for `Chemical`s that match the given `EnumMap`'s properties information.
     *
     * Does **not** take into account the special symbols.
     *
     * @param properties the `EnumMap` with properties to be queried
     * @return a `Collection` of `Chemical`s matching the properties
     * @see ChemDBInterface.queryEnumMapNFPA
     * @since 00.01.00
     */
    fun queryEnumMapNFPA(properties: EnumMap<ChemProp, Int>): Collection<Chemical>

    /**
     * Used to query for `Chemical`s that match the given `EnumMap`'s properties and specials information.
     *
     * **Does** take into account special symbols.
     *
     * @param properties the `EnumMap` with properties to be queried
     * @param specials the `EnumMap` with special symbols to be queried
     * @return a `Collection` of `Chemical`s matching the properties
     * @see ChemDBInterface.queryEnumMapNFPA
     * @since 00.01.00
     */
    fun queryEnumMapNFPA(properties: EnumMap<ChemProp, Int>,
                         specials: EnumMap<ChemSpecial, Boolean>): Collection<Chemical>

    companion object {

        // --------------------
        // CONSTANTS
        // --------------------
        // TODO: Maybe migrate to ChemDB class.

        /**
         * A file name referencing a NFPA 704 data file.
         *
         * This data was sourced from `www.newenv.com`.
         *
         * @see [https://www.newenv.com/resources/nfpa_chemicals/](https://www.newenv.com/resources/nfpa_chemicals/)
         * @since 00.02.00
         */
        val JSON_DATA_1 = "NFPA704_DataSet_1.json"

        /**
         * A file name referencing a NFPA 704 data file.
         *
         * This data was sourced from `www.weneedsigns.com`.
         *
         * @see [http://www.weneedsigns.com/pages.php?pageid=14](http://www.weneedsigns.com/pages.php?pageid=14)
         * @since 00.02.00
         */
        val JSON_DATA_2 = "NFPA704_DataSet_2.json"
    }

}
