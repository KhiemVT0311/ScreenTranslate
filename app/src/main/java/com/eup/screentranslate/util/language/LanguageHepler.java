package com.eup.screentranslate.util.language;

import com.eup.screentranslate.R;
import com.eup.screentranslate.model.LanguageItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class LanguageHepler {
    public static final int HIRAGANA_START = 0x3041;
    public static final int HIRAGANA_END = 0x309F;
    public static final int KATAKANA_START = 0x30A0;
    public static final int KATAKANA_END = 0x30FF;
    public static final int KATAKANA_PHONETIC_START = 0x31F0;
    public static final int KATAKANA_PHONETIC_END = 0x31FF;
    public static final int KANJI_START = 0x4e00;
    public static final int KANJI_END = 0x9faf;
    public static final int RARE_KANJI_START = 0x3400;
    public static final int RARE_KANJI_END = 0x4dbf;
    public static final int KANJI_RADICAL_START = 0x2E80;
    public static final int KANJI_RADICAL_END = 0x2FD5;

    public static int POS_JAPANESE = 41;


    public static String getLangCodeByPosition(int position){
        return LIST_LANGUAGE.get(position).code;
    }

    public static String getNameCountryByPosition(int position){
        return LIST_LANGUAGE.get(position).name;
    }

    public static String getEmojiFlag(int position){
        return LIST_LANGUAGE.get(position).emojiFlag;
    }

    public static int getDefaultPositionLanguage(){
        int defaultPositionLang = 19;
        Locale it = Locale.getDefault();
        String lang = it.getLanguage();

        for (int i = 0; i < LIST_LANGUAGE.size(); i++){
            LanguageItem item = LIST_LANGUAGE.get(i);
            if (lang.equals(item.code)){
                defaultPositionLang = i;
                break;
            } else if (lang.equals("zh")){
                if (it.getCountry().equals("CN") && item.code.equals("zh-CN")) {
                    defaultPositionLang = i;
                    break;
                } else if (it.getCountry().equals("TW") && item.code.equals("zh-TW")){
                    defaultPositionLang = i;
                    break;
                }
            }
        }

        return defaultPositionLang;
    }

    public static boolean isJapanese(String keyWord){
        if (keyWord != null){
            for (int i = 0; i < keyWord.length(); i++){
                if (isKanji(keyWord.charAt(i)) ||
                        isHiragana(keyWord.charAt(i)) ||
                        isKatakana(keyWord.charAt(i))) return true;
            }
        }
        return false;
    }

    public static boolean isRomaji(String str){
        int unicodePoint = Character.codePointAt(str, 0);
        if (inPath(0x0020, 0x007e, unicodePoint)) return true;
        return false;
    }


    public static boolean isKanji(char c){
        if (c == '々') return true;
        if (inPath(KANJI_START, KANJI_END, (int) c)) return true;
        if (inPath(RARE_KANJI_START, RARE_KANJI_END, (int) c)) return true;
        if (inPath(KANJI_RADICAL_START, KANJI_RADICAL_END, (int) c)) return true;
        if (inPath(0x3400, 0x4DB5, (int) c)) return true;
        if (inPath(0x4E00, 0x9FCB, (int ) c)) return true;
        if (inPath(0xF900, 0xFA6A, (int) c)) return true;

        return false;
    }

    public static boolean isKanji(int code){
        if (code == (int) '々') return true;
        if (inPath(KANJI_START, KANJI_END, code)) return true;
        if (inPath(RARE_KANJI_START, RARE_KANJI_END, code)) return true;

        return false;
    }

    public static boolean isHiragana(char c){
        return inPath(HIRAGANA_START, HIRAGANA_END, (int) c);
    }

    public static boolean isRomaji(char c){
        return inPath(0x0020, 0x007e, (int) c);
    }

    public static boolean isKatakana(char c){
        if (inPath(KATAKANA_START, KATAKANA_END, (int) c)) return true;
        if (inPath(KATAKANA_PHONETIC_START, KATAKANA_PHONETIC_END, (int) c)) return true;
        return inPath(0xff5f, 0xff9f, (int) c);
    }

    public static boolean inPath(int begin, int end, int number){
        for (int i = begin; i <= end; i++){
            if (number == i) return true;
        }
        return false;
    }

    public static ArrayList<LanguageItem> LIST_LANGUAGE = new ArrayList<>(
            Arrays.asList(
                    new LanguageItem(
                            "af",
                            "Afrikaans",
                            "",
                            "south_africa",
                            "\uD83C\uDDFF\uD83C\uDDE6",
                            R.drawable.ic_south_africa
                    ),
                    new LanguageItem(
                            "sq",
                            "Albanian",
                            "",
                            "albania",
                            "\uD83C\uDDE6\uD83C\uDDF1",
                            R.drawable.ic_albania
                    ),
                    new LanguageItem(
                            "ar",
                            "Arabic",
                            "ar-SA",
                            "saudi_arabia",
                            "\uD83C\uDDE6\uD83C\uDDEA",
                            R.drawable.ic_saudi_arabia
                    ),
                    new LanguageItem(
                            "hy",
                            "Armenian",
                            "",
                            "armenia",
                            "\uD83C\uDDE6\uD83C\uDDF2",
                            R.drawable.ic_armenia
                    ),
                    new LanguageItem(
                            "az",
                            "Azerbaijani",
                            "",
                            "azerbaijan",
                            "\uD83C\uDDE6\uD83C\uDDFF",
                            R.drawable.ic_azerbaijan
                    ),
                    new LanguageItem(
                            "eu",
                            "Basque",
                            "",
                            "spain",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_spain
                    ),
                    new LanguageItem(
                            "be",
                            "Belarusian",
                            "",
                            "belarus",
                            "\uD83C\uDDE7\uD83C\uDDFE",
                            R.drawable.ic_belarus
                    ),
                    new LanguageItem(
                            "bn",
                            "Bengali",
                            "",
                            "benin",
                            "\uD83C\uDDE7\uD83C\uDDE9",
                            R.drawable.ic_benin
                    ),
                    new LanguageItem(
                            "bs",
                            "Bosnian",
                            "",
                            "bosnia_and_herzegovina",
                            "\uD83C\uDDE7\uD83C\uDDE6",
                            R.drawable.ic_bosnia_and_herzegovina
                    ),
                    new LanguageItem(
                            "bg",
                            "Bulgarian",
                            "",
                            "bulgaria",
                            "\uD83C\uDDE7\uD83C\uDDEC",
                            R.drawable.ic_bulgaria
                    ),
                    new LanguageItem(
                            "ca",
                            "Catalan",
                            "",
                            "spain",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_spain
                    ),
                    new LanguageItem(
                            "ceb",
                            "Cebuano",
                            "",
                            "philippines",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_philippines
                    ),
                    new LanguageItem(
                            "ny",
                            "Chichewa",
                            "",
                            "malawi",
                            "\uD83C\uDDF2\uD83C\uDDFC",
                            R.drawable.ic_malawi
                    ),
                    new LanguageItem(
                            "zh-CN",
                            "Chinese Simplified",
                            "zh-CN",
                            "china",
                            "\uD83C\uDDE8\uD83C\uDDF3",
                            R.drawable.ic_china
                    ),
                    new LanguageItem(
                            "zh-TW",
                            "Chinese Traditional",
                            "zh-CN",
                            "china",
                            "\uD83C\uDDF9\uD83C\uDDFC",
                            R.drawable.ic_taiwan
                    ),
                    new LanguageItem(
                            "hr",
                            "Croatian",
                            "",
                            "croatia",
                            "\uD83C\uDDED\uD83C\uDDF7",
                            R.drawable.ic_croatia
                    ),
                    new LanguageItem(
                            "cs",
                            "Czech",
                            "cs-CZ",
                            "czech_republic",
                            "\uD83C\uDDE8\uD83C\uDDFF",
                            R.drawable.ic_czech_republic
                    ),
                    new LanguageItem(
                            "da",
                            "Danish",
                            "da-DK",
                            "denmark",
                            "\uD83C\uDDE9\uD83C\uDDF0",
                            R.drawable.ic_denmark
                    ),
                    new LanguageItem(
                            "nl",
                            "Dutch",
                            "",
                            "netherlands",
                            "\uD83C\uDDF3\uD83C\uDDF1",
                            R.drawable.ic_netherlands
                    ),
                    new LanguageItem(
                            "en",
                            "English",
                            "en-US",
                            "united_kingdom",
                            "\uD83C\uDDEC\uD83C\uDDE7",
                            R.drawable.ic_united_kingdom
                    ),
                    new LanguageItem(
                            "eo",
                            "Esperanto",
                            "",
                            "esperanto",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_esperanto
                    ),
                    new LanguageItem(
                            "et",
                            "Estonian",
                            "",
                            "estonia",
                            "\uD83C\uDDEA\uD83C\uDDEA",
                            R.drawable.ic_estonia
                    ),
                    new LanguageItem(
                            "tl",
                            "Filipino",
                            "",
                            "philippines",
                            "\uD83C\uDDF5\uD83C\uDDED",
                            R.drawable.ic_philippines
                    ),
                    new LanguageItem(
                            "fi",
                            "Finnish",
                            "fi-FI",
                            "finland",
                            "\uD83C\uDDEB\uD83C\uDDEE",
                            R.drawable.ic_finland
                    ),
                    new LanguageItem(
                            "fr",
                            "French",
                            "fr-CA",
                            "france",
                            "\uD83C\uDDEB\uD83C\uDDF7",
                            R.drawable.ic_france
                    ),
                    new LanguageItem(
                            "gl",
                            "Galician",
                            "",
                            "spain",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_spain
                    ),
                    new LanguageItem(
                            "ka",
                            "Georgian",
                            "",
                            "georgia",
                            "\uD83C\uDDEC\uD83C\uDDEA",
                            R.drawable.ic_georgia
                    ),
                    new LanguageItem(
                            "de",
                            "German",
                            "de-DE",
                            "germany",
                            "\uD83C\uDDE9\uD83C\uDDEA",
                            R.drawable.ic_germany
                    ),
                    new LanguageItem(
                            "gu",
                            "Gujarati",
                            "",
                            "india",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_india
                    ),
                    new LanguageItem(
                            "el",
                            "Greek",
                            "el-GR",
                            "greece",
                            "\uD83C\uDDEC\uD83C\uDDF7",
                            R.drawable.ic_greece
                    ),
                    new LanguageItem(
                            "ht",
                            "Haitian Creole",
                            "",
                            "haiti",
                            "\uD83C\uDDED\uD83C\uDDF9",
                            R.drawable.ic_haiti
                    ),
                    new LanguageItem(
                            "ha",
                            "Hausa",
                            "",
                            "niger",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_niger
                    ),
                    new LanguageItem(
                            "iw",
                            "Hebrew",
                            "he-IL",
                            "israel",
                            "\uD83C\uDDEE\uD83C\uDDF1",
                            R.drawable.ic_israel
                    ),
                    new LanguageItem(
                            "hi",
                            "Hindi",
                            "hi-IN",
                            "india",
                            "\uD83C\uDDEE\uD83C\uDDF3",
                            R.drawable.ic_india
                    ),
                    new LanguageItem(
                            "hmn",
                            "Hmong",
                            "",
                            "china",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_china
                    ),
                    new LanguageItem(
                            "hu",
                            "Hungarian",
                            "hu-HU",
                            "hungary",
                            "\uD83C\uDDED\uD83C\uDDFA",
                            R.drawable.ic_hungary
                    ),
                    new LanguageItem(
                            "is",
                            "Icelandic",
                            "",
                            "iceland",
                            "\uD83C\uDDEE\uD83C\uDDF8",
                            R.drawable.ic_iceland
                    ),
                    new LanguageItem(
                            "id",
                            "Indonesian",
                            "id-ID",
                            "indonesia",
                            "\uD83C\uDDEE\uD83C\uDDE9",
                            R.drawable.ic_indonesia
                    ),
                    new LanguageItem(
                            "ig",
                            "Igbo",
                            "",
                            "nigeria",
                            "\uD83C\uDDFF\uD83C\uDDE6",
                            R.drawable.ic_nigeria
                    ),
                    new LanguageItem(
                            "ga",
                            "Irish",
                            "",
                            "ireland",
                            "\uD83C\uDDEE\uD83C\uDDEA",
                            R.drawable.ic_ireland
                    ),
                    new LanguageItem(
                            "it",
                            "Italian",
                            "it-IT",
                            "italy",
                            "\uD83C\uDDEE\uD83C\uDDF9",
                            R.drawable.ic_italy
                    ),
                    new LanguageItem(
                            "ja",
                            "Japanese",
                            "ja-JP",
                            "japan",
                            "\uD83C\uDDEF\uD83C\uDDF5",
                            R.drawable.ic_japan
                    ),
                    new LanguageItem(
                            "jw",
                            "Javanese",
                            "",
                            "indonesia",
                            "\uD83C\uDDEE\uD83C\uDDE9",
                            R.drawable.ic_indonesia
                    ),
                    new LanguageItem(
                            "kk",
                            "Kazakh",
                            "",
                            "kazakhstan",
                            "\uD83C\uDDF0\uD83C\uDDFF",
                            R.drawable.ic_kazakhstan
                    ),
                    new LanguageItem(
                            "km",
                            "Khmer",
                            "",
                            "cambodia",
                            "\uD83C\uDDF0\uD83C\uDDED",
                            R.drawable.ic_cambodia
                    ),
                    new LanguageItem(
                            "kn",
                            "Kannada",
                            "",
                            "india",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_india
                    ),
                    new LanguageItem(
                            "ko",
                            "Korean",
                            "ko-KR",
                            "south_korea",
                            "\uD83C\uDDF0\uD83C\uDDF7",
                            R.drawable.ic_south_korea
                    ),
                    new LanguageItem(
                            "lo",
                            "Lao",
                            "",
                            "laos",
                            "\uD83C\uDDF1\uD83C\uDDE6",
                            R.drawable.ic_laos
                    ),
                    new LanguageItem(
                            "lv",
                            "Latvian",
                            "",
                            "latvia",
                            "\uD83C\uDDF1\uD83C\uDDFB",
                            R.drawable.ic_latvia
                    ),
                    new LanguageItem(
                            "lt",
                            "Lithuanian",
                            "",
                            "lithuania",
                            "\uD83C\uDDF1\uD83C\uDDF9",
                            R.drawable.ic_lithuania
                    ),
                    new LanguageItem(
                            "mk",
                            "Macedonian",
                            "",
                            "macedonia",
                            "\uD83C\uDDF2\uD83C\uDDF0",
                            R.drawable.ic_republic_of_macedonia
                    ),
                    new LanguageItem(
                            "mg",
                            "Malagasy",
                            "",
                            "madagascar",
                            "\uD83C\uDDF2\uD83C\uDDEC",
                            R.drawable.ic_madagascar
                    ),
                    new LanguageItem(
                            "ms",
                            "Malay",
                            "",
                            "malaysia",
                            "\uD83C\uDDF2\uD83C\uDDFE",
                            R.drawable.ic_malaysia
                    ),
                    new LanguageItem(
                            "ml",
                            "Malayalam",
                            "",
                            "india",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_india
                    ),
                    new LanguageItem(
                            "mi",
                            "Maori",
                            "",
                            "new_zealand",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_new_zealand
                    ),
                    new LanguageItem(
                            "mr",
                            "Marathi",
                            "",
                            "india",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_india
                    ),
                    new LanguageItem(
                            "my",
                            "Myanmar (Burmese)",
                            "",
                            "myanmar",
                            "\uD83C\uDDF2\uD83C\uDDF2",
                            R.drawable.ic_myanmar
                    ),
                    new LanguageItem(
                            "mn",
                            "Mongolian",
                            "",
                            "mongolia",
                            "\uD83C\uDDF2\uD83C\uDDF3",
                            R.drawable.ic_mongolia
                    ),
                    new LanguageItem(
                            "ne",
                            "Nepali",
                            "",
                            "nepal",
                            "\uD83C\uDDF3\uD83C\uDDF5",
                            R.drawable.ic_nepal
                    ),
                    new LanguageItem(
                            "no",
                            "Norwegian",
                            "no-NO",
                            "norway",
                            "\uD83C\uDDF3\uD83C\uDDF4",
                            R.drawable.ic_norway
                    ),
                    new LanguageItem(
                            "fa",
                            "Persian",
                            "",
                            "iran",
                            "\uD83C\uDDEE\uD83C\uDDF7",
                            R.drawable.ic_iran
                    ),
                    new LanguageItem(
                            "pl",
                            "Polish",
                            "pl-PL",
                            "poland",
                            "\uD83C\uDDF5\uD83C\uDDF1",
                            R.drawable.ic_poland
                    ),
                    new LanguageItem(
                            "pt",
                            "Portuguese",
                            "pt-BR",
                            "portugal",
                            "\uD83C\uDDF5\uD83C\uDDF9",
                            R.drawable.ic_portugal
                    ),
                    new LanguageItem(
                            "pa",
                            "Punjabi",
                            "",
                            "pakistan",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_pakistan
                    ),
                    new LanguageItem(
                            "ro",
                            "Romanian",
                            "ro-RO",
                            "romania",
                            "\uD83C\uDDF7\uD83C\uDDF4",
                            R.drawable.ic_romania
                    ),
                    new LanguageItem(
                            "ru",
                            "Russian",
                            "ru-RU",
                            "russia",
                            "\uD83C\uDDF7\uD83C\uDDFA",
                            R.drawable.ic_russia
                    ),
                    new LanguageItem(
                            "sr",
                            "Serbian",
                            "",
                            "serbia",
                            "\uD83C\uDDF7\uD83C\uDDF8",
                            R.drawable.ic_serbia
                    ),
                    new LanguageItem(
                            "st",
                            "Sesotho",
                            "",
                            "lesotho",
                            "\uD83C\uDDF1\uD83C\uDDF8",
                            R.drawable.ic_lesotho
                    ),
                    new LanguageItem(
                            "si",
                            "Sinhala",
                            "",
                            "sri_lanka",
                            "\uD83C\uDDF1\uD83C\uDDF0",
                            R.drawable.ic_sri_lanka
                    ),
                    new LanguageItem(
                            "sk",
                            "Slovak",
                            "sk-SK",
                            "slovakia",
                            "\uD83C\uDDF8\uD83C\uDDF0",
                            R.drawable.ic_slovakia
                    ),
                    new LanguageItem(
                            "sl",
                            "Slovenian",
                            "",
                            "slovenia",
                            "\uD83C\uDDF8\uD83C\uDDEE",
                            R.drawable.ic_slovenia
                    ),
                    new LanguageItem(
                            "so",
                            "Somali",
                            "",
                            "somalia",
                            "\uD83C\uDDF8\uD83C\uDDF4",
                            R.drawable.ic_somalia
                    ),
                    new LanguageItem(
                            "es",
                            "Spanish",
                            "es-ES",
                            "spain",
                            "\uD83C\uDDEA\uD83C\uDDF8",
                            R.drawable.ic_spain
                    ),
                    new LanguageItem(
                            "su",
                            "Sudanese",
                            "",
                            "sudan",
                            "\uD83C\uDDF8\uD83C\uDDE9",
                            R.drawable.ic_sudan
                    ),
                    new LanguageItem(
                            "sv",
                            "Swedish",
                            "sv-SE",
                            "sweden",
                            "\uD83C\uDDF8\uD83C\uDDEA",
                            R.drawable.ic_sweden
                    ),
                    new LanguageItem(
                            "sw",
                            "Swahili",
                            "",
                            "tanzania",
                            "\uD83C\uDDF0\uD83C\uDDEA",
                            R.drawable.ic_tanzania
                    ),
                    new LanguageItem(
                            "ta",
                            "Tamil",
                            "",
                            "singapore",
                            "\uD83C\uDFF3️\u200D\uD83C\uDF08",
                            R.drawable.ic_singapore
                    ),
                    new LanguageItem(
                            "te",
                            "Telugu",
                            "",
                            "india",
                            "\uD83C\uDDEE\uD83C\uDDEA",
                            R.drawable.ic_india
                    ),
                    new LanguageItem(
                            "tg",
                            "Tajik",
                            "",
                            "tajikistan",
                            "\uD83C\uDDF9\uD83C\uDDEF",
                            R.drawable.ic_tajikistan
                    ),
                    new LanguageItem(
                            "th",
                            "Thai",
                            "th-TH",
                            "thailand",
                            "\uD83C\uDDF9\uD83C\uDDED",
                            R.drawable.ic_thailand
                    ),
                    new LanguageItem(
                            "tr",
                            "Turkish",
                            "tr-TR",
                            "turkey",
                            "\uD83C\uDDF9\uD83C\uDDF7",
                            R.drawable.ic_turkey
                    ),
                    new LanguageItem(
                            "uk",
                            "Ukrainian",
                            "",
                            "ukraine",
                            "\uD83C\uDDFA\uD83C\uDDE6",
                            R.drawable.ic_ukraine
                    ),
                    new LanguageItem(
                            "ur",
                            "Urdu",
                            "",
                            "pakistan",
                            "\uD83C\uDDF5\uD83C\uDDF0",
                            R.drawable.ic_pakistan
                    ),
                    new LanguageItem(
                            "uz",
                            "Uzbek",
                            "",
                            "uzbekistan",
                            "\uD83C\uDDFA\uD83C\uDDFF",
                            R.drawable.ic_uzbekistan
                    ),
                    new LanguageItem(
                            "vi",
                            "Vietnamese",
                            "",
                            "vietnam",
                            "\uD83C\uDDFB\uD83C\uDDF3",
                            R.drawable.ic_vietnam
                    ),
                    new LanguageItem(
                            "cy",
                            "Welsh",
                            "",
                            "wales",
                            "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F",
                            R.drawable.ic_wales
                    ),
                    new LanguageItem(
                            "yi",
                            "Yiddish",
                            "",
                            "sweden",
                            "\uD83C\uDDEE\uD83C\uDDF1",
                            R.drawable.ic_sweden
                    ),
                    new LanguageItem(
                            "yo",
                            "Yoruba",
                            "",
                            "nigeria",
                            "\uD83C\uDDF3\uD83C\uDDEC",
                            R.drawable.ic_nigeria
                    ),
                    new LanguageItem(
                            "zu",
                            "Zulu",
                            "",
                            "south_africa",
                            "\uD83C\uDDFF\uD83C\uDDE6",
                            R.drawable.ic_south_africa
                    )
            )
    );
}
