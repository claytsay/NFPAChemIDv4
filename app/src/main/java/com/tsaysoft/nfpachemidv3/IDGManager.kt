package com.tsaysoft.nfpachemidv3

import java.util.*

/**
 * [IDGManager]s manage multiple [IDGAbstract]s.
 *
 * Past experience has shown that it is difficult to query chemical IDs
 * from chemical names as many names have added terms (e.g. "inhibited")
 * that interfere with name-to-ID conversion services. Therefore, [IDGManager]s
 * take in information from numerous sources to arrive at an accurate chemical ID.
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
class IDGManager : IDGInterface {

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    private val IDGs = ArrayList<IDGAbstract>()


    // --------------------
    // CONSTRUCTORS
    // --------------------

    /**
     * Constructs an empty `IDGManager`.
     *
     * @since 00.01.00
     */
    constructor() : super() {}

    /**
     * Constructs an [IDGManager] with one `IDGetter`.
     *
     * Does not possess the ability to determine between `null` and non-`null`
     * inputs.
     *
     * @param IDG the `IDGetter` to be put into the [IDGManager]
     * @since 00.01.00
     */
    constructor(IDG: IDGAbstract) : super() {
        IDGs.add(IDG)
    }

    /**
     * Constructs an [IDGManager] with multiple `IDGetter`s.
     *
     *
     * Does not possess the ability to determine between `null` and non-`null`
     * inputs.
     *
     * @param IDGCollection a [Collection] of `IDGetter`s to be put into
     *      the [IDGManager]
     * @since 00.01.00
     */
    constructor(IDGCollection: Collection<IDGAbstract>) : super() {
        IDGs.addAll(IDGCollection)
    }

    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    override fun requestID(chemName: String, id: ChemID): String {
        // Initialize variables
        val results = ArrayList<String>()
        val idFreqs = HashMap<String, Int>()
        var tempInt: Int?
        var maxToken: String? = null
        var maxFreq = 0

        // Ask all of the IDGetters for their IDs
        for (i in IDGs) {
            results.add(i.requestID(chemName, id))
        }

        // Find the numerical frequencies of the IDs
        // TODO: (somehow) null-proof this section of the programme
        for (token in results) {
            if (!idFreqs.containsKey(token)) {
                idFreqs[token] = 1
            } else {
                tempInt = idFreqs[token]
                idFreqs[token] = tempInt!! + 1
            }
        }

        // Find the ID with the greatest frequency
        // Maybe make it so that if there is a tie the ID with the greatest frequency is chosen.
        for (key in idFreqs.keys) {
            if (idFreqs[key] > maxFreq) {
                maxFreq = idFreqs[key]
                maxToken = key
            }
        }

        return maxToken

    }

    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    // TODO: Get this method working
    // You might also want to figure out what it's supposed to do
    private fun determineMax(strings: List<String>): String? {
        //Initialise variables
        val map = HashMap<String, Int>()

        // Initialise frequency table
        for (s in strings) {
            val freq = map[s]
            map[s] = if (freq == null) 1 else freq + 1
        }
        return null

    }


}
