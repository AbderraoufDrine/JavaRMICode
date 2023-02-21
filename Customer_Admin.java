import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Customer_Admin extends Remote {

    public String addMovieSlots(String movieID, String movieName, int bookingCapacity) throws RemoteException;
    public String removeMovieSlots(String movieID, String movieName) throws RemoteException;
    public String listMovieShowsAvailability(String movieName) throws RemoteException;
    public String bookMovieTickets(String customerId, String movieID, String movieName, int numberOfTickets) throws RemoteException;
    public String getBookingSchedule(String customerId) throws RemoteException;
    public String cancelMovieTickets(String customerId, String movieID, String movieName, int numberOfTickets) throws RemoteException;
    public String exchangeTicket(String customerId, String movieID, String oldMovieName, String newMovieID, String newMovieName, int numberOfTickets) throws RemoteException;

}
