package com.tsaysoft.nfpachemidv3

import org.json.JSONException
import org.json.JSONObject

/**
 * # OPSINIDG
 * ## (OPSIN ID Getter)
 * A chemical name-to-ID converter (IDG) named OPSIN.
 *
 * **OPSIN** (Open Parser for Systematic IUPAC Nomenclature) was created by
 * the Centre for Molecular Informatics at *Cambridge University*.
 * Note that this database only returns `InChI` codes and keys (other formats
 * supplied are not used in this program). See
 * [source](http://opsin.ch.cam.ac.uk/).
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
class OPSINIDG : IDGAbstract() {

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    internal var idTemp: ChemID


    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    // N/A


    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    override fun urlGenerator(chemName: String, id: ChemID): String {
        var chemName = chemName
        // Declaring and setting up variables
        var url = "http://opsin.ch.cam.ac.uk/opsin/"
        idTemp = id

        // Clean the chemical name and concatenate to URL
        chemName = IDGAbstract.Companion.cleanName(chemName)
        chemName = IDGAbstract.Companion.replaceSpaces(chemName, "%20")
        url += chemName

        // Concatenate the .json specifier to the URL
        url = "$url.json"

        return url
    }

    override fun parseJSON(JSONInput: String): String {
        var chemID: String? = null

        try {
            // Convert the JSON String into a JSONObject
            val object1 = JSONObject(JSONInput)

            // Choose which type of ID to return
            when (idTemp) {
                ChemID.InChI_Key -> chemID = object1.get("stdinchikey").toString()
            }

            return chemID

        } catch (e: JSONException) {
            println("$e - ID unavailable")
            return null
        }

    }

}