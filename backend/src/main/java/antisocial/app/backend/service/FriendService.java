package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;
import antisocial.app.backend.data.dto.IResponseDto;
import antisocial.app.backend.data.entity.UserEntity;
import antisocial.app.backend.errorHandling.exception.FriendRequestException;
import antisocial.app.backend.repository.IUserRepository;
import antisocial.app.backend.service.friendRequest.IHandleFriendRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class FriendService implements IFriendService{

    private IUserRepository userRepository;

    private List<IHandleFriendRequest> handleFriendRequests;


    public FriendService(IUserRepository userRepository, List<IHandleFriendRequest> handleFriendRequests) {
        this.userRepository = userRepository;
        this.handleFriendRequests = handleFriendRequests;
    }


    @Override
    public CompletableFuture<IResponseDto> getFriendsNamesAndRequests(String username) {

        return CompletableFuture.supplyAsync(() -> {
            UserEntity userEntity = userRepository.findByUsername(username).get();

            Set<String> friendsNames = userEntity.getFriendsNames();
            Set<String> requestsNames = userEntity.getFriendsRequests();

            IResponseDto friendsNamesAndRequests = new FriendsNamesAndRequestsDto(friendsNames, requestsNames);

            return friendsNamesAndRequests;
        });
    }

    @Override
    public CompletableFuture<Set<String>> findUsers(String usernameToSearch, String userUsername) {

        return CompletableFuture.supplyAsync(() -> {
            UserEntity userEntity = userRepository.findByUsername(userUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("There is no such user"));

            Set<String> friendsNames = userEntity.getFriendsNames();
            Set<String> friendsRequests = userEntity.getFriendsRequests();
            Set<String> sentFriendsRequests = userEntity.getFriendsRequestsSent();

            Set<String> usernamesToExclude = new HashSet<>();
            usernamesToExclude.add(userUsername);
            usernamesToExclude.addAll(friendsNames);
            usernamesToExclude.addAll(friendsRequests);
            usernamesToExclude.addAll(sentFriendsRequests);

            Set<String> usernames = userRepository.findAllByUsername(usernameToSearch, usernamesToExclude);

            return usernames;
        });
    }

    @Override
    public CompletableFuture<Void> handleFriendRequest(String receiver, String sender, String type) {

        return CompletableFuture.runAsync(() -> {
            if(userRepository.findByUsername(receiver).isEmpty() || userRepository.findByUsername(sender).isEmpty()){
                throw new FriendRequestException("There is no such user");
            }

            UserEntity userReceiver = userRepository.findByUsername(receiver).get();
            UserEntity userSender = userRepository.findByUsername(sender).get();

            IHandleFriendRequest handleFriendRequest = handleFriendRequests.stream().filter(request ->
                    request.isNeeded(type)).findFirst().orElseThrow(() ->
                    new FriendRequestException("There is no such request"));
            handleFriendRequest.handleRequest(userReceiver, userSender);

            userRepository.save(userReceiver);
            userRepository.save(userSender);
        });
    }

}
