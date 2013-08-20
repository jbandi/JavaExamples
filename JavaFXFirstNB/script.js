print('Starting...')

importPackage(Packages.javafx.scene);
importPackage(Packages.javafx.scene.layout);
importPackage(Packages.javafx.stage);
importClass(Packages.javafx.scene.layout.StackPane);
importClass(Packages.javafx.scene.Scene);
importClass(Packages.javafx.stage.Stage);

myroot = new Packages.javafx.scene.layout.StackPane();
myscene = new Packages.javafx.scene.Scene(myroot);
mystage = new Packages.javafx.stage.Stage();
mystage.setScene(myscene);
mystage.show();

