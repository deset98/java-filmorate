package ru.yandex.practicum.filmorate.dal.repositories.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserMapper;
import ru.yandex.practicum.filmorate.dal.repositories.BaseRepository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Qualifier("UserDbStorage")
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

    private static final String INSERT_USER =
            "INSERT INTO users (user_name, login, email, birthday) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_USER =
            "UPDATE users " +
                    "SET user_name = ?, login = ?, email = ?, birthday = ? " +
                    "WHERE user_id = ?";

    private static final String GET_USER =
            "SELECT * " +
                    "FROM users " +
                    "WHERE user_id = ?";

    private static final String GET_ALL_USERS =
            "SELECT * " +
                    "FROM users";

    private static final String GET_FRIENDS =
            "SELECT * FROM users AS u LEFT JOIN friendship AS fs ON fs.friend_id=u.user_id WHERE fs.user_id = ?";

    private static final String ADD_FRIEND =
            "INSERT INTO friendship (user_id, friend_id) " +
                    "VALUES (?, ?)";

    public static final String DELETE_FRIEND =
            "DELETE FROM friendship " +
                    "WHERE (user_id = ? AND friend_id = ?)";

    public static final String GET_COMMON_FRIENDS =
            "SELECT u.* " +
                    "FROM friendship fs1 " +
                    "JOIN friendship fs2 ON fs1.friend_id = fs2.friend_id " +
                    "JOIN users u ON u.user_id = fs1.friend_id " +
                    "WHERE fs1.user_id = ? AND fs2.user_id = ?";

    public UserDbStorage(JdbcTemplate jdbc,
                         UserMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User getUser(Long userId) {
        User user = findOne(
                GET_USER,
                userId
        );
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Set<Long> listOfFriendsIds = findMany(GET_FRIENDS, userId).stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        user.getFriends().addAll(listOfFriendsIds);

        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(GET_ALL_USERS);
    }

    @Override
    public User createUser(User newUser) throws InternalServerException {
        long id = insert(
                INSERT_USER,
                newUser.getName(),
                newUser.getLogin(),
                newUser.getEmail(),
                newUser.getBirthday().toString()
        );
        newUser.setId(id);
        return newUser;
    }

    @Override
    public User updateUser(User updUser) throws InternalServerException {
        insert(
                UPDATE_USER,
                updUser.getName(),
                updUser.getLogin(),
                updUser.getEmail(),
                updUser.getBirthday().toString(),
                updUser.getId()
        );
        return updUser;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (findOne(GET_USER, userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        if (findOne(GET_USER, friendId) == null) {
            throw new NotFoundException("Friend with id " + friendId + " not found");
        }
        jdbc.update(ADD_FRIEND, userId, friendId);
    }


    @Override
    public Collection<User> getFriends(Long userId) {
        if (findOne(GET_USER, userId) != null) {
            return findMany(GET_FRIENDS, userId);
        } else {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }


    @Override
    public void removeFriend(Long userId, Long friendId) {
        if (findOne(GET_USER, userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        if (findOne(GET_USER, friendId) == null) {
            throw new NotFoundException("Friend with id " + friendId + " not found");
        }
        jdbc.update(DELETE_FRIEND, userId, friendId);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long friendId) {


        return findMany(GET_COMMON_FRIENDS, userId, friendId);
    }
}
