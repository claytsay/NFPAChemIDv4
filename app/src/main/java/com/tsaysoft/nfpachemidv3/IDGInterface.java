package com.tsaysoft.nfpachemidv3;

/**
 * <h1>IDGInterface</h1>
 * <h2>(ID Getter Interface)</h2>
 * The Java interface that unifies chemical ID getters.
 * <p>
 *     Mainly applies to <code>IDGetter</code>, its subclasses, and <code>IDGManager</code>.
 * </p>
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
public interface IDGInterface {

    /**
     * Uses an HTTP request to convert a chemical name to a specific type of chemical ID.
     * <p>
     *     If the ID in unavailable, will print error to console and return <code>null</code>.
     * </p>
     *
     * @param chemName the trivial (common) or formal (IUPAC) name of the chemical
     * @return the chemical's requested ID as a <code>String</code>
     *
     * @since 00.01.00
     */
    String requestID(String chemName, ChemID id);
}
