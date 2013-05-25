package net.jonasbandi.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.io.IOUtils;

public class ScriptRunner {

    private final ScriptEngine engine;

    public class ScriptContext {

        public String inputString;
        public int inputInt;
        public String returnString;
    }

    public ScriptRunner() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
    }

    public void sayHello(String name) {
        System.out.println("Hello World!");
        try {
            engine.put("name", name);
            engine.eval("print('Hello ' + name + ' from JavaScript!\\n')");
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
        System.out.println("Goodbye World!");
    }

    public void runThread() throws ScriptException {

        String script = "var impl = new Object();"
                + "impl.run = function() {print(\"Yeah that's right, you better run!\")};"
                + "var runnable = new java.lang.Runnable(impl);"
                + "var thread = new java.lang.Thread(runnable);"
                + "thread.run()";

        engine.eval(script);

        System.out.println("\n");
    }

    public void runScriptFromFile() throws FileNotFoundException, IOException, ScriptException {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("script.js");
        String script = IOUtils.toString(inputStream);

        engine.eval(script);

        System.out.println("\n");
    }

    public void passContextToScript() throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("scriptWithContext.js");
        Reader file = new InputStreamReader(inputStream);

        ScriptContext context = new ScriptContext();
        context.inputString = "Durden";
        context.inputInt = 42;

        engine.put("name", "Tyler");
        engine.put("con", context);
        engine.eval(file);

        System.out.println("\n");
        System.out.println("Java: Return is: " + context.returnString);

    }

    public void callFunctionDefinedInScript() throws Exception {
        String script = "function hello(name) { print('Hello, ' + name); }";

        engine.eval(script);
        Invocable inv = (Invocable) engine;

        inv.invokeFunction("hello", "Scripting!!");
        System.out.println("\n");
    }

    public void callObjectDefinedInScript() throws Exception {
        String script = "var obj = {};"
                + "obj.hello = function hello(name) { print('Hello, ' + name); };";

        engine.eval(script);
        Invocable inv = (Invocable) engine;
        Object obj = engine.get("obj");

        inv.invokeMethod(obj, "hello", "Script Method!!");
        System.out.println("\n");
    }

    public void callJavaInterfaceImplementedInScript() throws Exception {

        String script = "function run() { println('run called'); }";
        String script2 = "var obj = new Object(); obj.run = function() { println('run method called'); }";

        engine.eval(script);
        Invocable inv = (Invocable) engine;


        Runnable r = inv.getInterface(Runnable.class);
        Thread th = new Thread(r);
        th.start();
        System.out.println("\n");
        
        engine.eval(script2);
        Invocable inv2 = (Invocable) engine;
        Object obj = engine.get("obj");
        Runnable r2 = inv.getInterface(obj, Runnable.class);
        Thread th2 = new Thread(r2);
        th2.start();
    }
}
