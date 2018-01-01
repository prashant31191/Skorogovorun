package shavkunov.skorogovorun.lite;

public class Constants {

    private Constants() {
    }

    public static final class Url {

        // Скороговорки
        private static final String ALL_PATTERS = "https://api.myjson.com/bins/1ggkv7";
        private static final String LIGHT_PATTERS = "https://api.myjson.com/bins/128bib";
        private static final String MEDIUM_PATTERS = "https://api.myjson.com/bins/1cetyb";
        private static final String HARD_PATTERS = "https://api.myjson.com/bins/1h6asj";
        public static final String[] ARRAY_PATTERS = {ALL_PATTERS, LIGHT_PATTERS, MEDIUM_PATTERS,
                HARD_PATTERS};

        // Курсы
        private static final String POSTURE = "https://api.myjson.com/bins/1gy8o3";
        private static final String BREATH = "https://api.myjson.com/bins/xdakz";
        private static final String VOICE = "https://api.myjson.com/bins/16ydf7";
        private static final String DICTION = "https://api.myjson.com/bins/c1z4j";
        public static final String[] ARRAY_COURSES = {POSTURE, BREATH, VOICE, DICTION};
    }
}
