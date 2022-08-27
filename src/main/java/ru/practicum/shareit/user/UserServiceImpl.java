package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = mapper.toModel(userDto, null);
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = patchUser(userId, userDto);
        return mapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto findUserById(long userId) {
        return mapper.toDto(userRepository.findById(userId).orElseThrow());
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return mapper.mapUserListToUserDtoList(userRepository.findAll());
    }

    private User patchUser(Long userId, UserDto patch) {
        UserDto entry = findUserById(userId);
        String name = patch.getName();
        if (name != null && !name.isBlank()) {
            entry.setName(name);
        }

        String oldEmail = entry.getEmail();
        String newEmail = patch.getEmail();
        if (newEmail != null && !newEmail.isBlank() && !oldEmail.equals(newEmail)) {
            entry.setEmail(newEmail);
        }
        return mapper.toModel(entry, userId);
    }
}
