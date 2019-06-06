package com.tsaysoft.nfpachemidv3

/**
 * <h1>ChemSpecial</h1>
 * <h2>(Chemical Special Warning Category, NFPA 704)</h2>
 * Specifies the special properties identified by the NFPA 704 fire diamond.
 *
 * The NFPA 704-standardized properties are as follows:
 * - `OXIDIZER`: the compound is an oxidiser (OX)
 * - `SIMPLE_ASPHYXIANT`: the compound is a simple asphyxiant gas (SA)
 * - `WATER_REACT`: the compound reacts violently with water (~~W~~)
 *
 * There are other symbols that are not NFPA 704-standardized. They are currently
 * unsupported. See [source]()
 *
 * @author Clay Tsay
 * @version 00.01.00
 * @see [NFPA 704 - Wikipedia](https://en.wikipedia.org/wiki/NFPA_704.Codes)
 */
enum class ChemSpecial {
    /**
     * Denotes that the chemical is an oxidizer.
     *
     *
     * Examples:
     *
     *  * KNO<sub>3</sub>
     *  * KClO<sub>3</sub>
     *
     *
     *
     * @since 00.01.00
     */
    OXIDIZER,

    /**
     * Denotes that the chemical is a simple asphyxiant gas.
     *
     *
     * Examples:
     *
     *  * N<sub>2</sub>
     *  * Ar
     *
     *
     *
     * @since 00.01.00
     */
    SIMPLE_ASPHYXIANT,

    /**
     * Denotes that the chemical reacts with water.
     *
     *
     * Examples:
     *
     *  * CH<sub>3</sub>NCO (a.k.a. MIC)
     *  * CaC<sub>2</sub>
     *
     *
     *
     * @since 00.01.00
     */
    WATER_REACT;

    /**
     * Converts a `ChemSpecial` to a `String`.
     *
     *
     * e.g. `SIMPLE_ASPHYXIANT` becomes "`SIMPLE_ASPHYXIANT`".
     *
     * @return the `ChemSpecial` converted into a `String`
     *
     * @since 00.02.00
     */
    override fun toString(): String {
        when (this) {
            OXIDIZER -> return "OXIDIZER"
            SIMPLE_ASPHYXIANT -> return "SIMPLE_ASPHYXIANT"
            WATER_REACT -> return "WATER_REACT"
        }

        throw RuntimeException("unable to convert ChemSpecial to String")
    }

    // TODO: Determine if this is necessary, or if there is a better solution
    fun fromString(string: String): ChemSpecial {
        when (string) {
            "OXIDIZER" -> return OXIDIZER
            "SIMPLE_ASPHYXIANT" -> return SIMPLE_ASPHYXIANT
            "WATER_REACT" -> return WATER_REACT
        }

        throw RuntimeException("unable to convert ChemSpecial to String")
    }
}
