package net.jonasbandi.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.script.ScriptException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class AppTest {
    private ScriptRunner runner;

    @Before
    public void setUp(){
        runner = new ScriptRunner();   
    }

    @Test
    public void testSayHello() {
        
        runner.sayHello("Tyler");
    }

    @Test
    public void runThread() throws ScriptException {
        runner.runThread();
    }

    @Test
    public void runScriptFile() throws ScriptException, FileNotFoundException, IOException {
        runner.runScriptFromFile();
    }

    @Test
    public void runScriptWithContext() throws Exception {
        runner.passContextToScript();
    }

    @Test
    public void runFunctionDefinedInScript() throws Exception {
        runner.callFunctionDefinedInScript();
    }

    @Test
    public void runObjectDefinedInScript() throws Exception {
        runner.callObjectDefinedInScript();
    }

    @Test
    public void runInterfaceDefinedInScript() throws Exception {
        runner.callJavaInterfaceImplementedInScript();
        Thread.sleep(2000);
    }
}
