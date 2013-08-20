print('Starting...')

importPackage(Packages.javafx.scene);
importPackage(Packages.javafx.scene.control);
importPackage(Packages.javafx.scene.layout);
importPackage(Packages.javafx.stage);
importClass(Packages.javafx.scene.control.Button);
importClass(Packages.javafx.scene.layout.StackPane);
importClass(Packages.javafx.scene.Scene);
importClass(Packages.javafx.stage.Stage);

myroot = new HBox();

mybutton = new Button();
mybutton.setText('Hello from Script2');

myroot.getChildren().addAll(mybutton);
myscene = new Packages.javafx.scene.Scene(myroot);
mystage = new Packages.javafx.stage.Stage();
mystage.setScene(myscene);
mystage.show();