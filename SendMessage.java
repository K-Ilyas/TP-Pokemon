import java.net.Socket;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendMessage implements Runnable {

    Socket soc = null;
    Player user = null;
    DataOutputStream out = null;
    ObjectOutputStream bos = null;
    String uniqueName = "";

    public SendMessage(DataOutputStream out, ObjectOutputStream bos, Player user,String uniqueName) {
        this.user = user;
        this.out = out;
        this.bos = bos;
        this.uniqueName = uniqueName;
    }

    public void run() {

        String pseudo = "", message = "";
        Scanner sc = new Scanner(System.in);
        try {

            do {

     
                out.writeUTF(this.uniqueName);
                if (!message.toUpperCase().equals("N")) {
                    System.out.println("|--> Enter your message [ or n/N to quit ] : ==> ");

                    message = sc.nextLine();
                    out.writeUTF(message.toString());
                }
                out.flush();

                bos.writeObject(user);
                bos.flush();
                bos.flush();
                
            } while (!message.toUpperCase().equals("N"));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
