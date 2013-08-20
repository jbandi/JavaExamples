/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxfirst;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author jbandi
 */
public class SampleController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        System.out.println("You clicked me!");
        label.setText("Hello World!");

//        openFxGui();
//        openSwingGui();
        openFXGuiScripted();



    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void openSwingGui() {
        JButton button = new JButton("Say Hello");
        JFrame frame = new JFrame("Rhino URL Fetcher");
        frame.add(button);
        frame.pack();
        frame.setVisible(true);
    }

    public void openFxGui() {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        Button btn2 = new Button();
        btn2.setText("Say 'Hello World2'");

//        StackPane root = new StackPane();
        HBox root = new HBox();
        root.getChildren().addAll(btn);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.show();
    }

    private void openFXGuiScripted() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

//        String script = "print('Starting...');"
//                + "importPackage(Packages.javafx.scene);"
//                + "importPackage(Packages.javafx.scene.layout);"
//                + "importPackage(Packages.javafx.scene.control);"
//                + "importPackage(Packages.javafx.stage);"
//                + "importClass(Packages.javafx.scene.layout.StackPane);"
//                + "importClass(Packages.javafx.scene.control.Button);"
//                + "importClass(Packages.javafx.scene.Scene);"
//                + "importClass(Packages.javafx.stage.Stage);"
//                + "mybutton = new Packages.javafx.scene.control.Button();"
//                + "mybutton.setText('Hello from Script');"
//                + "myroot = new Packages.javafx.scene.layout.StackPane();"
//                + "myroot.getChildren().add(mybutton);"
//                + "myscene = new Packages.javafx.scene.Scene(myroot);"
//                + "mystage = new Packages.javafx.stage.Stage();"
//                + "mystage.setScene(myscene);"
//                + "mystage.show();";
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

//        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("build/classes/javafxfirst/uiscript.js");
//        Reader file = new InputStreamReader(inputStream);
        Reader file = new FileReader("src/javafxfirst/uiscript.js");

        engine.eval(file);
    }
}
