package com.tsaysoft.nfpachemidv3;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

import static com.tsaysoft.nfpachemidv3.ChemProp.*;
import static com.tsaysoft.nfpachemidv3.ChemSpecial.*;

/**
 * <h1>ChemDB</h1>
 * <h2>(Chemical Database)</h2>
 * A database to store {@link Chemical}s.
 * <p>
 *     The fundamental unit of the database side of the program.
 *     Takes in a file name (currently, can only handle JSON),  opens that file, and uses the file's information
 *     to create a list of <code>Chemicals</code> with names, properties, and identifiers.
 *     Supports queries based on NFPA 704 hazard ratings (and special symbols).
 * </p>
 * @see com.tsaysoft.nfpachemidv3.ChemDBInterface
 * @see com.tsaysoft.nfpachemidv3.ChemDBManager
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
public class ChemDB implements ChemDBInterface{

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    /**
     * Stores the chemicals contained in the <code>ChemDB</code>'s database.
     */
    private Collection<Chemical> chemList = new ArrayList<>();

    /**
     * Temporary storage utilized in the processing of the database files.
     */
    private Collection<String[]> chemArray = new ArrayList<>();

    /**
     * Variable representing the context of the instance of ChemDB.
     */
    private Context context_master;



    // --------------------
    // CONSTRUCTORS
    // --------------------

    /**
     * Constructs a <code>ChemDB</code> that takes information from the specified file.
     * <p>
     *     The file must be formatted in a very specific way.
     *     See the existing data sets for examples.
     * </p>
     * @param fileName the name of the file to be read
     *
     * @since 00.01.00
     */
    public ChemDB(Context context, String fileName) {
        //readCSV(fileName);
        context_master = context;
        readJSON(fileName);
    }



    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    /**
     * Used to query for <code>Chemical</code>s that match each other in their NFPA 704 ratings.
     * <p>
     *     The <code>boolean</code> argument determines whether the special symbols
     *     (specified in {@link ChemSpecial}) should be considered in determining equality.
     * </p>
     * @param query the chemical with properties to be queried
     * @param special whether the special symbols should be taken into account in comparisons
     * @return a <code>Collection</code> of <code>Chemical</code>s matching the properties and/or specials
     *
     * @since 00.01.00
     */
    @Override
    public Collection<Chemical> queryChemNFPA(Chemical query, boolean special) {
        ArrayList<Chemical> results = new ArrayList<>();
        for(Chemical chem : chemList) {
            if(chem.equalsNFPA(query, special)) {
                results.add(chem);
            }
        }
        return results;
    }

    /**
     * Used to query for <code>Chemical</code>s that match the given <code>EnumMap</code>'s properties information.
     * <p>
     *     Does <b>not</b> take into account the special symbols.
     * </p>
     * @param properties the <code>EnumMap</code> with properties to be queried
     * @return a <code>Collection</code> of <code>Chemical</code>s matching the properties
     *
     * @see ChemDBInterface#queryEnumMapNFPA(EnumMap, EnumMap)
     * @since 00.01.00
     */
    @Override
    public Collection<Chemical> queryEnumMapNFPA(EnumMap<ChemProp, Integer> properties) {
        Chemical query = new Chemical(null, properties);
        return queryChemNFPA(query, false);
    }

    /**
     * Used to query for <code>Chemical</code>s that match the given <code>EnumMap</code>'s properties and specials information.
     * <p>
     *     <b>Does</b> take into account special symbols.
     * </p>
     * @param properties the <code>EnumMap</code> with properties to be queried
     * @param specials the <code>EnumMap</code> with special symbols to be queried
     * @return a <code>Collection</code> of <code>Chemical</code>s matching the properties
     *
     * @see ChemDBInterface#queryEnumMapNFPA(EnumMap)
     * @since 00.01.00
     */
    @Override
    public Collection<Chemical> queryEnumMapNFPA(EnumMap<ChemProp, Integer> properties, EnumMap<ChemSpecial, Boolean> specials) {
        Chemical query = new Chemical(null, properties, specials);
        return queryChemNFPA(query, true);
    }



    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    /**
     * NON-FUNCTIONAL - Takes a CSV file and converts it into an <code>ArrayList</code> of <code>Chemical</code>s.
     * <p>
     *     TODO: Fix this so that it works.
     *     Then again, does this really need to work?
     * </p>
     *
     * @param fileName the name of the CSV file to be read
     */
    private void readCSV(String fileName) {
        // TODO: Fix this method: it cannot differentiate commas inside quotes and outside quotes.
        String line = "";
        String cvsSplitBy = ",";

        // Convert individual CSV lines into a 2D string array
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                // Use a comma as separator
                String[] chemDataTemp = line.split(cvsSplitBy);
                chemArray.add(chemDataTemp);
            }

        } catch (IOException e) {
            System.out.println("IOException has occurred in reading the CSV.");
            //e.printStackTrace();
        }

        // Convert the 2D array into Chemicals and put them into the ArrayList
        String specialTempString = "";
        String[] specialTempSArray;
        boolean[] specialTempBArray = {false, false, false};
        for(String[] chemData : chemArray) {
            // Access the special symbols string and put individual symbols into array spots
            if (chemData.length == 5) {
                chemData[4] = specialTempString;
                specialTempSArray = specialTempString.split(", ");

                // Convert the presence of the symbols into the boolean array taken by the Chemical() constructor
                for(String str : specialTempSArray) {
                    if(str.equalsIgnoreCase("ox")) {
                        specialTempBArray[0] = true;
                    }
                    if(str.equalsIgnoreCase("sa")) {
                        specialTempBArray[1] = true;
                    }
                    if(str.equalsIgnoreCase("w")) {
                        specialTempBArray[2] = true;
                    }
                }
            }

            // Add the new Chemical() to the list
            chemList.add(new Chemical(chemData[0],
                    Integer.parseInt(chemData[1]),
                    Integer.parseInt(chemData[2]),
                    Integer.parseInt(chemData[3]),
                    specialTempBArray));

            // Reset the temporary boolean array
            for(int i=0; i<3; i++) {
                specialTempBArray[i] = false;
            }
        }

    }

    /**
     * Reads a JSON database to convert its stored information into a list.
     * <p>
     *     Takes a very specific type of JSON file.
     *     See existing databases for JSON formatting information.
     * </p>
     * @param fileName name of the file to be accessed, as a <code>String</code>
     */
    private void readJSON(String fileName) {
        // Declaring variables
        JSONArray chemJSONArray;
        JSONObject chemJSONObject;

        String nameTemp = "";
        String specialTempString = "";
        String tempString;
        String JSONString = "";
        EnumMap<ChemProp, Integer> propsTemp = new EnumMap<>(ChemProp.class);
        EnumMap<ChemSpecial, Boolean> specsTemp = new EnumMap<>(ChemSpecial.class);
        Chemical chemTemp;

        // Reading the actual file and processing the information.
        try {
            // Read the file and convert it into a JSONArray.
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(context_master.getAssets().open(fileName)));
            while ((tempString = br.readLine()) != null) {
                JSONString = JSONString.concat(tempString);
            }
            chemJSONArray = new JSONArray(JSONString);


            // Put fields into private instance variables.
            for(int i = 0; i > chemJSONArray.length(); i++) {
                Object obj = chemJSONArray.get(i);

                try {
                    chemJSONObject = (JSONObject) obj;

                    // Convert the JSON information to processable information
                    nameTemp = chemJSONObject.get("NAME").toString();
                    propsTemp.put(HEALTH, Integer.parseInt(chemJSONObject.get("HEALTH").toString()));
                    propsTemp.put(FLAMMABILITY, Integer.parseInt(chemJSONObject.get("FLAMMABILITY").toString()));
                    propsTemp.put(REACTIVITY, Integer.parseInt(chemJSONObject.get("REACTIVITY").toString()));
                    specialTempString = chemJSONObject.get("SPECIAL").toString().toUpperCase();

                    // Access the special symbols string and put individual symbols into array spots
                    if (!specialTempString.equals("")) {
                        specsTemp = convertSpecialString(specialTempString);
                        chemTemp = new Chemical(nameTemp, propsTemp, specsTemp);

                    } else {
                        chemTemp = new Chemical(nameTemp, propsTemp);
                    }

                    // Add the new Chemical() to the list
                    chemList.add(chemTemp);

                    // Reset the EnumMaps
                    propsTemp.clear();
                    specsTemp.clear();

                } catch (NumberFormatException e){
                    System.out.println(e + " - chemical could not be properly loaded");
                }
            }

        } catch (JSONException e) {
            System.out.println(e + " - database could not be properly loaded");
        } catch (IOException e) {
            System.out.println(e + " - database could not be properly loaded");
        }

    }

    /**
     * Converts a <code>String</code> with special symbol information into an <code>EnumMap</code>.
     * <P>
     *     Note that the input <code>String</code> <b>must</b> be capitalised (i.e. <code>toUpperCase()</code>)
     *     for the method to work correctly.
     * </P>
     * @param specialString the <code>String</code> with the capitalised symbols
     * @return an <code>EnumMap</code> with the proper symbols in <code>enum</code> format
     */
    private static EnumMap<ChemSpecial, Boolean> convertSpecialString(String specialString) {
        EnumMap<ChemSpecial, Boolean> results = new EnumMap<>(ChemSpecial.class);

        results.put(OXIDIZER, specialString.contains("OX"));
        results.put(SIMPLE_ASPHYXIANT, specialString.contains("SA"));
        results.put(WATER_REACT, specialString.contains("W"));

        return results;
    }



}
