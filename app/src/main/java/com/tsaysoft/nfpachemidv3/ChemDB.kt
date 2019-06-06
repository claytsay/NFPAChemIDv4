package com.tsaysoft.nfpachemidv3

import android.content.Context

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader

import java.util.ArrayList
import java.util.EnumMap

import com.tsaysoft.nfpachemidv3.ChemProp.*
import com.tsaysoft.nfpachemidv3.ChemSpecial.*

/**
 * <h1>ChemDB</h1>
 * <h2>(Chemical Database)</h2>
 * A database to store [Chemical]s.
 *
 *
 * The fundamental unit of the database side of the program.
 * Takes in a file name (currently, can only handle JSON),  opens that file, and uses the file's information
 * to create a list of `Chemicals` with names, properties, and identifiers.
 * Supports queries based on NFPA 704 hazard ratings (and special symbols).
 *
 * @see com.tsaysoft.nfpachemidv3.ChemDBInterface
 *
 * @see com.tsaysoft.nfpachemidv3.ChemDBManager
 *
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
class ChemDB : ChemDBInterface {

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    /**
     * Stores the chemicals contained in the `ChemDB`'s database.
     */
    private val chemList = ArrayList<Chemical>()

    /**
     * Temporary storage utilized in the processing of the database files.
     */
    private val chemArray = ArrayList<Array<String>>()

    /**
     * Variable representing the context of the instance of ChemDB.
     */
    private val context_master: Context

    private val fileName: String


    // --------------------
    // CONSTRUCTORS
    // --------------------

    /**
     * Constructs a `ChemDB` that takes information from the specified file.
     *
     * The file must be formatted in a very specific way.
     * See the existing data sets for examples.
     *
     * @param fileName the name of the file to be read
     * @since 00.01.00
     */
    constructor(context: Context, fileName: String) : super() {
        this.context_master = context
        this.fileName = fileName
        readJSON(fileName)
        //readCSV(fileName)
    }


    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    /**
     * Used to query for `Chemical`s that match each other in their NFPA 704 ratings.
     *
     * The `boolean` argument determines whether the special symbols
     * (specified in [ChemSpecial]) should be considered in determining equality.
     *
     * @param query the chemical with properties to be queried
     * @param special whether the special symbols should be taken into account in comparisons
     * @return a `Collection` of `Chemical`s matching the properties and/or specials
     * @since 00.01.00
     */
    override fun queryChemNFPA(query: Chemical, special: Boolean): Collection<Chemical> {
        val results = ArrayList<Chemical>()
        for (chem in chemList) {
            if (chem.equalsNFPA(query, special)) {
                results.add(chem)
            }
        }
        return results
    }

    /**
     * Used to query for `Chemical`s that match the given `EnumMap`'s
     * properties information.
     *
     * Does **not** take into account the special symbols.
     *
     * @param properties the `EnumMap` with properties to be queried
     * @return a `Collection` of `Chemical`s matching the properties
     * @see ChemDBInterface.queryEnumMapNFPA
     * @since 00.01.00
     */
    override fun queryEnumMapNFPA(properties: EnumMap<ChemProp, Int>): Collection<Chemical> {
        val query = Chemical(null, properties)
        return queryChemNFPA(query, false)
    }

    /**
     * Used to query for `Chemical`s that match the given `EnumMap`'s
     * properties and specials information.
     *
     * **Does** take into account special symbols.
     *
     * @param properties the `EnumMap` with properties to be queried
     * @param specials the `EnumMap` with special symbols to be queried
     * @return a `Collection` of `Chemical`s matching the properties
     * @see ChemDBInterface.queryEnumMapNFPA
     * @since 00.01.00
     */
    override fun queryEnumMapNFPA(properties: EnumMap<ChemProp, Int>,
                                  specials: EnumMap<ChemSpecial, Boolean>): Collection<Chemical> {
        val query = Chemical(null, properties, specials)
        return queryChemNFPA(query, true)
    }


    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    /**
     * ***NON-FUNCTIONAL*** - Takes a CSV file and converts it into an `ArrayList` of `Chemical`s.
     *
     * TODO: Fix this so that it works.
     * Then again, does this really need to work?
     *
     * @param fileName the name of the CSV file to be read
     */
    @Deprecated("This method doesn't work.")
    private fun readCSV(fileName: String) {
        // TODO: Fix this method: it cannot differentiate commas inside quotes and outside quotes.
        var line: String? = ""
        val cvsSplitBy = ","

        // Convert individual CSV lines into a 2D string array
        try {
            val br = BufferedReader(FileReader(fileName))
            // FIXME: "Assignments are not expressions."
            line = br.readLine();
            while (line != null) {
                // Use a comma as separator
                val chemDataTemp = line.split(cvsSplitBy.toRegex())
                        .dropLastWhile { it.isEmpty() }.toTypedArray()
                chemArray.add(chemDataTemp)
                line = br.readLine();
            }

        } catch (e: IOException) {
            println("IOException has occurred in reading the CSV.")
            //e.printStackTrace()
        }

        // Convert the 2D array into Chemicals and put them into the ArrayList
        val specialTempString = ""
        var specialTempSArray: Array<String>
        val specialTempBArray = booleanArrayOf(false, false, false)
        for (chemData in chemArray) {
            // Access the special symbols string and put individual symbols into array spots
            if (chemData.size == 5) {
                chemData[4] = specialTempString
                specialTempSArray = specialTempString.split(", ".toRegex())
                        .dropLastWhile { it.isEmpty() }.toTypedArray()

                /* Convert the presence of the symbols into the boolean array taken by the
                Chemical() constructor */
                for (str in specialTempSArray) {
                    if (str.equals("ox", ignoreCase = true)) {
                        specialTempBArray[0] = true
                    }
                    if (str.equals("sa", ignoreCase = true)) {
                        specialTempBArray[1] = true
                    }
                    if (str.equals("w", ignoreCase = true)) {
                        specialTempBArray[2] = true
                    }
                }
            }

            // Add the new Chemical() to the list
            chemList.add(Chemical(chemData[0],
                    Integer.parseInt(chemData[1]),
                    Integer.parseInt(chemData[2]),
                    Integer.parseInt(chemData[3]),
                    specialTempBArray))

            // Reset the temporary boolean array
            for (i in 0..2) {
                specialTempBArray[i] = false
            }
        }

    }

    /**
     * Reads a JSON database to convert its stored information into a list.
     *
     *
     * Takes a very specific type of JSON file.
     * See existing databases for JSON formatting information.
     *
     * @param fileName name of the file to be accessed, as a `String`
     */
    private fun readJSON(fileName: String) {
        // Declaring variables
        val chemJSONArray: JSONArray
        var chemJSONObject: JSONObject

        var nameTemp = ""
        var specialTempString = ""
        var tempString: String
        val JSONSB = StringBuffer("")
        val propsTemp = EnumMap<ChemProp, Int>(ChemProp::class.java)
        var specsTemp = EnumMap<ChemSpecial, Boolean>(ChemSpecial::class.java)
        var chemTemp: Chemical

        // Reading the actual file and processing the information.
        try {
            // Read the file and convert it into a JSONArray.
            val br = BufferedReader(
                    InputStreamReader(
                            context_master.assets.open(fileName)
                    )
            )
            // FIXME: Issue with assignment not being an expression.
            while ((tempString = br.readLine()) != null) {
                JSONSB.append(tempString)
            }
            chemJSONArray = JSONArray(JSONSB.toString())


            // Put fields into private instance variables.
            for (i in 0 until chemJSONArray.length()) {
                val obj = chemJSONArray.get(i)

                try {
                    chemJSONObject = obj as JSONObject

                    // Convert the JSON information to processable information
                    nameTemp = chemJSONObject.get("NAME").toString()
                    for (chemp in ChemProp.values()) {
                        propsTemp[chemp] = Integer.parseInt(
                                chemJSONObject.get(chemp.toString()).toString()
                        )
                    }
                    specialTempString = chemJSONObject.get("SPECIAL").toString().toUpperCase()

                    // Access the special symbols string and put individual symbols into array spots
                    if (specialTempString != "") {
                        specsTemp = convertSpecialString(specialTempString)
                        chemTemp = Chemical(nameTemp, propsTemp, specsTemp)

                    } else {
                        chemTemp = Chemical(nameTemp, propsTemp)
                    }

                    // Add the new Chemical() to the list
                    chemList.add(chemTemp)

                    // Reset the EnumMaps
                    propsTemp.clear()
                    specsTemp.clear()

                } catch (e: NumberFormatException) {
                    println("$e - chemical could not be properly loaded")
                }

            }

        } catch (e: JSONException) {
            println("$e - database could not be properly loaded")
        } catch (e: IOException) {
            println("$e - database could not be properly loaded")
        }

    }

    /**
     * Converts a `String` with special symbol information into an `EnumMap`.
     * <P>
     * Note that the input `String` **must** be capitalised (i.e. `toUpperCase()`)
     * for the method to work correctly.
    </P> *
     * @param specialString the `String` with the capitalised symbols
     * @return an `EnumMap` with the proper symbols in `enum` format
     */
    private fun convertSpecialString(specialString: String): EnumMap<ChemSpecial, Boolean> {
        val results = EnumMap<ChemSpecial, Boolean>(ChemSpecial::class.java)

        for (chemS in ChemSpecial.values()) {
            results[chemS] = specialString.contains(chemS.toString())
        }
        /*results.put(OXIDIZER, specialString.contains("OX"));
        results.put(SIMPLE_ASPHYXIANT, specialString.contains("SA"));
        results.put(WATER_REACT, specialString.contains("W"));*/

        return results
    }


}
