package antisocial.app.backend.controller;

import antisocial.app.backend.data.dto.FriendsNamesAndRequestsDto;
import antisocial.app.backend.data.dto.FriendsNamesDto;
import antisocial.app.backend.data.dto.SimpleResponseDto;
import antisocial.app.backend.service.IFriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("finduser/{username}")
    public ResponseEntity<FriendsNamesDto> getUser(@PathVariable String username){
        List<String> usernamesString = friendService.findUsers(username);

        FriendsNamesDto usernames = new FriendsNamesDto(usernamesString);

        return ResponseEntity.ok(usernames);
    }

    @PatchMapping("friendrequest/{receiver}")
    public ResponseEntity<SimpleResponseDto> sendFriendsRequest(@PathVariable String receiver){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sender = user.getUsername();

        friendService.sendFriendRequest(receiver, sender);

        SimpleResponseDto simpleResponse = new SimpleResponseDto("Request sent");

        return ResponseEntity.ok(simpleResponse);
    }

    @PatchMapping("acceptrequest/{sender}")
    public ResponseEntity<SimpleResponseDto> acceptFriendRequest(@PathVariable String sender){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String receiver = user.getUsername();

        friendService.acceptFriendRequest(receiver, sender);

        SimpleResponseDto simpleResponse = new SimpleResponseDto("Request accepted");

        return ResponseEntity.ok(simpleResponse);
    }
}
