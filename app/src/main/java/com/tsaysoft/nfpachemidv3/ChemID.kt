package com.tsaysoft.nfpachemidv3

/**
 * <h1>ChemID</h1>
 * <h2>(Chemical Identifier Type)</h2>
 * An `enum` that lists the various chemical ID formats.
 *
 * See the individual KDocs for each of the `enum`s for further information.
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
enum class ChemID {
    /**
     * Chemical Abstracts Service Registry Number.
     *
     * The Chemical Abstracts Service (CAS) is a division of the ACS (American
     * Chemical Society). The numerical ID is called a CASRN (CAS Registry
     * Number) or CAS Number.
     *
     * @see [https://www.cas.org/about/faqs](https://www.cas.org/about/faqs)
     * @since 00.01.00
     */
    CASRN,

    /**
     * Compound Identification Number.
     *
     * The Compound Identification Number (CID) is used by PubChem to identify
     * chemical compounds.
     *
     * [Source](https://pubchem.ncbi.nlm.nih.gov/upload/docs/upload_faq.html.UIDtypes)
     *
     * @since 00.01.00
     */
    CID,

    /**
     * International Chemical Identifier - Key version.
     *
     * The International Chemical Identifier (InChI) was developed by IUPAC and NIST.
     * - **InChI Code** is an ID format that is somewhat readable to chemists.
     * - **InChI Key** is a computer-friendly ID format that is not readable to chemists.
     *
     * [Source](https://iupac.org/who-we-are/divisions/division-details/inchi/)
     *
     * @since 00.01.00
     */
    InChI_Key;

    /**
     * Overrides the `toString` to print out the abbreviation/acronym of the ID type.
     *
     * @return the non-hashmap "toString" of the `ChemID` type
     * @since 00.01.00
     */
    override fun toString(): String {
        when (this) {
            CID -> return "CID"
            CASRN -> return "CASRN"
            InChI_Key -> return "InChI Key"
        }

        return "Error - unrecognized ChemID"
    }
}
