package server;

public class Constants {
    
    public static class Net {
        public final static int port = 9495;
    }

    public static  class Game {
        public final static String map = "map.txt";
    }

    public static class Project {
        
        public final static String title = "T-Project Server (952)";
        
        public class Version {
            public final static String code = "1.1.0";
            public final static String name = "noVersionName";
        }
        
        public static String getInfo() {
            return title + " | " + "Version: " + Version.code + " [" + Version.name + "]";
        }
        
    }
    
}
