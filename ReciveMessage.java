import java.net.Socket;

import java.io.DataInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReciveMessage implements Runnable {

    Socket soc = null;
    Player user = null;

    public ReciveMessage(Socket soc, Player user) {
        this.soc = soc;
        this.user = user;
    }

    public void run() {
        String sender = "", time = "", data = "";
        int header = 1;
        try {
            InputStream in = this.soc.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            do {

                System.out.println("[ " + user.getUniqueName() + " ] : keep Waiting ....");
                header = dis.readInt();

                if (header == 1) {
                    
                    sender = dis.readUTF();
                    time = dis.readUTF();
                    data = dis.readUTF();
                    System.out.println("| ****************************************************** |");
                    System.out.println("|--> { TIME : [" + time + "] FROM : [" + sender + "]  }");
                    System.out.println("|--> MESSAGE:  " + data + "   }");
                    System.out.println("| ****************************************************** |");

                } else if (header == 2) {
                    System.out.println(dis.readUTF());

                } else {
                    System.out.println(dis.readUTF());
                }

            } while (header != 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
