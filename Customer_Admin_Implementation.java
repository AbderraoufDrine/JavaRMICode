import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class Customer_Admin_Implementation extends UnicastRemoteObject implements Customer_Admin {

    private int ATPORT = 6789;
    private int OUTPORT = 6790;
    private int VERPORT = 6791;
    private static boolean connectionFlag = true;

    private HashMap<String, Integer> outMap = new HashMap<>();

    

    private String connect(int PORT, String identifier) {
        String response = "";
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            String msg = identifier;
            byte[] m = msg.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            int serverPort = PORT;
            DatagramPacket request =
                    new DatagramPacket(m, msg.length(), aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            response = new String(reply.getData()).trim();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) aSocket.close();
        }
        connectionFlag = true;
        return response;
    }
    private String ID;
    private HashMap<String, HashMap<String, ArrayList<String>>> movieDataBase;
    protected Customer_Admin_Implementation() throws RemoteException {
    }
    public void setID(String ID) {
        this.ID = ID;

    }
    public void setMovieDataBase(HashMap<String, HashMap<String, ArrayList<String>>> movieDataBase) {
        this.movieDataBase = movieDataBase;
    }
    @Override
    public String addMovieSlots(String movieID, String movieName, int bookingCapacity) throws RemoteException {
        String clientOutput = "";
        try {
            Character ch = ID.charAt(3);
            if (ch.equals('A') && ID.substring(0,3).equals(movieID.substring(0,3))) {
                if (movieDataBase.get(movieName) == null) {
                    System.out.println("This Movie does not exist");
                    clientOutput = clientOutput + "This Movie does not exist\n";
                } else {
                    System.out.println("The movie is in the database");
                    clientOutput = clientOutput + "The movie is in the database\n";
                    HashMap<String, ArrayList<String>> map = movieDataBase.get(movieName);
                    if (map.containsKey(movieID)) {
                        System.out.println("Updating Slots to this current Movie " + movieID);
                        clientOutput = clientOutput + "Updating Slots to this current Movie " + movieID + "\n";
                        movieDataBase.get(movieName).get(movieID).set(0, String.valueOf(bookingCapacity));
                        System.out.println("The slot has been updated");
                        clientOutput = clientOutput + "The slot has been updated\n";
                    } else {
                        String m = movieID.charAt(6) + "" + movieID.charAt(7);
                        String d = movieID.charAt(4) + "" + movieID.charAt(5);
                        int year = Integer.valueOf("20" + Character.valueOf(movieID.charAt(8)) + Character.valueOf(movieID.charAt(9)));
                        int month = Integer.valueOf(m);
                        int day = Integer.valueOf(d);
                        LocalDate movieDate = LocalDate.of(year, month, day);
                        LocalDate today = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
                        Period dif = Period.between(today, movieDate);
                        if (dif.getDays() >= 7) {
                            System.out.println("Adding Movie Slots to this current theater...");
                            clientOutput = clientOutput + "Adding Movie Slots to this current theater...\n";
                            ArrayList<String> list = new ArrayList<>();
                            list.add(String.valueOf(bookingCapacity));
                            list.add("");
                            movieDataBase.get(movieName).put(movieID, list);
                            System.out.println("Slots for movie added");
                            clientOutput = clientOutput + "Slots for movie added\n";
                        } else {
                            System.out.println("You can only add slots to the movie before 7 days of its release");
                            clientOutput = clientOutput + "You can only add slots to the movie before 7 days of its release\n";
                        }
                    }

                }
            } else {
                System.out.println("This operation can only be performed by an Admin");
                clientOutput = clientOutput + "This operation can only be performed by an Admin\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientOutput;
    }

    @Override
    public String removeMovieSlots(String movieID, String movieName) throws RemoteException {
        String clientOutput = "";
        try {
            Character ch = ID.charAt(3);
            if (ch.equals('A')) {
                if (movieDataBase.get(movieName) == null) {
                    System.out.println("There is no movie for deletion");
                    clientOutput = clientOutput + "There is no movie for deletion\n";
                } else {
                    System.out.println("Removing movie slots...");
                    clientOutput = clientOutput + "Removing movie slots...\n";
                    ArrayList<String> list = movieDataBase.get(movieName).get(movieID);
                    movieDataBase.get(movieName).remove(movieID);
                    System.out.println("Movie Slot Removed");
                    clientOutput = clientOutput + "Movie Slot Removed\n";
                    if (list.get(1).length() > 0) {
                        HashMap<String, ArrayList<String>> map = movieDataBase.get(movieName);
                        if (map.size() == 0) {
                            Scanner input = new Scanner(System.in);
                            System.out.println("Please enter an ID for " + movieName);
                            clientOutput = clientOutput + "Please enter an ID for " + movieName + "\n";
                            System.out.println("Tip: Movie ID for Atwater is ATW");
                            clientOutput = clientOutput + "Tip: Movie ID for Atwater is ATW\n";
                            System.out.println("Tip: Movie ID for Verdun is VER");
                            clientOutput = clientOutput + "Tip: Movie ID for Verdun is VER\n";
                            System.out.println("Tip: Movie ID for Outrement is OUT");
                            clientOutput = clientOutput + "Tip: Movie ID for Outrement is OUT\n";
                            System.out.println("Followed by Time (M - A - E) and Date DDMMYY");
                            clientOutput = clientOutput + "Followed by Time (M - A - E) and Date DDMMYY\n";
                            String nMovieID = input.nextLine();
                            String[] customers = list.get(1).split(",");
                            int capacity = 0;
                            for (String c : customers) {
                                capacity = capacity + Integer.valueOf(c.charAt(8));
                            }
                            System.out.println("Adding a new movie slot to the movie");
                            clientOutput = clientOutput + "Adding a new movie slot to the movie\n";
                            this.addMovieSlots(nMovieID, movieName, capacity * 2);
                            for (String c : customers) {
                                this.bookMovieTickets(c.substring(0, 7), nMovieID, movieName, Integer.valueOf(c.charAt(8)));
                            }
                            System.out.println("Customers have been added to the new slot");
                            clientOutput = clientOutput + "Customers have been added to the new slot\n";
                        } else if (map.size() > 0) {
                            System.out.println("Rebooking the clients to the  the next slot...");
                            clientOutput = clientOutput + "Rebooking the clients to the  the next slot...\n";
                            String[] customers = list.get(1).split(",");
                            int capacity = 0;
                            for (String c : customers) {
                                capacity = capacity + Character.getNumericValue(c.charAt(8));
                            }
                            for (Map.Entry<String, ArrayList<String>> m : map.entrySet()) {
                                int mSize = Integer.valueOf(m.getValue().get(0));
                                if ( mSize> capacity) {
                                    int nCapacity = capacity + Integer.valueOf(m.getValue().get(0));
                                    movieDataBase.get(movieName).get(m.getKey()).set(0, String.valueOf(nCapacity));
                                    for (String c : customers) {
                                        this.bookMovieTickets(c.substring(0, 7), m.getKey(), movieName, Character.getNumericValue(c.charAt(8)));
                                    }
                                    System.out.println("Customers have been booked to the next slot");
                                    clientOutput = clientOutput + "Customers have been booked to the next slot\n";
                                }
                                break;
                            }

                        }

                    }
                }
            } else {
                System.out.println("This operation can only be performed by an Admin");
                clientOutput = clientOutput + "This operation can only be performed by an Admin\n";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientOutput;
    }

    @Override
    public String listMovieShowsAvailability(String movieName) throws RemoteException {
        String clientOutput = "";
        try {
            Character ch = ID.charAt(3);
            if (ch.equals('A')) {
                if (movieDataBase.get(movieName) == null) {
                    System.out.println("There is no movie with such name");
                    clientOutput = clientOutput + "There is no movie with such name\n";
                } else {
                    System.out.println("Listing Movie Availability...");
                    HashMap<String, ArrayList<String>> map = movieDataBase.get(movieName);
                    System.out.println("The movie " + movieName + " Availability is: ");
                    clientOutput = clientOutput + "The movie " + movieName + " Availability is: \n";
                    for (Map.Entry<String, ArrayList<String>> m : map.entrySet()) {
                        System.out.print("Movie ID:" + m.getKey() + " ");
                        clientOutput = clientOutput + "Movie ID:" + m.getKey() + " \n";
                        ArrayList<String> list = m.getValue();
                        System.out.println("The Capacity is " + list.get(0));
                        clientOutput = clientOutput + "The Capacity is " + list.get(0) + "\n";
                    }
                    if (ID.contains("ATW") && connectionFlag) {
                        System.out.println("Calling other servers from Atwater");
                        connectionFlag = false;
                        String vReply = this.connect(VERPORT, movieName);
                        String oReply = this.connect(OUTPORT, movieName);
                        clientOutput= clientOutput + vReply+"\n";
                        clientOutput= clientOutput + oReply+"\n";
                        System.out.println(vReply);
                        System.out.println(oReply);

                    } else if (ID.contains("OUT") && connectionFlag) {
                        System.out.println("Calling other servers from Outremont");
                        connectionFlag = false;
                        String aReply = this.connect(ATPORT, movieName);
                        String vReply = this.connect(VERPORT, movieName);
                        clientOutput= clientOutput + aReply+"\n";
                        clientOutput= clientOutput + vReply+"\n";
                        System.out.println(aReply);
                        System.out.println(vReply);

                    } else if (ID.contains("VER") && connectionFlag) {
                        System.out.println("Calling other servers from Verdun");
                        connectionFlag = false;
                        String aReply = this.connect(ATPORT, movieName);
                        String oReply = this.connect(OUTPORT, movieName);
                        clientOutput= clientOutput + aReply+"\n";
                        clientOutput= clientOutput + oReply+"\n";
                        System.out.println(aReply);
                        System.out.println(oReply);
                    }
                }
            } else {
                System.out.println("This operation can only be performed by an Admin");
                clientOutput = clientOutput + "This operation can only be performed by an Admin\n";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionFlag = true;
        return clientOutput;
    }

    @Override
    public String bookMovieTickets(String customerId, String movieID, String movieName, int numberOfTickets) throws RemoteException {
        String clientOutput = "";
        try {
            if (movieDataBase.get(movieName) == null) {
                System.out.println("This movie is unavailable");
                clientOutput = clientOutput + "This movie is unavailable\n";
            } else if (movieDataBase.get(movieName) != null) {
                HashMap<String, ArrayList<String>> map = movieDataBase.get(movieName);
                if (map.containsKey(movieID)) {
                    ArrayList<String> list = map.get(movieID);
                    if (list.size() > 1) {
                        String[] customers = list.get(1).split(",");
                        for (String c : customers) {
                            if (c.contains(customerId)) {
                                System.out.println("It seems that you are already booked in this movie");
                                clientOutput = clientOutput + "It seems that you are already booked in this movie\n";
                            } else if (numberOfTickets > Integer.valueOf(list.get(0))) {
                                System.out.println("There are no spaces available for your request");
                                clientOutput = clientOutput + "There are no spaces available for your request\n";
                            } else {
                                if(ID.substring(0,3).equals(customerId.substring(0,3))) {
                                    System.out.println("Booking Tickets...");
                                    clientOutput = clientOutput + "Booking Tickets...\n";
                                    int newTickets = Integer.valueOf(list.get(0)) - numberOfTickets;
                                    movieDataBase.get(movieName).get(movieID).set(0, String.valueOf(newTickets));
                                    movieDataBase.get(movieName).get(movieID).set(1, movieDataBase.get(movieName).get(movieID).get(1) + customerId + " " + numberOfTickets + ",");
                                    System.out.println(customerId + " your booking is confirmed");
                                    clientOutput = clientOutput + customerId + " your booking is confirmed\n";
                                    break;
                                }
                                else if(!ID.substring(0,3).equals(customerId.substring(0,3))){
                                    if(!outMap.containsKey(customerId))
                                        outMap.put(customerId,0);
                                    int outMapNumber = 0;
                                    outMapNumber = outMapNumber + outMap.get(customerId);
                                    if (ID.contains("ATW") && connectionFlag) {
                                        System.out.println("Calling other servers from Atwater");
                                        connectionFlag = false;
                                        String vReply = this.connect(VERPORT, "outmovie"+customerId);
                                        String oReply = this.connect(OUTPORT, "outmovie"+customerId);
                                        outMapNumber = outMapNumber + Integer.parseInt(vReply) + Integer.parseInt(oReply);
                                    } else if (ID.contains("OUT") && connectionFlag) {
                                        System.out.println("Calling other servers from Outremont");
                                        connectionFlag = false;
                                        String aReply = this.connect(ATPORT, "outmovie"+customerId);
                                        String vReply = this.connect(VERPORT, "outmovie"+customerId);
                                        outMapNumber = outMapNumber + Integer.parseInt(vReply) + Integer.parseInt(aReply);
                                    } else if (ID.contains("VER") && connectionFlag) {
                                        System.out.println("Calling other servers from Verdun");
                                        connectionFlag = false;
                                        String aReply = this.connect(ATPORT, "outmovie"+customerId);
                                        String oReply = this.connect(OUTPORT, "outmovie"+customerId);
                                        System.out.println(aReply);
                                        System.out.println(oReply);
                                        outMapNumber = outMapNumber + Integer.parseInt(aReply) + Integer.parseInt(oReply);
                                    }

                                    System.out.println(outMapNumber);
                                    if(outMapNumber < 1) {
                                        System.out.println("Booking Tickets...");
                                        clientOutput = clientOutput + "Booking Tickets...\n";
                                        int newTickets = Integer.valueOf(list.get(0)) - numberOfTickets;
                                        movieDataBase.get(movieName).get(movieID).set(0, String.valueOf(newTickets));
                                        movieDataBase.get(movieName).get(movieID).set(1, movieDataBase.get(movieName).get(movieID).get(1) + customerId + " " + numberOfTickets + ",");
                                        System.out.println(customerId + " your booking is confirmed");
                                        clientOutput = clientOutput + customerId + " your booking is confirmed\n";
                                        int outnumber = outMap.get(customerId);
                                        outMap.put(customerId,++outnumber);
                                        break;
                                    }
                                    else if(outMapNumber >=1){
                                        System.out.println("You can't book more than 3 slots outside you're area");
                                        clientOutput = clientOutput + "You can't book more than 3 slots outside you're area\n";
                                    }
                                }
                            }
                            }




                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionFlag = true;
        return clientOutput;
    }

    @Override
    public String getBookingSchedule(String customerId) throws RemoteException {
        String clientOutput = "";
        try {
            System.out.println("The movie Schedule for customer " + customerId + " is:");
            clientOutput = clientOutput + "The movie Schedule for customer " + customerId + " is:\n";
            ArrayList<String> movies = new ArrayList<>();
            movies.add("Avengers");
            movies.add("Avatar");
            movies.add("Titanic");
            for (String a : movies) {
                HashMap<String, ArrayList<String>> map = movieDataBase.get(a);
                for (Map.Entry<String, ArrayList<String>> m : map.entrySet()) {
                    if (m.getValue().get(1).contains(customerId)) {
                        String[] customers = m.getValue().get(1).split(",");
                        System.out.println(a + ":");
                        clientOutput = clientOutput + a + ":\n";
                        for (String c : customers) {
                            if (c.contains(customerId)) {
                                System.out.println("Movie ID: " + m.getKey() + ", Capacity booked: " + c.charAt(8));
                                clientOutput = clientOutput + "Movie ID: " + m.getKey() + ", Capacity booked: " + c.charAt(8) + "\n";
                            }
                        }
                    }
                }
                if (ID.contains("ATW") && connectionFlag) {
                    System.out.println("Calling other servers from Atwater");
                    connectionFlag = false;
                    String vReply = this.connect(VERPORT, customerId + " " + a);
                    String oReply = this.connect(OUTPORT, customerId+ " " + a);
                    clientOutput= clientOutput + vReply+"\n";
                    clientOutput= clientOutput + oReply+"\n";
                    System.out.println(vReply);
                    System.out.println(oReply);

                } else if (ID.contains("OUT") && connectionFlag) {
                    System.out.println("Calling other servers from Outremont");
                    connectionFlag = false;
                    String aReply = this.connect(ATPORT, customerId+ " " + a);
                    String vReply = this.connect(VERPORT, customerId+ " " + a);
                    clientOutput= clientOutput + aReply+"\n";
                    clientOutput= clientOutput + vReply+"\n";
                    System.out.println(aReply);
                    System.out.println(vReply);

                } else if (ID.contains("VER") && connectionFlag) {
                    System.out.println("Calling other servers from Verdun");
                    connectionFlag = false;
                    String aReply = this.connect(ATPORT, customerId+ " " + a);
                    String oReply = this.connect(OUTPORT, customerId+ " " + a);
                    clientOutput= clientOutput + aReply+"\n";
                    clientOutput= clientOutput + oReply+"\n";
                    System.out.println(aReply);
                    System.out.println(oReply);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionFlag = true;
        return clientOutput;
    }

    @Override
    public String cancelMovieTickets(String customerId, String movieID, String movieName, int numberOfTickets) throws RemoteException {
        String clientOutput = "";
        try {
            if (movieDataBase.get(movieName) == null) {
                System.out.println("This movie is unavailable");
                clientOutput = clientOutput + "This movie is unavailable\n";
            } else {
                HashMap<String, ArrayList<String>> map = movieDataBase.get(movieName);
                if (!map.containsKey(movieID)) {
                    System.out.println("This movie is not available for the week");
                    clientOutput = clientOutput + "This movie is not available for the week\n";
                } else {
                    ArrayList<String> list = map.get(movieID);
                    String[] customers = list.get(1).split(",");
                    String updatedCustomerList = "";
                    boolean exists = false;
                    int increaseCapacity = 0;
                    boolean largerThan = false;
                    for (String c : customers) {
                        if (!c.contains(customerId)) {
                            updatedCustomerList = updatedCustomerList + c + ",";
                        } else if (c.contains(customerId)) {
                            String[] formattedC = c.split(" ");
                            int value = Integer.parseInt(formattedC[1]);
                            if (value == numberOfTickets) {
                                increaseCapacity = increaseCapacity + value;
                                if(!ID.substring(0,3).equals(customerId.substring(0,3))){
                                    int updatedValue = outMap.get(customerId);
                                    outMap.put(customerId,--updatedValue);
                                }
                                exists = true;
                            } else if (value > numberOfTickets) {
                                increaseCapacity = increaseCapacity + value;
                                int left = value - numberOfTickets;
                                updatedCustomerList = updatedCustomerList + formattedC[0] + " " + (left) + ",";
                                exists = true;
                            } else if (value < numberOfTickets) {
                                System.out.println("You have booked less tickets than what you are trying to cancel");
                                clientOutput = clientOutput + "You have booked less tickets than what you are trying to cancel\n";
                                largerThan = true;
                            }
                        }
                    }
                    if (largerThan) {

                    } else if (!exists) {
                        System.out.println("You are not booked in this movie");
                        clientOutput = clientOutput + "You are not booked in this movie\n";
                    } else {
                        System.out.println(increaseCapacity);
                        movieDataBase.get(movieName).get(movieID).set(0, String.valueOf(Integer.parseInt(movieDataBase.get(movieName).get(movieID).get(0)) + increaseCapacity));
                        movieDataBase.get(movieName).get(movieID).set(1, updatedCustomerList);
                        System.out.println("Your booking have been cancelled. customer: " + customerId);
                        clientOutput = clientOutput + "Your booking have been cancelled. customer: " + customerId + "\n";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientOutput;
    }

    @Override
    public String exchangeTicket(String customerId, String movieID, String oldMovieName, String newMovieID, String newMovieName, int numberOfTickets) throws RemoteException {
        String clientOutput = "";

        try {
            if(movieDataBase.get(oldMovieName).get(movieID).get(1).contains(customerId)){
                String reply = "";
                if (newMovieID.contains("ATW") && connectionFlag) {
                    connectionFlag = false;
                     reply = this.connect(ATPORT, "ex,"+customerId+","+newMovieID+","+newMovieName+","+numberOfTickets);
                    System.out.println(reply);
                } else if (newMovieID.contains("OUT") && connectionFlag) {
                    connectionFlag = false;
                     reply = this.connect(OUTPORT, "ex,"+customerId+","+newMovieID+","+newMovieName+","+numberOfTickets);
                    System.out.println(reply);
                } else if (newMovieID.contains("VER") && connectionFlag) {
                    connectionFlag = false;
                     reply = this.connect(VERPORT, "ex,"+customerId+","+newMovieID+","+newMovieName+","+numberOfTickets);
                    System.out.println(reply);
                }
                if(reply.contains("confirmed")){
                    this.cancelMovieTickets(customerId,movieID,oldMovieName,numberOfTickets);
                    System.out.println("The Bookings have been exchanged");
                    clientOutput = clientOutput + "The Bookings have been exchanged";
                }
                else{
                    System.out.println("We can't handle your request at the moment");
                    clientOutput = clientOutput + "We can't handle your request at the moment";
                }
            }
            else{
                System.out.println("The Customer is not booked in the current movie");
                clientOutput = clientOutput + "The Customer is not booked in the current movie";
            }
        }
        catch (Exception e){

        }
        return clientOutput;
    }

    public String listSingleMovieSchedule(String movieName) {
        String clientOutput = "";
        try {
            Character ch = ID.charAt(3);
            if (ch.equals('A')) {
                if (movieDataBase.get(movieName) == null) {
                    System.out.println("There is no movie with such name");
                    clientOutput = clientOutput + "There is no movie with such name\n";
                } else {
                    System.out.println("Listing Movie Availability...");
                    HashMap<String, ArrayList<String>> map = movieDataBase.get(movieName);
                    System.out.println("The movie " + movieName + " Availability is: ");
                    clientOutput = clientOutput + "The movie " + movieName + " Availability is: \n";
                    for (Map.Entry<String, ArrayList<String>> m : map.entrySet()) {
                        System.out.print("Movie ID:" + m.getKey() + " ");
                        clientOutput = clientOutput + "Movie ID:" + m.getKey() + " \n";
                        ArrayList<String> list = m.getValue();
                        System.out.println("The Capacity is " + list.get(0));
                        clientOutput = clientOutput + "The Capacity is " + list.get(0) + "\n";
                    }
                }
            }
        } catch (Exception e) {

        }
        return clientOutput;
    }

    public String getSingleSchedule(String customerId){
        String clientOutput = "";
        try {
            ArrayList<String> movies = new ArrayList<>();
            String []splitter = customerId.split(" ");
            customerId = splitter[0];
            String a = splitter[1];
                HashMap<String, ArrayList<String>> map = movieDataBase.get(a);
                for (Map.Entry<String, ArrayList<String>> m : map.entrySet()) {
                    if (m.getValue().get(1).contains(customerId)) {
                        System.out.println("The movie Schedule for customer " + customerId + " is:");
                        clientOutput = clientOutput + "The movie Schedule for customer " + customerId + " is:\n";
                        String[] customers = m.getValue().get(1).split(",");
                        System.out.println(a + ":");
                        clientOutput = clientOutput + a + ":\n";
                        for (String c : customers) {
                            if (c.contains(customerId)) {
                                System.out.println("Movie ID: " + m.getKey() + ", Capacity booked: " + c.charAt(8));
                                clientOutput = clientOutput + "Movie ID: " + m.getKey() + ", Capacity booked: " + c.charAt(8) + "\n";
                            }
                        }
                    }
                }
            }
        catch(Exception e){
        }
        return clientOutput;
    }

    public String getOutsideSchedule(String customerId){
        String clientOutput = "";
        try {
            if (!outMap.containsKey(customerId))
                outMap.put(customerId, 0);
                clientOutput = clientOutput + outMap.get(customerId);
            }
        catch(Exception e){
        }
        return clientOutput;
    }

    public boolean getEnough(String movieId, String movieName, int numberOfTickets){
        return Integer.parseInt(movieDataBase.get(movieName).get(movieId).get(0)) > numberOfTickets;
    }
}
