package com.wildstangs.logger;

/**
 *
 * @author Nathan
 */
public class Level {
    private int level;
   private String levelStr;
    
   public static final int ALWAYS_INT = Integer.MAX_VALUE;
   public static final int OFF_INT = Integer.MAX_VALUE-1;
   public static final int FATAL_INT = 70000;
   public static final int ERROR_INT = 60000;
   public static final int WARNING_INT = 50000;
   public static final int NOTICE_INT = 40000;
   public static final int INFO_INT = 30000;
   public static final int DEBUG_INT = 20000;
   public static final int TRACE_INT = 10000;
   public static final int ALL_INT = Integer.MIN_VALUE;
   
   public static final Level ALWAYS = new Level(ALWAYS_INT, "ALWAYS");
   public static final Level OFF = new Level(OFF_INT, "OFF");
   public static final Level FATAL = new Level(FATAL_INT, "FATAL");
   public static final Level ERROR = new Level(ERROR_INT, "ERROR");
   public static final Level WARNING = new Level(WARNING_INT, "WARNING");
   public static final Level NOTICE = new Level(NOTICE_INT, "NOTICE");
   public static final Level INFO = new Level(INFO_INT, "INFO");
   public static final Level DEBUG = new Level(DEBUG_INT, "DEBUG");
   public static final Level TRACE = new Level(TRACE_INT, "TRACE");
   public static final Level ALL = new Level(ALL_INT, "ALL");
   
   protected Level(int clevel, String clevelStr) {
       level = clevel;
       levelStr = clevelStr;
   }
   
   public static Level toLevel(String sLevel) {
       return (Level)toLevel(sLevel, Level.TRACE);
   }
   
   public static Level toLevel(int iLevel) {
       return (Level)toLevel(iLevel, Level.TRACE);
   }
   
   
   
   public static Level toLevel(int iLevel, Level defaultLevel) {
    switch(iLevel) {
        case ALWAYS_INT: return Level.ALWAYS;
        case OFF_INT: return OFF;
        case FATAL_INT: return Level.FATAL;
        case ERROR_INT: return Level.ERROR;
        case WARNING_INT: return Level.WARNING;
        case NOTICE_INT: return Level.NOTICE;
        case INFO_INT: return Level.INFO;
        case DEBUG_INT: return Level.DEBUG;
        case TRACE_INT: return Level.TRACE;
        case ALL_INT: return Level.ALL;
    default: return defaultLevel;
    }
  }

  public static Level toLevel(String sLevel, Level defaultLevel) {
        if (sLevel == null) {
            return defaultLevel;
        } 
            
        String s = sLevel.toUpperCase().replace('"', ' ').trim();
        
        if (s.equals ("ALWAYS")) { return Level.ALWAYS; }
        if (s.equals ("OFF")) { return OFF; }
        if (s.equals ("FATAL")) { return Level.FATAL; }
        if (s.equals ("ERROR")) { return Level.ERROR; }
        if (s.equals ("WARNING")) { return Level.WARNING; }
        if (s.equals ("NOTICE")) { return Level.NOTICE; }
        if (s.equals ("INFO")) { return Level.INFO; }
        if (s.equals ("DEBUG")) { return Level.DEBUG; }
        if (s.equals ("TRACE")) { return Level.TRACE; }
        if (s.equals ("ALL")) { return Level.ALL; }
        return defaultLevel;
  }
  
  public int toInt() {
      return this.level;
  }
  
  public String toString() {
      return this.levelStr;
  }
}
