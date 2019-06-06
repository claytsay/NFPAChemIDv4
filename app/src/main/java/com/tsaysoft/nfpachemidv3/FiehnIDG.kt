package com.tsaysoft.nfpachemidv3

import org.json.*

/**
 * # FiehnIDG
 * ## (Fiehn Lab ID Getter)
 * A chemical name-to-ID converter (IDG) created by the Fiehn Lab.
 *
 * The **Fiehn Laboratory** is part of *University of California, Davis*.
 * Academics there created a tool to convert between chemical names, IDs and
 * structures ([source](http://cts.fiehnlab.ucdavis.edu/conversion/)).
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
class FiehnIDG : IDGAbstract() {

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    // N/A


    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    // N/A


    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    override fun urlGenerator(chemName: String, id: ChemID): String {
        var chemName = chemName
        // Declaring variables
        var typeToken = ""
        var url = "http://cts.fiehnlab.ucdavis.edu/service/convert/Chemical%20Name/"

        // Find the typeToken "radical" from the provided enum and add to the URL
        // Assumes that all of the enums in ChemID will be supported
        when (id) {
            ChemID.CASRN -> typeToken = "CAS/"
            ChemID.CID -> typeToken = "PubChem%20CID/"
            ChemID.InChI_Key -> typeToken = "InChIKey/"
        }
        url = url + typeToken

        // Format and add the chemical name "radical" to the URL
        chemName = IDGAbstract.cleanName(chemName)
        chemName = IDGAbstract.replaceSpaces(chemName, "_")
        url = url + chemName

        return url
    }

    override fun parseJSON(JSONInput: String): String? {
        try {
            //System.out.println(JSONInput);
            val array1 = JSONArray(JSONInput)
            val object1 = array1.getJSONObject(0)
            val array2 = object1.getJSONArray("result")
            //JSONObject object2 = array2.getJSONObject(0);

            //String chemIDStr = object2.toString();

            /*String pageName = obj.getJSONObject("pageInfo").getString("pageName");
            JSONArray arr = obj.getJSONArray("posts");
            for (int i = 0; i < arr.length(); i++) {
                String post_id = arr.getJSONObject(i).getString("post_id");
            }*/

            return array2.getString(0)

        } catch (e: JSONException) {
            println("$e - ID unavailable")
            return null
        }

    }

}