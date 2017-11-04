package fi.haapatalo.moodwear.provider

import android.content.ComponentName
import android.support.wearable.complications.ComplicationData
import android.support.wearable.complications.ComplicationData.TYPE_LONG_TEXT
import android.support.wearable.complications.ComplicationData.TYPE_SHORT_TEXT
import android.support.wearable.complications.ComplicationManager
import android.support.wearable.complications.ComplicationProviderService
import android.support.wearable.complications.ComplicationText.plainText
import android.util.Log
import fi.haapatalo.moodwear.provider.ComplicationToggleReceiver.Companion.PREFERENCES_FILE_KEY
import fi.haapatalo.moodwear.provider.ComplicationToggleReceiver.Companion.getIcon
import fi.haapatalo.moodwear.provider.ComplicationToggleReceiver.Companion.getPreferenceKey
import fi.haapatalo.moodwear.provider.ComplicationToggleReceiver.Companion.getToggleIntent

class MoodComplicationProviderService : ComplicationProviderService() {

    override fun onComplicationActivated(complicationId: Int, dataType: Int, complicationManager: ComplicationManager) {
        Log.d(TAG, "onComplicationActivated(): $complicationId")
    }

    override fun onComplicationDeactivated(complicationId: Int) {
        Log.d(TAG, "onComplicationDeactivated(): $complicationId")
    }

    override fun onComplicationUpdate(complicationId: Int, type: Int, manager: ComplicationManager) {
        Log.d(TAG, "onComplicationUpdate() id: " + complicationId)

        val thisProvider = ComponentName(this, javaClass)
        val toggleMoodIntent = getToggleIntent(this, thisProvider, complicationId)

        val preferences = getSharedPreferences(PREFERENCES_FILE_KEY, 0)
        val index = preferences.getInt(getPreferenceKey(thisProvider, complicationId), 0)
        val moodIcon = getIcon(index)

        when (type) {
            TYPE_SHORT_TEXT -> ComplicationData.Builder(TYPE_SHORT_TEXT)
                    .setShortText(plainText(moodIcon))
                    .setTapAction(toggleMoodIntent)
                    .build()
            TYPE_LONG_TEXT -> ComplicationData.Builder(TYPE_LONG_TEXT)
                    .setLongText(plainText(moodIcon))
                    .setTapAction(toggleMoodIntent)
                    .build()
            else -> {
                Log.w(TAG, "Unexpected complication type $type")
                null
            }
        }?.let {
            manager.updateComplicationData(complicationId, it)
        } ?: manager.noUpdateRequired(complicationId)
    }

    companion object {
        val TAG = "MoodComplication"
    }
}