//package dk.flexibet.bettingapp;
//
//import static com.sun.btrace.BTraceUtils.*;
//import com.sun.btrace.annotations.*;
//import static com.sun.btrace.BTraceUtils.*;
//
//@BTrace public class Trace {
//    
//	
//	@TLS private static long time;
//	
//	@OnMethod(clazz="dk.bot.bettingengine.statemachine.marketlistener.MarketRunnerListenerImpl", method="onRunnerData",  location=@Location(value=Kind.ENTRY))
//    public static void entry() { // all calls to the methods with signature "()"
//        time = timeMillis();
//       // println(probeMethod());
//    }
//    
//    @OnMethod(clazz="dk.bot.bettingengine.statemachine.marketlistener.MarketRunnerListenerImpl", method="onRunnerData",  location=@Location(value=Kind.RETURN))
//    public static void ret() { // all calls to the methods with signature "()"
//        println(str(timeMillis()-time));
//    }
//}
