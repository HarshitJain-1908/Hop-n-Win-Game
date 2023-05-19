import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Random;

class HopnWin {
    //Game class
    private Player p;
    private ArrayList<Tile> carpet;
    private Calculator c;

    public HopnWin() {
        r.init(System.in);
        p = new Player();
        carpet = new ArrayList<>(20);
        c=new Calculator(); //generic calculator for only integers and strings
        setcarpet();
    }

    public String getAlphaNumericString(int n) {
        StringBuilder sb = new StringBuilder(n);
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        for (int i = 0; i < n; i++) {
            int index = (int) (s.length() * Math.random());
            sb.append(s.charAt(index));
        }
        return sb.toString();
    }

    public void startGame() throws IOException, InvalidTypeException {
        System.out.println("Game is ready");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int chance = p.getChances();
        int jump;
        String enter;
        while (chance > 0) {
            System.out.print("Hit enter for your hop " + (6 - chance) + ": ");
            enter = input.readLine();
            jump = p.hop();
            //System.out.println("Jump "+jump);
            try {

                Tile t = carpet.get(jump - 1);
                SoftToy toy=t.giveToy();
                System.out.println("You landed on tile " + jump);
                if (jump % 2 == 0) {
                    //that means player land on a even numbered tile
                    p.getB().addToy(toy);
                    System.out.println("You won a " + toy.getName() + " soft toy");
                } else {
                    //that means player land on a odd numbered tile
                    boolean giveToy= GiveToy();
                    if(giveToy) {
                        System.out.println("Correct answer");
                        p.getB().addToy(toy);
                        System.out.println("You won a " + toy.getName() + " soft toy");
                    }
                    else {
                        System.out.println("Incorrect answer");
                        System.out.println("You didn't win any soft toy");
                    }
                }
            }
            catch(IndexOutOfBoundsException e) {
                System.out.println("You are too energetic and zoomed past all the tiles. Muddy puddle Splash!");
            }
            updatePlayerchances();
            chance = p.getChances();
        }

    }

    private void setcarpet() {
        //setting up the carpet with fixed number of tiles i.e. 20
        carpet.add(new Tile(1, "Teddy"));
        carpet.add(new Tile(2, "Donald Duck"));
        carpet.add(new Tile(3, "Johny"));
        carpet.add(new Tile(4, "Mickey Mouse"));
        carpet.add(new Tile(5, "Jerry"));
        carpet.add(new Tile(6, "Tom"));
        carpet.add(new Tile(7, "Keymon"));
        carpet.add(new Tile(8, "Doraemon"));
        carpet.add(new Tile(9, "Shinchan"));
        carpet.add(new Tile(10, "Hattori"));
        carpet.add(new Tile(11, "Shishimanu"));
        carpet.add(new Tile(12, "Nobita"));
        carpet.add(new Tile(13, "Minnie"));
        carpet.add(new Tile(14, "Pluto"));
        carpet.add(new Tile(15, "Goofy"));
        carpet.add(new Tile(16, "Spike"));
        carpet.add(new Tile(17, "Stuart"));
        carpet.add(new Tile(18, "Snowbell"));
        carpet.add(new Tile(19, "Monty"));
        carpet.add(new Tile(20, "Humpty"));

    }

    private void updatePlayerchances() {
        int chance = p.getChances();
        p.setChances(chance - 1);
        if (p.getChances() == 0) {
            OverGame();
        }
    }

    private void OverGame() {
        System.out.println("Game Over");
        p.getB().displayToys();
    }
    public boolean GiveToy() throws IOException, InvalidTypeException {
        return askQuesChoice();
    }
    public boolean askQuesChoice() throws IOException, InvalidTypeException{
        System.out.println("Question answer round. integer or string?");
        String s = r.next();
        try{
            return askQuestion(s);
        }
        catch(InvalidTypeException e){
            System.out.println(e.getMessage());
            return askQuesChoice(); //redoing the asking choice step
        }
    }

    public boolean askQuestion(String s) throws IOException, InvalidTypeException {

        Random rand = new Random();
        if (s.equals("integer")) {
            int n1, n2;
            n1 = rand.nextInt();
            n2 = rand.nextInt();
            System.out.println("Calculate the result of " + n1 + " divided by " + n2);
            int res = r.nextInt();
            try {
                boolean status = c.verifyCalculation(n1, n2, res);
                return status;

            } catch (InvalidTypeException e) {
                System.out.println(e.getMessage());
            }

        } else if (s.equals("string")) {
            String s1 = getAlphaNumericString(4);
            String s2 = getAlphaNumericString(4);
            System.out.println("Calculate the concatenation of strings " + s1 + " and " + s2);
            String res = r.next();
            try {
                boolean status = c.verifyCalculation(s1, s2, res);
                return status;

            } catch (InvalidTypeException e) {
                System.out.println(e.getMessage());
            }

        } else {
            throw new InvalidTypeException("Invalid type entered");
        }
        return false;
    }

}
class InvalidTypeException extends Exception{
    public InvalidTypeException(String s){
        super(s);
    }
}
class Calculator<T> {
    private T operand1, operand2;

    public boolean verifyCalculation(T op1, T op2, T result) throws InvalidTypeException {
        operand1 = op1;
        operand2 = op2;

        try {

            int n1 = (Integer) operand1;
            int n2 = (Integer) operand2;
            if ((Integer) result == (n1 / n2)) {
                return true;
            } else {
                return false;
            }
        } catch (ClassCastException e) {
            try {
                String s1 = (String) operand1;
                String s2 = (String) operand2;
                if (((String) result).equals(s1 + s2)) {
                    return true;
                } else {
                    return false;
                }
            } catch (ClassCastException e1) {
                throw new InvalidTypeException("Invalid type in Calculator");
            }
        }
    }
}

class Player{
    private int chances;
    private Bucket b;

    public Player(){
        chances=5;
        b = new Bucket();
    }

    public void setChances(int chances) {
        if(chances>=0 && chances<=5) {
            this.chances = chances;
        }
    }
    public int getChances() {
        return chances;
    }

    public Bucket getB() {
        return b;
    }
    public int hop(){
        int jump;
        Random rand=new Random();
        jump = 1 + rand.nextInt(25);
        return jump;
    }
}
class Bucket{
    private ArrayList<SoftToy> toys;

    public Bucket(){
        toys = new ArrayList<>();
    }
    public void addToy(SoftToy t){
        toys.add(t);
    }
    public ArrayList<SoftToy> getToys(){
        return toys;
    }
    public void displayToys(){
        int i=0;

        try {
            System.out.println("Soft toys won by you are: ");
            while (i < toys.size()) {
                SoftToy t = toys.get(i);
                System.out.print(t.getName() + ", ");
                i++;
            }
        }
        catch(NullPointerException e){
            System.out.println("Oops! You haven't collected any toy.");
            System.out.println("Play again.");
        }
    }
}
class SoftToy implements Cloneable{
    private String name;
    public SoftToy(String s){
        name=s;
    }

    public String getName() {
        return name;
    }
    @Override
    public SoftToy clone(){
        try{
            SoftToy t= (SoftToy) super.clone();
            return t;
        }
        catch(CloneNotSupportedException e){
            System.out.println("Soft toy can't be cloned");
            return null;
        }
    }
}
class Tile {
    private final int position;
    private SoftToy t;
    public Tile(int pos,String name){
        position=pos;
        t= new SoftToy(name);
    }

    public int getPosition() {
        return position;
    }

    public SoftToy getT() {
        return t;
    }
    public SoftToy giveToy(){
        SoftToy t_clone=t.clone();
        return t_clone;
    }
}

public class Main {

    public static void main(String[] args) throws Exception {
        // write your code here
        r.init(System.in);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String name, enter;
        HopnWin g= new HopnWin();

        while(true) {
            System.out.print("Hit enter to initialize the game: ");
            enter=input.readLine();
            if (enter.equals("")) {
                g.startGame();
                break;
            }
        }
    }
}
class r {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    static void init(InputStream input) {
        reader = new BufferedReader(new InputStreamReader(input));
        tokenizer = new StringTokenizer("");
    }

    static String next() throws IOException {
        while (!tokenizer.hasMoreTokens()) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                    reader.readLine());
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    static long nextLong() throws IOException {
        return Long.parseLong(next());
    }

    static double nextDouble() throws IOException {
        return Double.parseDouble(next());
    }
}
