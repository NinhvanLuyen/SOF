package fossil.sof.sofuser.utils

import timber.log.Timber
import java.util.HashMap

/**
 * Created by ninhvanluyen on 16/11/18.
 */
object ApiUtils
{
    /**
     * Get extented param to call api
     *
     * @return
     */
    fun getExtendParam(): HashMap<String, String> {
        val result = HashMap<String, String>()
        try {
//            var language = UIUtils.getLanguage()
//
//            if (Configs.IS_DEBUG) {
//                language = LanguageID.ENG
//            }
//            result.put("language", URLEncoder.encode(language, Constants.CHARSET))
//            result.put("log", URLEncoder.encode(getLogParam(), Constants.CHARSET))
        } catch (e: Exception) {
            Timber.e(e, "Exception getExtendParam")
        }

        return result
    }
}