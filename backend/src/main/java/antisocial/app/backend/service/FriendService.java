package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;
import antisocial.app.backend.data.entity.UserEntity;
import antisocial.app.backend.repository.IUserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class FriendService implements IFriendService{
    private IUserRepository userRepository;

    public FriendService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /*private ExecutorService executorService;

    public FriendService(IUserRepository userRepository, ExecutorService executorService) {
        this.userRepository = userRepository;
        this.executorService = executorService;
    }*/

    @Override
    public FriendsNamesAndRequestsDto getFriendsNamesAndRequests(String username) {
        UserEntity user = userRepository.findByUsername(username).get();
        List<String> friendsNames = user.getFriendsNames();
        List<String> requestsNames = user.getFriendsRequests();
        FriendsNamesAndRequestsDto friendsNamesAndRequests = new FriendsNamesAndRequestsDto(friendsNames, requestsNames);
        return friendsNamesAndRequests;
    }

    @Override
    public void sendFriendRequest(String receiver, String sender) {
        UserEntity userReceive = userRepository.findByUsername(receiver).get();
        userReceive.addFriendRequest(sender);
        userRepository.save(userReceive);
    }

    @Override
    public void acceptFriendRequest(String receiver, String sender) {
        UserEntity userReceive = userRepository.findByUsername(receiver).get();
        UserEntity userSend = userRepository.findByUsername(sender).get();

        userReceive.addFriendName(userSend.getUsername());
        userSend.addFriendName(userReceive.getUsername());
        userReceive.removeFriendRequest(userSend.getUsername());

        userRepository.save(userReceive);
        userRepository.save(userSend);
    }

    /*
    @Override
    @Async
    public CompletableFuture<Void> acceptFriendRequestAsync(String receiver, String sender){
        acceptFriendRequest(receiver, sender);
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void acceptFriendRequest(String receiver, String sender) {
        UserEntity userReceive = userRepository.findByUsername(receiver).get();
        UserEntity userSend = userRepository.findByUsername(sender).get();

        userReceive.addFriendName(userSend.getUsername());
        userSend.addFriendName(userReceive.getUsername());
        userReceive.removeFriendRequest(userSend.getUsername());

        userRepository.save(userReceive);
        userRepository.save(userSend);
    }*/

    @Override
    public List<String> findUsers(String username) {
        List<String> usernames = userRepository.findAllByUsername(username);
        return usernames;
    }


    /*@Override
    @Async
    public CompletableFuture<List<String>> findUsers(String username) {
        System.out.println("This gets till service method");
        return CompletableFuture.supplyAsync(() -> userRepository.findAllByUsername(username), executorService);
    }*/

    @Override
    public List<String> getFriendsRequests(String username) {
        UserEntity user = userRepository.findByUsername(username).get();
        return user.getFriendsRequests();
    }
}
