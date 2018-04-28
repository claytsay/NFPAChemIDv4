package com.tsaysoft.nfpachemidv4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tsaysoft.nfpachemidv3.ChemDBInterface;
import com.tsaysoft.nfpachemidv3.ChemDBManager;
import com.tsaysoft.nfpachemidv3.ChemProp;
import com.tsaysoft.nfpachemidv3.ChemSpecial;
import com.tsaysoft.nfpachemidv3.Chemical;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsaysoft.nfpachemidv3.ChemDBInterface.*;

public class ResultsActivity extends AppCompatActivity {

    private HashMap<String, Object> queryData;
    private Chemical queryChem;
    private Collection<Chemical> resultList;
    private Chemical result;
    private ChemDBInterface database;

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Instantiation of instance variables, if necessary
        if (database == null) {
            String[] filenames =  {JSON_DATA_1, JSON_DATA_2};
            database = new ChemDBManager(this, filenames);
        }
        if (resultTextView == null) {
            resultTextView = findViewById(R.id.textView_results);
        }

        // Getting the query data
        /*Bundle bundle = getIntent().getExtras();
        String codedString = bundle.getString("QUERY");
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        queryData = gson.fromJson(codedString, type);*/

        Bundle bundle = getIntent().getExtras();

        String propsString = bundle.getString("PROPERTIES");
        EnumMap<ChemProp, Integer> propsEnumMap = MainActivity.gsonProps.fromJson(propsString,
                new TypeToken<EnumMap<ChemProp, String>>() {}.getType());

        String specsString = bundle.getString("SPECIALS");
        EnumMap<ChemSpecial, Boolean> specsEnumMap = MainActivity.gsonSpecs.fromJson(specsString,
                new TypeToken<EnumMap<ChemSpecial, Integer>>() {}.getType());

        // Utilising the query data to query the database
        // TODO: Find some way to confirm that the arguments are non-null before proceeding?
        queryChem = new Chemical(null, propsEnumMap, specsEnumMap);
        queryDB(queryChem);

    }

    /**
     * Handles what happens in the query process.
     *
     * TODO: Fix the method so that it makes more sense (read below):
     * Method currently shows the names of chemicals it finds out from the query.
     * Instead, should take users to a page fulled with chemical icons and photos to
     * have them select the right one (even if there are multiple chemicals).
     * The entries should open up the respective Wikipedia pages for the chemicals.
     *
     * @param chem
     */
    private void queryDB(Chemical chem) {
        Collection<Chemical> resultCollection = database.queryChemNFPA(queryChem, true);
        List<Chemical> resultList = new ArrayList<>(resultCollection);

        if (resultList.size() == 0) {
            // Case when no chemical matches the NFPA 704 input
            resultTextView.setText("No results. Try again.");

        } else if (resultList.size() == 1) {
            // Case when one chemical matches the NFPA 704 input
            resultTextView.setText(resultList.get(0).getName());

        } else {
            // Case when multiple chemicals match the NFPA 704 input
            String resultString = "";
            for(Chemical c : resultList) {
                resultString = resultString + "\n - " + c.getName();
            }
            resultTextView.setText(resultString);

        }
    }
}
