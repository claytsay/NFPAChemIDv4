package com.tsaysoft.nfpachemidv3

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * # IDGAbstract
 * ## (Abstract ID Getter)
 * An abstract class outlining classes that handle chemical name to chemical ID conversion.
 *
 * Handles the HTTP request involved in converting a chemical name to a specified chemical ID.
 * ID type is specified by using `enum`s from [ChemID].
 * Subclasses are denoted by the name of the service they use and the letters "IDG".
 * The class is engineered so that classes extending [IDGAbstract] only need
 * vary the [urlGenerator] and [parseJSON] to achieve functionality.
 *
 * @author Clay Tsay
 * @version 00.01.00
 */
abstract class IDGAbstract : IDGInterface {

    // --------------------
    // VARIABLES AND DATA
    // --------------------

    // N/A


    // --------------------
    // PUBLIC UTILITY METHODS
    // --------------------

    override fun requestID(chemName: String, id: ChemID): String? {
        // Make sure to remove cleanName() if it causes problems with ID generation
        val url = urlGenerator(cleanName(chemName), id)
        val JSON: String
        try {
            JSON = getHTML(url)
            return parseJSON(JSON)
        } catch (e: Exception) {
            println("$e - ID unavailable")
            return null
        }

    }


    // --------------------
    // PRIVATE UTILITY METHODS
    // --------------------

    /**
     * Converts a chemical name to a HTTP request-ready URL.
     *
     * @param chemName the chemical name
     * @return the URL as a `String`
     */
    protected abstract fun urlGenerator(chemName: String, id: ChemID): String

    /**
     * Parses `String` input into JSON and takes information from the latter,
     * returning a chemical ID.
     *
     * @param JSONInput the input containing the ID information in a specific
     *      form
     * @return the requested chemical ID as an `String`
     */
    protected abstract fun parseJSON(JSONInput: String): String

    companion object {

        /**
         * Takes a chemical name and cleans it of any extraneous content.
         *
         * When it encounters the following characters, it removes everything
         * after it:
         * - ", "
         * - "("
         *
         * By doing this, however, it could cause serious problems when
         * processing IUPAC names. As of now, the latter function is disabled.
         * Probably want to use this only in situations where ID generation
         * experiences issues due to the "unclean" name.
         * TODO: Fix this method so that it is actually useful.
         *
         * @param name the name to be cleaned as a `String`
         * @return the cleaned name as a `String`
         * @since N/A
         */
        fun cleanName(name: String): String {
            var name = name
            // Will not interfere with IUPAC names as IUPAC names do not have spaces after commas
            if (name.contains(", ")) {
                name = name.substring(0, name.indexOf(", "))
            }

            // WILL interfere with IUPAC names
            /*if(name.contains("(")) {
            name = name.substring(0, name.indexOf("("));
        }*/

            return name
        }

        /**
         * Replaces the spaces in a `String` with a provided replacement `String`.
         *
         * For example, if `name` is `Hello_World` and `replacement`
         * is `%20`, the program will return `Hello%20World`.
         *
         * @param name the `String` to be processed
         * @param replacement the `String` that will replace the spaces in `name`
         * @return the "de-spaced" `name` as a `String`
         */
        fun replaceSpaces(name: String?, replacement: String?): String {
            var name = name
            // Not sure if this anti-null precaution is necessary
            if (name == null || replacement == null) {
                throw RuntimeException("either name or replacement was null")
            } else {
                for (i in 0 until name!!.length) {
                    if (name[i] == ' ') {
                        name = name.substring(0, i) + replacement + name.substring(i + 1)
                    }
                }

                return name
            }
        }

        /**
         * Takes a URL and requests the HTML information.
         *
         * @param urlToRead the URL to be accessed
         * @return the information received as a `String`
         * @throws Exception if the method fails for whatever reason
         */
        @Throws(Exception::class)
        protected fun getHTML(urlToRead: String): String {
            val result = StringBuilder()
            val url = URL(urlToRead)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            val rd = BufferedReader(InputStreamReader(conn.inputStream))
            var line: String? = rd.readLine()
            while (line != null) {
                result.append(line)
                line = rd.readLine()
            }
            rd.close()
            return result.toString()
        }
    }

}
