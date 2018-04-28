package com.tsaysoft.nfpachemidv3;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>OPSINIDG</h1>
 * <h2>(OPSIN ID Getter)</h2>
 * A chemical name-to-ID converter (IDG) named OPSIN.
 * <p>
 *     <b>OPSIN</b> (Open Parser for Systematic IUPAC Nomenclature) was created by the Centre for Molecular
 *     Informatics at <u>Cambridge University</u>.
 *     Note that this database only returns <code>InChI</code> codes and keys
 *     (other formats supplied are not used in this program).
 * </p>
 *
 * @see <a href="http://opsin.ch.cam.ac.uk/">http://opsin.ch.cam.ac.uk/</a>
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
public class OPSINIDG extends IDGAbstract{

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    ChemID idTemp;


    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    // N/A



    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    @Override
    protected String urlGenerator(String chemName, ChemID id) {
        // Declaring and setting up variables
        String url = "http://opsin.ch.cam.ac.uk/opsin/";
        idTemp = id;

        // Clean the chemical name and concatenate to URL
        chemName = cleanName(chemName);
        chemName = replaceSpaces(chemName,"%20");
        url = url.concat(chemName);

        // Concatenate the .json specifier to the URL
        url = url.concat(".json");

        return url;
    }

    @Override
    protected String parseJSON(String JSONInput){
        String chemID = null;

        try {
            // Convert the JSON String into a JSONObject
            JSONObject object1 = new JSONObject(JSONInput);

            // Choose which type of ID to return
            switch(idTemp) {
                case InChI_Key:
                    chemID = object1.get("stdinchikey").toString();
                    break;
            }

            return chemID;

        } catch (JSONException e){
            System.out.println(e + " - ID unavailable");
            return null;
        }
    }

}