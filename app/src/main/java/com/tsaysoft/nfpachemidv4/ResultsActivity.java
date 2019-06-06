package com.tsaysoft.nfpachemidv4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tsaysoft.nfpachemidv3.ChemDB;
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
import static com.tsaysoft.nfpachemidv4.MainActivity.DATA_CONVERTER;

public class ResultsActivity extends AppCompatActivity {

    private Chemical queryChem;
    private ChemDBInterface database;

    EnumMap<ChemProp,Integer> props;
    EnumMap<ChemSpecial,Boolean> specs;

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Instantiation of instance variables, if necessary
        if (database == null) {
            String[] filenames =  {Companion.getJSON_DATA_1(), Companion.getJSON_DATA_2()};
            //database = new ChemDBManager(this, filenames);
            database = new ChemDB(this, Companion.getJSON_DATA_1());
        }
        if (resultTextView == null) {
            resultTextView = findViewById(R.id.textView_results);
        }
        if (props == null || specs == null) {
            props = new EnumMap<>(ChemProp.class);
            specs = new EnumMap<>(ChemSpecial.class);
        }

        // Getting the query data
        Bundle bundle = getIntent().getExtras();
        for (ChemProp e : ChemProp.values()) {
            props.put(e, (Integer)bundle.get(e.toString()));
        }
        for (ChemSpecial e : ChemSpecial.values()) {
            Boolean temp = (Boolean)bundle.get(e.toString());
            if (temp != null) {
                specs.put(e, temp);
            }
        }

        queryChem = new Chemical(null, props, specs);
        queryDB(queryChem);

    }

    /**
     * Handles what happens in the query process.
     *
     * TODO: Fix the method so that it makes more sense (read below):
     * Method currently shows the names of chemicals it finds out from the query.
     * Instead, should take users to a page filled with chemical icons and photos to
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
