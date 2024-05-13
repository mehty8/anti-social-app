package antisocial.app.backend.service;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;
import antisocial.app.backend.data.entity.UserEntity;
import antisocial.app.backend.errorHandling.exception.FriendRequestException;
import antisocial.app.backend.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FriendService implements IFriendService{

    private IUserRepository userRepository;


    public FriendService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public FriendsNamesAndRequestsDto getFriendsNamesAndRequests(String username) {
        UserEntity user = userRepository.findByUsername(username).get();
        Set<String> friendsNames = user.getFriendsNames();
        Set<String> requestsNames = user.getFriendsRequests();
        FriendsNamesAndRequestsDto friendsNamesAndRequests = new FriendsNamesAndRequestsDto(friendsNames, requestsNames);

        return friendsNamesAndRequests;
    }

    @Override
    public void handleFriendRequest(String receiver, String sender, String type) {

        if(userRepository.findByUsername(receiver).isEmpty() || userRepository.findByUsername(sender).isEmpty()){
            throw new FriendRequestException("There is no such user");
        }

        UserEntity userReceiver = userRepository.findByUsername(receiver).get();
        UserEntity userSender = userRepository.findByUsername(sender).get();

        if(type.equals("accepted") || type.equals("denied")){
            boolean isSenderRemoved = userReceiver.removeFriendRequest(sender);
            boolean isReceiverRemoved = userSender.removeSentFriendRequest(receiver);

            if(!isReceiverRemoved || !isSenderRemoved){
                throw new FriendRequestException("User did not receive friend request");
            }
        }

        if(type.equals("requested")){
            userReceiver.addFriendRequest(sender);
            userSender.addSentFriendRequest(receiver);
        } else if(type.equals("accepted")){
            userReceiver.addFriendName(sender);
            userSender.addFriendName(receiver);
        }


        userRepository.save(userReceiver);
        userRepository.save(userSender);
    }

    @Override
    public Set<String> findUsers(String usernameToSearch, String userUsername) {
        UserEntity user = userRepository.findByUsername(userUsername).get();
        Set<String> friendsNames = user.getFriendsNames();
        Set<String> friendRequests = user.getFriendsRequests();
        Set<String> sentFriendsRequests = user.getFriendsRequestsSent();

        Set<String> usernamesToExclude = new HashSet<>();
        usernamesToExclude.add(userUsername);
        usernamesToExclude.addAll(friendsNames);
        usernamesToExclude.addAll(friendRequests);
        usernamesToExclude.addAll(sentFriendsRequests);

        Set<String> usernames = userRepository.findAllByUsername(usernameToSearch, usernamesToExclude);

        return usernames;
    }

}
