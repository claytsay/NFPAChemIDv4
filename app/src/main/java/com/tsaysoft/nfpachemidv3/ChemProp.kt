package com.tsaysoft.nfpachemidv3

/**
 * <h1>ChemProp</h1>
 * <h2>(Chemical Property Category, NFPA 704)</h2>
 * Specifies the three main properties identified by the NFPA 704 fire diamond.
 *
 * The properties are as follows:
 * - `HEALTH`: how toxic or poisonous the compound is (blue)
 * - `FLAMMABILITY`: how flammable the compound is (red)
 * - `REACTIVITY`: how reactive the compound is, apart from being flammable (yellow)
 *
 * For each chemical, each property is ranked on a scale from 0 - 4, with 0 being least
 * dangerous and 4 being most dangerous.
 * Individual `enum` descriptions are taken from NFPA guidelines posted on Wikipedia
 * ([source](https://en.wikipedia.org/wiki/NFPA_704#Codes)).
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
enum class ChemProp {

    /**
     * Represents the health hazard rating of a chemical.
     *
     * Indicated on the NFPA 704 fire diamond by a <u>blue</u> diamond.
     * - **0** - Poses no health hazard, no precautions necessary and would offer no
     * hazard beyond that of ordinary combustible materials.
     * - **1** - Exposure would cause irritation with only minor residual injury.
     * - **2** - Intense or continued but not chronic exposure could cause temporary
     * incapacitation or possible residual injury.
     * - **3** - Short exposure could cause serious temporary or moderate residual injury.
     * - **4** - Very short exposure could cause death or major residual injury.
     *
     * @since 00.01.00
     */
    HEALTH,

    /**
     * Represents the flammability hazard rating of a chemical.
     *
     * Indicated on the NFPA 704 fire diamond by a <u>red</u> diamond.
     * - **0** - Will not burn under typical fire conditions.
     * - **1** -  Requires considerable preheating, under all ambient temperature conditions
     * before ignition and combustion can occur.
     * - **2** - Must be moderately heated or exposed to relatively high ambient
     * temperature before ignition can occur.
     * - **3** - Can be ignited under almost all ambient temperature conditions.
     * - **4** - Will rapidly or completely vaporize at normal atmospheric
     * pressure and temperature, or is readily dispersed in air and will burn readily.
     *
     * @since 00.01.00
     */
    FLAMMABILITY,

    /**
     * Represents the reactivity hazard rating of a chemical.
     *
     * Indicated on the NFPA 704 fire diamond by a <u>yellow</u> diamond.
     * - **0** - Normally stable, even under fire exposure conditions, and is not reactive with water.
     * - **1** - Normally stable, but can become unstable at elevated temperatures and pressures.
     * - **2** - Undergoes violent chemical change at elevated temperatures and pressures, reacts
     * violently with water, or may form explosive mixtures with water.
     * - **3** - Capable of detonation or explosive decomposition but requires a strong initiating
     * source, must be heated under confinement before initiation, reacts explosively with water, or
     * will detonate if severely shocked.
     * - **4** - Readily capable of detonation or explosive decomposition at normal temperatures
     * and pressures.
     *
     * @since 00.01.00
     */
    REACTIVITY;

    /**
     * Converts a [ChemProp] to a [String].
     *
     * e.g. `HEALTH` becomes "`HEALTH`".
     *
     * @return the [ChemProp] converted into a [String]
     * @since 00.02.00
     */
    override fun toString(): String {
        when (this) {
            HEALTH -> return "HEALTH"
            FLAMMABILITY -> return "FLAMMABILITY"
            REACTIVITY -> return "REACTIVITY"
        }

        throw RuntimeException("unable to convert ChemProp to String")
    }
}
