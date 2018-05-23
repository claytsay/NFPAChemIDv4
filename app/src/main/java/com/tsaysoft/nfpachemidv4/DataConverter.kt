package com.tsaysoft.nfpachemidv4

import com.tsaysoft.nfpachemidv3.ChemProp
import com.tsaysoft.nfpachemidv3.ChemSpecial
import java.util.*

// TODO: Write "Kotlin-docs" for the DataConverter Kotlin class
// TODO: Make sure that this class works
// However, this class may be obsolete...
class DataConverter {

    fun propsToString(props: EnumMap<ChemProp, Int>): String {
        var result: String = ""
        var tempSection: String

        for (item in ChemProp.values()) {
            tempSection = item.toString() + ":" + props.get(item) + "\n"
            result = result.plus(tempSection)
        }

        return result
    }

    fun specsToString(specs: EnumMap<ChemSpecial, Boolean>): String {
        var result: String = ""
        var tempSection: String

        for (item in ChemSpecial.values()) {
            tempSection = item.toString() + ":" + specs.get(item) + "\n"
            result = result.plus(tempSection)
        }

        return result
    }

    // TODO: Sumthin's up with dem fun[ctions]s here...
    // "Devs plz fix"
    fun stringToProps(string: String): EnumMap<ChemProp,Int> {
        var result: EnumMap<ChemProp, Int> = EnumMap(ChemProp::class.java)
        var tempIndex: Int
        var tempValue: Int

        for (item in ChemProp.values()) {
            tempIndex = string.indexOf(item.toString())
            tempValue = string.get(tempIndex+2).toInt()
            result.put(item, tempValue)
        }

        return result
    }

    fun stringToSpecs(string: String): EnumMap<ChemSpecial,Boolean> {
        var result: EnumMap<ChemSpecial, Boolean> = EnumMap(ChemSpecial::class.java)
        var tempIndex: Int
        var tempValue: Boolean

        for (item in ChemSpecial.values()) {
            tempIndex = string.indexOf(item.toString())
            tempValue = string.substring(tempIndex + 2, tempIndex + 6).toBoolean()
            result.put(item, tempValue)
        }

        return result
    }

}