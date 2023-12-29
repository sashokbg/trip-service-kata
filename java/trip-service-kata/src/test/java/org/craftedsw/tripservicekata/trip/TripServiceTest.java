package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TripServiceTest {
    @DisplayName("Non connected user is not allowed to see trips")
    @Test
    void non_connected() {
        User searchUser = new User();
        User loggedInUser = null;

        UserSession mockedSession = Mockito.mock(UserSession.class);
        when(mockedSession.getLoggedUser()).thenReturn(loggedInUser);

        TripService tripService = new TripService(mockedSession, new TripDAO());

        assertThrows(UserNotLoggedInException.class, () -> {
            tripService.getTripsByUser(searchUser);
        });
    }

    @DisplayName("A connected user cannot see the trips of non-friend users")
    @Test
    void cannot_see_others() {
        User searchUser = new User();
        User loggedInUser = new User();

        UserSession mockedSession = Mockito.mock(UserSession.class);
        when(mockedSession.getLoggedUser()).thenReturn(loggedInUser);

        TripService tripService = new TripService(mockedSession, new TripDAO());

        List<Trip> tripsByUser = tripService.getTripsByUser(searchUser);
        assertThat(tripsByUser).isEmpty();
    }

    @DisplayName("A connected user can see the trips of a friend")
    @Test
    void can_see_friend() {
        User friend = new User();
        User loggedInUser = new User();
        friend.addFriend(loggedInUser);

        Trip trip = new Trip();

        UserSession mockedSession = Mockito.mock(UserSession.class);
        when(mockedSession.getLoggedUser()).thenReturn(loggedInUser);

        TripService tripService = new TripService(mockedSession, new TripDAO() {
            @Override
            public List<Trip> findTripsFor(User searchedUser) {
                return Collections.singletonList(trip);
            }
        }
        );

        List<Trip> tripsByUser = tripService.getTripsByUser(friend);
        assertThat(tripsByUser).hasSize(1);
        assertThat(tripsByUser.get(0)).isEqualTo(trip);
    }

    @DisplayName("TOTO")
    @Test
    void can_see_friend_TOTO() {
        User friend = new User();
        User loggedInUser = new User();
        friend.addFriend(new User());

        UserSession mockedSession = Mockito.mock(UserSession.class);
        when(mockedSession.getLoggedUser()).thenReturn(loggedInUser);

        TripService tripService = new TripService(mockedSession, new TripDAO());

        List<Trip> tripsByUser = tripService.getTripsByUser(friend);
        assertThat(tripsByUser).isEmpty();
    }
}
