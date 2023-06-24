package cl.drcde.cqrs.presentation.shared;

public final class Routes {
    private static final String VERSION             = "/v1";
    private static final String API                 = "/management";


    public static class User {
        public static final String PREFIX              = "/user";
        public static final String USER_ID             = "/{user_id}";
        public static final String GET_ALL_USERS       = VERSION + API + PREFIX;
        public static final String GET                 = VERSION + API + PREFIX + USER_ID;
        public static final String POST                = VERSION + API + PREFIX;
    }

}
