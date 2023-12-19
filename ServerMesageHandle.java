import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.Set;

public class ServerMesageHandle implements Runnable {

    private SocketServer socketServer = null;
    private DataInputStream dis = null;
    private ObjectInputStream ois = null;
    private DataOutputStream dos = null;

    public ServerMesageHandle(SocketServer socketServer, DataOutputStream dos, DataInputStream dis,
            ObjectInputStream ois) {
        this.socketServer = socketServer;
        this.dos = dos;
        this.dis = dis;
        this.ois = ois;

    }

    @Override
    public void run() {

        boolean isTrue = true;
        String header = "", data = "";
        Player palyerInfo = null;

        try {
            while (isTrue) {
                int n = 0;

                header = dis.readUTF();

                data = dis.readUTF();

                if (data.toUpperCase().equals("N")) {
                    isTrue = false;
                    palyerInfo = null;
                    try {
                        palyerInfo = (Player) ois.readObject();
                    } catch (

                    ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (palyerInfo != null) {

                        if (this.socketServer.logOut(palyerInfo)) {
                            dos.writeInt(0);
                            dos.writeUTF("|--> SERVER : YOUR LOG OUT SUCCESFLY");
                            dos.flush();
                            System.out.println("|--> SERVER : bye bye " + palyerInfo.getUniqueName() + " !!!!");
                            isTrue = false;
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF(
                                    "|--> SERVER :YOUR PSEUDO IS NOT SUPORTABLE OR PASSWORD NOT FORT PLEASE RETAIT");
                            dos.flush();
                        }
                    } else {
                        dos.writeInt(0);
                        dos.writeUTF("|--> SERVER :YOUR OBJECT SEND IS NULL !!!");
                        dos.flush();
                        System.out.println("|--> SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");
                    }
                } else {
                    try {
                        palyerInfo = (Player) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Socket reciver = this.socketServer.foundUser(header);

                    if (reciver != null) {

                        try {
                            OutputStream outRecive = reciver.getOutputStream();
                            DataOutputStream dosRecive = new DataOutputStream(outRecive);
                            dosRecive.writeInt(1);
                            dosRecive.writeUTF(palyerInfo.getUniqueName());
                            dosRecive.writeUTF(LocalDateTime.now().toString());
                            dosRecive.writeUTF(data);
                            dosRecive.flush();
                            isTrue = true;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        dos.writeInt(2);
                        dos.writeUTF("SERVER:SORRY THE DESTINATIOn NOT FOUND");
                        dos.flush();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
