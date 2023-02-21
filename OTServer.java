import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

public class OTServer {
    public static String connect (Customer_Admin_Implementation obj){
        DatagramSocket aSocket = null;
        String res = null;
        try{
            aSocket = new DatagramSocket(6790);
            // create socket at agreed port
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                String b = new String(request.getData(), 0, request.getLength());
                if(b.equals("Avatar") || b.equals("Titanic") || b.equals("Avengers")){
                    System.out.println("Sending the Schedule of Outrement Movie Theater");
                    res = obj.listSingleMovieSchedule(b);
                }
                else if(b.contains("outmovie")){
                    System.out.println(b.substring(8,b.length()));
                    res = obj.getOutsideSchedule(b.substring(3,b.length()-1));
                }
                else if(b.contains("ex")){
                    String []arr = b.split(",");
                    if(obj.getEnough(arr[2],arr[3],Integer.parseInt(arr[4]))){
                        res = obj.bookMovieTickets(arr[1],arr[2],arr[3],Integer.parseInt(arr[4]));
                    }
                }
                else{
                    System.out.println("Sending the Schedule of Outrement Movie Theater");
                    res = obj.getSingleSchedule(b);
                }
                request.setData(res.getBytes());
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
                        request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
        return res;
    }

    public static void main(String[] args) {
        int number = 1;
        try {
//            File file = new File("Outrement Server Log #"+number);
//            PrintStream stream = new PrintStream(file);
//            System.setOut(stream);
//            number++;
            Customer_Admin_Implementation obj = new Customer_Admin_Implementation();
            Registry registry = LocateRegistry.createRegistry(1100);
            HashMap<String,HashMap<String, ArrayList<String>>> map = new HashMap<>();
            HashMap<String,ArrayList<String>> innerMap = new HashMap<>();
            HashMap<String,ArrayList<String>> innerMap2 = new HashMap<>();
            HashMap<String,ArrayList<String>> innerMap3 = new HashMap<>();
            ArrayList<String> list = new ArrayList<>();
            ArrayList<String> list2 = new ArrayList<>();
            ArrayList<String> list3 = new ArrayList<>();
//            list.add("5");
//            list2.add("10");
//            list3.add("20");
//            list.add("");
//            list2.add("");
//            list3.add("");
//            innerMap.put("OUTM160523",list);
//            innerMap2.put("OUTN160523",list2);
//            innerMap3.put("OUTE160523",list3);
            map.put("Avatar",innerMap);
            map.put("Titanic",innerMap2);
            map.put("Avengers",innerMap3);
            obj.setMovieDataBase(map);
            obj.setID("OUTA200");
            registry.rebind("AdminObject",obj);
            System.out.println("Outrement server is running");
            System.out.println("Outrement Server Ready");

            connect(obj);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
