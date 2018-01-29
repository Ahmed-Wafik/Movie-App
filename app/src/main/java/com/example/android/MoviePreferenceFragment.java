package com.example.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.android.movie.R;

public class MoviePreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    String TAG = MoviePreferenceFragment.class.getSimpleName();


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.movies_settings);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String string = sharedPreferences.getString(getString(R.string.pref_selection_key), getString(R.string.pref_movie_value_popular));
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.pref_selection_key));
        String select = select(string);
        listPreference.setSummary(select);
        Log.i(TAG,"onCreate Preference");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate ");
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (preference !=null && preference instanceof ListPreference){
            String string = sharedPreferences.getString(getString(R.string.pref_selection_key), getString(R.string.pref_movie_label_popular));
            ListPreference listPreference = (ListPreference) preference;
            String select = select(string);
            listPreference.setSummary(select);
        }
    }
    private String select(String type){

        String result="";
        switch (type){
            case "popular":
                result = "Popular";
                break;
            case "top_rated":
                result = "Top Rated";
                break;
            case "favorites":
                result = "Favorites";
                break;
            default:
                result = "Popular";

        }
        return result;
    }
}
