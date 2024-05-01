package ibf.paf.day29.revision.utils;

public interface Queries {

    public static final String SQL_GET_ALL_RSVP = """
            SELECT * FROM rsvp
            """;

    public static final String SQL_GET_RSVP_BY_EMAIL = """
            SELECT * FROM rsvp
            WHERE email = ?
            """;

    public static final String SQL_GET_RSVP_BY_NAME = """
            SELECT * FROM rsvp
            WHERE name LIKE ?
            """;
    
    public static final String SQL_INSERT_NEW_RSVP = """
            INSERT INTO rsvp (name, email, phone, confirmation_date, comments, food_type) VALUES (?, ?, ?, ?, ?, ?)
            """;

    public static final String SQL_UPDATE_RSVP = """
            UPDATE rsvp SET name = ?, email = ?, phone = ?, confirmation_date = ?, comments = ?, food_type = ? WHERE email = ?
            """;

    public static final String SQL_COUNT_ALL_RSVP = """
            SELECT COUNT(*) AS total FROM rsvp
            """;

} 