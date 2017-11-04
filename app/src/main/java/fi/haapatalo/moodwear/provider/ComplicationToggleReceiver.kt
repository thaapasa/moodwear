package fi.haapatalo.moodwear.provider

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getBroadcast
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.wearable.complications.ProviderUpdateRequester

class ComplicationToggleReceiver : BroadcastReceiver() {

    companion object {
        private val EXTRA_PROVIDER_COMPONENT = "fi.haapatalo.moodwear.provider.action.PROVIDER_COMPONENT"
        private val EXTRA_COMPLICATION_ID = "fi.haapatalo.moodwear.provider.action.COMPLICATION_ID"
        val PREFERENCES_FILE_KEY = "fi.haapatalo.moodwear.PREFERENCES_FILE_KEY"

        val MOOD_ICONS = listOf("\uD83D\uDE00", "\uD83D\uDE01", "\uD83D\uDE02", "\uD83E\uDD23", "\uD83D\uDE06",
                "\uD83D\uDE0A", "\uD83D\uDE0D", "\uD83E\uDD14", "\uD83D\uDE10", "\uD83D\uDE44", "\uD83D\uDE22",
                "\uD83D\uDE25", "\uD83D\uDE2D", "\uD83D\uDE13", "\uD83D\uDE2A", "\uD83D\uDE34", "\uD83E\uDD15")

        /**
         * Returns a pending intent, suitable for use as a tap intent, that causes a complication to be
         * toggled and updated.
         */
        fun getToggleIntent(context: Context, provider: ComponentName, complicationId: Int): PendingIntent {
            val intent = Intent(context, ComplicationToggleReceiver::class.java)
            intent.putExtra(EXTRA_PROVIDER_COMPONENT, provider)
            intent.putExtra(EXTRA_COMPLICATION_ID, complicationId)

            // Pass complicationId as the requestCode to ensure that different complications get
            // different intents.
            return getBroadcast(context, complicationId, intent, FLAG_UPDATE_CURRENT)
        }

        fun getIcon(index: Int) = MOOD_ICONS[index]

        fun getPreferenceKey(provider: ComponentName, complicationId: Int): String = provider.className + complicationId
    }

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras
        val provider = extras.getParcelable<ComponentName>(EXTRA_PROVIDER_COMPONENT)
        val complicationId = extras.getInt(EXTRA_COMPLICATION_ID)

        val preferenceKey = getPreferenceKey(provider, complicationId)
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, 0)

        // Updates data for complication.
        val value = (sharedPreferences.getInt(preferenceKey, 0) + 1) % MOOD_ICONS.size

        val editor = sharedPreferences.edit()
        editor.putInt(preferenceKey, value)
        editor.apply()

        // Request an update for the complication that has just been toggled.
        val requester = ProviderUpdateRequester(context, provider)
        requester.requestUpdate(complicationId)
    }

}