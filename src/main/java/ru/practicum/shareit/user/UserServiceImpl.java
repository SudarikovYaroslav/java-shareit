package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long userId, User user) {
        return userRepository.save(patchUser(findUserById(userId), user));
    }

    @Override
    public User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    private User patchUser(User entry, User patch) {
        String name = patch.getName();
        if (name != null && !name.isBlank()) {
            entry.setName(name);
        }

        String oldEmail = entry.getEmail();
        String newEmail = patch.getEmail();
        if (newEmail != null && !newEmail.isBlank() && !oldEmail.equals(newEmail)) {
            entry.setEmail(newEmail);
        }
        return entry;
    }
}

/*
*  @Override
    public User updateUser(long userId, User user) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId);
        }
        User oldEntry = users.get(userId);

        if (user.getName() != null) {
            oldEntry.setName(user.getName());
        }

        String oldEmail = users.get(userId).getEmail();
        String newEmail = user.getEmail();
        if (newEmail != null && !oldEmail.equals(newEmail)) {
            tryRefreshUserEmail(oldEmail, newEmail);
            oldEntry.setEmail(newEmail);
        }
        return oldEntry;
    }
    *
    * private void tryRefreshUserEmail(String oldEmail, String newEmail) {
        emails.remove(oldEmail);
        if (emails.contains(newEmail)) {
            emails.add(oldEmail);
            throw new EmailConflictException(EMAIL_CONFLICT_MESSAGE + newEmail);
        }
        emails.add(newEmail);
    }
* */
