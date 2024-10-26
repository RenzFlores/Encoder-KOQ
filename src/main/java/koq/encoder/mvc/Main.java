package koq.encoder.mvc;

public class Main {

    public static void main(String[] args) {
        Model m = new Model();
        View v = new View();
        Controller c = new Controller(m, v); // the controller must have a copy of the model and view
    } 

}
