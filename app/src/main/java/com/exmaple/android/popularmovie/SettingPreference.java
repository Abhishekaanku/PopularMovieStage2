package com.exmaple.android.popularmovie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingPreference extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
    {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settingpreference);
        SharedPreferences sharedPreferences= getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen=getPreferenceScreen();
        int count=preferenceScreen.getPreferenceCount();
        int i=0;
        for(i=0;i<count;++i) {
            Preference preference=preferenceScreen.getPreference(i);
            if(preference instanceof ListPreference) {
                String value=sharedPreferences.getString(preference.getKey(),
                        getString(R.string.prefSortDefaultValue));
                setPreferenceSummary(preference,value);
            }
        }

    }

    private void setPreferenceSummary(Preference preference, String value) {
        if(preference instanceof ListPreference) {
            int index=((ListPreference) preference).findIndexOfValue(value);
            preference.setSummary(((ListPreference) preference).getEntries()[index]);

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference=findPreference(s);
        if(preference instanceof ListPreference) {
            String value=sharedPreferences.getString(preference.getKey(),
                    getString(R.string.prefSortDefaultValue));
            setPreferenceSummary(preference,value);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences=getPreferenceScreen().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

}
