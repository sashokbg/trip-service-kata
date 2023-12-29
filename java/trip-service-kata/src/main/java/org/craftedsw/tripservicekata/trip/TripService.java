package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;

import java.util.ArrayList;
import java.util.List;

public class TripService {
    UserSession instance = UserSession.getInstance();
    private final TripDAO tripDAO;

    public TripService(UserSession instance, TripDAO tripDAO) {
        this.instance = instance;
        this.tripDAO = tripDAO;
    }

    public List<Trip> getTripsByUser(User searchedUser) throws UserNotLoggedInException {
        User loggedUser = instance.getLoggedUser();

        if (loggedUser == null) {
            throw new UserNotLoggedInException();
        }

        List<Trip> tripList = new ArrayList<Trip>();

        if (loggedUser.isFriendOf(searchedUser)) {
            tripList = tripDAO.findTripsFor(searchedUser);
        }
        return tripList;
    }


}
