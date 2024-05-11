package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;
import antisocial.app.backend.data.dto.FriendsNamesDto;
import antisocial.app.backend.data.dto.ResponseMessageDto;
import antisocial.app.backend.service.IFriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private IFriendService friendService;


    public FriendController(IFriendService friendService) {
        this.friendService = friendService;
    }


    @GetMapping
    public ResponseEntity<FriendsNamesAndRequestsDto> getFriendsNamesAndRequests(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        FriendsNamesAndRequestsDto friendsNamesAndRequests = friendService.getFriendsNamesAndRequests(username);

        return ResponseEntity.ok(friendsNamesAndRequests);
    }

    @GetMapping("finduser/{usernameToSearch}")
    public ResponseEntity<FriendsNamesDto> getUser(@PathVariable String usernameToSearch){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userUsername = user.getUsername();
        Set<String> usernamesString = friendService.findUsers(usernameToSearch, userUsername);

        FriendsNamesDto usernames = new FriendsNamesDto(usernamesString);

        return ResponseEntity.ok(usernames);
    }

    @PatchMapping("friendrequest/{receiver}")
    public ResponseEntity<ResponseMessageDto> sendFriendsRequest(@PathVariable String receiver){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = user.getUsername();

        friendService.handleFriendRequest(receiver, sender, "requested");

        ResponseMessageDto simpleResponse = new ResponseMessageDto("Request sent");

        return ResponseEntity.ok(simpleResponse);
    }

    @PatchMapping("acceptrequest/{sender}")
    public ResponseEntity<ResponseMessageDto> acceptFriendRequest(@PathVariable String sender){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String receiver = user.getUsername();

        friendService.handleFriendRequest(receiver, sender, "accepted");

        ResponseMessageDto simpleResponse = new ResponseMessageDto("Request accepted");

        return ResponseEntity.ok(simpleResponse);
    }

    @PatchMapping("denyrequest/{sender}")
    public ResponseEntity<ResponseMessageDto> denyFriendRequest(@PathVariable String sender){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String receiver = user.getUsername();

        friendService.handleFriendRequest(receiver, sender, "denied");

        ResponseMessageDto simpleResponse = new ResponseMessageDto("Request denied");

        return ResponseEntity.ok(simpleResponse);
    }
}
