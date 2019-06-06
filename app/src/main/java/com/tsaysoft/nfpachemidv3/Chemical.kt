package com.tsaysoft.nfpachemidv3

import java.util.EnumMap

import com.tsaysoft.nfpachemidv3.ChemProp.*
import com.tsaysoft.nfpachemidv3.ChemSpecial.*

/**
 * <h1>Chemical</h1>
 * <h2>(Chemical)</h2>
 * Represents a chemical and its associated information.
 *
 *
 * Carries NFPA 704 information and chemical ID information.
 * Can ask an [IDGAbstract] to retrieve a chemical ID from a name.
 *
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
class Chemical {

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    var name: String? = null
    val props = EnumMap<ChemProp, Int>(ChemProp::class.java)
    var specials = EnumMap<ChemSpecial, Boolean>(ChemSpecial::class.java)

    private val ids = EnumMap<ChemID, String>(ChemID::class.java)

    /**
     * Constructs an instance of [Chemical] with special symbols.
     *
     * @constructor Creates a new [Chemical]
     * @param chemName name of the chemical
     * @param health health rating of the chemical
     * @param flammability flammability rating of the chemical
     * @param reactivity reactivity rating of the chemical
     * @param special array terms correspond to symbols: `0` = OX, `1` = SA, `2` = W
     * @see com.tsaysoft.nfpachemidv3.Chemical.Chemical
     * @since 00.01.00
     */
    @Deprecated("This function is relatively depreciated due to the switch to using " +
            "<code>EnumMap</code>s to store information.")
    @JvmOverloads
    constructor(chemName: String?,
                health: Int, flammability:
                Int, reactivity: Int,
                special: BooleanArray? = null) : super() {

        name = chemName
        props[HEALTH] = health
        props[FLAMMABILITY] = flammability
        props[REACTIVITY] = reactivity

        try {
            // Make sure to alter this for special non-NFPA 704 symbols that will be added later
            if (special == null || special.size != 3) {
                throw ArrayIndexOutOfBoundsException()
            } else {
                specials[OXIDIZER] = special[0]
                specials[SIMPLE_ASPHYXIANT] = special[1]
                specials[WATER_REACT] = special[2]
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("special array is wrong size - Chemical constructed without specials")
        }

    }


    // --------------------
    // CONSTRUCTORS
    // --------------------

    /**
     * Constructs an instance of `Chemical` either with or without special
     * symbols.
     *
     * Takes a properties `EnumMap` that **must** have the the following
     * `enums`:
     *  - [ChemProp.HEALTH]
     *  - [ChemProp.FLAMMABILITY]
     *  - [ChemProp.REACTIVITY]
     * Otherwise, will throw `IllegalArgumentException`.
     * The specials `EnumMap` is not checked in order to provide versatility
     * (i.e. for non-NFPA 704 special symbols).
     *
     * @param chemName name of the chemical
     * @param props `EnumMap` containing the chemical's properties information
     * @param specs `EnumMap` containing the chemical's special properties/symbols information
     * @throws IllegalArgumentException if the input `EnumMap` is invalid
     * @since 00.01.00
     */
    @Throws(IllegalArgumentException::class)
    @JvmOverloads
    constructor(chemName: String?,
                props: EnumMap<ChemProp, Int>,
                specs: EnumMap<ChemSpecial, Boolean>? = null) {
        name = chemName
        if (props.containsKey(HEALTH) &&
                props.containsKey(FLAMMABILITY) &&
                props.containsKey(REACTIVITY)) {
            this.props[HEALTH] = props[HEALTH]
            this.props[FLAMMABILITY] = props[FLAMMABILITY]
            this.props[REACTIVITY] = props[REACTIVITY]
        } else {
            throw IllegalArgumentException("wrong EnumMap - Chemical not constructed")
        }

        if (specs == null) {
            for (symbol in ChemSpecial.values()) {
                this.specials[symbol] = false
            }
        } else {
            for (symbol in ChemSpecial.values()) {
                val tempBool = specs[symbol]
                if (tempBool == null) {
                    specials[symbol] = false
                } else {
                    specials[symbol] = tempBool
                }
            }
        }

    }

    /**
     * Gets an identification code of the chemical.
     *
     * Has problems with chemicals with commas in their names.
     * (e.g. "Phosphorus, amorphous, red")
     * TODO Fix ID issues
     *
     * @return the chemical ID code as a `String`
     * @since 00.01.00
     */
    fun getID(idType: ChemID): String {
        return ids[idType]
    }

    /**
     * Gets a specific hazard rating of the chemical.
     *
     * Takes an `enum` that refers to one of the numbers inside the coloured diamonds of the
     * overall fire diamond.
     *
     * @param prop the specific hazard being queried
     * @return the specific chemical hazard rating as an `int`
     * @see com.tsaysoft.nfpachemidv3.ChemProp
     * @see Chemical.getProps
     * @since 00.01.00
     */
    fun getProp(prop: ChemProp): Int {
        return props[prop]
    }

    /**
     * Sets a specific hazard rating of the `Chemical`.
     *
     * @param prop the `ChemProp` property to be set
     * @param newRating the new rating of the property to be set
     * @since 00.01.00
     */
    fun setProp(prop: ChemProp, newRating: Int) {
        props[prop] = newRating
    }

    /**
     * Sets the hazard ratings of the `Chemical`.
     *
     * The provided `EnumMap` argument **must** contain all of the required `enum`s
     * as specified in the `ChemID` class.
     *
     * @param newProperties an `EnumMap` that contains `ChemProp` and `Integer` information
     * @return whether the attempted operation succeeded or not
     * @since 00.01.00
     */
    fun setProps(newProperties: EnumMap<ChemProp, Int>): Boolean {
        if (newProperties.containsKey(HEALTH) &&
                newProperties.containsKey(FLAMMABILITY) &&
                newProperties.containsKey(REACTIVITY)) {
            props[HEALTH] = newProperties[HEALTH]
            props[FLAMMABILITY] = newProperties[FLAMMABILITY]
            props[REACTIVITY] = newProperties[REACTIVITY]
            return true
        } else {
            return false
        }
    }

    /**
     * Gets the presence of a specific special hazard symbol of the chemical.
     *
     * Takes an `enum` that refers to the symbol(s) inside the white square of the
     * overall fire diamond.
     *
     * @param special the presence of the special hazard symbol being queried
     * @return the presence of the special hazard symbol as a `boolean`
     * @see com.tsaysoft.nfpachemidv3.ChemSpecial
     * @see Chemical.getSpecials
     * @since 00.01.00
     */
    fun getSpecial(special: ChemSpecial): Boolean {
        return specials[special]
    }

    /**
     * Sets a specific special symbol of the `Chemical`.
     *
     * @param spec the `ChemSpecial` special symbol to be set
     * @param newBoolean the new `boolean` of the special symbol to be set
     * @since 00.01.00
     */
    fun setSpecial(spec: ChemSpecial, newBoolean: Boolean) {
        specials[spec] = newBoolean
    }


    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    /**
     * Compares two `Chemical`s based on the provided NFPA parameters/values only.
     *
     * The `special` argument determines whether special symbol should be considered in
     * comparing chemicals:
     *  - `true` means that special symbols will be taken into account
     *  - `false` means that special symbols will **not** be taken into account
     *
     * @param chem the `Chemical` to be compared with the current one
     * @param special whether the special symbols of the `Chemical`s should be considered
     * @return `true` if the NFPA values are the same, `false` if not
     * @since 00.01.00
     */
    fun equalsNFPA(chem: Chemical, special: Boolean): Boolean {
        val propSame = this.props == chem.props
        val specialSame = this.specials == chem.specials

        return if (special) {
            propSame && specialSame
        } else {
            propSame
        }

    }

    /**
     * Uses an HTTP request to get the ID for the `Chemical`.
     *
     * After generation, IDs are stored in an `EnumMap` named `ids`.
     * By default, there are no IDs available.
     * Used to prevent slowdowns as only the queried `Chemical`s need ID's.
     * Note that a chemical with a `null` or blank name will **not** attempt to generate an ID.
     *
     * TODO: Maybe refactor name to "genID" for the sake of uniformity
     * @param id the ID type requested to be generated
     * @return the generated ID as a `String`
     * @since 00.01.00
     */
    fun genChemID(id: ChemID): String? {
        var idTemp: String? = null
        if (name != null && name != "") {
            idTemp = idg.requestID(name, id)
            if (idTemp != null) {
                ids[id] = idTemp
            } else {
                println("ID null - ChemID not generated")
            }
        } else {
            println("name null or blank - ChemID not generated")
        }
        return idTemp
    }

    /**
     * TODO: Write this KDoc.
     */
    fun instantiateChemID() {
        for (chemID in ChemID.values()) {
            ids[chemID] = null
        }
    }

    companion object {
        // TODO: Confirm that the IDGManager works correctly
        private val idg = IDGManager()
    }


    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------


}
