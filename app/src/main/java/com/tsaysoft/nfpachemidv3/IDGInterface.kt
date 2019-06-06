package com.tsaysoft.nfpachemidv3

/**
 * # IDGInterface
 * ## (ID Getter Interface)
 * The interface that unifies chemical ID getters.
 *
 * Mainly applies to [IDGetter], its subclasses, and [IDGManager].
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
interface IDGInterface {

    /**
     * Uses an HTTP request to convert a chemical name to a specific type of
     * chemical ID.
     *
     * If the ID in unavailable, will print error to console and return `null`.
     *
     * @param chemName the trivial (common) or formal (IUPAC) name of the
     *      chemical
     * @return the chemical's requested ID as a `String`
     * @since 00.01.00
     */
    fun requestID(chemName: String, id: ChemID): String

}
