package com.tsaysoft.nfpachemidv4

import com.tsaysoft.nfpachemidv3.ChemProp
import com.tsaysoft.nfpachemidv3.ChemSpecial
import java.util.*

// TODO: Write "Kotlin-docs" for the DataConverter Kotlin class
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

    fun stringToProps(string: String): EnumMap<ChemProp,Int> {
        // TODO: Finish this
        /*val charSequence: CharSequence = string
        var result: EnumMap<ChemProp, Int>
        var tempIndex: Int

        for (item in ChemProp.values()) {
            tempIndex = charSequence.find(    )
            tempIndex = string.indexOf()
        }
        return null*/
    }

}