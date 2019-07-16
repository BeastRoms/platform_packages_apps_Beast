/*
 *  Copyright (C) 2018 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.beast.settings.fragments;

import com.android.settings.SettingsPreferenceFragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.provider.Settings;
import com.android.settings.R;
import android.provider.SearchIndexableResource;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.beast.settings.preferences.CustomSeekBarPreference;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import java.util.List;
import java.util.Arrays;

public class OmniGestureSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {
    private static final String TAG = "OmniGestureSettings";
    private static final String KEY_SWIPE_LENGTH = "gesture_swipe_length";
    private static final String KEY_SWIPE_TIMEOUT = "gesture_swipe_timeout";
    private static final String KEY_SWIPE_START = "gesture_swipe_start";

    private CustomSeekBarPreference mSwipeTriggerLength;
    private CustomSeekBarPreference mSwipeTriggerTimeout;
    private CustomSeekBarPreference mSwipeTriggerStart;

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.BEAST;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.omni_gesture_settings);
        mFooterPreferenceMixin.createFooterPreference().setTitle(R.string.gesture_settings_info_new);
		
        mSwipeTriggerLength = (CustomSeekBarPreference) findPreference(KEY_SWIPE_LENGTH);
        int value = Settings.System.getInt(getContentResolver(),
                Settings.System.OMNI_BOTTOM_GESTURE_SWIPE_LIMIT,
                getSwipeLengthInPixel(getResources().getInteger(com.android.internal.R.integer.nav_gesture_swipe_min_length)));

        mSwipeTriggerLength.setMin(getSwipeLengthInPixel(40));
        mSwipeTriggerLength.setMax(getSwipeLengthInPixel(80));
        mSwipeTriggerLength.setValue(value);
        mSwipeTriggerLength.setOnPreferenceChangeListener(this);

        mSwipeTriggerTimeout = (CustomSeekBarPreference) findPreference(KEY_SWIPE_TIMEOUT);
        value = Settings.System.getInt(getContentResolver(),
                Settings.System.OMNI_BOTTOM_GESTURE_TRIGGER_TIMEOUT,
                getResources().getInteger(com.android.internal.R.integer.nav_gesture_swipe_timout));
        mSwipeTriggerTimeout.setValue(value);
        mSwipeTriggerTimeout.setOnPreferenceChangeListener(this);

        mSwipeTriggerStart = (CustomSeekBarPreference) findPreference(KEY_SWIPE_START);
        value = Settings.System.getInt(getContentResolver(),
                Settings.System.OMNI_BOTTOM_GESTURE_SWIPE_START,
                getResources().getInteger(com.android.internal.R.integer.nav_gesture_swipe_start));
        mSwipeTriggerStart.setValue(value);
        mSwipeTriggerStart.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }
	
    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();

        if (preference == mSwipeTriggerLength) {
            int value = (Integer) objValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.OMNI_BOTTOM_GESTURE_SWIPE_LIMIT, value);
        } else if (preference == mSwipeTriggerTimeout) {
            int value = (Integer) objValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.OMNI_BOTTOM_GESTURE_TRIGGER_TIMEOUT, value);
        } else if (preference == mSwipeTriggerStart) {
            int value = (Integer) objValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.OMNI_BOTTOM_GESTURE_SWIPE_START, value);
        } else {
            return false;
        }
        return true;
    }

    private int getSwipeLengthInPixel(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {

            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(
                    Context context, boolean enabled) {
                final SearchIndexableResource sir = new SearchIndexableResource(context);
                sir.xmlResId = R.xml.omni_gesture_settings;
                return Arrays.asList(sir);
            }
	};
}

