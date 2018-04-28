package com.tsaysoft.nfpachemidv3;

import java.util.Collection;
import java.util.EnumMap;

/**
 * <h1>ChemDBInterface</h1>
 * <h2>(Chemical Database Interface)</h2>
 * An interface specifying the functions of {@link Chemical} databases.
 * <p>
 *     Applies to chemical databases and super-databases (collections of databases).
 * </p>
 *
 * @see com.tsaysoft.nfpachemidv3.ChemDB
 * @see com.tsaysoft.nfpachemidv3.ChemDBManager
 *
 * @author Clay Tsay
 * @version 00.01.01
 */
public interface ChemDBInterface {

    // --------------------
    // CONSTANTS
    // --------------------
    // TODO: Maybe migrate to ChemDB class.

    /**
     * A file name referencing a NFPA 704 data file.
     * <p>
     *     This data was sourced from <code>www.newenv.com</code>.
     * </p>
     *
     * @see <a href="https://www.newenv.com/resources/nfpa_chemicals/">https://www.newenv.com/resources/nfpa_chemicals/</a>
     * @since 00.02.00
     */
    String JSON_DATA_1 = "NFPA704_DataSet_1.json";

    /**
     * A file name referencing a NFPA 704 data file.
     * <p>
     *     This data was sourced from <code>www.weneedsigns.com</code>.
     * </p>
     *
     * @see <a href="http://www.weneedsigns.com/pages.php?pageid=14">http://www.weneedsigns.com/pages.php?pageid=14</a>
     * @since 00.02.00
     */
    String JSON_DATA_2 = "NFPA704_DataSet_2.json";



    // --------------------
    // METHODS
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
    Collection<Chemical> queryChemNFPA(Chemical query, boolean special);

    /**
     * Used to query for <code>Chemical</code>s that match the given <code>EnumMap</code>'s properties information.
     * <p>
     *     Does <b>not</b> take into account the special symbols.
     * </p>
     * @param properties the <code>EnumMap</code> with properties to be queried
     * @return a <code>Collection</code> of <code>Chemical</code>s matching the properties
     *
     * @see ChemDBInterface#queryEnumMapNFPA(EnumMap, EnumMap)
     * @since 00.01.00
     */
    Collection<Chemical> queryEnumMapNFPA(EnumMap<ChemProp, Integer> properties);

    /**
     * Used to query for <code>Chemical</code>s that match the given <code>EnumMap</code>'s properties and specials information.
     * <p>
     *     <b>Does</b> take into account special symbols.
     * </p>
     * @param properties the <code>EnumMap</code> with properties to be queried
     * @param specials the <code>EnumMap</code> with special symbols to be queried
     * @return a <code>Collection</code> of <code>Chemical</code>s matching the properties
     *
     * @see ChemDBInterface#queryEnumMapNFPA(EnumMap)
     * @since 00.01.00
     */
    Collection<Chemical> queryEnumMapNFPA(EnumMap<ChemProp, Integer> properties,
                                          EnumMap<ChemSpecial, Boolean> specials);

}
