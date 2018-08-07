package Main;
import aboutyou.Parser;

public class Main {
    public static void main(String[] args) throws Exception{
        Parser parser = new Parser("https://www.aboutyou.de/suche?term="+args[0], args[0]);

    }
}
