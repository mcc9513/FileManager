package cats;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        GUI gui = new GUI(fileManager);
        gui.createAndShowGUI();
    }
}
