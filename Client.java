import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try{
            Scanner input = new Scanner(System.in);
            int port = 0000;
            boolean flag = true;
            while(flag){
                System.out.println("Please Enter the server Code you want to select");
                System.out.println("1. (ATW)Atwater (OUT)2.Outrement 3. (VER)Verdun");
                String server = input.next();
                if(server.equals("ATW"))
                    port = 1099;
                else if(server.equals("OUT"))
                    port = 1100;
                else if(server.equals("VER"))
                    port = 1101;
                Registry registry = LocateRegistry.getRegistry("localhost", port);
                Customer_Admin obj = (Customer_Admin) registry.lookup("AdminObject");
                boolean innerFlag = true;
                while(innerFlag){
                    System.out.println("Please Choose One of the Following Options");
                    System.out.println("1. Add Movie Slots\n 2.Remove Movie Slots\n 3.List Movies Availability\n 4.Book Movie Tickets\n 5. Get a Booking Schedule\n 6. Cancel Movie Tickets\n 7. Exit\n 8. Choose Another Server\n 9.Exchange Tickets\n");
                    int select = input.nextInt();
                    if(select == 1){
                        System.out.println("Please Enter Movie ID:");
                        String mID = input.next();
                        System.out.println("Please Enter Movie Name:");
                        String mName = input.next();
                        System.out.println("Please Enter The Booking Capacity:");
                        int mCapacity = input.nextInt();
                        System.out.println(obj.addMovieSlots(mID, mName, mCapacity));
                    }
                    else if(select == 2){
                        System.out.println("Please Enter Movie ID:");
                        String mID = input.next();
                        System.out.println("Please Enter Movie Name:");
                        String mName = input.next();
                        System.out.println(obj.removeMovieSlots(mID, mName));
                    }
                    else if (select ==3){
                        System.out.println("Please Enter Movie Name:");
                        String mName = input.next();
                        System.out.println(obj.listMovieShowsAvailability(mName));
                    }
                    else if(select == 4){
                        System.out.println("Please Enter The Customer ID:");
                        String cID = input.next().trim();
                        System.out.println("Please Enter Movie ID:");
                        String mID = input.next().trim();
                        System.out.println("Please Enter Movie Name:");
                        String mName = input.next().trim();
                        System.out.println("Please Enter The Number of Tickets:");
                        int mCapacity = input.nextInt();
                        System.out.println(obj.bookMovieTickets(cID, mID, mName, mCapacity));
                    }
                    else if(select == 5){
                        System.out.println("Please Enter The Customer ID:");
                        String cID = input.next();
                        System.out.println(obj.getBookingSchedule(cID));
                    }
                    else if(select == 6){
                        System.out.println("Please Enter The Customer ID:");
                        String cID = input.next();
                        System.out.println("Please Enter Movie ID:");
                        String mID = input.next();
                        System.out.println("Please Enter Movie Name:");
                        String mName = input.next();
                        System.out.println("Please Enter The Number of Tickets:");
                        int mCapacity = input.nextInt();
                        System.out.println(obj.cancelMovieTickets(cID, mID, mName, mCapacity));
                    }
                    else if(select == 7){
                        innerFlag = false;
                        flag = false;
                    }
                    else if(select == 8){
                        innerFlag = false;
                    }
                    else if(select == 9){
                        System.out.println("Please Enter The Customer ID:");
                        String cID = input.next();
                        System.out.println("Please Enter Movie ID:");
                        String mID = input.next();
                        System.out.println("Please Enter Movie Name:");
                        String mName = input.next();
                        System.out.println("Please Enter New Movie ID:");
                        String nID = input.next();
                        System.out.println("Please Enter New Movie Name:");
                        String nName = input.next();
                        System.out.println("Please Enter The Number of Tickets:");
                        int mCapacity = input.nextInt();
                        System.out.println(obj.exchangeTicket(cID, mID, mName,nID,nName,mCapacity));
                    }
                }


            }
//            obj.addMovieSlots("ATWz160523","Avengers",3);
//            obj.addMovieSlots("ATWM160523","Aveangers",3);
//            obj.addMovieSlots("ATWM160523","Avengers",3);
//            obj.removeMovieSlots("ATWM160523","Avengers");
//            obj.cancelMovieTickets("ATWC235","ATWE160523","Avengers",1);
//            System.out.println(obj.listMovieShowsAvailability("Avengers"));
//            obj.getBookingSchedule("ATWC235");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
